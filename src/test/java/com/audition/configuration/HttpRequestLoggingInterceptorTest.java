package com.audition.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.logging.AuditionLogger;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

@ExtendWith(MockitoExtension.class)
class HttpRequestLoggingInterceptorTest {

    @Mock
    private AuditionLogger auditionLogger;

    @Mock
    private ClientHttpRequestExecution ex;

    @InjectMocks
    private HttpRequestLoggingInterceptor httpRequestLoggingInterceptor;

    @Test
    void whenInterceptIsCalledVerifyLogMessage() throws Exception {
        // Arrange
        final String requestBody = "Request Body";
        final MockClientHttpRequest req = new MockClientHttpRequest();
        final byte[] reqBody = requestBody.getBytes(StandardCharsets.UTF_8);
        try (MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], 200)) {

            when(ex.execute(any(), eq(reqBody))).thenReturn(response);
        }

        // Act
        httpRequestLoggingInterceptor.intercept(req, reqBody, ex);

        // Assert
        verify(auditionLogger, times(1)).debug(any(Logger.class), eq("Sending request: [GET] / " + requestBody));
        verify(auditionLogger, times(1)).debug(any(Logger.class), eq("Received response: 200 OK "));
    }
}