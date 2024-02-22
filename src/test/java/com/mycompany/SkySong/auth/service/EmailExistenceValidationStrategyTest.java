package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailExistenceValidationStrategyTest extends BaseIT {

    @Autowired
    private EmailExistenceValidationStrategy strategy;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;
}
