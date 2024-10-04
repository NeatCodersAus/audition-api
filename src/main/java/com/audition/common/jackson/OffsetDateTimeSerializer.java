package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A custom serializer for serializing OffsetDateTime objects to JSON strings using the Jackson library. This class
 * extends JsonSerializer and overrides the serialize method to provide the custom serialization logic.
 *
 * <p>The serialization process converts the OffsetDateTime object to a string formatted according to the
 * DateTimeFormatter.ISO_OFFSET_DATE_TIME pattern. If the OffsetDateTime object is null, no string is written.
 */
public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(final OffsetDateTime offsetDateTime,
        final JsonGenerator jsonGenerator,
        final SerializerProvider serializerProvider) throws IOException {
        if (offsetDateTime != null) {
            jsonGenerator.writeString(offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
    }
}
