package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentsDTO {
    @JsonProperty("count")
    private Integer count = null;

    @JsonProperty("shipmentList")
    private List<ShipmentDTO> shipmentList = new ArrayList<>();
}
