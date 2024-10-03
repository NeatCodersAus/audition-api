package com.audition.common.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;

public class ObjectMapperHolder {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());

        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setPropertyNamingStrategy(LOWER_CAMEL_CASE);
        OBJECT_MAPPER.setDefaultPropertyInclusion(Include.NON_NULL);
        OBJECT_MAPPER.setDefaultPropertyInclusion(Include.NON_EMPTY);

    }

    public static ObjectMapper mapper() {
        return OBJECT_MAPPER;
    }

}
