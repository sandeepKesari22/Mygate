package com.gapbe.lib.apilib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapbe.base.Constants;
import com.gapbe.base.TestBase;
import com.gapbe.commons.DTOs.requestDTOs.HandoverDTO;
import com.qautils.commonutils.HttpUtility;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;

import java.util.HashMap;
@Log4j2
public class CollectorLib {
  HttpUtility httpUtility;
  String collectorLoginUrl = Constants.COLLECTOR_LOGIN.value();
  String collectConsignmentUrl = Constants.COLLECT_CONSIGNMENT.value();
  String consignmentHandover = Constants.CONSIGNMENT_HANDOVER.value();

  public CollectorLib() {
    this.httpUtility = TestBase.httpUtility;
  }

  public HttpResponse getcollectorLoginResponse(String passcode) throws Exception {
    log.info("Logging the collector");
    HashMap<String, String> header = new HashMap<>();
    header.put("Content-Type", "application/json");
    String body =
        "{\n"
            + "\"passcode\":"
            + passcode
            + ",\n"
            + "\"deviceid\":863982043172951,\n"
            + "\"entrytype\":\"0\",\n"
            + "\"authtype\":\"LOGIN\",\n"
            + "\"language\":\"en-IN\"\n"
            + "\t\n"
            + "}";
    HttpResponse response = httpUtility.post(header, body, collectorLoginUrl);
    if(response.getStatusLine().getStatusCode() != 200){
      throw new Exception();
    }
    log.info("Collector logged in successfully");
    return response;
  }

  public HttpResponse collectConsignment(String authkey, String body) throws Exception{
    log.info("collecting the consignment");
    HashMap<String, String> header = new HashMap<>();
    header.put("Content-Type", "application/json");
    header.put("Authorization", "Bearer "+authkey);
    HttpResponse response = httpUtility.put(header, body, collectConsignmentUrl);
    return response;
  }

  public HttpResponse consignmentHandover(String authkey, HandoverDTO handoverDTO) throws Exception {
    log.info("Handing over the consignment with parcelId: " + handoverDTO.getShipment().getShipmentParcelId());
    HashMap<String, String> header = new HashMap<>();
    header.put("Content-Type", "application/json");
    header.put("Authorization", "Bearer "+authkey);
    ObjectMapper mapper = new ObjectMapper();
    String body = mapper.writeValueAsString(handoverDTO);
    HttpResponse response = httpUtility.put(header, body, consignmentHandover);
    return response;

  }
}
