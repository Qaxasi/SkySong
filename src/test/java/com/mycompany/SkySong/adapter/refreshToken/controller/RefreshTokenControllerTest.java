package com.mycompany.SkySong.adapter.refreshToken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RefreshTokenControllerTest  {

    @Autowired
    private TestRestTemplate restTemplate;
}
