package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.common.entity.Role;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;
import com.mycompany.SkySong.registration.dto.UserRegistrationDTO;
import com.mycompany.SkySong.registration.xyz.UserRegistrationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
class TransactionUserSaver implements UserSaver {

    private final UserDAO userDAO;
    private final TransactionTemplate transactionTemplate;
    private final UserRegistrationMapper userMapper;

    public TransactionUserSaver(UserDAO userDAO,
                                TransactionTemplate transactionTemplate,
                                UserRegistrationMapper userMapper) {
        this.userDAO = userDAO;
        this.transactionTemplate = transactionTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(UserRegistrationDTO userDto) {
        transactionTemplate.executeWithoutResult(status -> {
            User user = userMapper.toEntity(userDto);
            int userId = userDAO.save(user);

            for (Role roles : user.getRoles()) {
                userDAO.assignRoleToUser(userId, roles.getId());
            }
        });
    }
}
