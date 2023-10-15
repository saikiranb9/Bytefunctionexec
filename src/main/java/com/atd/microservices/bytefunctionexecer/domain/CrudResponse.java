package com.atd.microservices.bytefunctionexecer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrudResponse {
    private String status;
    private String details;
    private String uniqueId;
}
