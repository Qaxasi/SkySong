package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeleter;
import com.mycompany.skysong.identity.domain.UserId;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
final class JdbiUserDeleter implements UserDeleter {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserDeleter.class);
    private final TransactionTemplate transactionTemplate;
    private final UserIdentityDAO dao;

    JdbiUserDeleter(final TransactionTemplate transactionTemplate,
                    final UserIdentityDAO dao) {
        this.dao = dao;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Result<Unit> deleteWithGuard(final UserId id) {
        return transactionTemplate.execute(status -> {
            try {
                dao.lockLastAdminInvariantGuard();

                final DeleteUserPrecheckProjection precheck = dao.getDeleteUserPrecheck(id.asInt());

                if (!precheck.userExists()) {
                    return Result.failure("User not found", ErrorType.USER_NOT_FOUND);
                }
                if (precheck.isAdmin() && !precheck.otherAdminExists()) {
                    return Result.failure("Cannot delete last admin", ErrorType.LAST_ADMIN_DELETE_FORBIDDEN);
                }

                final int delete = dao.deleteUserById(id.asInt());
                if (delete == 0) {
                    return Result.failure("User not found", ErrorType.USER_NOT_FOUND);
                }
                return Result.success();

            } catch (JdbiException ex) {
                status.setRollbackOnly();
                log.error("db operation failed {} {}",
                        kv("op", "user.delete"),
                        kv("userId", id.asInt()),
                        ex);
                return Result.failure("Persistence error", ErrorType.PERSISTENCE_ERROR);
            }
        });
    }
}