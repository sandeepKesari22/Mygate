package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.RiderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiderDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("mobile")
    private String mobile = null;

    @JsonProperty("picture")
    private String picture = null;

    @JsonProperty("guestId")
    private String guestId;

    @JsonProperty("isVerified")
    private String isVerified;

    @JsonProperty("riderStatus")
    private RiderStatus riderStatus;

    @JsonProperty("status")
    private RiderStatus status;

    @JsonProperty("passcode")
    private String passcode;
}
