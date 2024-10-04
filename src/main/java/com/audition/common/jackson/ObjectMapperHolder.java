package com.audition.common.jackson;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * A utility class that holds a pre-configured instance of {@link ObjectMapper}.
 *
 * <p>The {@link ObjectMapper} instance is configured with custom serializers and deserializers for {@link LocalDate}
 * and {@link OffsetDateTime} types.
 *
 * <p>The following configurations are applied to the {@link ObjectMapper} instance:
 * <ul>
 *   <li>Custom serializers and deserializers for {@link LocalDate} and {@link OffsetDateTime}.</li>
 *   <li>Disables failure on unknown properties during deserialization.</li>
 *   <li>Uses lower camel case for property naming strategy.</li>
 *   <li>Includes non-null and non-empty properties by default.</li>
 * </ul>
 */
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
