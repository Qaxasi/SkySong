package com.mycompany.SkySong.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private String sessionId;
    private Integer userId;
    private Date createAt;
    private Date expiresAt;
}
