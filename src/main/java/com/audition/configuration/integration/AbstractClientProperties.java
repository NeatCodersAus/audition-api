package com.audition.configuration.integration;

import java.io.Serializable;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Abstract class representing properties required for a client. It enforces the presence of a host and provides utility
 * methods to build URI components from given paths.
 */
@Setter
public abstract class AbstractClientProperties implements Serializable {

    private static final long serialVersionUID = 2183012132750068488L;
    protected String host;
    protected String basePath;

    protected UriComponentsBuilder uriComponentsBuilderFor(final String path) {
        Assert.hasText(host, "Property 'host' must not be null or empty");
        Assert.hasText(path, "Path must not be null or empty");

        final String basePath = StringUtils.isBlank(this.basePath) ? host : host.concat(this.basePath);
        return UriComponentsBuilder.fromUriString(basePath + path);
    }
}
