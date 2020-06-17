package com.gapbe.commons.DTOs.responseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsignmentsResponse {
    @JsonProperty("es")
    private int es;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("successCount")
    private int successCount;

    @JsonProperty("failureCount")
    private int failureCount;

    @JsonProperty("consignments")
    private List<SimpleConsignmentDTO> consignments = new ArrayList<>();
}
