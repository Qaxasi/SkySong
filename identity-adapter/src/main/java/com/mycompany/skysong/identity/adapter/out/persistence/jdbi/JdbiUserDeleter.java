package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeleter;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
class JdbiUserDeleter implements UserDeleter {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserDeleter.class);
    private final UserIdentityDAO userDAO;
    JdbiUserDeleter(final UserIdentityDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public Result<Unit> deleteEverythingById(final int id) {
        try {
            userDAO.deleteUserRoles(id);
            userDAO.delete(id);

            return Result.success();

        } catch (JdbiException ex) {
            log.error("unexpected database error {} {}",
                    kv("op", "user.delete"),
                    kv("userId", id),
                    ex);
            return Result.failure("Unexpected persistence error during user deletion", ErrorType.PERSISTENCE_ERROR);
        }
    }
}