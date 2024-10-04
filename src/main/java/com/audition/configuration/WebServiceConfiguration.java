package com.audition.configuration;

import com.audition.common.jackson.ObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for web service-related beans and settings.
 *
 * <p>This configuration class sets up several key components required for the application:
 * - `ObjectMapper` bean for JSON processing.
 * - `RestTemplate` bean for HTTP client operations, including customized request and response handling.
 * - HTTP request logging through `HttpRequestLoggingInterceptor`.
 *
 * <p>The class implements `WebMvcConfigurer` to provide additional configuration for Spring MVC.
 */
@Configuration
@Import({JacksonAutoConfiguration.class})
@RequiredArgsConstructor
public class WebServiceConfiguration implements WebMvcConfigurer {

    private final HttpRequestLoggingInterceptor httpRequestLoggingInterceptor;
    private final AuditionApiProperties auditionApiProperties;

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperHolder.mapper();
    }

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            .requestFactory(() -> new BufferingClientHttpRequestFactory(createClientFactory()))
            .messageConverters(createMappingJacksonHttpMessageConverter())
            .additionalInterceptors(httpRequestLoggingInterceptor)
            .setConnectTimeout(Duration.of(auditionApiProperties.getConnectTimeoutInSecs(), ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(auditionApiProperties.getReadTimeoutInSecs(), ChronoUnit.SECONDS))
            .build();

    }

    private SimpleClientHttpRequestFactory createClientFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

}
