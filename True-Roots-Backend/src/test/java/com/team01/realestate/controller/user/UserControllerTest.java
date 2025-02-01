package com.team01.realestate.controller.user;

import com.team01.realestate.base.TestDataReader;
import com.team01.realestate.payload.request.business.UpdatePasswordRequest;
import com.team01.realestate.payload.request.user.LoginRequest;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.UserRequestWithoutPassword;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.payload.response.user.UserResponseWithOtherEntities;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class UserControllerTest {

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
    private UserController userController;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService; // Mocking the UserService here

    @Test
    @DisplayName("Test register user controller")
    //@WithMockUser(roles = "ADMIN")
    void registerUserTest() {
       ResponseMessage<UserResponse> userResponse = authenticationController.register(TestDataReader.getUserRequestPayload());

       assertThat(userResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
       assertThat(userResponse).isNotNull();
       assertThat(userResponse.getObject().getEmail()).isEqualTo("john.doe@example.com");
       assertThat(userResponse.getObject().getPhone()).isEqualTo("123-456-7890");
       assertThat(userResponse.getObject().getFirstName()).isEqualTo("John");
       assertThat(userResponse.getObject().getLastName()).isEqualTo("Doe");
      //assertThat(userResponse.getObject().getUserId()).isEqualTo(2L);

        logger.info("registered user: {}", userResponse);
    }

    @Test
    @DisplayName("Test login user controller")
    void loginUserTest() {

        // Test sırasında kullanıcıyı önce kaydet
        ResponseMessage<UserResponse> registerResponse = authenticationController.register(TestDataReader.getUserRequestPayload());
        assertThat(registerResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);

        // Ardından login işlemini test et
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("securePassword123");

        ResponseMessage<LoginResponse> loginResponse = authenticationController.login(loginRequest);
        assertThat(loginResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getObject().getToken()).isNotEmpty();

        // Dönüş değerlerinin doğruluğunu kontrol ediyoruz
        assertThat(loginResponse).isNotNull();
        //assertThat(loginResponse.getObject().getUserId()).isEqualTo(2L); // register testine göre userId sıralaması.

        logger.info("logged in user: {}", loginResponse);
    }

    @Test
    @DisplayName("Test get user by ID for admin or manager roles")
    @WithMockUser(authorities = {"ADMIN"}) // Admin authority for the test
    void getUserByIdTest() {
        // Test için kullanılacak userId
        // Test sırasında kullanıcıyı önce kaydet
        ResponseMessage<UserResponse> registerResponse = authenticationController.register(TestDataReader.getUserRequestPayload());
        assertThat(registerResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);

        // Beklenen sonuç
        UserResponseWithOtherEntities expectedResponse = new UserResponseWithOtherEntities();
        expectedResponse.setUserId(registerResponse.getObject().getUserId());
        expectedResponse.setFirstName("John");
        expectedResponse.setLastName("Doe");
        expectedResponse.setEmail("john.doe@example.com");

        // Mocklama (userService'in getUserById metodu çağrıldığında beklenen sonucu döndürmesi için)
        Mockito.when(userService.getUserById(registerResponse.getObject().getUserId())).thenReturn(ResponseMessage.<UserResponseWithOtherEntities>builder()
                .httpStatus(HttpStatus.OK)
                .object(expectedResponse)
                .build());

        // Controller çağrısı (userService'in gerçek implementasyonu çağrılmadan controller metodunu çağırıyoruz)
        ResponseMessage<UserResponseWithOtherEntities> actualResponse = userController.getUserById(registerResponse.getObject().getUserId());

        // Assertions (Doğrulamalar)
        assertThat(actualResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getObject().getUserId()).isEqualTo(registerResponse.getObject().getUserId());
        assertThat(actualResponse.getObject().getFirstName()).isEqualTo("John");
        assertThat(actualResponse.getObject().getLastName()).isEqualTo("Doe");
        assertThat(actualResponse.getObject().getEmail()).isEqualTo("john.doe@example.com");

        // Loglama
        logger.info("Fetched user by ID: {}", actualResponse);
    }

    @Test
    @DisplayName("Test get users by page controller")
    @WithMockUser(authorities = {"ADMIN"}) // Admin authority for the test
    void testGetUsersByPage() throws Exception {

        // Mock the userService.getUsersByPage method to return a non-null Page<UserResponse>
        UserResponse user1 = new UserResponse();
        user1.setUserId(1L);
        user1.setFirstName("Built in Admin");
        user1.setLastName("Built in Admin");
        user1.setEmail("admin@example.com");
        user1.setPhone("123-456-7890");

        List<UserResponse> users = Arrays.asList(user1);
        Page<UserResponse> mockedPage = new PageImpl<>(users);

        // Mocking the service method
        Mockito.when(userService.getUsersByPage("", 0, 10, "firstName", "asc")).thenReturn(mockedPage);

        // Call the controller method instead of the service directly
        ResponseEntity<Page<UserResponse>> responseEntity = userController.getUsersByPage("", 0, 10, "firstName", "asc");
        Page<UserResponse> userResponses = responseEntity.getBody();

        // Assertions to ensure the response is not null and contains expected data
        assertThat(userResponses).isNotNull();
        assertThat(userResponses.getContent()).isNotEmpty();
        UserResponse firstUserResponse = userResponses.getContent().get(0);
        assertThat(firstUserResponse.getUserId()).isEqualTo(1L);
        assertThat(firstUserResponse.getFirstName()).isEqualTo("Built in Admin");
        assertThat(firstUserResponse.getLastName()).isEqualTo("Built in Admin");
        assertThat(firstUserResponse.getEmail()).isEqualTo("admin@example.com");
        assertThat(firstUserResponse.getPhone()).isEqualTo("123-456-7890");
    }

    @Test
    @DisplayName("Test update authenticated user's password")
    @WithMockUser(authorities = {"CUSTOMER"}) // Customer authority for the test
    void updateAuthenticatedUsersPasswordTest() {
        // İlk olarak kullanıcıyı register ediyoruz
        ResponseMessage<UserResponse> registerResponse = authenticationController.register(TestDataReader.getUserRequestPayload());
        assertThat(registerResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);

        // Password güncelleme isteği oluşturuyoruz
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("securePassword123");
        updatePasswordRequest.setNewPassword("newSecurePassword456");

        // HttpServletRequest mocklama
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("john.doe@example.com"); // "username" olarak düzenlendi

        // Gerekirse mockRequest üzerinde gerekli davranışları tanımlayabilirsiniz
        // Örneğin, authenticated user bilgilerini eklemek gibi

        // Şifre güncelleme metodunu çağırıyoruz
        ResponseMessage<String> updateResponse = authenticationController.updateAuthenticatedUsersPassword(updatePasswordRequest, mockRequest);

        // Sonucun başarılı olduğunu doğruluyoruz
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getMessage()).isEqualTo("Password changed successfully"); // SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE

        logger.info("password updated for user: {}", registerResponse.getObject());
    }

    @Test
    @DisplayName("Test update user by ID as ADMIN or MANAGER")
    @WithMockUser(authorities = {"ADMIN"}) // ADMIN authority for the test
    void updateUserTest() {
        // Prepare test data
        Long userId = 10L;
       // UserRequestWithoutPassword userRequest = TestDataReader.getUserRequestWithoutPasswordPayload();

        UserRequestWithoutPassword userRequest = new UserRequestWithoutPassword();
        userRequest.setFirstName("Jane");
        userRequest.setLastName("Doe");
        userRequest.setEmail("jane.doe@example.com");
        userRequest.setPhone("987-654-3210");

        UserResponse updatedUserResponse = new UserResponse();
        updatedUserResponse.setUserId(userId);
        updatedUserResponse.setFirstName("Jane");
        updatedUserResponse.setLastName("Doe");
        updatedUserResponse.setEmail("jane.doe@example.com");
        updatedUserResponse.setPhone("987-654-3210");

        // Mock the service method
        Mockito.when(userService.updateUserById(Mockito.eq(userRequest), Mockito.eq(userId), Mockito.any(HttpServletRequest.class)))
                .thenReturn(ResponseMessage.<UserResponse>builder()
                        .httpStatus(HttpStatus.OK)
                        .object(updatedUserResponse)
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@example.com");

        // Call the controller method
        ResponseMessage<UserResponse> response = userController.updateUser(userRequest, userId, mockRequest);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getObject().getUserId()).isEqualTo(userId);
        assertThat(response.getObject().getFirstName()).isEqualTo("Jane");
        assertThat(response.getObject().getLastName()).isEqualTo("Doe");
        assertThat(response.getObject().getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(response.getObject().getPhone()).isEqualTo("987-654-3210");

        // Loglama
        logger.info("updated user: {}", response);
    }

    @Test
    @DisplayName("Test delete user by ID as ADMIN or MANAGER")
    @WithMockUser(authorities = {"MANAGER"}) // MANAGER authority for the test
    void deleteUserTest() {
        // Prepare test data
        Long userId = 2L;

        // Mock the service method
        Mockito.when(userService.deleteUserById(Mockito.eq(userId), Mockito.any(HttpServletRequest.class)))
                .thenReturn(ResponseMessage.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("User deleted successfully")
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("manager@example.com");

        // Call the controller method
        ResponseMessage response = userController.deleteUser(userId, mockRequest);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo("User deleted successfully");

        // Loglama
        logger.info("deleted user with ID: {}", userId);
    }

    @Test
    @DisplayName("Test create user with role as ADMIN or MANAGER")
    @WithMockUser(authorities = {"ADMIN"}) // ADMIN authority for the test
    void createUserTest() {
        // Prepare test data
        String userRole = "CUSTOMER";
        UserRequest userRequest = TestDataReader.getUserRequestPayload();

        UserResponse createdUserResponse = new UserResponse();
        createdUserResponse.setUserId(3L);
        createdUserResponse.setFirstName(userRequest.getFirstName());
        createdUserResponse.setLastName(userRequest.getLastName());
        createdUserResponse.setEmail(userRequest.getEmail());
        createdUserResponse.setPhone(userRequest.getPhone());

        // Mock the service method
        Mockito.when(userService.createUser(Mockito.eq(userRequest), Mockito.any(HttpServletRequest.class), Mockito.eq(userRole)))
                .thenReturn(ResponseMessage.<UserResponse>builder()
                        .httpStatus(HttpStatus.CREATED)
                        .object(createdUserResponse)
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@example.com");

        // Call the controller method
        ResponseMessage<UserResponse> response = userController.createUser(userRequest, mockRequest, userRole);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getObject().getUserId()).isEqualTo(3L);
        assertThat(response.getObject().getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(response.getObject().getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(response.getObject().getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(response.getObject().getPhone()).isEqualTo(userRequest.getPhone());

        // Loglama
        logger.info("created user: {}", response);
    }

    // New Tests for Authenticated User Endpoints

    @Test
    @DisplayName("Test get authenticated user")
    @WithMockUser(authorities = {"ADMIN", "MANAGER", "CUSTOMER"}) // Authorized roles for the test
    void getAuthenticatedUserTest() {
        // Prepare test data
        UserResponse authenticatedUser = new UserResponse();
        authenticatedUser.setUserId(4L);
        authenticatedUser.setFirstName("Authenticated");
        authenticatedUser.setLastName("User");
        authenticatedUser.setEmail("auth.user@example.com");
        authenticatedUser.setPhone("555-555-5555");

        // Mock the service method
        Mockito.when(userService.getAuthenticatedUser(Mockito.any(HttpServletRequest.class)))
                .thenReturn(ResponseMessage.<UserResponse>builder()
                        .httpStatus(HttpStatus.OK)
                        .object(authenticatedUser)
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("auth.user@example.com");

        // Call the controller method
        ResponseMessage<UserResponse> response = userController.getAuthenticatedUser(mockRequest);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getObject().getUserId()).isEqualTo(4L);
        assertThat(response.getObject().getFirstName()).isEqualTo("Authenticated");
        assertThat(response.getObject().getLastName()).isEqualTo("User");
        assertThat(response.getObject().getEmail()).isEqualTo("auth.user@example.com");
        assertThat(response.getObject().getPhone()).isEqualTo("555-555-5555");

        // Loglama
        logger.info("fetched authenticated user: {}", response);
    }

    @Test
    @DisplayName("Test update authenticated user")
    @WithMockUser(authorities = {"ADMIN", "MANAGER", "CUSTOMER"}) // Authorized roles for the test
    void updateAuthenticatedUserTest() {
        // Prepare test data
        UserRequestWithoutPassword updateRequest = new UserRequestWithoutPassword();
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setEmail("updated.user@example.com");
        updateRequest.setPhone("666-666-6666");

        UserResponse updatedUser = new UserResponse();
        updatedUser.setUserId(5L);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated.user@example.com");
        updatedUser.setPhone("666-666-6666");

        // Mock the service method
        Mockito.when(userService.updateAuthenticatedUser(Mockito.eq(updateRequest), Mockito.any(HttpServletRequest.class)))
                .thenReturn(ResponseMessage.<UserResponse>builder()
                        .httpStatus(HttpStatus.OK)
                        .object(updatedUser)
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("updated.user@example.com");

        // Call the controller method
        ResponseMessage<UserResponse> response = userController.updateAuthenticatedUser(updateRequest, mockRequest);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getObject().getUserId()).isEqualTo(5L);
        assertThat(response.getObject().getFirstName()).isEqualTo("Updated");
        assertThat(response.getObject().getLastName()).isEqualTo("User");
        assertThat(response.getObject().getEmail()).isEqualTo("updated.user@example.com");
        assertThat(response.getObject().getPhone()).isEqualTo("666-666-6666");

        // Loglama
        logger.info("updated authenticated user: {}", response);
    }

    @Test
    @DisplayName("Test delete authenticated user")
    @WithMockUser(authorities = {"CUSTOMER"}) // Only CUSTOMER authority for the test
    void deleteAuthenticatedUserTest() {
        // Prepare test data
        Long userId = 6L;

        // Mock the service method
        Mockito.when(userService.deleteAuthenticatedUser(Mockito.any(HttpServletRequest.class)))
                .thenReturn(ResponseMessage.<UserResponse>builder()
                        .httpStatus(HttpStatus.OK)
                        .object(null)
                        .message("Authenticated user deleted successfully")
                        .build());

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("deleted.user@example.com");

        // Call the controller method
        ResponseMessage<UserResponse> response = userController.deleteAuthenticatedUser(mockRequest);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo("Authenticated user deleted successfully");
        assertThat(response.getObject()).isNull();

        // Loglama
        logger.info("deleted authenticated user with username: {}", "deleted.user@example.com");
    }

    @Test
    @DisplayName("Test get user by Advert ID as ADMIN or MANAGER")
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getUserByAdvertIdTest() {
        // Arrange
        Long advertId = 1L;
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(10L);
        userResponse.setFirstName("Test");
        userResponse.setLastName("User");
        userResponse.setEmail("test.user@example.com");
        userResponse.setPhone("123-456-7890");

        ResponseMessage<UserResponse> expectedResponse = ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(userResponse)
                .build();

        // Mock the service method
        Mockito.when(userService.getAdvertsUsersById(advertId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<UserResponse> actualResponse = userController.getUserByAdvertId(advertId);

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getObject()).isNotNull();
        assertThat(actualResponse.getObject().getUserId()).isEqualTo(userResponse.getUserId());
        assertThat(actualResponse.getObject().getFirstName()).isEqualTo(userResponse.getFirstName());
        assertThat(actualResponse.getObject().getLastName()).isEqualTo(userResponse.getLastName());
        assertThat(actualResponse.getObject().getEmail()).isEqualTo(userResponse.getEmail());
        assertThat(actualResponse.getObject().getPhone()).isEqualTo(userResponse.getPhone());

        // Log the result
        logger.info("Fetched user by Advert ID: {}", actualResponse);
    }

    @BeforeEach
    public void resetDatabase() {
        // Perform any necessary clean-up steps
        userRepository.deleteAll(); // Delete all users to ensure a clean slate
    }


}
