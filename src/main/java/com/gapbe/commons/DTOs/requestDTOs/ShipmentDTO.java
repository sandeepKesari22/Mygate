package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("pic")
    private String pic = null;

    @JsonProperty("category")
    private String category = null;

    @JsonProperty("subCategory")
    private String subCategory = null;

    @JsonProperty("count")
    private Integer count = null;

    @JsonProperty("countCollected")
    private Integer countCollected = null;
}
