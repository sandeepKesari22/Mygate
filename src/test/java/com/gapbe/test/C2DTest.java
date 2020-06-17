package com.gapbe.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapbe.base.Constants;
import com.gapbe.base.TestBase;
import com.gapbe.commons.DTOs.requestDTOs.ConsigneeDTO;
import com.gapbe.commons.DTOs.requestDTOs.CustomerDTO;
import com.gapbe.commons.DTOs.requestDTOs.HandoverDTO;
import com.gapbe.commons.DTOs.requestDTOs.MatchedAddressDTO;
import com.gapbe.commons.DTOs.requestDTOs.OrderShipmentDTO;
import com.gapbe.commons.DTOs.requestDTOs.RiderDTO;
import com.gapbe.commons.DTOs.responseDTOs.CreateConsignmentsResponse;
import com.gapbe.commons.DTOs.responseDTOs.GetConsignmentResponseDTO;
import com.gapbe.commons.DTOs.responseDTOs.SimpleConsignmentDTO;
import com.gapbe.commons.DTOs.responseDTOs.StatusHistoryDTO;
import com.gapbe.commons.Utils.CommonUtils;
import com.gapbe.commons.enums.AddressMatchStatus;
import com.gapbe.commons.enums.ConsignmentStatus;
import com.gapbe.commons.enums.OrderShipmentStatus;
import com.gapbe.commons.enums.PartnerId;
import com.gapbe.commons.enums.PartnerKey;
import com.gapbe.commons.enums.RiderStatus;
import com.gapbe.lib.apilib.CollectorLib;
import com.gapbe.lib.apilib.ConsignmentLib;
import com.gapbe.lib.apilib.RiderLib;
import com.gapbe.lib.helperClass.ConsignmentHelper;
import com.gapbe.lib.helperClass.RequestForCollectRejectConsignment;
import dataProviders.ConsignmentDP;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Log4j2
public class C2DTest extends TestBase {
  ConsignmentLib consignmentLib;
  ConsignmentHelper consignmentHelper;
  RequestForCollectRejectConsignment requestForCollectRejectConsignment;
  RiderLib riderLib;
  CollectorLib collectorLib;
  CommonUtils commonUtils;

