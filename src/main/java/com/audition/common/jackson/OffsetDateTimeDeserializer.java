package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 * A custom deserializer for deserializing OffsetDateTime objects from JSON strings using the Jackson library. This
 * class extends JsonDeserializer and overrides the deserialize method to provide the custom deserialization logic.
 *
 * <p>The deserialization process checks if the JSON string is blank and returns null if it is. Otherwise, it uses the
 * DateTimeFormatter.ISO_OFFSET_DATE_TIME to parse and return the OffsetDateTime object.
 */
public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(final JsonParser jsonParser,
        final DeserializationContext deserializationContext) throws IOException {
        return StringUtils.isBlank(jsonParser.getValueAsString()) ? null
            : OffsetDateTime.parse(jsonParser.getValueAsString(),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
