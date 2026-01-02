package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.user.registration.model.UniquenessStatus;
import com.mycompany.skysong.identity.application.user.registration.port.UserUniquenessChecker;
import com.mycompany.skysong.identity.domain.Email;
import com.mycompany.skysong.identity.domain.Username;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
final class JdbiUserUniquenessChecker implements UserUniquenessChecker {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserUniquenessChecker.class);
    private final UserIdentityDAO dao;

    JdbiUserUniquenessChecker(final UserIdentityDAO dao) {
        this.dao = dao;
    }

    @Override
    public Result<UniquenessStatus> check(final Username username,
                                          final Email email) {
        try {
            final UniquenessStatusProjection view = dao.fetchUniquenessStatus(
                    username.value(),
                    email.value());

            final UniquenessStatus status = new UniquenessStatus(
                    view.usernameExists(),
                    view.emailExists());

            return Result.success(status);
        } catch (JdbiException ex) {
            log.error("db operation failed {}",
                    kv("op", "registration.uniqueness.check"),
                    ex);
            return Result.failure(
                    "Persistence error",
                    ErrorType.PERSISTENCE_ERROR);
        }
    }
}

