package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(final JsonParser jsonParser, final DeserializationContext context)
        throws IOException {
        return StringUtils.isBlank(jsonParser.getValueAsString()) ? null
            : LocalDate.parse(jsonParser.getValueAsString(), LocalDateSerializer.YEAR_MONTH_DAY_FORMATTER);
    }
}
