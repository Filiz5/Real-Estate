package com.team01.realestate.controller.business;

import com.team01.realestate.controller.business.ReportController;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.TourRequestStatus;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.ReportResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;

import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.service.business.AdvertService;
import com.team01.realestate.service.business.ReportService;
import com.team01.realestate.service.business.TourRequestService;
import com.team01.realestate.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Mock
    private AdvertService advertService;

    @Mock
    private TourRequestService tourRequestService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getStatistics_ShouldReturnSuccessResponse() {
        // Arrange
        ReportResponse reportResponse = new ReportResponse();
        ResponseMessage<ReportResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Statistics retrieved successfully");
        expectedResponse.setObject(reportResponse);

        when(reportService.getStatistic()).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ReportResponse> response = reportController.getStatistics();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Statistics retrieved successfully", response.getMessage());
        verify(reportService, times(1)).getStatistic();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getPopularAdverts_ShouldReturnSuccessResponse() {
        // Arrange
        int amount = 10;
        List<AdvertResponse> adverts = List.of(new AdvertResponse(), new AdvertResponse());
        ResponseMessage<List<AdvertResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Popular adverts retrieved successfully");
        expectedResponse.setObject(adverts);

        when(advertService.getPopularAdvertsForAdmin(amount)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<List<AdvertResponse>> response = reportController.getPopularAdverts(amount);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Popular adverts retrieved successfully", response.getMessage());
        verify(advertService, times(1)).getPopularAdvertsForAdmin(amount);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getAdvertsReports_ShouldReturnSuccessResponse() {
        // Arrange
        LocalDate date1 = LocalDate.of(2023, 2, 1);
        LocalDate date2 = LocalDate.of(2023, 2, 28);
        Long categoryId = 1L;
        Long advertTypeId = 2L;
        AdvertStatus status = AdvertStatus.PENDING;

        List<AdvertResponse> adverts = List.of(new AdvertResponse(), new AdvertResponse());
        ResponseMessage<List<AdvertResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Adverts reports retrieved successfully");
        expectedResponse.setObject(adverts);

        when(advertService.getAdvertsReports(
                date1.atStartOfDay(), date2.atTime(23, 59, 59), categoryId, advertTypeId, status))
                .thenReturn(expectedResponse);

        // Act
        ResponseMessage<List<AdvertResponse>> response = reportController.getAdvertsReports(
                date1, date2, categoryId, advertTypeId, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Adverts reports retrieved successfully", response.getMessage());
        verify(advertService, times(1)).getAdvertsReports(
                date1.atStartOfDay(), date2.atTime(23, 59, 59), categoryId, advertTypeId, status);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getReportUsers_ShouldReturnSuccessResponse() {
        // Arrange
        String role = "manager";
        UserResponse user1 = new UserResponse();
        user1.setUserId(1L); // Benzersiz bir ID veya diğer alanları ayarlayın
        user1.setFirstName("John");
        user1.setLastName("Doe");

        UserResponse user2 = new UserResponse();
        user2.setUserId(2L); // Benzersiz bir ID veya diğer alanları ayarlayın
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        Set<UserResponse> users = Set.of(user1, user2);
        when(reportService.getUsersWithRole(role)).thenReturn(users);

        // Act
        ResponseMessage<Set<UserResponse>> response = reportController.getReportUsers(role);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.USER_FOUND, response.getMessage());
        assertNotNull(response.getObject());
        verify(reportService, times(1)).getUsersWithRole(role);
    }


    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getTourRequestReports_ShouldReturnSuccessResponse() {
        // Arrange
        LocalDate date1 = LocalDate.of(2023, 2, 1);
        LocalDate date2 = LocalDate.of(2023, 2, 28);
        TourRequestStatus status = TourRequestStatus.PENDING;

        List<TourRequestResponse> tourRequests = List.of(new TourRequestResponse(), new TourRequestResponse());
        ResponseMessage<List<TourRequestResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Tour request reports retrieved successfully");
        expectedResponse.setObject(tourRequests);

        when(tourRequestService.getTourRequestReports(
                date1, date2, status))
                .thenReturn(expectedResponse);

        // Act
        ResponseMessage<List<TourRequestResponse>> response = reportController.getTourRequestReports(
                date1, date2, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Tour request reports retrieved successfully", response.getMessage());
        verify(tourRequestService, times(1)).getTourRequestReports(
                date1, date2, status);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void getReportUsers_ShouldReturnBadRequestForInvalidRole() {
        // Arrange
        String invalidRole = "invalidRole";
        String errorMessage = "Role not found: " + invalidRole;

        when(reportService.getUsersWithRole(invalidRole))
                .thenThrow(new IllegalArgumentException(errorMessage));

        // Act
        ResponseMessage<Set<UserResponse>> response = reportController.getReportUsers(invalidRole);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getObject());
        verify(reportService, times(1)).getUsersWithRole(invalidRole);
    }
}

