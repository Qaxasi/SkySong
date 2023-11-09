package com.mycompany.SkySong.authentication.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "blacklisted_token")
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
}
