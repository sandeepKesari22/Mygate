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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AddressDTO {
    @JsonProperty("houseFlatNo")
    private String houseFlatNo = null;

    @JsonProperty("buildingName")
    private String buildingName = null;

    @JsonProperty("societyName")
    private String societyName = null;

    @JsonProperty("street")
    private String street = null;

    @JsonProperty("landmark")
    private String landmark = null;

    @JsonProperty("locality")
    private String locality = null;

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("state")
    private String state = null;

    @JsonProperty("pincode")
    private String pincode = null;

    @JsonProperty("addressText")
    private String addressText = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("matchedAddress")
    private MatchedAddressDTO matchedAddress = null;
}
