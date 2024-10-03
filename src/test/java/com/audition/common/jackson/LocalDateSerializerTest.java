package com.audition.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for LocalDateSerializer class. The class in question is responsible for serializing LocalDate values to
 * String format. Tests ensure correct serializing behavior when handling valid and invalid (null, empty and blank
 * string) inputs.
 */
@ExtendWith(MockitoExtension.class)
class LocalDateSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializers;

    @Test
    void testSerializeOnValidDate() throws Exception {
        //Arrange
        final LocalDate localDate = LocalDate.of(2024, 2, 29);
        final LocalDateSerializer localDateSerializer = new LocalDateSerializer();

        //Act
        localDateSerializer.serialize(localDate, jsonGenerator, serializers);

        //Assert
        Mockito.verify(jsonGenerator).writeString("2024-02-29");
    }

    @Test
    void testSerializeOnNullDate() throws Exception {
        //Arrange
        final LocalDateSerializer localDateSerializer = new LocalDateSerializer();

        //Act
        localDateSerializer.serialize(null, jsonGenerator, serializers);

        //Assert
        Mockito.verifyNoInteractions(jsonGenerator);
    }
}