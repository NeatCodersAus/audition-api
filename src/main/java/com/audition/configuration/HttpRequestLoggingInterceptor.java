package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * HttpRequestLoggingInterceptor is a Spring component that implements the ClientHttpRequestInterceptor interface. It is
 * responsible for logging HTTP requests and responses.
 */
@Component
@RequiredArgsConstructor
public class HttpRequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestLoggingInterceptor.class);

    private final AuditionLogger auditionLogger;

    @Override
    public ClientHttpResponse intercept(
        final HttpRequest request,
        final byte[] reqBody,
        final ClientHttpRequestExecution ex) throws IOException {

        logRequest(request, reqBody);
        final ClientHttpResponse response = ex.execute(request, reqBody);
        logResponse(response);

        return response;
    }

    private void logRequest(final HttpRequest request, final byte[] body) {
        auditionLogger.debug(LOG, String.format("Sending request: [%s] %s %s",
            request.getMethod(),
            request.getURI(),
            new String(body, StandardCharsets.UTF_8)));
    }

    private void logResponse(final ClientHttpResponse response) throws IOException {
        auditionLogger.debug(LOG, String.format("Received response: %s %s",
            response.getStatusCode(),
            new BufferedReader(new InputStreamReader(response.getBody(),
                StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"))));
    }
}