package com.mycompany.SkySong.adapter.user.registration.persistence;

import com.mycompany.SkySong.application.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.application.user.registration.dto.UserSaveDto;
import com.mycompany.SkySong.application.user.registration.mapper.UserSaveMapper;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.application.user.registration.ports.UserSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionUserSaver implements UserSaver {

    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;
    private final UserSaveMapper mapper;
    private final ApplicationLogger logger;

    public TransactionUserSaver(UserDAO userDAO,
                                TransactionTemplate transactionTemplate,
                                UserSaveMapper mapper,
                                ApplicationLogger logger) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.mapper = mapper;
        this.logger = logger;
    }

    @Override
    public void saveUser(UserSaveDto userDto) {
        User user = mapper.toEntity(userDto);
        transactionTemplate.executeWithoutResult(status -> {
            try {
                int userId = userDAO.save(user);

                for (Role roles : user.getRoles()) {
                    userDAO.assignRoleToUser(userId, roles.getId());
                }
            } catch (RuntimeException ex) {
                logger.error("Failed to save user", ex);
                throw ex;
            }
        });
    }
}
