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
public class CollectorDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("mobile")
    private String mobile = null;

}


