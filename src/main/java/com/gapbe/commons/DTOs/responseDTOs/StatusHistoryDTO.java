package com.gapbe.commons.DTOs.responseDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.ConsignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusHistoryDTO {
    @JsonProperty("status")
    private ConsignmentStatus status = null;

    @JsonProperty("timestamp")
    private Integer timestamp = null;
}