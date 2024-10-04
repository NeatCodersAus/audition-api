package com.audition.common.jackson;

import static com.audition.common.Constants.YEAR_MONTH_DAY_PATTERN;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A custom serializer for serializing LocalDate objects to JSON strings using the Jackson library. This class extends
 * JsonSerializer and overrides the serialize method to provide the custom serialization logic.
 *
 * <p>The serialization process converts the LocalDate object to a string formatted according to the
 * YEAR_MONTH_DAY_FORMATTER pattern. If the LocalDate object is null, no string is written.
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    public static final DateTimeFormatter YEAR_MONTH_DAY_FORMATTER =
        DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_PATTERN);

    @Override
    public void serialize(final LocalDate value,
        final JsonGenerator jsonGenerator,
        final SerializerProvider serializers) throws IOException {
        if (value != null) {
            jsonGenerator.writeString(value.format(YEAR_MONTH_DAY_FORMATTER));
        }
    }
}
