package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class RoleManagerImpl implements RoleManager {
    private final RoleDAO roleDAO;
    private final ApplicationMessageService messageService;

    public RoleManagerImpl(RoleDAO roleDAO, ApplicationMessageService messageService) {
        this.roleDAO = roleDAO;
        this.messageService = messageService;
    }
    @Override
    public Role getRoleByName(UserRole userRole) {
        return roleDAO.findByName(userRole)
                .orElseThrow(() -> {
                    log.error("User role not set in the system: " + userRole);
                    return new InternalErrorException(messageService.getMessage("user.registration.error"));
                });
    }
}
