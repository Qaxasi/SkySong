package com.mycompany.SkySong.testutils.common;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@TestConfiguration
@Slf4j
public class CustomTestRestTemplateConfig {

    @Bean
    public TestRestTemplate customTestRestTemplate() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        restTemplate.getRestTemplate().setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(@NotNull ClientHttpResponse response) throws IOException {
                HttpStatusCode statusCode = response.getStatusCode();
                return (statusCode.is4xxClientError() || statusCode.is5xxServerError());
            }

            @Override
            public void handleError(@NotNull ClientHttpResponse response) throws IOException {
                log.error("Http error: " + response.getStatusCode());
            }
        });

        return restTemplate;
    }
}