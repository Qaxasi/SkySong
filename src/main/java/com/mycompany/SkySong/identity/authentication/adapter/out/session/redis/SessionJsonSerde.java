package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.logstash.logback.argument.StructuredArguments.kv;

class SessionJsonSerde {
    private static final Logger log = LoggerFactory.getLogger(SessionJsonSerde.class);
    private final ObjectMapper objectMapper;

    SessionJsonSerde(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    Result<String> serialize(final Session session) {
        try {
            final SessionEntry entry = SessionMapper.toDto(session);
            final String json = objectMapper.writeValueAsString(entry);
            return Result.success(json);
        } catch (JsonProcessingException ex) {
            log.error("session serialization failed {}",
                    kv("op", "session.serialize"),
                    ex);
            return Result.failure("Internal serialization error", ErrorType.SERIALIZATION_ERROR);
        }
    }

    Result<Session> deserialize(final String json) {
        try {
            final SessionEntry entry = objectMapper.readValue(json, SessionEntry.class);
            final Result<Session> mapperResult = SessionMapper.toDomain(entry);

            return mapperResult
                    .peekFailure(f ->
                            log.error("failed to map session entry {} {} {}",
                                    kv("op", "session deserialize"),
                                    kv("errorType", f.errorType()),
                                    kv("message", f.message())));
        } catch (JsonProcessingException ex) {
            log.error("session deserialization failed {}",
                    kv("op", "session.deserialize"),
                    ex);
            return Result.failure("Internal deserialization error", ErrorType.DESERIALIZATION_ERROR);
        }
    }
}
