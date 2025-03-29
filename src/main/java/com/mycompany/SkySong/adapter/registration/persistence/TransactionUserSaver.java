package com.mycompany.SkySong.adapter.registration.persistence;

import com.mycompany.SkySong.application.registration.dto.UserSaveDto;
import com.mycompany.SkySong.application.registration.mapper.UserSaveMapper;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.sql.UserDAO;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.application.registration.ports.UserSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionUserSaver implements UserSaver {

    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;
    private final UserSaveMapper mapper;

    public TransactionUserSaver(UserDAO userDAO,
                                TransactionTemplate transactionTemplate,
                                UserSaveMapper mapper) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.mapper = mapper;
    }

    @Override
    public void saveUser(UserSaveDto userDto) {
        User user = mapper.toEntity(userDto);
        transactionTemplate.executeWithoutResult(status -> {
            int userId = userDAO.save(user);

            for (Role roles : user.getRoles()) {
                userDAO.assignRoleToUser(userId, roles.getId());
            }
        });
    }
}
