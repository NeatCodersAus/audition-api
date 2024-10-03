package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OffsetDateTimeDeserializerTest {

    private final OffsetDateTimeDeserializer deserializer = new OffsetDateTimeDeserializer();

    @Test
    void shouldDeserialize() throws Exception {

        //Arrange
        // Mock json parser
        final OffsetDateTime result;
        try (JsonParser jsonParser = new JsonFactory().createParser("\"2023-04-01T10:15:30+01:00\"")) {
            jsonParser.nextToken();

            // Act
            result = deserializer.deserialize(jsonParser, null);
        }

        // Assert
        Assertions.assertEquals(OffsetDateTime.parse("2023-04-01T10:15:30+01:00"), result);
    }

    @Test
    void shouldReturnNullWhenDeserializeBlankValue() throws Exception {
        //Arrange
        // Mock json parser
        final OffsetDateTime result;
        try (JsonParser mockJsonParser = Mockito.mock(JsonParser.class)) {
            Mockito.when(mockJsonParser.getValueAsString()).thenReturn("");

            // Act
            result = deserializer.deserialize(mockJsonParser, null);
        }

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    void shouldThrowExceptionWhenDeserializeInvalidFormat() throws IOException {
        //Arrange
        // Mock json parser
        try (JsonParser mockJsonParser = Mockito.mock(JsonParser.class)) {
            Mockito.when(mockJsonParser.getValueAsString()).thenReturn("invalidFormat");

            // Assert
            Assertions.assertThrows(
                Exception.class,
                () -> deserializer.deserialize(mockJsonParser, null),
                "Expected to throw exception on invalid date format"
            );
        }
    }
}