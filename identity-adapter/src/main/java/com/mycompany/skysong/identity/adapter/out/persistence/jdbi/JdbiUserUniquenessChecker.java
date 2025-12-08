package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.registration.model.UniquenessStatus;
import com.mycompany.skysong.identity.application.registration.port.UserUniquenessChecker;
import com.mycompany.skysong.identity.domain.Email;
import com.mycompany.skysong.identity.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Transactional(readOnly = true)
class JdbiUserUniquenessChecker implements UserUniquenessChecker {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserUniquenessChecker.class);
    private final UserIdentityDAO dao;

    JdbiUserUniquenessChecker(final UserIdentityDAO dao) {
        this.dao = dao;
    }

    @Override
    public Result<UniquenessStatus> check(final Username username,
                                          final Email email) {
        try {
            final UniquenessStatusView view = dao.checkUniqueness(
                    username.asString(),
                    email.value());

            final UniquenessStatus status = new UniquenessStatus(
                    view.usernameExists(),
                    view.emailExists());

            return Result.success(status);
        } catch (DataAccessException ex) {
            log.error("sql read failed {}",
                    kv("op", "registration.uniqueness.check"),
                    ex);
            return Result.failure("Internal persistence error", ErrorType.PERSISTENCE_ERROR);
        }
    }
}

