package com.audition.configuration;

import static com.audition.common.Constants.MICROMETER_SPAN_ID;
import static com.audition.common.Constants.MICROMETER_TRACE_ID;
import static com.audition.common.Constants.X_SPAN_ID;
import static com.audition.common.Constants.X_TRACE_ID;

import com.audition.common.logging.AuditionLogger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@WebFilter("/*")
@Component
@RequiredArgsConstructor
public class ResponseHeaderInjector implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHeaderInjector.class);

    private final AuditionLogger auditionLogger;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
        final FilterChain filterChain)
        throws IOException, ServletException {
        auditionLogger.debug(LOG, "Setting trace and span Ids in the response headers.");

        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        setResponseHeaders(response);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    void setResponseHeaders(final HttpServletResponse response) {
        response.setHeader(X_TRACE_ID, MDC.get(MICROMETER_TRACE_ID));
        response.setHeader(X_SPAN_ID, MDC.get(MICROMETER_SPAN_ID));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
