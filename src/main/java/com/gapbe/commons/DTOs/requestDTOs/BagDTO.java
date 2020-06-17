package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.BagSubType;
import com.gapbe.commons.enums.BagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BagDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("type")
    private BagType type = null;

    @JsonProperty("subType")
    private BagSubType subType = null;

    @JsonProperty("picture")
    private String picture = null;

    @JsonProperty("shipments")
    private ShipmentsDTO shipments = null;
}
