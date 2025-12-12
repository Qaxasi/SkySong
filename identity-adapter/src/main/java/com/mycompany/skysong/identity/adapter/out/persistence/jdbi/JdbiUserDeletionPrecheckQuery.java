package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.user.deletion.model.UserDeletionPrecheck;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeletionPrecheckQuery;
import com.mycompany.skysong.identity.domain.UserId;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
class JdbiUserDeletionPrecheckQuery implements UserDeletionPrecheckQuery {
    private static final Logger log = LoggerFactory.getLogger(JdbiUserDeletionPrecheckQuery.class);
    private final UserIdentityDAO userDao;

    public JdbiUserDeletionPrecheckQuery(final UserIdentityDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public Result<UserDeletionPrecheck> fetch(final UserId userId) {
        try {
            final UserDeletionPrecheckView view =
                    userDao.fetchUserDeletionPrecheck(userId.asInt());

            return Result.success(
                    new UserDeletionPrecheck(
                            view.userExists(),
                            view.isLastAdmin()));

        } catch (JdbiException ex) {
            log.error("sql read failed {}",
                    kv("op", "user_deletion_status.read"),
                    ex);
            return Result.failure("Persistence error", ErrorType.PERSISTENCE_ERROR);
        }
    }
}
