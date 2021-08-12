package com.fhs.module.base.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import java.util.List;

public class FhsJsonSerializer extends JsonSerializer {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Json toJson(Object toSerialize) {
        try {
            return new Json(objectMapper.writeValueAsString(toSerialize));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not write JSON", e);
        }
    }
    public FhsJsonSerializer(List<JacksonModuleRegistrar> modules) {
        super(modules);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
