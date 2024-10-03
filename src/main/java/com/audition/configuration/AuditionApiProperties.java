package com.audition.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "audition.api")
public class AuditionApiProperties {

    private String version;
    private long connectTimeoutInSecs;
    private long readTimeoutInSecs;


}
