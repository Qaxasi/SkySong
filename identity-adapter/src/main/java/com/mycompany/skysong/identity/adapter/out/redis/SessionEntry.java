package com.mycompany.skysong.identity.adapter.out.redis;

record SessionEntry(
        int userId,
        long issuedAt,
        long expiresAt
) {
}
