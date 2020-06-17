package com.gapbe.lib.apilib;

import com.gapbe.base.Constants;
import com.gapbe.base.TestBase;
import com.gapbe.commons.DTOs.requestDTOs.AddressDTO;
import com.gapbe.commons.DTOs.requestDTOs.BagDTO;
import com.gapbe.commons.DTOs.requestDTOs.ConsigneeDTO;
import com.gapbe.commons.DTOs.requestDTOs.ConsignmentDTO;
import com.gapbe.commons.DTOs.requestDTOs.ConsignmentsDTO;
import com.gapbe.commons.DTOs.requestDTOs.CustomerDTO;
import com.gapbe.commons.DTOs.requestDTOs.OrderShipmentDTO;
import com.gapbe.commons.DTOs.requestDTOs.RiderDTO;
import com.gapbe.commons.DTOs.requestDTOs.ShipmentDTO;
import com.gapbe.commons.DTOs.requestDTOs.ShipmentsDTO;
import com.gapbe.commons.DTOs.requestDTOs.TransportDetailsDTO;
import com.gapbe.commons.enums.BagSubType;
import com.gapbe.commons.enums.BagType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapbe.commons.enums.PartnerId;
import com.gapbe.commons.enums.PartnerKey;
import com.qautils.commonutils.HttpUtility;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;

@Log4j2
public class ConsignmentLib {
  private HttpUtility httpUtility;
  private String createConsignmentUrl = Constants.CREATE_CONSIGNMENT.value();
  private String getConsignmentForPartnerurl = Constants.GET_CONSIGNMENT_FOR_PARTNER.value();
  private String getConsignmentfromDeliveryApi = Constants.GET_CONSIGNMENT_VIA_DELIVERY_API.value();
  private String deliveryAuth = Constants.DELIVERY_API_AUTH.value();
  private List shipmentDTOList;

  public ConsignmentLib() {
    this.httpUtility = TestBase.httpUtility;
  }

