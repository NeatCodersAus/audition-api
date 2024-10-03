package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.audition.common.Constants.YEAR_MONTH_DAY_PATTERN;

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
