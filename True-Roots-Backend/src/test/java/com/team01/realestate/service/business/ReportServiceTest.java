package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.AdvertType;
import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.payload.mapper.UserMapper;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.ReportResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.AdvertTypeRepository;
import com.team01.realestate.repository.business.CategoryRepository;
import com.team01.realestate.repository.business.TourRequestRepository;
import com.team01.realestate.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private AdvertTypeRepository advertTypeRepository;

    @Mock
    private TourRequestRepository tourRequestRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ReportService reportService;

    ReportServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatistic_ShouldReturnValidReport() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(new Category(), new Category())); // 2 kategori
        when(advertRepository.findAll()).thenReturn(List.of(new Advert(), new Advert(), new Advert())); // 3 ilan
        when(advertTypeRepository.findAll()).thenReturn(List.of(new AdvertType())); // 1 ilan türü
        when(tourRequestRepository.findAll()).thenReturn(List.of(new TourRequest(), new TourRequest())); // 2 tur isteği

        Role customerRole = new Role();
        customerRole.setRoleName("Customer"); // Ensure the role name is exactly "Customer"

        // Müşteri kullanıcısını oluştur
        User customer1 = new User();
        customer1.setId(1L);
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setRoles(List.of(customerRole));

        // Müşteri kullanıcısını oluştur
        User customer2 = new User();
        customer2.setId(2L);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setRoles(List.of(customerRole));

        when(userRepository.findAll()).thenReturn(List.of(customer1, customer2)); // 2 customer

        // Act
        ResponseMessage<ReportResponse> response = reportService.getStatistic();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.REPORT_CREATED_SUCCESSFULLY, response.getMessage());
        assertNotNull(response.getObject());

        ReportResponse report = response.getObject();
        assertEquals(2, report.getNumberOfCategory());
        assertEquals(3, report.getNumberOfAdvert());
        assertEquals(1, report.getNumberOfAdvertType());
        assertEquals(2, report.getNumberOfTourRequest());
        assertEquals(2, report.getNumberOfCustomer()); // Ensure customer count matches

        verify(categoryRepository, times(1)).findAll();
        verify(advertRepository, times(1)).findAll();
        verify(advertTypeRepository, times(1)).findAll();
        verify(tourRequestRepository, times(1)).findAll();
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void getStatistic_ShouldHandleRepositoryException() {
        // Arrange
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> reportService.getStatistic());

        verify(categoryRepository, times(1)).findAll();
        verifyNoInteractions(advertRepository, advertTypeRepository, tourRequestRepository, userRepository);
    }


    @Test
    void getUsersWithRole_ShouldReturnUsersWithValidRole() {
        // Arrange
        String roleName = "MANAGER";
        RoleType roleType = RoleType.MANAGER;

        User user1 = new User();
        user1.setId(1L); // Benzersiz bir ID atayın
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2L); // Farklı bir ID atayın
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        UserResponse userResponse1 = new UserResponse();
        userResponse1.setUserId(1L); // Benzersiz bir ID atayın
        UserResponse userResponse2 = new UserResponse();
        userResponse2.setUserId(2L); // Farklı bir ID atayın

        when(userRepository.findUsersQueryForReports(roleType))
                .thenReturn(Set.of(user1, user2)); // Artık benzersiz nesneler
        when(userMapper.mapUserToUserResponse(user1)).thenReturn(userResponse1);
        when(userMapper.mapUserToUserResponse(user2)).thenReturn(userResponse2);

        // Act
        Set<UserResponse> users = reportService.getUsersWithRole(roleName);

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(userResponse1));
        assertTrue(users.contains(userResponse2));
        verify(userRepository, times(1)).findUsersQueryForReports(roleType);
        verify(userMapper, times(1)).mapUserToUserResponse(user1);
        verify(userMapper, times(1)).mapUserToUserResponse(user2);
    }


        @Test
    void getUsersWithRole_ShouldThrowExceptionForInvalidRole() {
        // Arrange
        String invalidRole = "INVALID_ROLE";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getUsersWithRole(invalidRole)
        );

        assertEquals("Invalid role name: " + invalidRole, exception.getMessage());
        verifyNoInteractions(userRepository, userMapper);
    }
}

