package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

record SessionEntry(
        int userId,
        long issuedAt,
        long expiresAt,
        int accessVersionAtIssue
) {
}
