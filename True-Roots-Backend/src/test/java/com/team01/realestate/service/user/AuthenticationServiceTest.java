package com.team01.realestate.service.user;

import com.team01.realestate.controller.user.UserControllerTest;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ConflictException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.UpdatePasswordRequest;
import com.team01.realestate.payload.request.user.ResetPasswordRequest;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.LoginRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.security.jwt.JwtUtils;
import com.team01.realestate.security.service.UserDetailsImpl;
import com.team01.realestate.service.helper.MethodHelper;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AuthenticationServiceTest {

    Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

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
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MethodHelper methodHelper;

    @Autowired
    private RoleService roleService;

    @MockBean
    private EmailService mailService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test update authenticated user's password successfully")
    void testUpdateAuthenticatedUsersPasswordSuccess() {
        // Arrange
        String userEmail = "john.doe@example.com";
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword456";

        // Create and save a user
        User user = new User();
        user.setEmail(userEmail);
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("333-333-3312");
        user.setPasswordHash(passwordEncoder.encode(oldPassword));
        user.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);

        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn(userEmail);

        // Create UpdatePasswordRequest
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);

        // Act
        authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);

        // Assert
        User updatedUser = userRepository.findByEmail(userEmail);
        assertThat(updatedUser).isNotNull();
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPasswordHash())).isTrue();
    }

    @Test
    @DisplayName("Test update authenticated user's password with incorrect old password")
    void testUpdateAuthenticatedUsersPasswordIncorrectOldPassword() {
        // Arrange
        String userEmail = "jane.doe@example.com";
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword456";

        // Create and save a user
        User user = new User();
        user.setEmail(userEmail);
        user.setPasswordHash(passwordEncoder.encode("correctOldPassword"));
        user.setBuiltIn(false);
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("333-333-3312");
        user.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn(userEmail);

        // Create UpdatePasswordRequest
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.PASSWORD_NOT_MATCHED);
    }

    @Test
    @DisplayName("Test update authenticated user's password with same new password")
    void testUpdateAuthenticatedUsersPasswordSameNewPassword() {
        // Arrange
        String userEmail = "alice.smith@example.com";
        String oldPassword = "currentPassword123";
        String newPassword = "currentPassword123"; // Same as old password

        // Create and save a user
        User user = new User();
        user.setEmail(userEmail);
        user.setPasswordHash(passwordEncoder.encode(oldPassword));
        user.setBuiltIn(false);
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("333-333-3312");
        user.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn(userEmail);

        // Create UpdatePasswordRequest
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.PASSWORD_IS_SAME);
    }

    @Test
    @DisplayName("Test update authenticated user's password for built-in user")
    void testUpdateAuthenticatedUsersPasswordBuiltInUser() {
        // Arrange
        String userEmail = "built.in.user@example.com";
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword456";

        // Create and save a built-in user
        User user = new User();
        user.setEmail(userEmail);
        user.setPasswordHash(passwordEncoder.encode(oldPassword));
        user.setBuiltIn(true); // Built-in user
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("333-333-3312");
        Role adminRole = roleService.getRole(RoleType.ADMIN);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(adminRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn(userEmail);

        // Create UpdatePasswordRequest
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
    }

    @Test
    @DisplayName("Test update authenticated user's password when user does not exist")
    void testUpdateAuthenticatedUsersPasswordUserNotFound() {
        // Arrange
        String userEmail = "nonexistent.user@example.com";
        String oldPassword = "anyPassword123";
        String newPassword = "newPassword456";

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn(userEmail);

        // Create UpdatePasswordRequest
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Cannot invoke \"com.team01.realestate.entity.concretes.user.User.getBuiltIn()\" because \"foundUser\" is null");
    }

    @Test
    @DisplayName("Test user registration successfully")
    void testRegisterUserSuccess() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("new.user@example.com");
        userRequest.setPassword("SecurePass123");
        userRequest.setFirstName("New");
        userRequest.setLastName("User");
        userRequest.setPhone("444-444-4444");

        // Act
        var response = authenticationService.register(userRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(org.springframework.http.HttpStatus.CREATED);
        assertThat(response.getMessage()).isEqualTo("User registered successfully"); // Assuming SuccessMessages.USER_REGISTER = "User registered successfully"
        assertThat(response.getObject()).isNotNull();
        User savedUser = userRepository.findByEmail("new.user@example.com");
        assertThat(savedUser).isNotNull();
        assertThat(passwordEncoder.matches("SecurePass123", savedUser.getPasswordHash())).isTrue();
        assertThat(savedUser.getRoles()).isNotEmpty();
        assertThat(savedUser.getRoles().iterator().next().getRoleType()).isEqualTo(RoleType.CUSTOMER);
    }

    @Test
    @DisplayName("Test user registration with duplicate email")
    void testRegisterUserDuplicateEmail() {
        // Arrange
        String email = "duplicate.user@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setPasswordHash(passwordEncoder.encode("ExistingPass123"));
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setPhone("555-555-5555");
        existingUser.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        existingUser.setRoles(new ArrayList<>());
        existingUser.getRoles().add(customerRole);
        existingUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email); // Duplicate email
        userRequest.setPassword("NewPass123");
        userRequest.setFirstName("New");
        userRequest.setLastName("User");
        userRequest.setPhone("666-666-6666");

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            authenticationService.register(userRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email)); // Assuming uniquePropertyValidator throws this message
    }

    // New Unit Tests for authenticateUser method
    @Test
    @DisplayName("Test user authentication successfully")
    void testAuthenticateUserSuccess() {
        // Arrange
        String email = "valid.user@example.com";
        String password = "ValidPass123";
        String token = "test.jwt.token";

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFirstName("Valid");
        user.setLastName("User");
        user.setPhone("111-222-3333");
        user.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock Authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock JWT token generation
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Act
        ResponseMessage<LoginResponse> response = authenticationService.authenticateUser(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo(SuccessMessages.USER_LOGIN); // Assuming SuccessMessages.USER_LOGIN is defined
        assertThat(response.getObject()).isNotNull();

        LoginResponse loginResponse = response.getObject();
        assertThat(loginResponse.getEmail()).isEqualTo(email);
        assertThat(loginResponse.getToken()).isEqualTo(token); // As per the method implementation
        assertThat(loginResponse.getName()).isEqualTo(user.getFirstName());
        assertThat(loginResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(loginResponse.getRoles()).containsExactlyInAnyOrderElementsOf(
                user.getRoles().stream().map(role -> role.getRoleType().name()).collect(Collectors.toSet())
        );
        assertThat(loginResponse.getUserId()).isEqualTo(user.getId());

        // Verify that JWT token generation was called
        verify(jwtUtils).generateJwtToken(authentication);
    }

    @Test
    @DisplayName("Test user authentication with invalid credentials")
    void testAuthenticateUserInvalidCredentials() {
        // Arrange
        String email = "invalid.user@example.com";
        String password = "InvalidPass456";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Mock authentication to throw exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadRequestException("Invalid email or password"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.authenticateUser(loginRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid email or password");
    }

    @Test
    @DisplayName("Test user authentication for non-existent user")
    void testAuthenticateUserNonExistentUser() {
        // Arrange
        String email = "nonexistent.user@example.com";
        String password = "SomePass789";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Mock authentication to throw exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadRequestException("Invalid email or password"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.authenticateUser(loginRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid email or password");
    }

    @Test
    @DisplayName("Test user login with incorrect password")
    void testLoginUserIncorrectPassword() {
        // Arrange
        String email = "invalid.login@example.com";
        String correctPassword = "CorrectPass123";
        String wrongPassword = "WrongPass456";

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(correctPassword));
        user.setFirstName("Invalid");
        user.setLastName("Login");
        user.setPhone("888-888-8888");
        user.setBuiltIn(false);
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(wrongPassword);

        // Mock authentication to throw exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadRequestException("Invalid email or password"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.authenticateUser(loginRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid email or password");
    }

    // New Unit Tests for resetPassword method
    @Test
    @DisplayName("Test reset password successfully")
    void testResetPasswordSuccess() {
        // Arrange
        String resetCode = "validResetCode123";
        String newPassword = "NewSecurePass456";
        User user = new User();
        user.setEmail("reset.user@example.com");
        user.setPasswordHash(passwordEncoder.encode("OldPass123"));
        user.setBuiltIn(false);
        user.setResetPasswordCode(resetCode);
        user.setFirstName("Reset");
        user.setLastName("User");
        user.setPhone("777-777-7777");
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setCode(resetCode);
        resetPasswordRequest.setPassword(newPassword);

        // Act
        authenticationService.resetPassword(resetPasswordRequest);

        // Assert
        User updatedUser = userRepository.findByResetPasswordCode(resetCode);
        assertThat(updatedUser).isNull(); // Reset code should be null
        User fetchedUser = userRepository.findByEmail("reset.user@example.com");
        assertThat(fetchedUser).isNotNull();
        assertThat(passwordEncoder.matches(newPassword, fetchedUser.getPasswordHash())).isTrue();
    }

    @Test
    @DisplayName("Test reset password with invalid reset code")
    void testResetPasswordInvalidCode() {
        // Arrange
        String invalidResetCode = "invalidCode456";
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setCode(invalidResetCode);
        resetPasswordRequest.setPassword("AnyPassword789");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.resetPassword(resetPasswordRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.INVALID_RESET_CODE);
    }

    @Test
    @DisplayName("Test reset password for built-in user")
    void testResetPasswordBuiltInUser() {
        // Arrange
        String resetCode = "builtInResetCode789";
        String newPassword = "NewPass789";
        User builtInUser = new User();
        builtInUser.setEmail("built.in.reset@example.com");
        builtInUser.setPasswordHash(passwordEncoder.encode("OldBuiltInPass"));
        builtInUser.setBuiltIn(true);
        builtInUser.setResetPasswordCode(resetCode);
        builtInUser.setFirstName("BuiltIn");
        builtInUser.setLastName("ResetUser");
        builtInUser.setPhone("999-999-9999");
        Role adminRole = roleService.getRole(RoleType.ADMIN);
        builtInUser.setRoles(new ArrayList<>());
        builtInUser.getRoles().add(adminRole);
        builtInUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(builtInUser);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setCode(resetCode);
        resetPasswordRequest.setPassword(newPassword);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.resetPassword(resetPasswordRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);

        // Verify that password was not changed
        User fetchedUser = userRepository.findByEmail("built.in.reset@example.com");
        assertThat(passwordEncoder.matches("OldBuiltInPass", fetchedUser.getPasswordHash())).isTrue();
        assertThat(fetchedUser.getResetPasswordCode()).isEqualTo(resetCode);
    }

   //  New Unit Tests for generateResetCodeAndSendEmail method
    @Test
    @DisplayName("Test generate reset code and send email successfully")
    void testGenerateResetCodeAndSendEmailSuccess() {
        // Arrange
        String email = "existing.user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode("UserPass123"));
        user.setBuiltIn(false);
        user.setFirstName("Existing");
        user.setLastName("User");
        user.setPhone("123-456-7890");
        Role customerRole = roleService.getRole(RoleType.CUSTOMER);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(customerRole);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mock email service behavior
        doNothing().when(mailService).sendPasswordResetEmail(anyString(), anyString());

        // Act
        authenticationService.generateResetCodeAndSendEmail(email);

        // Assert
        User updatedUser = userRepository.findByEmail(email);
        assertThat(updatedUser.getResetPasswordCode()).isNotNull();

    }

    @Test
    @DisplayName("Test generate reset code and send email when user does not exist")
    void testGenerateResetCodeAndSendEmailUserNotFound() {
        // Arrange
        String nonExistentEmail = "nonexistent.user@example.com";

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.generateResetCodeAndSendEmail(nonExistentEmail);
        });

        assertThat(exception.getMessage()).isEqualTo(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, nonExistentEmail));
    }
}
