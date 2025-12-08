package com.mycompany.skysong.identity.adapter.out.persistence.redis;

import com.mycompany.skysong.identity.domain.UserTag;

class RedisSessionKeyBuilder {
    private final RedisSessionKeyProperties properties;
    RedisSessionKeyBuilder(final RedisSessionKeyProperties properties) {
        this.properties = properties;
    }

    String prefix(final UserTag userTag) {
        return properties.namespace() + ":v" + properties.version() + ":{" + userTag.asString() + "}:";
    }

    String sessionKey(final UserTag userTag, final String refreshTokenHash) {
        return prefix(userTag) + "rt:" + refreshTokenHash;
    }
}
