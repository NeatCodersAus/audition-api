package com.audition.common.jackson;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class ObjectMapperHolderTest {

    @Test
    void testMapperHasSerializerForLocalDateAndOffsetDateTime() {
        assertEquals(1, ObjectMapperHolder.mapper().getRegisteredModuleIds().size());
        assertTrue(ObjectMapperHolder.mapper().getRegisteredModuleIds().contains("jackson-datatype-jsr310"));

        assertDoesNotThrow(() -> ObjectMapperHolder.mapper().writer().writeValueAsString(LocalDate.now()));
        assertDoesNotThrow(() -> ObjectMapperHolder.mapper().writer().writeValueAsString(OffsetDateTime.now()));
    }

    @Test
    void testMapperIgnoresUnknownProperties() {
        assertFalse(ObjectMapperHolder.mapper().getDeserializationConfig()
            .isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    @Test
    void testMapperUsesLowerCamelCase() {
        assertEquals(PropertyNamingStrategies.LOWER_CAMEL_CASE,
            ObjectMapperHolder.mapper().getPropertyNamingStrategy());
    }

    //Testing using a sample object to verify if Null and Empty values are being ignored
    //Though this is not the unit test of the code that is written by us (this test verifies library code actually)
    //Couldn't find better way to verify if PropertyInclusion of NON_NULL and NON_EMPTY is set
    record Person(String name, String address) {

    }

    @Test
    void testMapperDoesNotIncludeNullValues() throws JsonProcessingException {
        final String expectedPerson = "{\"name\":\"MyName\"}";
        assertEquals(expectedPerson,
            ObjectMapperHolder.mapper().writer().writeValueAsString(new Person("MyName", null)));
    }

    @Test
    void testMapperDoesNotMapEmptyValues() throws JsonProcessingException {
        final String expectedPerson = "{\"name\":\"MyName\"}";
        assertEquals(expectedPerson,
            ObjectMapperHolder.mapper().writer().writeValueAsString(new Person("MyName", "")));
    }

    @Test
    void testMapperMapsNonNullAndNonEmptyFields() throws JsonProcessingException {
        //Arrange
        final String samplePerson = """
            {"name": "MyName",
             "address": "someAddress"}
            """;

        //Act
        final Person actualPerson = ObjectMapperHolder.mapper().readValue(samplePerson, Person.class);

        //Assert
        assertEquals("MyName", actualPerson.name);
        assertEquals("someAddress", actualPerson.address);
    }
}