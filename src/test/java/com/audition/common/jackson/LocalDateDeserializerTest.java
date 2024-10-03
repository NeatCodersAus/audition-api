package com.audition.common.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for LocalDateDeserializer class. The class in question is responsible for deserializing LocalDate values
 * from String format using Jackson library. Tests ensure correct deserialization behavior when handling valid and
 * invalid (null, empty and blank string) inputs.
 */
@ExtendWith(MockitoExtension.class)
class LocalDateDeserializerTest {

    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;

    @Test
    void testDeserializeOnValidDate() throws IOException {

        //Arrange
        final String dateAsString = "2024-12-23";
        final LocalDate expectedDate = LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        when(jsonParser.getValueAsString()).thenReturn(dateAsString);
        final LocalDateDeserializer deserializer = new LocalDateDeserializer();

        //Act
        final LocalDate resultDate = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    @Test
    void testDeserializeOnNullDate() throws IOException {

        //Arrange
        when(jsonParser.getValueAsString()).thenReturn(null);
        final LocalDateDeserializer deserializer = new LocalDateDeserializer();

        //Act
        final LocalDate resultDate = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        assertNull(resultDate);
    }

    @Test
    void testDeserializeOnEmptyDate() throws IOException {

        //Arrange
        final String emptyDate = StringUtils.EMPTY;
        when(jsonParser.getValueAsString()).thenReturn(emptyDate);

        final LocalDateDeserializer deserializer = new LocalDateDeserializer();

        //Act
        final LocalDate resultDate = deserializer.deserialize(jsonParser, deserializationContext);

        //Assert
        assertNull(resultDate);
    }
}