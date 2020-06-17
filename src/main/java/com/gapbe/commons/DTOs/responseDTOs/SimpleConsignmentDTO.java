package com.gapbe.commons.DTOs.responseDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SimpleConsignmentDTO {
    @JsonProperty("consignmentId")
    private String consignmentId = null;

    @JsonProperty("requestId")
    private String requestId = null;

    @JsonProperty("status")
    private ConsignmentStatus status = null;

    @JsonProperty("statusHistory")
    private List<StatusHistoryDTO> statusHistory = new ArrayList<>();
}
