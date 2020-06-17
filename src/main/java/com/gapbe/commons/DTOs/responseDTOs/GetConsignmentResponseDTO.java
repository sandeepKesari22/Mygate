package com.gapbe.commons.DTOs.responseDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.DTOs.requestDTOs.BagDTO;
import com.gapbe.commons.DTOs.requestDTOs.ConsigneeDTO;
import com.gapbe.commons.DTOs.requestDTOs.OrderShipmentDTO;
import com.gapbe.commons.DTOs.requestDTOs.TransportDetailsDTO;
import com.gapbe.commons.enums.ConsignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetConsignmentResponseDTO {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("consignmentId")
  private String consignmentId = null;

  @JsonProperty("requestId")
  private String requestId = null;

  @JsonProperty("status")
  private ConsignmentStatus status = null;

  @JsonProperty("reason")
  private String reason = null;

  @JsonProperty("statusHistory")
  private List<StatusHistoryDTO> statusHistory = new ArrayList<>();

  @JsonProperty("transportDetails")
  private TransportDetailsDTO transportDetails = null;

  @JsonProperty("etaStart")
  private Integer etaStart = null;

  @JsonProperty("etaEnd")
  private Integer etaEnd = null;

  @JsonProperty("consignee")
  private ConsigneeDTO consignee = null;

  @JsonProperty("bags")
  private List<BagDTO> bags = new ArrayList<>();

  @JsonProperty("orderShipments")
  private List<OrderShipmentDTO> orderShipments = new ArrayList<>();

  @JsonProperty("callbackURL")
  private String callbackURL = null;

  @JsonProperty("dayId")
  private String dayId = null;

  @JsonProperty("partnerId")
  private String partnerId;

  @JsonProperty("partnerName")
  private String partnerName;

  @JsonProperty("partnerImage")
  private String partnerImage;

  @JsonProperty("expiryTime")
  private Long expiryTime;

  @JsonProperty("gateId")
  private String gateId;

  @JsonProperty("collectedBy")
  private Person collectedBy;

  @JsonProperty("creationTime")
  private String creationTime;

  @JsonProperty("createdBy")
  private String createdBy;

  @JsonProperty("lastUpdatedTime")
  private String lastUpdatedTime;

  @JsonProperty("lastUpdatedBy")
  private String lastUpdatedBy;
}
