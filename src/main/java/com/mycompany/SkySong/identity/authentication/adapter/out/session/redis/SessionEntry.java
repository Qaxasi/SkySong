package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import java.util.Set;

record SessionEntry(
        int userId,
        String username,
        Set<String> roles,
        long issuedAt,
        long expiresAt,
        long sessionVersionAtIssue
) {
}
