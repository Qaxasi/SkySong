package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.UserTag;
import com.mycompany.SkySong.identity.infrastructure.config.redis.RedisSessionKeyProperties;

class RedisSessionKeyBuilder {
    private final RedisSessionKeyProperties properties;
    RedisSessionKeyBuilder(final RedisSessionKeyProperties properties) {
        this.properties = properties;
    }

    String prefix(final UserTag userTag) {
        return properties.namespace() + ":v" + properties.version() + ":{" + userTag.asString() + "}:";
    }
    String refreshTokenSessionKey(final UserTag userTag, final String tokenHash) {
        return prefix(userTag) + tokenHash;
    }
    String refreshTokenHashesKey(final UserTag userTag) {
        return prefix(userTag) + "hashes";
    }
}
