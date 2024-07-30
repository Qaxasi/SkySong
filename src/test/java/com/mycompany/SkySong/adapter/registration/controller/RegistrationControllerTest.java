//package com.mycompany.SkySong.registration.application.controller;
//
//import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
//import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
//import com.mycompany.SkySong.testsupport.common.BaseIT;
//import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static com.mycompany.SkySong.testsupport.common.JsonUtils.asJsonString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc
//public class RegistrationControllerTest extends BaseIT {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private RegistrationRequests registrationHelper;
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
//    void whenRegistrationSuccess_Return201() throws Exception {
//        mockMvc.perform(post("/api/v1/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(registrationHelper.validCredentials)))
//                .andExpect(status().is(201));
//    }
//
//    @Test
//    void whenRegistrationSuccess_ReturnCorrectFieldName() throws Exception {
//        mockMvc.perform(post("/api/v1/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(registrationHelper.validCredentials)))
//                .andExpect(jsonPath("$.message").isNotEmpty());
//    }
//
//    @Test
//    void whenInvalidCredentials_ReturnBadRequest() throws Exception {
//        mockMvc.perform(post("/api/v1/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(registrationHelper.emailInvalidFormat)))
//                .andExpect(status().is(400));
//    }
//
//    @Test
//    void whenMalformedRequest_ReturnBadRequest() throws Exception {
//        mockMvc.perform(post("/api/v1/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(registrationHelper.malformedRequest))
//                .andExpect(status().is(400));
//    }
//
//    @Test
//    void whenEmptyCredentials_ReturnErrorMessages() throws Exception {
//        ResultActions actions = mockMvc.perform(post("/api/v1/users/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(registrationHelper.emptyCredentials)));
//
//        actions.andExpect(jsonPath("$.errors.username")
//                        .value("The username field cannot be empty"))
//                .andExpect(jsonPath("$.errors.email")
//                        .value("The email field cannot be empty"))
//                .andExpect(jsonPath("$.errors.password")
//                        .value("The password field cannot be empty"));
//    }
//}
