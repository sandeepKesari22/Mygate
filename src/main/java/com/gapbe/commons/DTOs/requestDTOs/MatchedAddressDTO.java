package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.AddressMatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchedAddressDTO {
    @JsonProperty("societyId")
    private int societyId;

    @JsonProperty("societyName")
    private String societyName = null;

    @JsonProperty("buildingId")
    private String buildingId = null;

    @JsonProperty("buildingName")
    private String buildingName = null;

    @JsonProperty("flatId")
    private String flatId = null;

    @JsonProperty("flatName")
    private String flatName = null;

    @JsonProperty("match")
    private AddressMatchStatus match = null;

    @JsonProperty("matchedBy")
    private String matchedBy = null;

    @JsonProperty("matchedAt")
    private String matchedAt = null;

}
