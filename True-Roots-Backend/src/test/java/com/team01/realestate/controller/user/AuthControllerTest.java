package com.team01.realestate.controller.user;

import com.team01.realestate.base.TestDataReader;
import com.team01.realestate.payload.request.user.ForgotPasswordRequest;
import com.team01.realestate.payload.request.user.LoginRequest;
import com.team01.realestate.payload.request.user.ResetPasswordRequest;

import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.user.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AuthControllerTest {

    Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService; // Mocking the AuthenticationService here

    @Test
    @DisplayName("Test register user controller")
    void registerUserTest() {
        UserRequest registerRequest = TestDataReader.getUserRequestPayload();

        // Mock the userService.register method
        UserResponse mockUserResponse = new UserResponse();
        mockUserResponse.setUserId(2L);
        mockUserResponse.setEmail("john.doe@example.com");
        mockUserResponse.setPhone("123-456-7890");
        mockUserResponse.setFirstName("John");
        mockUserResponse.setLastName("Doe");

        when(authenticationService.register(registerRequest)).thenReturn(ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .object(mockUserResponse)
                .build());

        // Call the controller method
        ResponseMessage<UserResponse> userResponse = authenticationController.register(registerRequest);

        // Assertions
        assertThat(userResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getObject().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(userResponse.getObject().getPhone()).isEqualTo("123-456-7890");
        assertThat(userResponse.getObject().getFirstName()).isEqualTo("John");
        assertThat(userResponse.getObject().getLastName()).isEqualTo("Doe");
        assertThat(userResponse.getObject().getUserId()).isEqualTo(2L);

        logger.info("Registered user: {}", userResponse);
    }

    @Test
    @DisplayName("Test login user controller")
    void loginUserTest() {
        // Prepare login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("securePassword123");

        // Mock the authenticationService.login method
        LoginResponse mockLoginResponse = new LoginResponse();
        mockLoginResponse.setToken("mocked-jwt-token");

        when(authenticationService.authenticateUser(loginRequest)).thenReturn(ResponseMessage.<LoginResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(mockLoginResponse)
                .build());

        // Call the controller method
        ResponseMessage<LoginResponse> loginResponse = authenticationController.login(loginRequest);

        // Assertions
        assertThat(loginResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getObject().getToken()).isNotEmpty();

        logger.info("Logged in user: {}", loginResponse);
    }

    @Test
    @DisplayName("Test forgot password controller")
    void forgotPasswordTest() {
        // Prepare the ForgotPasswordRequest
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("john.doe@example.com");

        // Mock the authenticationService.generateResetCodeAndSendEmail method
        Mockito.doNothing().when(authenticationService).generateResetCodeAndSendEmail(forgotPasswordRequest.getEmail());

        // Call the controller method
        ResponseMessage<Void> response = authenticationController.forgotPassword(forgotPasswordRequest);

        // Verify that the service method was called with the correct email
        Mockito.verify(authenticationService, Mockito.times(1)).generateResetCodeAndSendEmail(forgotPasswordRequest.getEmail());

        // Assertions to ensure the response is as expected
        assertThat(response).isNotNull();
        assertThat(response.getObject()).isNull(); // Since it's ResponseMessage<Void>

        logger.info("Forgot password response: {}", response);
    }

    @Test
    @DisplayName("Test reset password controller")
    void resetPasswordTest() {
        // Prepare the ResetPasswordRequest
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setCode("validResetCode123");
        resetPasswordRequest.setPassword("newSecurePassword456");

        // Mock the authenticationService.resetPassword method
        Mockito.doNothing().when(authenticationService).resetPassword(resetPasswordRequest);

        // Call the controller method
        ResponseMessage<Void> response = authenticationController.resetPassword(resetPasswordRequest);

        // Verify that the service method was called with the correct request
        Mockito.verify(authenticationService, Mockito.times(1)).resetPassword(resetPasswordRequest);

        // Assertions to ensure the response is as expected
        assertThat(response).isNotNull();
        assertThat(response.getObject()).isNull(); // Since it's ResponseMessage<Void>

        logger.info("Reset password response: {}", response);
    }

    @BeforeEach
    public void resetDatabase() {
        // Perform any necessary clean-up steps
        userRepository.deleteAll(); // Delete all users to ensure a clean slate
    }

} 