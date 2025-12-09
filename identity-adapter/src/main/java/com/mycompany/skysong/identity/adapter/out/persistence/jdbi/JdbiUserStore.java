package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.registration.port.UserStore;
import com.mycompany.skysong.identity.domain.User;
import com.mycompany.skysong.identity.domain.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
class JdbiUserStore implements UserStore {
    private static final Logger logger = LoggerFactory.getLogger(JdbiUserStore.class);
    private final UserIdentityDAO userDAO;
    public JdbiUserStore(final UserIdentityDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Result<Unit> save(final User user) {
        try {
            final UserInsertRow newUser = new UserInsertRow(
                    user.getUsername().value(),
                    user.getEmail().value(),
                    user.getPassword(),
                    user.getUserTag().value());


            final int userId = userDAO.saveUser(newUser);

            for (final UserRole role : user.getRoles()) {

                final RoleAssignmentRow row = new RoleAssignmentRow(
                        userId,
                        role.code());

                userDAO.assignRole(row);
            }

            return Result.success();

        } catch (DuplicateKeyException ex) {
            return Result.failure("Username or email already exists", ErrorType.DUPLICATE_RESOURCE);

        } catch (DataAccessException ex) {
            logger.error("sql write failed {}",
                    kv("op", "user.save"),
                    ex);
            return Result.failure("Unexpected error while saving user", ErrorType.PERSISTENCE_ERROR);
        }
    }
}


