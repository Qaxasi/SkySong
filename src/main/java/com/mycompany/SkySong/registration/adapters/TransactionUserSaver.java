package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.common.dao.RoleDAO;
import com.mycompany.SkySong.common.dao.UserDAO;
import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionUserRegistration {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final TransactionTemplate transactionTemplate;

    public TransactionUserRegistration(UserDAO userDAO, RoleDAO roleDAO, TransactionTemplate transactionTemplate) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.transactionTemplate = transactionTemplate;
    }

    public ApiResponse registerUser(RegisterRequest request) {
        return transactionTemplate.execute(() -> {
            User user = userFactory.createUser(request, role);

            int userId = userDAO.save(user);

            for (Role roles : user.getRoles()) {
                userRepository.assignRoleToUser(userId, roles.getId());
            }
            return new ApiResponse("User registered successfully.");
        });
    }
}
