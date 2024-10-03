package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(final JsonParser jsonParser,
        final DeserializationContext deserializationContext) throws IOException {
        return StringUtils.isBlank(jsonParser.getValueAsString()) ? null
            : OffsetDateTime.parse(jsonParser.getValueAsString(),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
