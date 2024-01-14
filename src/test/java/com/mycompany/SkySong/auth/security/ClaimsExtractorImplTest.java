package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.BaseIT;

import org.springframework.beans.factory.annotation.Autowired;


public class ClaimsExtractorImplTest extends BaseIT {
    @Autowired
    private ClaimsExtractor claimsExtractor;
    @Autowired
    private LoginService login;
}