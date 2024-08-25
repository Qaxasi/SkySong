//package com.mycompany.SkySong.adapter.login.adapter;
//
//import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
//import com.mycompany.SkySong.testsupport.common.BaseIT;
//import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
//import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class SpringAuthenticationTest extends BaseIT {
//    @Autowired
//    private SpringAuthentication authentication;
//    @Autowired
//    private UserFixture userFixture;
//
//    @Autowired
//    private SqlDatabaseInitializer initializer;
//    @Autowired
//    private SqlDatabaseCleaner cleaner;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        initializer.setup("data_sql/test-setup.sql");
//    }
//
//    @AfterEach
//    void cleanUp() {
//        cleaner.clean();
//    }
//
//    @Test
//    void whenAuthenticationSuccess_ReturnUsername() {
//        createUser("Alex", "Password#3");
//        String name = authenticate("Alex", "Password#3");
//        assertEquals("Alex", name);
//    }
//
//    @Test
//    void whenInvalidCredentials_ThrowException() {
//        createUser("Alex", "Password#3");
//        assertThrows(BadCredentialsException.class,
//                () -> authenticate("Invalid", "Invalid"));
//    }
//
//    private String authenticate(String username, String password) {
//        return authentication.authenticateUser(username, password);
//    }
//
//    private void createUser(String username, String password) {
//        userFixture.createUserWithUsernameAndPassword(username, password);
//    }
//}
