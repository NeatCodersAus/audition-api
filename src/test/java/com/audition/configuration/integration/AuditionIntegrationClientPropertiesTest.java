package com.audition.configuration.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponents;

@SpringBootTest
@TestPropertySource(properties = {
    "audition.integration.client.api.host=https://localhost",
    "audition.integration.client.api.auditionPostsPath=/posts",
    "audition.integration.client.api.auditionPostPath=/posts/{postId}",
    "audition.integration.client.api.auditionPostCommentsPath=/posts/{postId}/comments",
    "audition.integration.client.api.auditionCommentsPath=/comments",
})
class AuditionIntegrationClientPropertiesTest {

    @Autowired
    private AuditionIntegrationClientProperties auditionIntegrationClientProperties;

    @Test
    void testGetAuditionPostUriWithEmptyPostId() {
        assertThrows(IllegalArgumentException.class, () ->
            auditionIntegrationClientProperties.getAuditionPostUri(""));
    }

    @Test
    void testGetAuditionPostCommentsUriWithEmptyPostId() {
        assertThrows(IllegalArgumentException.class, () ->
            auditionIntegrationClientProperties.getAuditionPostCommentsUri(""));
    }

    @Test
    void testGetAuditionCommentsUriWithEmptyPostId() {
        assertThrows(IllegalArgumentException.class, () ->
            auditionIntegrationClientProperties.getAuditionCommentsUri(""));
    }

    @Test
    void testGetAuditionCommentsUriWithNullPostId() {
        assertThrows(IllegalArgumentException.class, () ->
            auditionIntegrationClientProperties.getAuditionCommentsUri(null));
    }

    @Test
    void testGetAuditionPostsUri() {
        assertEquals("https://localhost/posts",
            auditionIntegrationClientProperties.getAuditionPostsUri().toString());
    }

    @Test
    void testGetAuditionPostUri() {
        assertEquals("https://localhost/posts/12345",
            auditionIntegrationClientProperties.getAuditionPostUri("12345").toString());
    }

    @Test
    void testGetAuditionPostCommentsUri() {
        assertEquals("https://localhost/posts/12345/comments",
            auditionIntegrationClientProperties.getAuditionPostCommentsUri("12345").toString());
    }

    @Test
    void testGetAuditionCommentsUri() {
        assertEquals("https://localhost/comments?postId=12345",
            auditionIntegrationClientProperties.getAuditionCommentsUri("12345").toString());
    }


    @Test
    void testUriComponentsBuilderForWithBasePath() {
        //Arrange
        final AbstractClientProperties properties = new AuditionIntegrationClientProperties();
        properties.setHost("https://localhost");
        properties.setBasePath("/api");

        //Act
        final UriComponents uriComponents = properties.uriComponentsBuilderFor("/posts").build();

        //Assert
        assertEquals("https://localhost/api/posts", uriComponents.toUriString());
    }

    @Test
    void testUriComponentsBuilderForWithoutBasePath() {
        //Arrange
        final AbstractClientProperties properties = new AuditionIntegrationClientProperties();
        properties.setHost("https://localhost");
        properties.setBasePath(null); // Or we could skip setting basePath altogether

        //Act
        final UriComponents uriComponents = properties.uriComponentsBuilderFor("/posts").build();

        //Assert
        assertEquals("https://localhost/posts", uriComponents.toUriString());
    }

}