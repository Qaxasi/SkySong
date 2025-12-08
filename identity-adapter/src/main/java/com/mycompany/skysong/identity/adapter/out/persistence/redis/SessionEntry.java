package com.mycompany.skysong.identity.adapter.out.persistence.redis;

record SessionEntry(
        int userId,
        long issuedAt,
        long expiresAt
) {
}
