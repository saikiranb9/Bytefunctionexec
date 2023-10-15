package com.atd.microservices.bytefunctionexecer.domain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.atd.microservices.bytefunctionexecer.configs.ApplicationConstants.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "BYTEFUNCTION_DATA")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ByteFunctionData {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    private String id;
    private String uniqueId;
    private String script;
    private String description;
    private String name;
    private String language;

    public boolean hasAUniqueId(){
        log.info("this.uniqueID check : "+this.uniqueId);

        if(this.uniqueId == null || this.uniqueId.isEmpty()){
            return false;
        }else{
            return true;
        }
    }


}
