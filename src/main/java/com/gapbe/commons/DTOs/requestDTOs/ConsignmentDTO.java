package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsignmentDTO {
    @JsonProperty("consignmentId")
    private String consignmentId = null;

    @JsonProperty("transportDetails")
    private TransportDetailsDTO transportDetails = null;

    @JsonProperty("etaStart")
    private Integer etaStart = null;

    @JsonProperty("etaEnd")
    private Integer etaEnd = null;

    @JsonProperty("consignee")
    private ConsigneeDTO consignee = null;

    @JsonProperty("bags")
    private List<BagDTO> bags = new ArrayList<>();

    @JsonProperty("orderShipments")
    private List<OrderShipmentDTO> orderShipments = new ArrayList<>();

    @JsonProperty("callbackURL")
    private String callbackURL = null;
}
