package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.SessionExistenceChecker;
import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionDeletionTest extends BaseIT {

    @Autowired
    private SessionDeletion sessionDeletion;
    @Autowired
    private SessionExistenceChecker checker;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;
}
