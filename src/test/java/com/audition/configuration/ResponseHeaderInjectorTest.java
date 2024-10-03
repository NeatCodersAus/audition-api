package com.audition.configuration;

import static com.audition.common.Constants.MICROMETER_SPAN_ID;
import static com.audition.common.Constants.MICROMETER_TRACE_ID;
import static com.audition.common.Constants.X_SPAN_ID;
import static com.audition.common.Constants.X_TRACE_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.audition.common.logging.AuditionLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class ResponseHeaderInjectorTest {

    @Mock
    FilterConfig mockFilterConfig;
    @Mock
    FilterChain mockFilterChain;
    @Mock
    AuditionLogger mockAuditionLogger;
    @Mock
    HttpServletRequest mockHttpRequest;
    @Mock
    HttpServletResponse mockHttpResponse;

    @InjectMocks
    ResponseHeaderInjector responseHeaderInjector;

    @Test
    void testDoFilterMethodWithValidServletRequestsAndResponses() throws ServletException, IOException {
        //Act
        responseHeaderInjector.doFilter(mockHttpRequest, mockHttpResponse, mockFilterChain);

        //Assert
        verify(mockFilterChain, times(1)).doFilter(mockHttpRequest, mockHttpResponse);
    }

    @Test
    void testSetResponseHeaders() {
        //Act
        responseHeaderInjector.setResponseHeaders(mockHttpResponse);

        //Assert
        verify(mockHttpResponse, times(1)).setHeader(X_TRACE_ID, MDC.get(MICROMETER_TRACE_ID));
        verify(mockHttpResponse, times(1)).setHeader(X_SPAN_ID, MDC.get(MICROMETER_SPAN_ID));
    }
}