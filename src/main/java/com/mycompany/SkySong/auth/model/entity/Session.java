package com.mycompany.SkySong.auth.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    private String sessionId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Date createAt;

    @Column(nullable = false)
    private Date expiresAt;


}
