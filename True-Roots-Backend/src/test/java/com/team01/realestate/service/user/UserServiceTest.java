package com.team01.realestate.service.user;


import com.team01.realestate.controller.user.UserControllerTest;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.UserRequestWithoutPassword;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.payload.response.user.UserResponseWithOtherEntities;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class UserServiceTest {

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
    private UserService userService;

    @Autowired
    private MethodHelper methodHelper;

    @Autowired
    private UserRepository userRepository; // Gerçek repo kullanılıyor

    @Test
    @DisplayName("Test get user by ID service")
    void testGetUserById() {
        // Service metodu çağrısı
        UserResponseWithOtherEntities userResponse = userService.getUserById(1L).getObject();
        // Doğrulamalar
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getUserId()).isEqualTo(1L);
        assertThat(userResponse.getFirstName()).isEqualTo("Built in Admin");
        assertThat(userResponse.getLastName()).isEqualTo("Built in Admin");
        assertThat(userResponse.getEmail()).isEqualTo("admin@admin.com");
    }

    @Test
    @DisplayName("Test get users by page service")
    void testGetUsersByPage() {
        // Call the method under test
        Page<UserResponse> userResponses = userService.getUsersByPage("", 0, 10, "firstName", "asc");

        // Validate the results
        assertThat(userResponses).isNotNull();

        UserResponse response1 = userResponses.getContent().get(0);
        assertThat(response1.getUserId()).isEqualTo(1L);
        assertThat(response1.getFirstName()).isEqualTo("Built in Admin");
        assertThat(response1.getLastName()).isEqualTo("Built in Admin");

    }


    @Test
    void testGetAuthenticatedUser() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com"); // "username" olarak düzenlendi

        // Act
        ResponseMessage<UserResponse> response = userService.getAuthenticatedUser(mockRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo(SuccessMessages.USER_FOUND);
        assertThat(response.getObject().getUserId()).isEqualTo(1L);
        assertThat(response.getObject().getFirstName()).isEqualTo("Built in Admin");
        assertThat(response.getObject().getLastName()).isEqualTo("Built in Admin");
        assertThat(response.getObject().getPhone()).isEqualTo("111-111-1111");

        //assertThat(response.getObject()).isEqualTo(mockUserResponse);

    }

    @Test
    void testCreateAndDeleteUser(){
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com");

        UserRequest user = new UserRequest();
        user.setEmail("email1@email1.com");
        user.setFirstName("userFirstName");
        user.setLastName("Last");
        user.setBuiltIn(false);
        user.setPhone("123-456-7890");
        user.setPassword("ngfajkfgnjfkg");


        ResponseMessage<UserResponse> createResponse =  userService.createUser(user, mockRequest, "Customer");

        // Assertler: Kullanıcı oluşturma
        assertThat(createResponse).isNotNull();
        assertThat(createResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getMessage()).isEqualTo(SuccessMessages.USER_CREATE);
        assertThat(createResponse.getObject()).isNotNull();
        assertThat(createResponse.getObject().getEmail()).isEqualTo(user.getEmail());
        assertThat(createResponse.getObject().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(createResponse.getObject().getLastName()).isEqualTo(user.getLastName());

        // Kullanıcı silme
        ResponseMessage deleteResponse = userService.deleteUserById(2L, mockRequest);

        // Assertler: Kullanıcı silme
        assertThat(deleteResponse).isNotNull();
        assertThat(deleteResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResponse.getMessage()).isEqualTo(SuccessMessages.USER_DELETE);
    }

    @Test
    void testDeleteUserWithBuiltInUserThrowsException() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com");

        User builtInAdminUser = methodHelper.findUserById(1L);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.deleteUserById(1L, mockRequest);
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
    }

    @Test
    @DisplayName("Test delete user by ID - Manager cannot delete non-Customer")
    void testDeleteUserByManagerThrowsExceptionForNonCustomer() {
        HttpServletRequest mockRequestBuiltInAdmin = mock(HttpServletRequest.class);
        when(mockRequestBuiltInAdmin.getAttribute("username")).thenReturn("admin@admin.com");

        UserRequest user = new UserRequest();
        user.setEmail("email@email.com");
        user.setFirstName("userFirstName");
        user.setLastName("Last");
        user.setBuiltIn(false);
        user.setPhone("123-456-7890");
        user.setPassword("ngfajkfgnjfkg");

        ResponseMessage<UserResponse> createAdminToDelete =  userService.createUser(user, mockRequestBuiltInAdmin, "Admin");

        // Mock user to be deleted (Admin role)

        UserRequest userManager = new UserRequest();
        userManager.setEmail("manager@email.com");
        userManager.setFirstName("managerFirstName");
        userManager.setLastName("managerLast");
        userManager.setBuiltIn(false);
        userManager.setPhone("123-456-7891");
        userManager.setPassword("nglajkfgnjfkg");
        ResponseMessage<UserResponse> createManagerToTryToDeleteNonCustomer =  userService.createUser(userManager, mockRequestBuiltInAdmin, "Manager");
        HttpServletRequest mockRequestManager = mock(HttpServletRequest.class);
        when(mockRequestManager.getAttribute("username")).thenReturn("manager@email.com");

        User userAdmin = methodHelper.findUserById(2L);
        User managerUser=  methodHelper.findAuthenticatedUser(mockRequestManager);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.deleteUserById(2L, mockRequestManager);
        });

        assertThat(methodHelper.findAuthenticatedUser(mockRequestManager)).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        assertThat(managerUser).isNotNull();
        assertThat(managerUser.getEmail()).isEqualTo("manager@email.com");
        assertThat(managerUser.getFirstName()).isEqualTo("managerFirstName");
        assertThat(managerUser.getLastName()).isEqualTo("managerLast");
        assertThat(managerUser.getPhone()).isEqualTo("123-456-7891");
    }

    @Test
    public void testFindAuthenticatedUser() {
        HttpServletRequest mockRequestBuiltInAdmin = mock(HttpServletRequest.class);
        when(mockRequestBuiltInAdmin.getAttribute("username")).thenReturn("admin@admin.com");
        User managerUser = methodHelper.findAuthenticatedUser(mockRequestBuiltInAdmin);
        assertThat(managerUser).isNotNull();
        assertThat(managerUser.getEmail()).isEqualTo("admin@admin.com");
        assertThat(managerUser.getFirstName()).isEqualTo("Built in Admin");
        assertThat(managerUser.getLastName()).isEqualTo("Built in Admin");
        assertThat(managerUser.getPhone()).isEqualTo("111-111-1111");
    }

    @Test
    public void testFindAuthenticatedUser_UserNotFound() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("nonexistent@user.com");
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            methodHelper.findAuthenticatedUser(mockRequest);
        });

        // Assert
        assertThat(exception.getMessage())
                .isEqualTo(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, "nonexistent@user.com"));
    }

    @Test
    public void testFindUserById_UserNotFound() {
        // Arrange
        Long nonExistentUserId = 96941239L; // Bulunamayan kullanıcı ID'si

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            methodHelper.findUserById(nonExistentUserId); // userService, test edilen sınıf
        });

        // Assert
        assertThat(exception.getMessage())
                .isEqualTo(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, nonExistentUserId));
    }

    @Test
    public void testDeleteAuthenticatedUser_Success() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com");
        UserRequest user = new UserRequest();
        user.setEmail("authdelete@email.com");
        user.setFirstName("userFirstName");
        user.setLastName("Last");
        user.setBuiltIn(false);
        user.setPhone("123-456-7889");
        user.setPassword("ngfajkfgnjfkg");
        ResponseMessage<UserResponse> createdUserToDelete =  userService.createUser(user, mockRequest, "Customer");

        UserResponse authenticatedUser = createdUserToDelete.getObject();

        HttpServletRequest mockRequestAuthUser = mock(HttpServletRequest.class);
        when(mockRequestAuthUser.getAttribute("username")).thenReturn("authdelete@email.com");

        // Act
        ResponseMessage<UserResponse> response = userService.deleteAuthenticatedUser(mockRequestAuthUser);

        // Assert
        assertThat(response.getMessage()).isEqualTo(SuccessMessages.USER_DELETE);
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Test updating an authenticated user")
    @Transactional
    void testUpdateAuthenticatedUser_Success() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com");

        UserRequest user = new UserRequest();
        user.setEmail("authupdate@email.com");
        user.setFirstName("userFirstName");
        user.setLastName("Last");
        user.setBuiltIn(false);
        user.setPhone("123-456-1889");
        user.setPassword("ngfajkfgnjfkg");
        ResponseMessage<UserResponse> createdUserToUpdate =  userService.createUser(user, mockRequest, "Customer");

        UserRequestWithoutPassword userRequest = new UserRequestWithoutPassword();
        userRequest.setEmail("updateduser@example.com");
        userRequest.setFirstName("Updated");
        userRequest.setLastName("User");
        userRequest.setPhone("987-654-3210");

        HttpServletRequest mockRequestAuthUpdate = mock(HttpServletRequest.class);
        when(mockRequestAuthUpdate.getAttribute("username")).thenReturn("authupdate@email.com");

        // Act: Kullanıcıyı güncelleyin
        ResponseMessage<UserResponse> response = userService.updateAuthenticatedUser(userRequest, mockRequestAuthUpdate);

        // Assert: Yanıtı doğrulayın
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo(SuccessMessages.USER_UPDATE);
        assertThat(response.getObject().getEmail()).isEqualTo("updateduser@example.com");
        assertThat(response.getObject().getFirstName()).isEqualTo("Updated");
        assertThat(response.getObject().getLastName()).isEqualTo("User");
        assertThat(response.getObject().getPhone()).isEqualTo("987-654-3210");
    }

    @Test
    @DisplayName("Test updating a user by ID")
    @Transactional
    void testUpdateUserById_Success() {
        // Arrange: Prepare mock objects
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("username")).thenReturn("admin@admin.com");
        User admin = methodHelper.findAuthenticatedUser(mockRequest);

        // Mock User to be updated
        UserRequestWithoutPassword userRequest = new UserRequestWithoutPassword();
        userRequest.setEmail("updateduser@example.com");
        userRequest.setFirstName("Updated");
        userRequest.setLastName("User");
        userRequest.setPhone("987-654-3210");

        UserRequest user = new UserRequest();
        user.setEmail("idupdate@email.com");
        user.setFirstName("userFirstName");
        user.setLastName("Last");
        user.setBuiltIn(false);
        user.setPhone("123-456-1289");
        user.setPassword("ngfajkfgnjfkg");
        ResponseMessage<UserResponse> createdUserToUpdate =  userService.createUser(user, mockRequest, "Customer");
        Long userId = createdUserToUpdate.getObject().getUserId();

        // Act: Call the update method
        ResponseMessage<UserResponse> response = userService.updateUserById(userRequest, userId, mockRequest);

        // Assert: Validate the response
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo(SuccessMessages.USER_UPDATE_FOR_SUPERUSER);
        assertThat(response.getObject().getEmail()).isEqualTo("updateduser@example.com");
        assertThat(response.getObject().getFirstName()).isEqualTo("Updated");
        assertThat(response.getObject().getLastName()).isEqualTo("User");
        assertThat(response.getObject().getPhone()).isEqualTo("987-654-3210");

    }

}

