package com.gapbe.lib.helperClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapbe.base.TestBase;
import com.gapbe.commons.DTOs.requestDTOs.ConsigneeDTO;
import com.gapbe.commons.DTOs.responseDTOs.GetConsignmentResponseDTO;
import com.gapbe.commons.enums.PartnerKey;
import com.gapbe.lib.apilib.ConsignmentLib;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConsignmentHelper {
  ConsignmentLib consignmentLib = new ConsignmentLib();
  String consignmentId = null;

  public String createConsignment(
      String societyName, String flatNo, String mobileNumber, String partnerType) throws Exception {
    Calendar cal = Calendar.getInstance();
    ConsigneeDTO consigneeDTO =
        ConsigneeDTO.builder().societyName(societyName).city("Bengaluru").build();
    consignmentId = "Consignment_automation" + cal.getTimeInMillis();
    HashMap<String, ConsigneeDTO> consignment = new HashMap<>();
    consignment.put(consignmentId, consigneeDTO);
    List<String> shipmentId = new ArrayList<>();
    shipmentId.add("234");
    shipmentId.add("235");
    shipmentId.add("236");
    List<List<String>> baglist = new ArrayList<>();
    baglist.add(shipmentId);
    HashMap<String, Integer> orderShipment = new HashMap<>();
    orderShipment.put("200", 2);
    String houseNo = flatNo;
    String street = societyName;
        consignmentLib.consignmentCreation(
            consignment, baglist, orderShipment, houseNo, street, mobileNumber, PartnerKey.valueOf(partnerType));
   return consignmentId;
  }

  public GetConsignmentResponseDTO createAndGetConsignmentResponse(String societyName, String flatNo, String mobileNumber, String partnerType) throws Exception {
    createConsignment(societyName, flatNo, mobileNumber, partnerType);
    Thread.sleep(1000);
    HttpResponse httpResponse =
            consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(consignmentId, partnerType);
    ObjectMapper objectMapper = new ObjectMapper();
    GetConsignmentResponseDTO getInternalConsignmentResponseDto =
            objectMapper.readValue(
                    httpResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
    return getInternalConsignmentResponseDto;
  }
}
