package com.audition.configuration.integration;

import java.net.URI;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

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

    public URI getAuditionPostsUri() {
        return uriComponentsBuilderFor(auditionPostsPath).build().toUri();
    }

    public URI getAuditionPostUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionPostPath).build(postId);
    }

    public URI getAuditionPostCommentsUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionPostCommentsPath).build(postId);
    }

    public URI getAuditionCommentsUri(final String postId) {
        Assert.hasText(postId, POST_ID_MSG);
        return uriComponentsBuilderFor(auditionCommentsPath).queryParam("postId", postId).build().toUri();
    }

}
