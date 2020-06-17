package com.gapbe.commons.DTOs.requestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gapbe.commons.enums.OrderShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderShipmentDTO {
    @JsonProperty("orderId")
    private String orderId = null;

    @JsonProperty("shipmentParcelId")
    private String shipmentParcelId = null;

    @JsonProperty("shipmentPasscode")
    private String shipmentPasscode = null;

    @JsonProperty("societyId")
    private Long societyId = null;

    @JsonProperty("customer")
    private CustomerDTO customer = null;

    @JsonProperty("shipments")
    private ShipmentsDTO shipments = null;

    @JsonProperty("deliveredBy")
    private DeliveredBy deliveredBy = null;

    @JsonProperty("deliveredTo")
    private DeliveredBy deliveredTo = null;

    @JsonProperty("status")
    private OrderShipmentStatus status = null;

    @JsonProperty("orderShipmentStatus")
    private OrderShipmentStatus orderShipmentStatus;

    @JsonProperty("statusHistory")
    private List<statusHistoryDTO>statusHistory;

    @JsonProperty("reminderEnabled")
    private boolean reminderEnabled;


}
