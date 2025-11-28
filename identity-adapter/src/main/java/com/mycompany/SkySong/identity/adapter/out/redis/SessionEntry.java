package com.mycompany.SkySong.identity.a.adapter.out.redis;

record SessionEntry(
        int userId,
        long issuedAt,
        long expiresAt,
        int accessVersionAtIssue
) {
}
