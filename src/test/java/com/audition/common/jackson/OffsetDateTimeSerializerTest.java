package com.audition.common.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import java.io.IOException;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
class OffsetDateTimeSerializerTest {

    @Test
    void testSerializeWithValidOffsetDateTimeAndVerifyItWritesFormattedDateTime() throws IOException {
        //Arrange
        final OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        final StringWriter writer = new StringWriter();

        //Act
        try (JsonGenerator generator = new JsonFactory().disable(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature())
            .createGenerator(writer)) {
            new OffsetDateTimeSerializer().serialize(offsetDateTime, generator, null);
        }

        //Assert
        final String expected = "\"2024-02-01T00:00:00Z\"";
        assertEquals(expected, writer.toString(), "Serialized string should be in ISO offset date-time format.");
    }

    @Test
    void testSerializeWithNullOffsetDateTimeAndVerifyItDoesNotThrowException() throws IOException {
        //Arrange
        final StringWriter writer = new StringWriter();

        //Act
        try (JsonGenerator generator = new JsonFactory().disable(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature())
            .createGenerator(writer)) {
            new OffsetDateTimeSerializer().serialize(null, generator, null);
        }

        //Assert
        assertEquals("", writer.toString(), "Serialized string should be empty if input OffsetDateTime is null.");
    }

}