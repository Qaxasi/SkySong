package com.mycompany.skysong.identity.adapter.in.web.cookie;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthCookieProperties.class)
class AuthCookiePropertiesConfig {}
