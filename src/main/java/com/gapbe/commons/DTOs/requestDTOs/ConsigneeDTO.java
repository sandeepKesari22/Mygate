package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.DTOs.responseDTOs.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsigneeDTO {
    @JsonProperty("flatId")
    private String flatId = null;

    @JsonProperty("societyId")
    private String societyId = null;

    @JsonProperty("societyNameFromDB")
    private String societyNameFromDB = null;

    @JsonProperty("societyName")
    private String societyName = null;

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("collectors")
    private List<CollectorDTO> collectors = null;

    @JsonProperty("collectedBy")
    private Person collectedBy;

    @JsonProperty("activeCollector")
    private String activeCollector;

    @JsonProperty("activeCollectorDetail")
    private Person activeCollectorDetail;

    @JsonProperty("addressText")
    private String addressText;

}