  public HttpResponse consignmentCreation(
      /*consignment[consignment_id, consignee_details[]]
      Baglist - [shipmentList[id]]
      Order shipment list[orderId]*/
      final HashMap<String, ConsigneeDTO> consignment,
      final List<List<String>> baglist,
      final HashMap<String, Integer> ordershipment,
      final String houseNo,
      final String society,
      final String mobileNumber,
      final PartnerKey partnerKey)
      throws Exception {
    int count = 0;
    List<ConsignmentDTO> consignmentDTOList = new ArrayList<>();
    for (Map.Entry<String, ConsigneeDTO> map : consignment.entrySet()) {
      RiderDTO riderDTO =
          RiderDTO.builder()
              .name("rider" + map.getKey())
              .mobile("9916991699")
              .picture("https://m.media-amazon.com/images/I/81wqWCtgF1L._AC_UL640_FMwebp_QL65_.jpg")
              .build();
      TransportDetailsDTO transportDetailsDTO =
          TransportDetailsDTO.builder().vehicleNumber("ka03fj121").rider(riderDTO).build();

      List<BagDTO> bagDTOList = new ArrayList<>();
      for (int z = 0; z < baglist.size(); z++) {
        List<String> shipmentList = baglist.get(z);
        shipmentDTOList = createShipmentList(shipmentList);
        ShipmentsDTO shipmentsDTO =
            ShipmentsDTO.builder()
                .count(4 * shipmentList.size())
                .shipmentList(shipmentDTOList)
                .build();
        count += 4 * shipmentList.size();
        BagDTO bagDTO =
            BagDTO.builder()
                .id("234")
                .name("Milk")
                .type(BagType.BOX)
                .subType(BagSubType.LOOSE)
                .picture("https://m.media-amazon.com/images/I/818ZUl5f6TL._AC_UL640_FMwebp_QL65_.jpg")
                .shipments(shipmentsDTO)
                .build();
        bagDTOList.add(bagDTO);
      }
      AddressDTO addressDTO =
          AddressDTO.builder()
              .houseFlatNo(houseNo)
              .buildingName("")
              .street("")
              .societyName(society)
              .landmark("test")
              .locality("")
              .city("Bangalore")
              .state("Karnataka")
              .pincode("560037")
              .addressText("test")
              .type("home")
              .build();
      CustomerDTO customerDTO =
          CustomerDTO.builder()
              .name(TestBase.name)
              .mobile(mobileNumber)
              .address(addressDTO)
              .build();
      List<OrderShipmentDTO> orderShipmentDTOList = new ArrayList<>();
      int i = 0;

      for (Map.Entry<String, Integer> os : ordershipment.entrySet()) {
        List shipmentList = new ArrayList<>();
        int iteration = 0;
        int counter = os.getValue();
        while (counter > 0) {
          shipmentList.add(shipmentDTOList.get(i));
          i++;
          iteration++;
          counter--;
        }
        ShipmentsDTO shipmentsDTO =
            ShipmentsDTO.builder().count(4 * iteration).shipmentList(shipmentList).build();
        OrderShipmentDTO orderShipmentDTO =
            OrderShipmentDTO.builder()
                .orderId(os.getKey())
                .customer(customerDTO)
                .shipments(shipmentsDTO)
                .build();
        orderShipmentDTOList.add(orderShipmentDTO);
      }
      Integer startTime = Math.toIntExact(Instant.now().getEpochSecond() + 10000);
      Integer endTime = Math.toIntExact(Instant.now().getEpochSecond() + 100000);
      ConsignmentDTO consignmentDTO =
          ConsignmentDTO.builder()
              .consignmentId(map.getKey())
              .transportDetails(transportDetailsDTO)
              .etaStart(startTime)
              .etaEnd(endTime)
              .consignee(map.getValue())
              .bags(bagDTOList)
              .orderShipments(orderShipmentDTOList)
              .callbackURL("http://172.21.92.51:8080/general/log")
              .build();
      consignmentDTOList.add(consignmentDTO);
      log.info("Creating consignment for id: " + map.getKey());
    }
    ConsignmentsDTO consignmentsDTO =
        ConsignmentsDTO.builder().consignments(consignmentDTOList).build();
    HashMap<String, String> header = new HashMap<>();
    header.put("Content-Type", "application/json");
    header.put("x-api-key", partnerKey.getKey());
    ObjectMapper mapper = new ObjectMapper();
    String body = mapper.writeValueAsString(consignmentsDTO);
    log.info("Firing API: " + createConsignmentUrl);
    HttpResponse response = this.httpUtility.post(header, body, createConsignmentUrl);
    return response;
  }

  public HttpResponse getConsignmentviaConsignmentIdForPartner(
      String consignmentId, PartnerKey partnerKey) throws Exception {
    log.info("Partner side GET api for consignment: " + consignmentId);
    HashMap<String, String> headers = new HashMap<>();
    headers.put("x-api-key", partnerKey.getKey());
    headers.put("Content-Type", "application/json");
    String url = getConsignmentForPartnerurl + "/" + consignmentId;
    HttpResponse response = httpUtility.get(headers, url);
    return response;
  }

  public HttpResponse getConsignmentviaConsignmentIdUsingDeliveryAPI(
      String consignmentId, String partnerId) throws Exception {
    log.info("deliveryAPI GET for consignment: " + consignmentId);
    HashMap<String, String> headers = new HashMap<>();
    headers.put("api-key", deliveryAuth);
    headers.put("Content-Type", "application/json");
    String url =
        getConsignmentfromDeliveryApi
            + "/"
            + consignmentId
            + "?partner_id="
            + PartnerId.valueOf(partnerId).getKey()
            + "&external-request=false";
    HttpResponse response = httpUtility.get(headers, url);
    log.info(response.toString());
    return response;
  }

  private List createShipmentList(List<String> shipmentList) {
    List<ShipmentDTO> shipmentDTOList = new ArrayList<>();
    for (int k = 0; k < shipmentList.size(); k++) {
      ShipmentDTO shipmentDTO =
          ShipmentDTO.builder()
              .id(shipmentList.get(k))
              .name("Onion" + shipmentList.get(k))
              .category(null)
              .subCategory(null)
              .pic("https://images-na.ssl-images-amazon.com/images/I/71785Jrt80L._SL1483_.jpg")
              .count(4)
              .build();
      shipmentDTOList.add(shipmentDTO);
    }
    return shipmentDTOList;
  }
}
