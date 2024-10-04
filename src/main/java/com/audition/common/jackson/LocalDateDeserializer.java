package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;

/**
 * A custom deserializer for deserializing LocalDate objects from JSON strings using the Jackson library. This class
 * extends JsonDeserializer and overrides the deserialize method to provide the custom deserialization logic.
 *
 * <p>The deserialization process checks if the JSON string is blank and returns null if it is. Otherwise, it uses the
 * LocalDateSerializer.YEAR_MONTH_DAY_FORMATTER to parse and return the LocalDate object.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(final JsonParser jsonParser, final DeserializationContext context)
        throws IOException {
        return StringUtils.isBlank(jsonParser.getValueAsString()) ? null
            : LocalDate.parse(jsonParser.getValueAsString(), LocalDateSerializer.YEAR_MONTH_DAY_FORMATTER);
    }
}
