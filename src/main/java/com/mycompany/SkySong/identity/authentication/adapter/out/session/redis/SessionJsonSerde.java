package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

class SessionJsonSerde {
    private final ObjectMapper objectMapper;
    private final ApplicationLogger logger;

    SessionJsonSerde(final ObjectMapper objectMapper,
                     final ApplicationLogger logger) {
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    Result<String> serialize(final Session session) {
        try {
            final SessionEntry entry = SessionMapper.toDto(session);
            return Result.success(objectMapper.writeValueAsString(entry));
        } catch (JsonProcessingException ex) {
            logger.error("session serialization failed", context("op", "session.serialize"), ex);
            return Result.failure("Internal serialization error", ErrorType.SERIALIZATION_ERROR);
        }
    }

    Result<Session> deserialize(final String json) {
        try {
            final SessionEntry entry = objectMapper.readValue(json, SessionEntry.class);
            final Result<Session> mapperResult = SessionMapper.toDomain(entry);
            if (mapperResult.isFailure()) {
                logger.error("failed to map session entry", context(
                        Map.of("op", "session.deserialize",
                                "error", mapperResult.errorMessage())));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }
            return mapperResult;
        } catch (JsonProcessingException ex) {
            logger.error("session deserialization failed", context("op", "session.deserialize"), ex);
            return Result.failure("Internal deserialization error", ErrorType.DESERIALIZATION_ERROR);
        }
    }
}
