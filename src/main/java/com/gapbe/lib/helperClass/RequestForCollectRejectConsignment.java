package com.gapbe.lib.helperClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapbe.commons.DTOs.responseDTOs.GetConsignmentResponseDTO;
import com.gapbe.commons.enums.ConsignmentStatus;

public class RequestForCollectRejectConsignment {
  public String collectConsignmentRequestGenerator(GetConsignmentResponseDTO getConsignmentResponseDTO) throws JsonProcessingException {
    /*Erase this classs once MOHAK has fixed the bug.*/
    getConsignmentResponseDTO.setStatus(ConsignmentStatus.COLLECTED);
    ObjectMapper ob = new ObjectMapper();
    String temp = ob.writeValueAsString(getConsignmentResponseDTO);
    return temp.replace("orderShipments", "shipmentList");
  }

  public String rejectConsignmentRequestGenerator(GetConsignmentResponseDTO getConsignmentResponseDTO) throws JsonProcessingException {
    /*Erase this classs once MOHAK has fixed the bug.*/
    getConsignmentResponseDTO.setStatus(ConsignmentStatus.REJECTED);
    ObjectMapper ob = new ObjectMapper();
    String temp = ob.writeValueAsString(getConsignmentResponseDTO);
    return temp.replace("orderShipments", "shipmentList");
  }
}
