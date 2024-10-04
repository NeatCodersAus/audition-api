package com.audition.configuration;

import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_ENV;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.NamingConvention;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for setting up Micrometer metrics in a Spring application.
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class MicrometerConfig {

    @Value("${spring.profiles.active:}")
    private String springProfileName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonElements() {
        return registry -> {
            registry.config().namingConvention(NamingConvention.dot);

            if (StringUtils.isNotBlank(springProfileName)) {
                registry.config().commonTags(MM_CUSTOM_TAG_NAME_ENV, springProfileName);
            }
        };
    }

    @Bean
    public TimedAspect timedAspect(final MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }

}
