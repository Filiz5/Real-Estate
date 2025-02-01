package com.team01.realestate.controller.business;

import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.request.business.UpdateAdvertStatusRequest;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.service.business.AdvertService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdvertControllerTest {

    @InjectMocks
    private AdvertController advertController;

    @Mock
    private AdvertService advertService;

    @Mock
    private HttpServletRequest httpServletRequest;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

    @Test
    @WithMockUser(authorities = {"CUSTOMER"})
    void createAdvert_ShouldReturnAdvertResponse() {
        // Arrange
        AdvertRequest advertRequest = new AdvertRequest();
        advertRequest.setTitle("Sample Title");
        advertRequest.setDesc("Sample Description");

        AdvertResponse advertResponse = new AdvertResponse();
        advertResponse.setId(1L);
        advertResponse.setTitle("Sample Title");
        advertResponse.setDesc("Sample Description");

        ResponseMessage<AdvertResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.CREATED);
        expectedResponse.setObject(advertResponse);

        when(advertService.createAdvert(advertRequest, httpServletRequest)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.createAdvert(advertRequest, httpServletRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals("Sample Title", response.getObject().getTitle());
        assertEquals("Sample Description", response.getObject().getDesc());
        verify(advertService, times(1)).createAdvert(advertRequest, httpServletRequest);
    }

    @Test
    void getAdvertsSearchAndParametersPage_ShouldReturnPageOfAdverts() {
        // Arrange
        String q = "Beautiful";
        Long categoryId = 1L;
        Long advertTypeId = 1L;
        BigDecimal priceStart = new BigDecimal("750000");
        BigDecimal priceEnd = new BigDecimal("750000.50");
        Integer statusValue = 0;
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        String type = "asc";

        Page<AdvertResponse> advertPage = new PageImpl<>(Collections.emptyList());
        ResponseMessage<Page<AdvertResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(advertPage);

        when(advertService.getAdvertSearchAndParametersPage(
                q, categoryId, advertTypeId, priceStart, priceEnd, AdvertStatus.fromValue(statusValue), page, size, sort, type
        )).thenReturn(expectedResponse);

        // Act
        ResponseMessage<Page<AdvertResponse>> response = advertController.getAdvertsSearchAndParametersPage(
                q, categoryId, advertTypeId, priceStart, priceEnd, statusValue, page, size, sort, type
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        verify(advertService, times(1)).getAdvertSearchAndParametersPage(
                q, categoryId, advertTypeId, priceStart, priceEnd, AdvertStatus.fromValue(statusValue), page, size, sort, type
        );
    }

    @Test
    void getAdvertsGroupedByCity_ShouldReturnCityAdverts() {
        // Arrange
        List<CityAdvertResponse> cityAdverts = Collections.emptyList();
        ResponseMessage<List<CityAdvertResponse>> expectedResponse = ResponseMessage.<List<CityAdvertResponse>>builder()
                .message(SuccessMessages.ADVERTS_FOUND_GROUPED_BY_CITIES)
                .httpStatus(HttpStatus.OK)
                .object(cityAdverts)
                .build();

        when(advertService.getAdvertsGroupedByCity()).thenReturn(cityAdverts);

        // Act
        ResponseMessage<List<CityAdvertResponse>> response = advertController.getAdvertsGroupedByCity();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(cityAdverts, response.getObject());
        verify(advertService, times(1)).getAdvertsGroupedByCity();
    }

    @Test
    void getAdvertsGroupedByCity_ShouldHandleException() {
        // Arrange
        when(advertService.getAdvertsGroupedByCity()).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseMessage<List<CityAdvertResponse>> response = advertController.getAdvertsGroupedByCity();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertNull(response.getObject());
        assertEquals(ErrorMessages.NOT_FOUND_ADVERTS_GROUPED_BY_CITY_MESSAGE, response.getMessage());
        verify(advertService, times(1)).getAdvertsGroupedByCity();
    }

    @Test
    void getAdvertsGroupedByCategory_ShouldReturnCategoryAdverts() {
        // Arrange
        List<CategoryAdvertResponse> categoryAdverts = Collections.emptyList();
        ResponseMessage<List<CategoryAdvertResponse>> expectedResponse = ResponseMessage.<List<CategoryAdvertResponse>>builder()
                .message(SuccessMessages.ADVERTS_FOUND_GROUPED_BY_CATEGORIES)
                .httpStatus(HttpStatus.OK)
                .object(categoryAdverts)
                .build();

        when(advertService.getAdvertsGroupedByCategory()).thenReturn(categoryAdverts);

        // Act
        ResponseMessage<List<CategoryAdvertResponse>> response = advertController.getAdvertsGroupedByCategory();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(categoryAdverts, response.getObject());
        assertEquals(SuccessMessages.ADVERTS_FOUND_GROUPED_BY_CATEGORIES, response.getMessage());
        verify(advertService, times(1)).getAdvertsGroupedByCategory();
    }

    @Test
    void getAdvertsGroupedByCategory_ShouldHandleException() {
        // Arrange
        when(advertService.getAdvertsGroupedByCategory()).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseMessage<List<CategoryAdvertResponse>> response = advertController.getAdvertsGroupedByCategory();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertNull(response.getObject());
        assertEquals(ErrorMessages.NOT_FOUND_ADVERTS_GROUPED_BY_CATEGORY_MESSAGE, response.getMessage());
        verify(advertService, times(1)).getAdvertsGroupedByCategory();
    }

    @Test
    void getPopularAdverts_ShouldReturnPopularAdverts() {
        // Arrange
        int amount = 10;
        List<AdvertResponse> popularAdverts = Collections.emptyList();
        ResponseMessage<List<AdvertResponse>> expectedResponse = ResponseMessage.<List<AdvertResponse>>builder()
                .object(popularAdverts)
                .message(SuccessMessages.ADVERTS_FOUND_BY_POPULARITY)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getPopularAdverts(amount)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertController.getPopularAdverts(amount, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(popularAdverts, response.getObject());
        verify(advertService, times(1)).getPopularAdverts(amount);
    }

    @Test
    void getUserAdverts_ShouldReturnUserAdverts() {
        // Arrange
        Page<AdvertResponseForList> advertsPage = Page.empty();
        ResponseMessage<Page<AdvertResponseForList>> expectedResponse = ResponseMessage.<Page<AdvertResponseForList>>builder()
                .object(advertsPage)
                .message(SuccessMessages.ADVERTS_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getAuthenticatedUsersAdverts(anyString(), anyInt(), anyInt(), anyString(), anyString(), any(HttpServletRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertController.getUserAdverts("query", 0, 20, "category_id", "asc", mock(HttpServletRequest.class));

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertsPage, response.getObject());
        verify(advertService, times(1)).getAuthenticatedUsersAdverts(anyString(), anyInt(), anyInt(), anyString(), anyString(), any(HttpServletRequest.class));
    }

    @Test
    void getAdvertsSearchAndParametersPageForAdmin_ShouldReturnAdvertsPage() {
        // Arrange
        Page<AdvertResponseForList> advertsPage = Page.empty();
        ResponseMessage<Page<AdvertResponseForList>> expectedResponse = ResponseMessage.<Page<AdvertResponseForList>>builder()
                .object(advertsPage)
                .message(SuccessMessages.ADVERTS_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getAdvertSearchAndParametersPageForAdmin(anyString(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(expectedResponse);

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertController.getAdvertsSearchAndParametersPageForAdmin(
                "query", 2L, 1L, BigDecimal.valueOf(10000), BigDecimal.valueOf(50000), 1, 2, 20, "createdAt", "desc"
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertsPage, response.getObject());
        verify(advertService, times(1)).getAdvertSearchAndParametersPageForAdmin(anyString(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    void getAdvertBySlug_ShouldReturnAdvert() {
        // Arrange
        String slug = "sample-slug";
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getAdvertBySlug(slug)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.getAdvertBySlug(slug);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).getAdvertBySlug(slug);
    }

    @Test
    void getAuthenticatedUsersAdvertById_ShouldReturnAdvert() {
        // Arrange
        Long advertId = 1L;
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getAuthenticatedUsersAdvertById(advertId, httpRequest)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.getAuthenticatedUsersAdvertById(advertId, httpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).getAuthenticatedUsersAdvertById(advertId, httpRequest);
    }

    @Test
    void getAdvertById_ShouldReturnAdvert() {
        // Arrange
        Long advertId = 1L;
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getAdvertByIdForAdmin(advertId)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.getAdvertById(advertId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).getAdvertByIdForAdmin(advertId);
    }

    @Test
    void updateAuthenticatedUsersAdvertById_ShouldUpdateAdvert() {
        // Arrange
        Long advertId = 1L;
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.updateAuthenticatedUsersAdvertById(advertId, httpRequest, advertRequestForUpdate)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.updateAuthenticatedUsersAdvertById(advertId, advertRequestForUpdate, httpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).updateAuthenticatedUsersAdvertById(advertId, httpRequest, advertRequestForUpdate);
    }

    @Test
    void updateAdvertById_ShouldUpdateAdvert() {
        // Arrange
        Long advertId = 1L;
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.updateAdvertById(advertId, advertRequestForUpdate, httpRequest)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.updateAdvertById(advertId, advertRequestForUpdate, httpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).updateAdvertById(advertId, advertRequestForUpdate, httpRequest);
    }

    @Test
    void deleteAdvert_ShouldDeleteAdvert() {
        // Arrange
        Long advertId = 5L;
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.deleteAdvert(advertId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.deleteAdvert(advertId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(expectedResponse, response);
        verify(advertService, times(1)).deleteAdvert(advertId);
    }

    @Test
    void getUsersAdvertsById_ShouldReturnAdverts() {
        // Arrange
        Long userId = 1L;
        Set<AdvertResponse> adverts = new HashSet<>();
        ResponseMessage<Set<AdvertResponse>> expectedResponse = ResponseMessage.<Set<AdvertResponse>>builder()
                .object(adverts)
                .message(SuccessMessages.ADVERTS_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.getUsersAdvertsById(userId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<Set<AdvertResponse>> response = advertController.getUsersAdvertsById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(adverts, response.getObject());
        verify(advertService, times(1)).getUsersAdvertsById(userId);
    }

    @Test
    void approveAdvert_ShouldApproveAdvert() {
        // Arrange
        Long advertId = 1L;
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        AdvertResponse advertResponse = new AdvertResponse();
        ResponseMessage<AdvertResponse> expectedResponse = ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

        when(advertService.approveAdvert(advertId, httpRequest, updateRequest)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertController.approveAdvert(advertId, httpRequest, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(advertResponse, response.getObject());
        verify(advertService, times(1)).approveAdvert(advertId, httpRequest, updateRequest);
    }



}

