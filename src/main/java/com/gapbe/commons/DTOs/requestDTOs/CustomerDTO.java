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
public class CustomerDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("mobile")
    private String mobile = null;

    @JsonProperty("status")
    private AddressMatchStatus status = null;

    @JsonProperty("customerStatus")
    private AddressMatchStatus customerStatus = null;

    @JsonProperty("internalStatus")
    private AddressMatchStatus internalStatus = null;

    @JsonProperty("address")
    private AddressDTO address = null;
}
