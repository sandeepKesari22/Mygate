package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HandoverDTO {

  @JsonProperty("consignmentId")
  private String consignmentId = null;

  @JsonProperty("requestId")
  private String requestId = null;

  @JsonProperty("shipment")
  private OrderShipmentDTO shipment = null;

  @JsonProperty("es")
  private String es = null;

  @JsonProperty("message")
  private String message = null;
}
