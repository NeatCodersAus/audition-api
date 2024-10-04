package com.audition.configuration;

import com.audition.service.AuditionMeterService;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MetricsAutoConfiguration is a configuration class for setting up meter registries and services related to application
 * metrics.
 *
 * <p>This configuration will create a CompositeMeterRegistry bean if one is not already present and
 * management.metrics.enable.all property is set to false.
 */
@Configuration
public class MetricsAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(MeterRegistry.class)
    @ConditionalOnProperty(prefix = "management.metrics", name = "enable.all", havingValue = "false")
    public CompositeMeterRegistry compositeMeterRegistry(final Clock clock) {
        LOG.info("Creating Composite meter registry");
        return new CompositeMeterRegistry(clock);
    }

    @Bean
    public AuditionMeterService auditionMeterService(final MeterRegistry meterRegistry) {
        LOG.info("Creating AuditionMeterService");
        return new AuditionMeterService(meterRegistry);
    }

}
