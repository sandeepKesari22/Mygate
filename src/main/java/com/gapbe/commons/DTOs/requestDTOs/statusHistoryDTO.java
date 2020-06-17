package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.OrderShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class statusHistoryDTO {
    @JsonProperty("status")
    private OrderShipmentStatus status = null;

    @JsonProperty("timestamp")
    private String timestamp = null;

}