  @BeforeTest
  public void C2DTest() {
    this.consignmentLib = new ConsignmentLib();
    this.riderLib = new RiderLib();
    this.consignmentHelper = new ConsignmentHelper();
    this.collectorLib = new CollectorLib();
    this.requestForCollectRejectConsignment = new RequestForCollectRejectConsignment();
    this.commonUtils = new CommonUtils();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 1)
  public void createConsignmentTest(String partnerType) {
    SoftAssert softAssert = new SoftAssert();
    Calendar cal = Calendar.getInstance();
    try {
      ConsigneeDTO consigneeDTO =
          ConsigneeDTO.builder().societyName(societyName).city("Bengaluru").build();
      String consignmentId = "Consignment_automation" + cal.getTimeInMillis();
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
      orderShipment.put("201", 1);
      String houseNo = flatNo;
      String street = societyName;
      HttpResponse response =
          consignmentLib.consignmentCreation(
              consignment,
              baglist,
              orderShipment,
              houseNo,
              street,
              TestBase.mobileNumber,
              PartnerKey.valueOf(partnerType));
      if (response.getStatusLine().getStatusCode() != 200) {
        Assert.fail();
      }
      ObjectMapper objectMapper = new ObjectMapper();
      CreateConsignmentsResponse createConsignmentsResponse =
          objectMapper.readValue(
              response.getEntity().getContent(), CreateConsignmentsResponse.class);
      softAssert.assertEquals(createConsignmentsResponse.getEs(), 0, "ES mismatch");
      softAssert.assertEquals(
          createConsignmentsResponse.getMessage(),
          "Request Successfully Received",
          "message mismatch");
      softAssert.assertEquals(
          createConsignmentsResponse.getSuccessCount(), 1, "success count mismatch");
      SimpleConsignmentDTO simpleConsignmentDTO =
          createConsignmentsResponse.getConsignments().get(0);
      softAssert.assertEquals(
          simpleConsignmentDTO.getConsignmentId(), consignmentId, "consignmentID mismatch");
      softAssert.assertEquals(
          simpleConsignmentDTO.getStatus(), ConsignmentStatus.INITIATED, "status mismatch");
      log.info("Consignment created successfully");

      // After db connection is done, need to check for DUPLICATE scenario
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 2)
  public void getConsignmentTest(String partnerType) {
    // calling GET consignment/consignmentID with Partner url
    SoftAssert softAssert = new SoftAssert();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String consignmentId =
          consignmentHelper.createConsignment(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      Thread.sleep(1000);
      HttpResponse consignmentResponse =
          consignmentLib.getConsignmentviaConsignmentIdForPartner(
              consignmentId, PartnerKey.valueOf(partnerType));
      if (consignmentResponse.getStatusLine().getStatusCode() != 200) {
        Assert.fail();
      }
      GetConsignmentResponseDTO getConsignmentResponseDTO =
          objectMapper.readValue(
              consignmentResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      validationForGetConsignment(getConsignmentResponseDTO, consignmentId);
      // calling GET consignment/consignmentID with Ecom APi url
      HttpResponse httpResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(consignmentId, partnerType);
      if (httpResponse.getStatusLine().getStatusCode() != 200) {
        Assert.fail();
      }
      GetConsignmentResponseDTO getInternalConsignmentResponseDto =
          objectMapper.readValue(
              httpResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      validationForGetConsignment(getInternalConsignmentResponseDto, consignmentId);
      softAssert.assertEquals(
          getInternalConsignmentResponseDto.getPartnerId(),
          PartnerId.valueOf(partnerType).getKey(),
          "partnerId mismatch");
      softAssert.assertTrue(
          getInternalConsignmentResponseDto.getPartnerName().equalsIgnoreCase(partnerType),
          "partner name mismatch");
      RiderDTO riderDTO = getInternalConsignmentResponseDto.getTransportDetails().getRider();
      softAssert.assertEquals(
          riderDTO.getRiderStatus(), RiderStatus.INVITED, "Rider status mismatch");
      softAssert.assertNotNull(riderDTO.getPasscode(), "Passcode is NULL");
      List<OrderShipmentDTO> orderShipmentDTOList =
          getInternalConsignmentResponseDto.getOrderShipments();
      for (OrderShipmentDTO orderShipmentDTO : orderShipmentDTOList) {
        CustomerDTO customerDTO = orderShipmentDTO.getCustomer();
        softAssert.assertEquals(
            customerDTO.getCustomerStatus(), AddressMatchStatus.MATCH, "customer status mismatch");
        softAssert.assertEquals(
            customerDTO.getInternalStatus(),
            AddressMatchStatus.MG_MATCH,
            "Internal status mismatch");
        MatchedAddressDTO matchedAddressDTO = customerDTO.getAddress().getMatchedAddress();
        softAssert.assertEquals(
            matchedAddressDTO.getMatch(),
            AddressMatchStatus.FULL_MATCH_PHONE,
            "Address matched address failed");
        softAssert.assertEquals(
            orderShipmentDTO.getOrderShipmentStatus(),
            OrderShipmentStatus.CONFIRMED,
            "Order shipment status not in CONFIRMED state");
        log.info("Get call of delivery and partner API verified");
      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 3)
  public void riderStatusUpdateTest(String partnerType) {
    SoftAssert softAssert = new SoftAssert();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      GetConsignmentResponseDTO getConsignmentResponseDTO =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      RiderDTO riderDTO = getConsignmentResponseDTO.getTransportDetails().getRider();
      HttpResponse response =
          riderLib.riderStatusUpdate(
              riderDTO.getPasscode(), riderDTO.getGuestId(), RiderStatus.ARRIVED);
      GetConsignmentResponseDTO getConsignmentResponseAfterRiderUpdate =
          objectMapper.readValue(
              response.getEntity().getContent(), GetConsignmentResponseDTO.class);
      softAssert.assertEquals(
          getConsignmentResponseAfterRiderUpdate.getStatus(),
          ConsignmentStatus.ARRIVED,
          "Rider status mismatch");
      RiderDTO riderDTOpostUpdate =
          getConsignmentResponseAfterRiderUpdate.getTransportDetails().getRider();
      softAssert.assertEquals(
          riderDTOpostUpdate.getStatus(),
          RiderStatus.ARRIVED,
          "rider status in RIDER DTO is not correct");
      log.info("Rider has successfully been ARRIVED");
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 4)
  public void collectorLoginAndCollectionTest(String partnerType) {
    SoftAssert softAssert = new SoftAssert();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      HttpResponse loginResponse =
          collectorLib.getcollectorLoginResponse(Constants.COLLECTOR_PASSCODE.value());
      JSONObject json = commonUtils.getJsonObjectViaEntity(loginResponse);
      String authKey = commonUtils.getKeyValueViaJsonObject(json, "auth_token");
      String collectorId = commonUtils.getKeyValueViaJsonObject(json, "gaurdid");
      GetConsignmentResponseDTO consignmentResponseDTO =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      RiderDTO riderDTO = consignmentResponseDTO.getTransportDetails().getRider();
      riderLib.riderStatusUpdate(
          riderDTO.getPasscode(), riderDTO.getGuestId(), RiderStatus.ARRIVED);
      GetConsignmentResponseDTO consignmentResponseDTO01 =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      String request =
          requestForCollectRejectConsignment.collectConsignmentRequestGenerator(
              consignmentResponseDTO01);
      HttpResponse response = collectorLib.collectConsignment(authKey, request);
      Thread.sleep(3000);
      softAssert.assertEquals(
          response.getStatusLine().getStatusCode(), 200, "response was non-200");
      HttpResponse consignmentResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(
              consignmentResponseDTO01.getConsignmentId(), partnerType);
      GetConsignmentResponseDTO consignmentPostCollection =
          objectMapper.readValue(
              consignmentResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      softAssert.assertEquals(
          consignmentPostCollection.getPartnerId(),
          PartnerId.valueOf(partnerType).getKey(),
          "Partner id mismatch");
      softAssert.assertTrue(
          consignmentPostCollection.getPartnerName().equalsIgnoreCase(partnerType),
          "partner name mismatch");
      softAssert.assertEquals(
          consignmentPostCollection.getStatus(), ConsignmentStatus.COLLECTED, "status mismatch");
      softAssert.assertEquals(
          consignmentPostCollection.getConsignee().getActiveCollector(),
          collectorId,
          "Active collector mismatch");
      softAssert.assertEquals(
          consignmentPostCollection.getConsignee().getActiveCollectorDetail().getId().toString(),
          collectorId,
          "active collector details mismatch");
      softAssert.assertNotNull(
          consignmentPostCollection.getOrderShipments().get(0).getShipmentPasscode(),
          "shipment passcode  not generated");
      softAssert.assertNotNull(
          consignmentPostCollection.getOrderShipments().get(0).getShipmentParcelId(),
          "shipment parcel ID  not generated");
      softAssert.assertEquals(
          consignmentPostCollection.getOrderShipments().get(0).getOrderShipmentStatus(),
          OrderShipmentStatus.NOTIFIED,
          "OrdershipmentStatus mismatched");

    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 5)
  public void consignmentHandoverTest(String partnerType) {
    SoftAssert softAssert = new SoftAssert();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      HttpResponse loginResponse =
          collectorLib.getcollectorLoginResponse(Constants.COLLECTOR_PASSCODE.value());
      JSONObject json = commonUtils.getJsonObjectViaEntity(loginResponse);
      String authKey = commonUtils.getKeyValueViaJsonObject(json, "auth_token");
      GetConsignmentResponseDTO consignmentResponseDTO =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      RiderDTO riderDTO = consignmentResponseDTO.getTransportDetails().getRider();
      riderLib.riderStatusUpdate(
          riderDTO.getPasscode(), riderDTO.getGuestId(), RiderStatus.ARRIVED);
      GetConsignmentResponseDTO consignmentResponseDTO01 =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      String request =
          requestForCollectRejectConsignment.collectConsignmentRequestGenerator(
              consignmentResponseDTO01);
      HttpResponse response = collectorLib.collectConsignment(authKey, request);
      softAssert.assertEquals(
          response.getStatusLine().getStatusCode(),
          200,
          "non-200 response for collect consignment");
      HttpResponse consignmentResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(
              consignmentResponseDTO01.getConsignmentId(), partnerType);
      GetConsignmentResponseDTO consignmentPostCollection =
          objectMapper.readValue(
              consignmentResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      int trial = 1;
      while (trial > 0) {
        if (consignmentPostCollection.getStatus().equals("COLLECTED")) {
          log.info("consignment collected");
          break;
        }
        trial--;
        Thread.sleep(5000);
        log.info("Consignment still not COLLECTED, hence waiting with trial: " + trial);
      }
      log.info(consignmentPostCollection.toString());
      OrderShipmentDTO orderShipmentDTO = consignmentPostCollection.getOrderShipments().get(0);
      orderShipmentDTO.setOrderShipmentStatus(OrderShipmentStatus.DELIVERED);

      HandoverDTO handoverDTO =
          HandoverDTO.builder()
              .consignmentId(consignmentPostCollection.getConsignmentId())
              .requestId(consignmentPostCollection.getRequestId())
              .shipment(orderShipmentDTO)
              .es("test")
              .message("test")
              .build();

      HttpResponse res = collectorLib.consignmentHandover(authKey, handoverDTO);
      softAssert.assertEquals(
          res.getStatusLine().getStatusCode(),
          200,
          "git non-200 response for handover consignment");
      HttpResponse postHandoverResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(
              consignmentPostCollection.getConsignmentId(), partnerType);
      GetConsignmentResponseDTO consignmentPostHandover =
          objectMapper.readValue(
              postHandoverResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      softAssert.assertEquals(
          consignmentPostHandover.getStatus(),
          ConsignmentStatus.CLOSED,
          "consignment still not closed");
      softAssert.assertEquals(
          consignmentPostHandover.getOrderShipments().get(0).getOrderShipmentStatus(),
          OrderShipmentStatus.DELIVERED,
          "order still not in DELIVERED state");
      softAssert.assertNotNull(
          consignmentPostHandover.getOrderShipments().get(0).getDeliveredTo().getId(),
          "no id found");
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "partnerList", dataProviderClass = ConsignmentDP.class, priority = 6)
  public void consignmentRejectionTest(String partnerType) {
    SoftAssert softAssert = new SoftAssert();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      GetConsignmentResponseDTO getConsignmentResponseDTO =
          consignmentHelper.createAndGetConsignmentResponse(
              societyName, flatNo, TestBase.mobileNumber, partnerType);
      riderLib.riderStatusUpdate(
          getConsignmentResponseDTO.getTransportDetails().getRider().getPasscode(),
          getConsignmentResponseDTO.getTransportDetails().getRider().getGuestId(),
          RiderStatus.ARRIVED);
      HttpResponse arrivedResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(
              getConsignmentResponseDTO.getConsignmentId(), partnerType);
      GetConsignmentResponseDTO arrivedconsignment =
          objectMapper.readValue(
              arrivedResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      HttpResponse response =
          collectorLib.getcollectorLoginResponse(Constants.COLLECTOR_PASSCODE.value());
      JSONObject object = commonUtils.getJsonObjectViaEntity(response);
      String authKey = commonUtils.getKeyValueViaJsonObject(object, "auth_token");
      String request =
          requestForCollectRejectConsignment.rejectConsignmentRequestGenerator(arrivedconsignment);
      collectorLib.collectConsignment(authKey, request);
      HttpResponse postHandoverResponse =
          consignmentLib.getConsignmentviaConsignmentIdUsingDeliveryAPI(
              getConsignmentResponseDTO.getConsignmentId(), partnerType);
      GetConsignmentResponseDTO consignmentPostRejection =
          objectMapper.readValue(
              postHandoverResponse.getEntity().getContent(), GetConsignmentResponseDTO.class);
      softAssert.assertEquals(
          consignmentPostRejection.getStatus(), ConsignmentStatus.REJECTED, "status mismatch");

    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test
  public void verifyNoAutoCloseTest() {
    SoftAssert softAssert = new SoftAssert();
    Calendar cal = Calendar.getInstance();
    try {
      ConsigneeDTO consigneeDTO =
          ConsigneeDTO.builder().societyName(societyName).city("Bengaluru").build();
      String consignmentId = "Consignment_automation" + cal.getTimeInMillis();
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
      orderShipment.put("201", 1);
      String houseNo = flatNo;
      String street = societyName;
      HttpResponse response =
          consignmentLib.consignmentCreation(
              consignment,
              baglist,
              orderShipment,
              houseNo,
              street,
              TestBase.mobileNumber,
              PartnerKey.valueOf(PartnerKey.FLIPKART.toString()));
      if (response.getStatusLine().getStatusCode() != 200) {
        log.info("response is non-200");
        Assert.fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  @Test(dataProvider = "addressCombinations", dataProviderClass = ConsignmentDP.class, priority = 6)
  public void addressTypeCombinationTest(
      String mobileNumber, String flatNo, AddressMatchStatus internalStatus) {
    SoftAssert softAssert = new SoftAssert();
    try {
      switch (internalStatus) {
        case MG_MATCH:
          GetConsignmentResponseDTO getConsignmentForMatch =
              consignmentHelper.createAndGetConsignmentResponse(
                  TestBase.societyName, flatNo, mobileNumber, PartnerKey.FLIPKART.toString());
          softAssert.assertEquals(
              getConsignmentForMatch.getStatus(), ConsignmentStatus.CONFIRMED, "status mismtach");
          CustomerDTO customerDTOForMatch =
              getConsignmentForMatch.getOrderShipments().get(0).getCustomer();
          softAssert.assertEquals(
              customerDTOForMatch.getCustomerStatus(),
              AddressMatchStatus.MATCH,
              "Customer status mismatch");
          softAssert.assertEquals(
              customerDTOForMatch.getInternalStatus(),
              AddressMatchStatus.MG_MATCH,
              "Internal status mismatch");
          softAssert.assertEquals(
              customerDTOForMatch.getAddress().getMatchedAddress().getMatch(),
              AddressMatchStatus.FULL_MATCH_PHONE,
              "matched Address status mismatch");
          break;

        case NON_MG_MATCH:
          GetConsignmentResponseDTO getConsignmentResponseDTO =
              consignmentHelper.createAndGetConsignmentResponse(
                  TestBase.societyName, flatNo, mobileNumber, PartnerKey.FLIPKART.toString());
          softAssert.assertEquals(
              getConsignmentResponseDTO.getStatus(),
              ConsignmentStatus.PARTIAL_CONFIRMED,
              "status mismtach");
          CustomerDTO customerDTO =
              getConsignmentResponseDTO.getOrderShipments().get(0).getCustomer();
          softAssert.assertEquals(
              customerDTO.getCustomerStatus(),
              AddressMatchStatus.MATCH,
              "Customer status mismatch");
          softAssert.assertEquals(
              customerDTO.getInternalStatus(),
              AddressMatchStatus.NON_MG_MATCH,
              "Internal status mismatch");
          softAssert.assertEquals(
              customerDTO.getAddress().getMatchedAddress().getMatch(),
              AddressMatchStatus.FULL_MATCH_SCORE,
              "matched Address status mismatch");
          break;

        case MG_NO_MATCH:
          GetConsignmentResponseDTO getConsignmentResponseDTO01 =
              consignmentHelper.createAndGetConsignmentResponse(
                  TestBase.societyName, flatNo, mobileNumber, PartnerKey.FLIPKART.toString());
          softAssert.assertEquals(
              getConsignmentResponseDTO01.getStatus(),
              ConsignmentStatus.PARTIAL_CONFIRMED,
              "status mismtach");
          CustomerDTO customerDTO01 =
              getConsignmentResponseDTO01.getOrderShipments().get(0).getCustomer();
          softAssert.assertEquals(
              customerDTO01.getCustomerStatus(),
              AddressMatchStatus.NO_MATCH,
              "Customer status mismatch");
          softAssert.assertEquals(
              customerDTO01.getInternalStatus(),
              AddressMatchStatus.MG_NO_MATCH,
              "Internal status mismatch");
          softAssert.assertEquals(
              customerDTO01.getAddress().getMatchedAddress().getMatch(),
              AddressMatchStatus.RASA_MATCH,
              "matched Address status mismatch");
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }

  private void validationForGetConsignment(
      GetConsignmentResponseDTO getConsignmentResponseDTO, String consignmentId) {
    SoftAssert softAssert = new SoftAssert();
    try {

      softAssert.assertEquals(
          getConsignmentResponseDTO.getConsignmentId(), consignmentId, "consignmentId mismatch");
      softAssert.assertNotNull(getConsignmentResponseDTO.getRequestId(), "requestId is null");
      softAssert.assertEquals(
          getConsignmentResponseDTO.getStatus(), ConsignmentStatus.CONFIRMED, "status mismatch");
      StatusHistoryDTO statusHistoryDTO = getConsignmentResponseDTO.getStatusHistory().get(2);
      softAssert.assertEquals(
          statusHistoryDTO.getStatus(), ConsignmentStatus.CONFIRMED, "status history mismatch");
      softAssert.assertEquals(
          getConsignmentResponseDTO.getOrderShipments().get(0).getOrderShipmentStatus(),
          OrderShipmentStatus.CONFIRMED,
          "order shipment status mismatch");

    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
    softAssert.assertAll();
  }
}
