package com.team01.realestate.service.business;

import com.team01.realestate.controller.business.TourRequestControllerTest;
import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.entity.enums.TourRequestStatus;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.payload.mapper.TourRequestMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.repository.business.TourRequestRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourRequestServiceTest {


    @Mock
    private TourRequestRepository tourRequestRepository;

    @Mock
    private TourRequestMapper tourRequestMapper;

    @Mock
    private MethodHelper methodHelper;

    @Mock
    private LogService logService;

    @Mock
    private PageableHelper pageableHelper;

    @InjectMocks
    private TourRequestService tourRequestService;

    @Mock
    private HttpServletRequest httpServletRequest;


    @Test
    @DisplayName("Test CREATE tourRequest in SERVICE")
    void createTourRequest() {
        // Arrange
        LocalDate tourDate = LocalDate.of(2025,2,5);
        LocalTime tourTime = LocalTime.of(10,30,30);

        TourRequestRequest tourRequestRequest = new TourRequestRequest();
        tourRequestRequest.setTourDate(tourDate);
        tourRequestRequest.setTourTime(tourTime);
        tourRequestRequest.setAdvertId(1L);

        User authenticatedUser = new User();                // Mock user
        authenticatedUser.setId(3L);

        User ownerUser = new User();                        // Mock owner user
        ownerUser.setId(2L);

        Advert advert = new Advert();                       // Mock advert
        advert.setId(1L);
        advert.setUser(ownerUser);
        advert.setAdvertStatus(AdvertStatus.ACTIVATED); // Durumu aktif

        TourRequest tourRequest = new TourRequest();        // Mock TourRequest
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        TourRequest savedTourRequest = new TourRequest();   // Mock saved tour request
        savedTourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        when(methodHelper.findAuthenticatedUser(any())).thenReturn(authenticatedUser);
        when(methodHelper.findAdvertById(anyLong())).thenReturn(advert);
        when(methodHelper.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        when(tourRequestMapper.tourRequestRequestToTourRequest(any())).thenReturn(tourRequest);
        when(tourRequestRepository.save(any())).thenReturn(savedTourRequest);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.createTourRequest(tourRequestRequest, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.CREATED, actualResponse.getHttpStatus());

        verify(tourRequestRepository, times(1)).save(tourRequest);
        verify(logService).createLogForTourRequest(any() ,anyString(), eq(authenticatedUser), eq(ownerUser), eq(advert));

    }

    @Test
    @DisplayName("Test GET tourRequests by authUser in SERVICE ")
    void getTourRequestsByAuthUser() {
        // Arrange
        String query = "some query";
        int page = 0;
        int size = 10;
        String sort = "date";
        String type = "asc";
        String email = "testuser@example.com";


        // Mock authenticated user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getAttribute("username")).thenReturn(email);

        // Mock pageable
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);

        // Mock TourRequest data
        TourRequest tourRequest1 = new TourRequest();
        tourRequest1.setTourRequestStatus(TourRequestStatus.PENDING);
        TourRequest tourRequest2 = new TourRequest();
        tourRequest2.setTourRequestStatus(TourRequestStatus.APPROVED);

        Page<TourRequest> tourRequests = new PageImpl<>(List.of(tourRequest1, tourRequest2));

        when(tourRequestRepository.findTourRequestsByAuthUser(email, query, pageable)).thenReturn(tourRequests);

        // Mock mapping to TourRequestResponse
        TourRequestResponse response1 = new TourRequestResponse();
        TourRequestResponse response2 = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest1)).thenReturn(response1);
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest2)).thenReturn(response2);

        // Act
        ResponseMessage<Page<TourRequestResponse>> actualResponse = tourRequestService.getTourRequestsByAuthUser(query, page, size, sort, type, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertNotNull(actualResponse.getObject());
        assertEquals(2, actualResponse.getObject().getTotalElements());

        verify(tourRequestRepository, times(1)).findTourRequestsByAuthUser(email, query, pageable);
        verify(tourRequestMapper, times(2)).tourRequestToTourRequestResponse(any(TourRequest.class));
    }

    @Test
    @DisplayName("Test GET tourRequests by user in SERVICE")
    void getTourRequestsByUserId() {
        // Arrange
        Long userId = 3L;

        // Mock TourRequest data
        TourRequest tourRequest1 = new TourRequest();
        tourRequest1.setTourRequestStatus(TourRequestStatus.PENDING);
        TourRequest tourRequest2 = new TourRequest();
        tourRequest2.setTourRequestStatus(TourRequestStatus.APPROVED);

        List<TourRequest> tourRequests = List.of(tourRequest1, tourRequest2);

        // Mock the repository call to return the mock tourRequests
        when(tourRequestRepository.findTourRequestsByUserId(userId)).thenReturn(tourRequests);

        // Mock the mapping to TourRequestResponse
        TourRequestResponse response1 = new TourRequestResponse();
        TourRequestResponse response2 = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest1)).thenReturn(response1);
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest2)).thenReturn(response2);

        // Act
        List<TourRequestResponse> actualResponse = tourRequestService.getTourRequestsByUserId(userId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());
        assertEquals(response1, actualResponse.get(0));
        assertEquals(response2, actualResponse.get(1));

        verify(tourRequestRepository, times(1)).findTourRequestsByUserId(userId);
        verify(tourRequestMapper, times(2)).tourRequestToTourRequestResponse(any(TourRequest.class));
    }

    @Test
    @DisplayName("Test GET all tourRequests by page in SERVICE")
    void getAllTourRequestByPage() {
        // Arrange
        String query = "some query";
        int page = 0;
        int size = 10;
        String sort = "date";
        String type = "asc";

        // Mock pageable object
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);

        // Mock TourRequest data
        TourRequest tourRequest1 = new TourRequest();
        tourRequest1.setTourRequestStatus(TourRequestStatus.PENDING);
        TourRequest tourRequest2 = new TourRequest();
        tourRequest2.setTourRequestStatus(TourRequestStatus.APPROVED);

        Page<TourRequest> tourRequests = new PageImpl<>(List.of(tourRequest1, tourRequest2));

        // Mock the repository call to return the mock tourRequests
        when(tourRequestRepository.findAllByQuery(query, pageable)).thenReturn(tourRequests);

        // Mock mapping to TourRequestResponse
        TourRequestResponse response1 = new TourRequestResponse();
        TourRequestResponse response2 = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest1)).thenReturn(response1);
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest2)).thenReturn(response2);

        // Act
        ResponseMessage<Page<TourRequestResponse>> actualResponse = tourRequestService.getAllTourRequestByPage(query, page, size, sort, type);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertNotNull(actualResponse.getObject());
        assertEquals(2, actualResponse.getObject().getTotalElements());
        assertEquals(response1, actualResponse.getObject().getContent().get(0));
        assertEquals(response2, actualResponse.getObject().getContent().get(1));

        verify(tourRequestRepository, times(1)).findAllByQuery(query, pageable);
        verify(tourRequestMapper, times(2)).tourRequestToTourRequestResponse(any(TourRequest.class));
    }

    @Test
    @DisplayName("Test GET tourRequest detail for authenticated user")
    void getTourRequestDetailForAuthUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock authenticated user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Mock TourRequest data
        User guestUser = new User();
        guestUser.setId(3L); // The guest user ID should match the authenticated user

        TourRequest foundTourRequest = new TourRequest();
        foundTourRequest.setGuestUser(guestUser); // Set the guest user for the TourRequest

        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(foundTourRequest);

        // Mock mapping to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(foundTourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.getTourRequestDetailForAuthUser(tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(methodHelper, times(1)).findAuthenticatedUser(httpServletRequest);
        verify(methodHelper, times(1)).findTourRequestById(tourRequestId);
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(foundTourRequest);
    }

    @Test
    @DisplayName("Test GET tourRequest detail for unauthorized user")
    void getTourRequestDetailForUnauthorizedUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock authenticated user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Mock TourRequest data with a different guest user ID
        User guestUser = new User();
        guestUser.setId(2L); // The guest user ID is different from the authenticated user

        TourRequest foundTourRequest = new TourRequest();
        foundTourRequest.setGuestUser(guestUser); // Set the guest user for the TourRequest

        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(foundTourRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            tourRequestService.getTourRequestDetailForAuthUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(methodHelper, times(1)).findAuthenticatedUser(httpServletRequest);
        verify(methodHelper, times(1)).findTourRequestById(tourRequestId);
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse(any(TourRequest.class)); // Mapper should not be called
    }

    @Test
    @DisplayName("Test GET tourRequest detail for admin")
    void getTourRequestDetailForAdmin() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock TourRequest data
        TourRequest foundTourRequest = new TourRequest();
        foundTourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the repository call to return the mock TourRequest
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(foundTourRequest);

        // Mock mapping to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(foundTourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.getTourRequestDetailForAdmin(tourRequestId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(methodHelper, times(1)).findTourRequestById(tourRequestId);
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(foundTourRequest);
    }


    @Test
    @DisplayName("Test UPDATE TourRequest for authenticated user")
    void updateTourRequestForAuthUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock TourRequestRequest object
        TourRequestRequest tourRequestRequest = new TourRequestRequest();
        LocalDate tourDate = LocalDate.of(2025,2,5);
        LocalTime tourTime = LocalTime.of(10,30,30);
        tourRequestRequest.setTourDate(tourDate);
        tourRequestRequest.setTourTime(tourTime);

        // Mock authenticated user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Mock the TourRequest object
        TourRequest tourRequest = new TourRequest();
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);
        tourRequest.setGuestUser(authUser); // Set the guest user to match the authenticated user
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Mock the repository method to check if a tour request already exists
        when(tourRequestRepository.existsByGuestUserAndTourDateWithinHour(authUser.getId(), tourDate, tourTime, tourTime.plusHours(1),tourTime.minusHours(1)))
                .thenReturn(false);

        // Mock the mapper to convert TourRequest to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Mock the repository save method
        when(tourRequestRepository.save(tourRequest)).thenReturn(tourRequest);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.updateTourRequestForAuthUser(tourRequestRequest, tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(tourRequestRepository, times(1)).save(tourRequest);
        verify(tourRequestRepository, times(1)).existsByGuestUserAndTourDateWithinHour(authUser.getId(), tourDate, tourTime, tourTime.plusHours(1),tourTime.minusHours(1));
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test update TourRequest for unauthorized user")
    void updateTourRequestForUnauthorizedUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock authenticated user (with a different ID)
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Mock the TourRequest object with a different guest user ID
        User guestUser = new User();
        guestUser.setId(2L); // The guest user ID is different from the authenticated user
        TourRequest tourRequest = new TourRequest();
        tourRequest.setGuestUser(guestUser); // The guest user is different from the authenticated user
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            tourRequestService.updateTourRequestForAuthUser(new TourRequestRequest(), tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(tourRequestRepository, times(0)).existsByGuestUserAndTourDateWithinHour(any(), any(), any(), any(),  any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }

    @Test
    @DisplayName("Test update TourRequest with invalid status")
    void updateTourRequestWithInvalidStatus() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock TourRequestRequest object
        TourRequestRequest tourRequestRequest = new TourRequestRequest();
        LocalDate tourDate = LocalDate.of(2025,2,5);
        LocalTime tourTime = LocalTime.of(10,30,30);
        tourRequestRequest.setTourDate(tourDate);
        tourRequestRequest.setTourTime(tourTime);

        // Mock authenticated user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Mock the TourRequest object with an invalid status (APPROVED)
        TourRequest tourRequest = new TourRequest();
        tourRequest.setTourRequestStatus(TourRequestStatus.APPROVED);
        tourRequest.setGuestUser(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            tourRequestService.updateTourRequestForAuthUser(tourRequestRequest, tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(tourRequestRepository, times(0)).existsByGuestUserAndTourDateWithinHour(any(), any(), any(), any(),any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }


    @Test
    @DisplayName("Test cancel TourRequest for authenticated user")
    void cancelTourRequestForUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        tourRequest.setGuestUser(authUser); // The guest user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock HTTP request and user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Mock log service to avoid actual logging
        doNothing().when(logService).createLogForTourRequest(eq(LogType.TOUR_REQUEST_CANCELED), anyString(), eq(authUser), any(), any());

        // Mock the repository save method
        when(tourRequestRepository.save(tourRequest)).thenReturn(tourRequest);

        // Mock the mapper to convert TourRequest to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.cancelTourRequestForUser(tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(tourRequestRepository, times(1)).save(tourRequest);
        verify(logService, times(1)).createLogForTourRequest(LogType.TOUR_REQUEST_CANCELED, "Tour request canceled by advert guest.", authUser, tourRequest.getOwnerUser(), tourRequest.getAdvert());
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test cancel TourRequest for unauthorized user")
    void cancelTourRequestForUnauthorizedUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with a different guest user
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        User guestUser = new User();
        guestUser.setId(2L); // Guest user with a different ID
        tourRequest.setGuestUser(guestUser); // The guest user is different from the authenticated user

        // Mock HTTP request and user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            tourRequestService.cancelTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }

    @Test
    @DisplayName("Test cancel TourRequest that is already canceled")
    void cancelTourRequestAlreadyCanceled() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with CANCELED status
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3
        tourRequest.setGuestUser(authUser); // The guest user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.CANCELED);

        // Mock HTTP request and user
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            tourRequestService.cancelTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.STATUS_ALREADY_CANCELED, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }


    @Test
    @DisplayName("Test approve TourRequest for the advert owner")
    void approveTourRequestForUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with the status PENDING
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (owner)
        tourRequest.setOwnerUser(authUser); // The owner user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Mock the repository save method
        when(tourRequestRepository.save(tourRequest)).thenReturn(tourRequest);

        // Mock log service to avoid actual logging
        doNothing().when(logService).createLogForTourRequest(eq(LogType.TOUR_REQUEST_ACCEPTED), anyString(), eq(tourRequest.getGuestUser()), eq(authUser), eq(tourRequest.getAdvert()));

        // Mock the mapper to convert TourRequest to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.approveTourRequestForUser(tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(tourRequestRepository, times(1)).save(tourRequest);
        verify(logService, times(1)).createLogForTourRequest(LogType.TOUR_REQUEST_ACCEPTED, "Tour request approved by advert owner.", tourRequest.getGuestUser(), authUser, tourRequest.getAdvert());
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test approve TourRequest by non-owner user")
    void approveTourRequestForNonOwnerUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with a different owner
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (not the owner)
        User ownerUser = new User();
        ownerUser.setId(2L); // Different owner user
        tourRequest.setOwnerUser(ownerUser); // The owner user is different from the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            tourRequestService.approveTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }

    @Test
    @DisplayName("Test approve TourRequest that is already approved")
    void approveTourRequestAlreadyApproved() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with APPROVED status
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (owner)
        tourRequest.setOwnerUser(authUser); // The owner user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.APPROVED);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            tourRequestService.approveTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.STATUS_ALREADY_APPROVED, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }


    @Test
    @DisplayName("Test decline TourRequest for the advert owner")
    void declineTourRequestForUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with the status PENDING
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (owner)
        tourRequest.setOwnerUser(authUser); // The owner user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Mock the repository save method
        when(tourRequestRepository.save(tourRequest)).thenReturn(tourRequest);

        // Mock log service to avoid actual logging
        doNothing().when(logService).createLogForTourRequest(eq(LogType.TOUR_REQUEST_DECLINED), anyString(), eq(tourRequest.getGuestUser()), eq(authUser), eq(tourRequest.getAdvert()));

        // Mock the mapper to convert TourRequest to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.declineTourRequestForUser(tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());

        verify(tourRequestRepository, times(1)).save(tourRequest);
        verify(logService, times(1)).createLogForTourRequest(LogType.TOUR_REQUEST_DECLINED, "Tour request declined by advert owner.", tourRequest.getGuestUser(), authUser, tourRequest.getAdvert());
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test decline TourRequest by non-owner user")
    void declineTourRequestForNonOwnerUser() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with a different owner
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (not the owner)
        User ownerUser = new User();
        ownerUser.setId(2L); // Different owner user
        tourRequest.setOwnerUser(ownerUser); // The owner user is different from the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            tourRequestService.declineTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }

    @Test
    @DisplayName("Test decline TourRequest that is already declined")
    void declineTourRequestAlreadyDeclined() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the TourRequest object with DECLINED status
        TourRequest tourRequest = new TourRequest();
        User authUser = new User();
        authUser.setId(3L); // Authenticated user with ID 3 (owner)
        tourRequest.setOwnerUser(authUser); // The owner user is the same as the authenticated user
        tourRequest.setTourRequestStatus(TourRequestStatus.DECLINED);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            tourRequestService.declineTourRequestForUser(tourRequestId, httpServletRequest);
        });

        assertEquals(ErrorMessages.STATUS_ALREADY_DECLINED, exception.getMessage());

        verify(tourRequestRepository, times(0)).save(any());
        verify(logService, times(0)).createLogForTourRequest(any(), anyString(), any(), any(), any());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }


    @Test
    @DisplayName("Test delete TourRequest by Admin or Manager")
    void deleteTourRequestForAdminAndManager() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the authenticated user with 'ADMIN' role
        User authUser = new User();
        authUser.setId(3L);
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        authUser.setRoles(Collections.singletonList(adminRole));

        // Mock the TourRequest object
        TourRequest tourRequest = new TourRequest();
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);
        when(methodHelper.findTourRequestById(tourRequestId)).thenReturn(tourRequest);

        // Mock the repository deleteById method
        doNothing().when(tourRequestRepository).deleteById(tourRequestId);

        // Mock the mapper to convert TourRequest to TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse();
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<TourRequestResponse> actualResponse = tourRequestService.deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getHttpStatus());
        assertEquals(tourRequestResponse, actualResponse.getObject());
        assertEquals(SuccessMessages.TOUR_REQUEST_DELETED, actualResponse.getMessage());

        verify(tourRequestRepository, times(1)).deleteById(tourRequestId);
        verify(tourRequestMapper, times(1)).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test delete TourRequest by non-Admin/Manager")
    void deleteTourRequestForNonAdminAndManager() {
        // Arrange
        Long tourRequestId = 1L;

        // Mock the authenticated user with 'USER' role (non-admin/non-manager)
        User authUser = new User();
        authUser.setId(3L);
        Role userRole = new Role();
        userRole.setRoleName("USER"); // 'USER' role
        authUser.setRoles(Collections.singletonList(userRole)); // Add role to the user

        // Mock the HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authUser);

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            tourRequestService.deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest); // Call the method
        });

        // Assert: Ensure the exception message matches
        assertEquals(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE, exception.getMessage());

        // Verify that no deletion or mapping occurred
        verify(tourRequestRepository, times(0)).deleteById(anyLong());
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }






    @Test
    @DisplayName("Test getTourRequestReports when no tour requests are found")
    void testGetTourRequestReportsWhenEmptyList() {
        // Arrange
        LocalDate date1 = LocalDate.now().minusDays(7);
        LocalDate date2 = LocalDate.now();
        TourRequestStatus status = TourRequestStatus.PENDING;

        // Mock the repository to return an empty list
        when(tourRequestRepository.findByFilters(date1, date2, status)).thenReturn(Collections.emptyList());

        // Act
        ResponseMessage<List<TourRequestResponse>> response = tourRequestService.getTourRequestReports(date1, date2, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.ADVERTS_LIST_IS_EMPTY, response.getMessage());
        assertTrue(response.getObject().isEmpty());

        verify(tourRequestRepository).findByFilters(date1, date2, status);
        verify(tourRequestMapper, times(0)).tourRequestToTourRequestResponse((TourRequest) any());
    }

    @Test
    @DisplayName("Test getTourRequestReports when tour requests are found")
    void testGetTourRequestReportsWhenListIsNotEmpty() {
        // Arrange
        LocalDate date1 = LocalDate.now().minusDays(7);
        LocalDate date2 = LocalDate.now();
        TourRequestStatus status = TourRequestStatus.APPROVED;

        // Mock the repository to return a non-empty list
        TourRequest tourRequest = new TourRequest(); // You can set mock values for this object
        List<TourRequest> tourRequests = Collections.singletonList(tourRequest);
        when(tourRequestRepository.findByFilters(date1, date2, status)).thenReturn(tourRequests);

        // Mock the mapper to return a TourRequestResponse
        TourRequestResponse tourRequestResponse = new TourRequestResponse(); // Set mock response data if needed
        when(tourRequestMapper.tourRequestToTourRequestResponse(tourRequest)).thenReturn(tourRequestResponse);

        // Act
        ResponseMessage<List<TourRequestResponse>> response = tourRequestService.getTourRequestReports(date1, date2, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.TOUR_REQUEST_FOUND, response.getMessage());
        assertEquals(1, response.getObject().size()); // Should contain one item
        assertEquals(tourRequestResponse, response.getObject().get(0));

        verify(tourRequestRepository).findByFilters(date1, date2, status);
        verify(tourRequestMapper).tourRequestToTourRequestResponse(tourRequest);
    }

    @Test
    @DisplayName("Test deleteTourRequestsByUserId when user has associated tour requests")
    void testDeleteTourRequestsByUserIdWhenRequestsExist() {
        // Arrange
        Long userId = 1L;
        TourRequest tourRequest = new TourRequest();
        tourRequest.setId(1L);
        List<TourRequest> tourRequests = Collections.singletonList(tourRequest); // Mocked list of tour requests for user

        // Mock the repository to return a list of tour requests
        when(tourRequestRepository.findTourRequestsByUserId(userId)).thenReturn(tourRequests);

        // Act
        tourRequestService.deleteTourRequestsByUserId(userId);

        // Assert
        verify(tourRequestRepository).findTourRequestsByUserId(userId);  // Ensure the method was called
        verify(tourRequestRepository).deleteAll(tourRequests);            // Ensure deleteAll was called with the correct list

        // Verify no other interactions occurred with the repository
        verifyNoMoreInteractions(tourRequestRepository);
    }

}