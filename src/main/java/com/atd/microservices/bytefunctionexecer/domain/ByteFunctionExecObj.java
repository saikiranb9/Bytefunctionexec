package com.atd.microservices.bytefunctionexecer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ByteFunctionExecObj {
/*

{
 "input" : {},
 "output" : {},
 "metadata" : {},
 "schema" : {},
 "optionalParams" : {},
 "uniqueId" : "uniqueId of function"
}
 */
    private String status;
    private String details;
    private JsonNode input;
    private JsonNode output;
    private JsonNode metadata;
    private JsonNode schema;
    private JsonNode optionalParams;
    @JsonIgnore
    private String funcUuid;
    private String uniqueId;
    private String script;
    private String language;
}
