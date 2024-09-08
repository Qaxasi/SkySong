package com.mycompany.SkySong.adapter.registration.adapters;

import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionUserSaverTest extends BaseIT {
    @Autowired
    private TransactionUserSaver userSaver;
    @Autowired
    private UserExistenceChecker userExistenceChecker;
    @Autowired
    private UserRoleChecker userRoleChecker;
}
