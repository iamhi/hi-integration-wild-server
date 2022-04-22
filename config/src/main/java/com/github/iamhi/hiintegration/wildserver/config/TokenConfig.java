package com.github.iamhi.hiintegration.wildserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("hi.zone.wild.token")
@Data
public class TokenConfig {

    String secret;
}
