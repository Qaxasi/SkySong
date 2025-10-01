package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import java.time.Instant;
import java.util.Set;

record SessionEntry(
        int userId,
        String username,
        Set<String> roles,
        Instant issuedAt,
        Instant expiresAt,
        long sessionVersionAtIssue
) {
}
