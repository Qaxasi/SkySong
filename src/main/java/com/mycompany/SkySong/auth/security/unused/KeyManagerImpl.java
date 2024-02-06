package com.mycompany.SkySong.auth.security.unused;

import com.mycompany.SkySong.auth.security.unused.KeyManager;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
@Component
public class KeyManagerImpl implements KeyManager {
    private final String jwtSecret;

    public KeyManagerImpl(@Value("${JWT_SECRET}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
    @Override
    public Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
