package com.mycompany.SkySong.auth.security.unused;

import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class DateProvider {
    public Date getCurrentDate() {
        return new Date();
    }
}
