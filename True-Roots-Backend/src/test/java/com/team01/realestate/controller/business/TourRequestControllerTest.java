package com.team01.realestate.controller.business;


import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.service.business.TourRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TourRequestControllerTest {

    Logger logger = LoggerFactory.getLogger(TourRequestControllerTest.class);



    @InjectMocks
    private TourRequestController tourRequestController;

    @Mock
    private TourRequestService tourRequestService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test create tourRequest in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void createTourRequestTest() {

        // Arrange
        LocalDate tourDate = LocalDate.of(2025,2,5);
        LocalTime tourTime = LocalTime.of(10,30,30);

        TourRequestRequest tourRequestRequest = new TourRequestRequest();
        tourRequestRequest.setTourDate(tourDate);
        tourRequestRequest.setTourTime(tourTime);

        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        tourRequestResponse.setId(1L);
        tourRequestResponse.setTourDate(tourDate);
        tourRequestResponse.setTourTime(tourTime);

        ResponseMessage<TourRequestResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.CREATED);
        expectedResponse.setObject(tourRequestResponse);

        when(tourRequestService.createTourRequest(tourRequestRequest, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.createTourRequest(tourRequestRequest, httpServletRequest);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourDate, response.getObject().getTourDate());
        assertEquals(tourTime, response.getObject().getTourTime());
        verify(tourRequestService, times(1)).createTourRequest(tourRequestRequest, httpServletRequest);


        // Log
        logger.info("created tourRequest: {}", response);

    }

    @Test
    @DisplayName("Test get tourRequestsForAuthUserByPage in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void getTourRequestsByAuthUserTest() {
        //Arrange
        String q = "House";
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        String type = "asc";

        Page<TourRequestResponse> tourRequestPage = new PageImpl<>(Collections.emptyList());
        ResponseMessage<Page<TourRequestResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(tourRequestPage);

        when(tourRequestService.getTourRequestsByAuthUser(
                q, page, size, sort, type, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<Page<TourRequestResponse>> response = tourRequestController.getTourRequestsByAuthUser(
                q, page, size, sort, type, httpServletRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        verify(tourRequestService, times(1)).getTourRequestsByAuthUser(
                q, page, size, sort, type, httpServletRequest);

        // Log
        logger.info("get tourRequest by Page: {}", response);
    }


    @Test
    @DisplayName("Test get tourRequestsForAdminByPage in controller")
    @WithMockUser(authorities = {"ADMIN"})
    void getAllTourRequestByPageTest() {
        //Arrange
        String q = "House";
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        String type = "asc";

        Page<TourRequestResponse> tourRequestPage = new PageImpl<>(Collections.emptyList());
        ResponseMessage<Page<TourRequestResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(tourRequestPage);

        when(tourRequestService.getAllTourRequestByPage(
                q, page, size, sort, type)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<Page<TourRequestResponse>> response = tourRequestController.getAllTourRequestByPage(
                q, page, size, sort, type);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        verify(tourRequestService, times(1)).getAllTourRequestByPage(
                q, page, size, sort, type);

        // Log
        logger.info("get tourRequest by Page: {}", response);
    }

    @Test
    @DisplayName("Test get tourRequestsDetailForUser in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void getTourRequestDetailForAuthUserTest() {
        //Arrange
        Long id = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.getTourRequestDetailForAuthUser(id, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.getTourRequestDetailForAuthUser(id, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).getTourRequestDetailForAuthUser(id, httpServletRequest);

        // Log
        logger.info("get tourRequest Details: {}", response);

    }

    @Test
    @DisplayName("Test get tourRequestsDetailForAdmin in controller")
    @WithMockUser(authorities = {"ADMIN"})
    void getTourRequestDetailForAdminTest() {
        //Arrange
        Long id = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.getTourRequestDetailForAdmin(id)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.getTourRequestDetailForAdmin(id);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).getTourRequestDetailForAdmin(id);

        // Log
        logger.info("get tourRequest Details: {}", response);
    }

    @Test
    @DisplayName("Test update tourRequestsForUser in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void updateTourRequestForUserTest() {
        // Arrange
        Long tourRequestId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestRequest tourRequestRequest = new TourRequestRequest();
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.updateTourRequestForAuthUser(tourRequestRequest, tourRequestId, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.updateTourRequestForUser(tourRequestRequest, tourRequestId, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).updateTourRequestForAuthUser(tourRequestRequest, tourRequestId, httpServletRequest);

        // Log
        logger.info("update tourRequest Details: {}", response);
    }


    @Test
    @DisplayName("Test cancel tourRequestsForUser in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void cancelTourRequestForUserTest() {
        // Arrange
        Long tourRequestId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_CANCELLED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.cancelTourRequestForUser(tourRequestId, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.cancelTourRequestForUser(tourRequestId, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).cancelTourRequestForUser(tourRequestId, httpServletRequest);

        // Log
        logger.info("cancel tourRequest Details: {}", response);
    }


    @Test
    @DisplayName("Test cancel tourRequestsForUser in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void approveTourRequestForUserTest() {
        // Arrange
        Long tourRequestId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_APPROVED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.approveTourRequestForUser(tourRequestId, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.approveTourRequestForUser(tourRequestId, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).approveTourRequestForUser(tourRequestId, httpServletRequest);

        // Log
        logger.info("approve tourRequest Details: {}", response);
    }

    @Test
    @DisplayName("Test cancel tourRequestsForUser in controller")
    @WithMockUser(authorities = {"CUSTOMER"})
    void declineTourRequestForUserTest() {
        // Arrange
        Long tourRequestId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_DECLINED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.declineTourRequestForUser(tourRequestId, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.declineTourRequestForUser(tourRequestId, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).declineTourRequestForUser(tourRequestId, httpServletRequest);

        // Log
        logger.info("decline tourRequest Details: {}", response);
    }

    @Test
    @DisplayName("Test delete tourRequestsForAdmin in controller")
    @WithMockUser(authorities = {"ADMIN"})
    void deleteTourRequestForAdminAndManager() {
        //Arrange
        Long tourRequestId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        ResponseMessage<TourRequestResponse> expectedResponse = ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(tourRequestService.deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest)).thenReturn(expectedResponse);

        //Act
        ResponseMessage<TourRequestResponse> response = tourRequestController.deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(tourRequestResponse, response.getObject());
        verify(tourRequestService, times(1)).deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest);

        // Log
        logger.info("delete tourRequest Details: {}", response);
    }

}