package com.mycompany.skysong.app.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppSecurityProperties.class)
public class SecurityPropertiesConfig {
}
