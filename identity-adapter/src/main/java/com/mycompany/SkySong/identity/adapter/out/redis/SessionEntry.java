package com.mycompany.SkySong.identity.adapter.out.redis;

record SessionEntry(
        int userId,
        long issuedAt,
        long expiresAt
) {
}
