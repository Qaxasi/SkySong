package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.security.TokenValidatorTestHelper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenValidatorTest extends BaseIT {
    @Autowired
    private TokenValidator validator;

}
