package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.registration.port.UserSaver;
import com.mycompany.skysong.identity.domain.User;
import com.mycompany.skysong.identity.domain.UserRole;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
final class JdbiUserSaver implements UserSaver {
    private static final Logger logger = LoggerFactory.getLogger(JdbiUserSaver.class);
    private final TransactionTemplate transactionTemplate;
    private final UserIdentityDAO userDAO;
    public JdbiUserSaver(final TransactionTemplate transactionTemplate,
                         final UserIdentityDAO userDAO) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public Result<Unit> save(final User user) {
        return transactionTemplate.execute(status -> {
            try {
                final NewUserParams userParams = new NewUserParams(
                        user.getUsername().value(),
                        user.getEmail().value(),
                        user.getPassword(),
                        user.getUserTag().value());


                final int userId = userDAO.saveUser(userParams);

                for (final UserRole role : user.getRoles()) {

                    final RoleAssignmentParams roleAssignmentParams =
                            new RoleAssignmentParams(
                                    userId,
                                    role.code());

                    userDAO.assignRole(roleAssignmentParams);
                }
                return Result.success();

            } catch (JdbiException ex) {
                logger.error("db operation failed {}",
                        kv("op", "user.save"),
                        ex);
                return Result.failure(
                        "Persistence error",
                        ErrorType.PERSISTENCE_ERROR);
            }
        });
    }
}


