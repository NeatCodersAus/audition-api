package com.audition.configuration.integration;

import java.net.URI;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Properties specific to the integration client for audition functionalities. This class extends
 * {@link AbstractClientProperties} and provides additional paths required to access various endpoints related to
 * audition posts and comments. It includes methods to build URIs for these endpoints.
 *
 * <p>The following paths are configurable:
 * - auditionPostsPath: Represents the path to fetch all audition posts.
 * - auditionPostPath: Represents the path to fetch a specific audition post.
 * - auditionPostCommentsPath: Represents the path to fetch comments for a specific post.
 * - auditionCommentsPath: Represents the path to fetch all comments for a specific post.
 */
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "audition.integration.client.api")
public class AuditionIntegrationClientProperties extends AbstractClientProperties {

    private static final long serialVersionUID = 5997051886680758649L;
    private static final String POST_ID_MSG = "postId is mandatory param and must not be null or empty";

    private String auditionPostsPath;
    private String auditionPostPath;
    private String auditionPostCommentsPath;
    private String auditionCommentsPath;

    /**
     * Builds and returns the URI to fetch all audition posts.
     *
     * @return the URI pointing to the resources representing all audition posts
     */
    public URI getAuditionPostsUri() {
        return uriComponentsBuilderFor(auditionPostsPath).build().toUri();
    }

    /**
     * Builds and returns the URI to fetch a specific audition post using its unique identifier.
     *
     * @param postId The unique identifier of the audition post. It must not be null or empty.
     * @return The URI pointing to the resource representing the specified audition post.
     * @throws IllegalArgumentException If the provided postId is null or empty.
     */
    public URI getAuditionPostUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionPostPath).build(postId);
    }

    /**
     * Builds and returns the URI to fetch comments for a specific audition post using its unique identifier.
     *
     * @param postId The unique identifier of the audition post. It must not be null or empty.
     * @return The URI pointing to the resources representing the comments for the specified audition post.
     * @throws IllegalArgumentException If the provided postId is null or empty.
     */
    public URI getAuditionPostCommentsUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionPostCommentsPath).build(postId);
    }

    /**
     * Builds and returns the URI to fetch all comments for a specific audition post.
     *
     * @param postId The unique identifier of the audition post. It must not be null or empty.
     * @return The URI pointing to the resources representing all comments for the specified audition post.
     * @throws IllegalArgumentException If the provided postId is null or empty.
     */
    public URI getAuditionCommentsUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionCommentsPath).queryParam("postId", postId).build().toUri();
    }

}
