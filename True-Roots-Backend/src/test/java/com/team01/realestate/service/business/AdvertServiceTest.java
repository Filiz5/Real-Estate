package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.CategoryPropertyValue;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.request.business.UpdateAdvertStatusRequest;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.CategoryPropertyValueRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.user.EmailService;
import com.team01.realestate.util.SlugUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertServiceTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private AdvertMapper advertMapper;

    @Mock
    private MethodHelper methodHelper;

    @Mock
    private LogService logService;

    @Mock
    private PageableHelper pageableHelper;

    @Mock
    private CategoryPropertyValueRepository categoryPropertyValueRepository;

    @Mock
    private EmailService mailService;

    @InjectMocks
    private AdvertService advertService;

    @Test
    void shouldCreateAdvertSuccessfully() {
        // Arrange
        AdvertRequest advertRequest = new AdvertRequest();
        advertRequest.setTitle("Test Advert");
        User authenticatedUser = new User(); // Mock user
        Advert advert = new Advert();       // Mock advert
        Advert savedAdvert = new Advert(); // Mock saved advert

        when(methodHelper.findAuthenticatedUser(any())).thenReturn(authenticatedUser);
        when(advertMapper.advertRequestToAdvert(any())).thenReturn(advert);
        when(advertRepository.save(any())).thenReturn(savedAdvert);

        // Act
        ResponseMessage<AdvertResponse> response = advertService.createAdvert(advertRequest, mock(HttpServletRequest.class));

        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        verify(advertRepository).save(advert);
        verify(logService).createLog(any(), anyString(), eq(authenticatedUser), eq(savedAdvert));
    }

    @Test
    void shouldReturnAdvertPageSuccessfully() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        when(pageableHelper.getPageableWithProperties(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(pageable);

        Page<Advert> mockPage = new PageImpl<>(List.of(new Advert()));
        when(advertRepository.findAdvertsWithParameters(anyString(), anyLong(), anyLong(), any(), any(), any(), any()))
                .thenReturn(mockPage);

        // Act
        ResponseMessage<Page<AdvertResponse>> response = advertService.getAdvertSearchAndParametersPage(
                "query", 1L, 1L, BigDecimal.ZERO, BigDecimal.TEN, AdvertStatus.PENDING, 0, 10, "title", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        verify(advertRepository).findAdvertsWithParameters(anyString(), anyLong(), anyLong(), any(), any(), any(), any());
    }

    @Test
    void shouldReturnBadRequestWhenExceptionOccurs() {
        // Arrange
        when(pageableHelper.getPageableWithProperties(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("An error occurred"));

        // Act
        ResponseMessage<Page<AdvertResponse>> response = advertService.getAdvertSearchAndParametersPage(
                "query", 1L, 1L, BigDecimal.ZERO, BigDecimal.TEN, AdvertStatus.PENDING, 0, 10, "title", "asc");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("An error occurred", response.getMessage());
        verify(pageableHelper).getPageableWithProperties(anyInt(), anyInt(), anyString(), anyString());
        verifyNoInteractions(advertRepository); // Ensure no call to repository was made
    }

    @Test
    void shouldSaveCategoryPropertyValuesWhenProvided() {
        // Arrange
        AdvertRequest advertRequest = new AdvertRequest();
        advertRequest.setTitle("Test Advert");

        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);

        Advert mockAdvert = new Advert();
        mockAdvert.setId(1L);
        mockAdvert.setCategoryPropertyValues(
                List.of(new CategoryPropertyValue(), new CategoryPropertyValue()) // Non-empty list
        );
        when(advertMapper.advertRequestToAdvert(advertRequest)).thenReturn(mockAdvert);
        when(advertRepository.save(any(Advert.class))).thenReturn(mockAdvert);
        when(advertMapper.advertToAdvertResponse(mockAdvert)).thenReturn(new AdvertResponse());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        // Act
        ResponseMessage<AdvertResponse> response = advertService.createAdvert(advertRequest, mockRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertNotNull(response.getObject());
        verify(categoryPropertyValueRepository).saveAll(mockAdvert.getCategoryPropertyValues());
    }

    @Test
    void shouldNotCallSaveAllWhenCategoryPropertyValuesIsNullOrEmpty() {
        // Arrange
        AdvertRequest advertRequest = new AdvertRequest();
        advertRequest.setTitle("Test Advert");

        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);

        Advert mockAdvert = new Advert();
        mockAdvert.setId(1L);
        mockAdvert.setCategoryPropertyValues(Collections.emptyList()); // Empty list
        when(advertMapper.advertRequestToAdvert(advertRequest)).thenReturn(mockAdvert);
        when(advertRepository.save(any(Advert.class))).thenReturn(mockAdvert);
        when(advertMapper.advertToAdvertResponse(mockAdvert)).thenReturn(new AdvertResponse());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        // Act
        ResponseMessage<AdvertResponse> response = advertService.createAdvert(advertRequest, mockRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        verify(categoryPropertyValueRepository, never()).saveAll(anyList());
    }

    @Test
    void shouldReturnAdvertsGroupedByCity() {
        // Arrange
        List<Object[]> mockResults = List.of(
                new Object[]{"Istanbul", 5L},
                new Object[]{"Ankara", 3L}
        );
        when(advertRepository.getAdvertsGroupedByCity()).thenReturn(mockResults);

        // Act
        List<CityAdvertResponse> responses = advertService.getAdvertsGroupedByCity();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Istanbul", responses.get(0).getCity());
        assertEquals(5, responses.get(0).getAmount());
        assertEquals("Ankara", responses.get(1).getCity());
        assertEquals(3, responses.get(1).getAmount());
    }

    @Test
    void shouldReturnAdvertsGroupedByCategory() {
        // Arrange
        List<Object[]> mockResults = List.of(
                new Object[]{"Electronics", 10L},
                new Object[]{"Real Estate", 7L}
        );
        when(advertRepository.getAdvertsGroupedByCategory()).thenReturn(mockResults);

        // Act
        List<CategoryAdvertResponse> responses = advertService.getAdvertsGroupedByCategory();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Electronics", responses.get(0).getCategory());
        assertEquals(10, responses.get(0).getAmount());
        assertEquals("Real Estate", responses.get(1).getCategory());
        assertEquals(7, responses.get(1).getAmount());
    }

    @Test
    void shouldReturnPopularAdverts() {
        // Arrange
        List<Advert> mockAdverts = List.of(new Advert(), new Advert());
        when(advertRepository.findPopularAdverts(5)).thenReturn(mockAdverts);
        when(advertMapper.advertToAdvertResponse(any(Advert.class)))
                .thenReturn(new AdvertResponse(), new AdvertResponse());

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getPopularAdverts(5);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(2, response.getObject().size());
        verify(advertRepository).findPopularAdverts(5);
        verify(advertMapper, times(2)).advertToAdvertResponse(any(Advert.class));
    }

    @Test
    void shouldReturnBadRequestWhenGetPopularAdvertsFails() {
        // Arrange
        when(advertRepository.findPopularAdverts(anyInt())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getPopularAdverts(5);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals(ErrorMessages.NOT_FOUND_ADVERTS_BY_POPULARITY, response.getMessage());
    }

    @Test
    void shouldReturnPopularAdvertsForAdmin() {
        // Arrange
        List<Advert> mockAdverts = List.of(new Advert(), new Advert());
        when(advertRepository.findPopularAdvertsByTourRequestCount(5)).thenReturn(mockAdverts);
        when(advertMapper.advertToAdvertResponse(any(Advert.class)))
                .thenReturn(new AdvertResponse(), new AdvertResponse());

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getPopularAdvertsForAdmin(5);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(2, response.getObject().size());
        verify(advertRepository).findPopularAdvertsByTourRequestCount(5);
        verify(advertMapper, times(2)).advertToAdvertResponse(any(Advert.class));
    }

    @Test
    void shouldReturnBadRequestWhenGetPopularAdvertsForAdminFails() {
        // Arrange
        when(advertRepository.findPopularAdvertsByTourRequestCount(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getPopularAdvertsForAdmin(5);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals(ErrorMessages.NOT_FOUND_ADVERTS_BY_POPULARITY, response.getMessage());
    }

    @Test
    void shouldReturnAuthenticatedUsersAdverts() {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(mockRequest)).thenReturn(mockUser);

        Advert advert1 = new Advert();
        Advert advert2 = new Advert();
        advert1.setId(1L);
        advert2.setId(2L);

        Page<Advert> mockAdvertsPage = new PageImpl<>(List.of(advert1, advert2));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "asc")).thenReturn(pageable);

        when(advertRepository.findUserAdvertsWithParameters(eq(1L), eq("query"), eq(pageable)))
                .thenReturn(mockAdvertsPage);

        when(advertMapper.advertToAdvertResponseForList(advert1)).thenReturn(new AdvertResponseForList());
        when(advertMapper.advertToAdvertResponseForList(advert2)).thenReturn(new AdvertResponseForList());

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertService.getAuthenticatedUsersAdverts(
                "query", 0, 10, "createdAt", "asc", mockRequest
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(2, response.getObject().getContent().size());
        verify(advertRepository).findUserAdvertsWithParameters(eq(1L), eq("query"), eq(pageable));
        verify(advertMapper, times(2)).advertToAdvertResponseForList(any(Advert.class));
    }


    @Test
    void shouldReturnBadRequestWhenGetAuthenticatedUsersAdvertsFails() {
        // Arrange
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class)))
                .thenThrow(new RuntimeException("User not authenticated"));

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertService.getAuthenticatedUsersAdverts(
                "query", 0, 10, "createdAt", "asc", mock(HttpServletRequest.class)
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals(ErrorMessages.NOT_FOUND_ADVERTS_MESSAGE, response.getMessage());
    }

    @Test
    void shouldReturnAdvertSearchAndParametersPageForAdmin() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "asc")).thenReturn(pageable);

        Advert advert1 = new Advert();
        advert1.setId(1L);
        Advert advert2 = new Advert();
        advert2.setId(2L);

        Page<Advert> mockAdvertsPage = new PageImpl<>(List.of(advert1, advert2));
        when(advertRepository.findAdvertsWithParametersForAdmin(
                eq("query"), eq(1L), eq(2L), eq(new BigDecimal(100)), eq(new BigDecimal(500)),
                eq(AdvertStatus.PENDING), eq(pageable)))
                .thenReturn(mockAdvertsPage);

        when(advertMapper.advertToAdvertResponseForList(advert1)).thenReturn(new AdvertResponseForList());
        when(advertMapper.advertToAdvertResponseForList(advert2)).thenReturn(new AdvertResponseForList());

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertService.getAdvertSearchAndParametersPageForAdmin(
                "query", 1L, 2L, new BigDecimal(100), new BigDecimal(500),
                AdvertStatus.PENDING, 0, 10, "createdAt", "asc");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getObject());
        assertEquals(2, response.getObject().getContent().size());
        verify(advertRepository).findAdvertsWithParametersForAdmin(
                eq("query"), eq(1L), eq(2L), eq(new BigDecimal(100)), eq(new BigDecimal(500)),
                eq(AdvertStatus.PENDING), eq(pageable));
        verify(advertMapper, times(2)).advertToAdvertResponseForList(any(Advert.class));
    }

    @Test
    void shouldReturnErrorWhenUnexpectedErrorOccurs() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "asc")).thenReturn(pageable);

        // Beklenmedik bir hata olması için exception fırlatıyoruz
        when(advertRepository.findAdvertsWithParametersForAdmin(
                eq("query"), eq(1L), eq(2L), eq(new BigDecimal(100)), eq(new BigDecimal(500)),
                eq(AdvertStatus.PENDING), eq(pageable)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseMessage<Page<AdvertResponseForList>> response = advertService.getAdvertSearchAndParametersPageForAdmin(
                "query", 1L, 2L, new BigDecimal(100), new BigDecimal(500),
                AdvertStatus.PENDING, 0, 10, "createdAt", "asc");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus()); // Beklenen durum BAD_REQUEST
        assertNull(response.getObject()); // Response body boş olmalı
        assertEquals(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE, response.getMessage()); // Hata mesajı doğru olmalı
    }

    @Test
    void shouldReturnAdvertBySlug() {
        // Arrange
        Advert advert = new Advert();
        advert.setSlug("sample-slug");
        advert.setViewCount(5);

        when(advertRepository.findBySlug("sample-slug")).thenReturn(Optional.of(advert));
        when(advertMapper.advertToAdvertResponse(advert)).thenReturn(new AdvertResponse());

        // Act
        AdvertResponse response = advertService.getAdvertBySlug("sample-slug");

        // Assert
        assertNotNull(response);
        assertEquals(6, advert.getViewCount());  // Görüntülenme sayısı 1 artmalı
        verify(advertRepository).save(advert);  // save çağrılmalı
    }

    @Test
    void shouldThrowExceptionWhenAdvertNotFoundBySlug() {
        // Arrange
        when(advertRepository.findBySlug("non-existent-slug")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            advertService.getAdvertBySlug("non-existent-slug");
        });
    }

    @Test
    void shouldReturnAuthenticatedUsersAdvertById() {
        // Arrange
        Long advertId = 1L;
        User mockUser = new User();
        mockUser.setId(1L);
        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);
        mockAdvert.setUser(mockUser);

        // Mocking the behavior of methodHelper
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);
        when(advertMapper.advertToAdvertResponse(mockAdvert)).thenReturn(new AdvertResponse());

        // Act
        AdvertResponse response = advertService.getAuthenticatedUsersAdvertById(advertId, mock(HttpServletRequest.class));

        // Assert
        assertNotNull(response);
        verify(methodHelper).findAuthenticatedUser(any(HttpServletRequest.class));
        verify(methodHelper).findAdvertById(advertId);
        verify(advertMapper).advertToAdvertResponse(mockAdvert);
    }

    @Test
    void shouldThrowBadRequestWhenUserTriesToAccessAnotherUsersAdvert() {
        // Arrange
        Long advertId = 1L;
        User mockUser = new User();
        mockUser.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);
        mockAdvert.setUser(otherUser);  // Farklı bir kullanıcıya ait

        // Mocking the behavior of methodHelper
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            advertService.getAuthenticatedUsersAdvertById(advertId, mock(HttpServletRequest.class));
        });
    }

    @Test
    void shouldReturnAdvertByIdForAdmin() {
        // Arrange
        Long advertId = 1L;
        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);

        // Mocking the behavior of methodHelper
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);
        when(advertMapper.advertToAdvertResponse(mockAdvert)).thenReturn(new AdvertResponse());

        // Act
        AdvertResponse response = advertService.getAdvertByIdForAdmin(advertId);

        // Assert
        assertNotNull(response);
        verify(methodHelper).findAdvertById(advertId);
        verify(advertMapper).advertToAdvertResponse(mockAdvert);
    }

    @Test
    void shouldThrowBadRequestWhenAdvertIsBuiltInTest() {
        // Arrange
        Long advertId = 1L;
        User mockUser = new User();
        mockUser.setId(1L);
        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);
        mockAdvert.setUser(mockUser);
        mockAdvert.setBuiltIn(true);  // İlan built-in olarak ayarlanıyor

        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setActive(true);

        // Mocking the behavior of methodHelper
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            advertService.updateAuthenticatedUsersAdvertById(advertId, mock(HttpServletRequest.class), advertRequestForUpdate);
        });
    }

    @Test
    void shouldThrowBadRequestWhenUserTriesToUpdateAnotherUsersAdvert() {
        // Arrange
        Long advertId = 1L;
        User mockUser = new User();
        mockUser.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);
        mockAdvert.setUser(otherUser);  // Farklı bir kullanıcıya ait ilan

        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setActive(true);

        // Mocking the behavior of methodHelper
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            advertService.updateAuthenticatedUsersAdvertById(advertId, mock(HttpServletRequest.class), advertRequestForUpdate);
        });
    }

    @Test
    void shouldThrowBadRequestWhenAdvertIsBuiltIn() {
        // Arrange
        Long advertId = 1L;
        User mockAdmin = new User();
        mockAdmin.setId(1L);
        mockAdmin.setFirstName("Admin");

        Advert mockAdvert = new Advert();
        mockAdvert.setId(advertId);
        mockAdvert.setUser(mockAdmin);
        mockAdvert.setBuiltIn(true); // İlan built-in olarak ayarlanıyor

        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setTitle("Updated Title");

        // Mocking the behavior of methodHelper
        when(methodHelper.findAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(mockAdmin);
        when(methodHelper.findAdvertById(advertId)).thenReturn(mockAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            advertService.updateAdvertById(advertId, advertRequestForUpdate, mock(HttpServletRequest.class));
        });
    }
    @Test
    void shouldDeleteAdvertSuccessfully() {
        // Arrange
        Long advertId = 1L;
        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setTitle("Test Advert");
        foundAdvert.setBuiltIn(false);
        AdvertResponse advertResponse = new AdvertResponse();

        when(advertRepository.findById(advertId)).thenReturn(Optional.of(foundAdvert));
        when(advertMapper.advertToAdvertResponse(foundAdvert)).thenReturn(advertResponse);

        // Act
        ResponseMessage<AdvertResponse> response = advertService.deleteAdvert(advertId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.ADVERT_DELETED, response.getMessage());
        verify(advertRepository).delete(foundAdvert);  // Verify that delete was called
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAdvertDoesNotExist() {
        // Arrange
        Long advertId = 1L;
        when(advertRepository.findById(advertId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            advertService.deleteAdvert(advertId);
        });
        assertEquals(String.format(ErrorMessages.NOT_FOUND_ADVERTS_MESSAGE, advertId), exception.getMessage());
    }

    @Test
    void shouldThrowBadRequestExceptionForBuiltInAdvert() {
        // Arrange
        Long advertId = 1L;
        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setBuiltIn(true);
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(foundAdvert));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            advertService.deleteAdvert(advertId);
        });
        assertEquals(ErrorMessages.ADVERT_NOT_DELETE_MESSAGE_FOR_BUILTIN, exception.getMessage());
    }

    @Test
    void shouldReturnEmptyAdvertListWhenNoAdvertsFound() {
        // Arrange
        LocalDateTime date1 = LocalDateTime.now().minusDays(1);
        LocalDateTime date2 = LocalDateTime.now();
        Long categoryId = 1L;
        Long typeId = 1L;
        AdvertStatus status = AdvertStatus.ACTIVATED;

        when(advertRepository.findByFilters(date1, date2, categoryId, typeId, status)).thenReturn(Collections.emptyList());

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getAdvertsReports(date1, date2, categoryId, typeId, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.ADVERTS_LIST_IS_EMPTY, response.getMessage());
        assertTrue(response.getObject().isEmpty());
    }

    @Test
    void shouldReturnAdvertListWhenAdvertsFound() {
        // Arrange
        LocalDateTime date1 = LocalDateTime.now().minusDays(1);
        LocalDateTime date2 = LocalDateTime.now();
        Long categoryId = 1L;
        Long typeId = 1L;
        AdvertStatus status = AdvertStatus.ACTIVATED;

        Advert advert = new Advert();
        advert.setId(1L);
        List<Advert> adverts = Arrays.asList(advert);

        AdvertResponse advertResponse = new AdvertResponse();
        when(advertRepository.findByFilters(date1, date2, categoryId, typeId, status)).thenReturn(adverts);
        when(advertMapper.advertToAdvertResponse(advert)).thenReturn(advertResponse);

        // Act
        ResponseMessage<List<AdvertResponse>> response = advertService.getAdvertsReports(date1, date2, categoryId, typeId, status);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.ADVERTS_FOUND, response.getMessage());
        assertFalse(response.getObject().isEmpty());
        verify(advertRepository).findByFilters(date1, date2, categoryId, typeId, status);
    }

    @Test
    void shouldReturnUsersAdverts() {
        // Arrange
        Long userId = 1L;

        // Mocking User object
        User foundUser = mock(User.class);
     //   when(foundUser.getId()).thenReturn(userId);

        // Mocking adverts
        Advert advert = new Advert();
        advert.setId(1L);
        Set<Advert> adverts = new HashSet<>(Collections.singletonList(advert));

        AdvertResponse advertResponse = new AdvertResponse();
        when(foundUser.getAdverts()).thenReturn(adverts); // Mocking getAdverts
        when(advertMapper.advertToAdvertResponse(advert)).thenReturn(advertResponse);

        // Mocking the methodHelper to return the mocked User
        when(methodHelper.findUserById(userId)).thenReturn(foundUser);

        // Act
        ResponseMessage<Set<AdvertResponse>> response = advertService.getUsersAdvertsById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.USERS_ADVERTS_FOUND, response.getMessage());
        assertFalse(response.getObject().isEmpty());
        verify(methodHelper).findUserById(userId);
        verify(foundUser).getAdverts(); // Verify that getAdverts was called on the mocked user
    }

    @Test
    void shouldReturnDeclinedLogTypeWhenAdvertStatusIsRejected() {
        // Arrange
        Advert foundAdvert = new Advert();
        foundAdvert.setAdvertStatus(AdvertStatus.REJECTED);
        AdvertStatus oldStatus = AdvertStatus.PENDING; // Simulating a change from PENDING to REJECTED

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.DECLINED, logType);
    }

    @Test
    void shouldReturnUpdatedLogTypeWhenAdvertStatusIsActivated() {
        // Arrange
        Advert foundAdvert = new Advert();
        foundAdvert.setAdvertStatus(AdvertStatus.ACTIVATED);
        AdvertStatus oldStatus = AdvertStatus.PENDING; // Simulating a change from PENDING to ACTIVATED

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.UPDATED, logType);
    }

    @Test
    void shouldReturnUpdatedLogTypeForOtherStatusChanges() {
        // Arrange
        Advert foundAdvert = new Advert();
        foundAdvert.setAdvertStatus(AdvertStatus.ACTIVATED);
        AdvertStatus oldStatus = AdvertStatus.PENDING;

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.UPDATED, logType);
    }

    @Test
    void shouldReturnUpdatedLogTypeWhenStatusIsUnchanged() {
        // Arrange
        Advert foundAdvert = new Advert();
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING);
        AdvertStatus oldStatus = AdvertStatus.PENDING; // No change in status

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.UPDATED, logType);
    }

    @Test
    void shouldUpdateAuthenticatedUsersAdvertSuccessfully() {
        // Arrange
        Long advertId = 1L;
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setActive(true); // Set any fields required for the update

        // Mock authenticated user
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setFirstName("John");

        // Mock found advert
        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setUser(authenticatedUser);
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING);
        foundAdvert.setTitle("Old Advert Title");

        // Mock updated advert
        Advert updatedAdvert = new Advert();
        updatedAdvert.setId(advertId);
        updatedAdvert.setTitle("Updated Advert Title");
        updatedAdvert.setAdvertStatus(AdvertStatus.PENDING);
        updatedAdvert.setActive(true);

        // Mock HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // Mock AdvertResponse to be returned by the mapper
        AdvertResponse advertResponse = new AdvertResponse();
        advertResponse.setTitle("Updated Advert Title");
        advertResponse.setAdvertStatus(AdvertStatus.PENDING);
        advertResponse.setActive(true);

        // Mock method behaviors
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);
        when(advertMapper.advertRequestToUpdatedAdvert(advertRequestForUpdate, foundAdvert)).thenReturn(updatedAdvert);
        when(advertRepository.save(updatedAdvert)).thenReturn(updatedAdvert);
        when(advertMapper.advertToAdvertResponse(updatedAdvert)).thenReturn(advertResponse);

        // Act
        AdvertResponse response = advertService.updateAuthenticatedUsersAdvertById(advertId, httpServletRequest, advertRequestForUpdate);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Advert Title", response.getTitle());
        assertEquals(AdvertStatus.PENDING, response.getAdvertStatus());
        assertTrue(response.isActive());
        verify(advertRepository).save(updatedAdvert);
        verify(logService).createLog(eq(LogType.UPDATED), anyString(), eq(authenticatedUser), eq(updatedAdvert));
    }

    @Test
    void shouldThrowExceptionIfAdvertIsBuiltIn() {
        // Arrange
        Long advertId = 1L;
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setActive(true);

        // Mock authenticated user
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);

        // Mock found advert as built-in
        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setUser(authenticatedUser);
        foundAdvert.setBuiltIn(true); // Advert is built-in

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // Mock method behaviors
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                advertService.updateAuthenticatedUsersAdvertById(advertId, httpServletRequest, advertRequestForUpdate)
        );
    }

    @Test
    void shouldThrowExceptionIfUserDoesNotOwnAdvert() {
        // Arrange
        Long advertId = 1L;
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setActive(true);

        // Mock authenticated user
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);

        // Mock found advert belonging to another user
        User otherUser = new User();
        otherUser.setId(2L);

        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setUser(otherUser); // Advert belongs to a different user

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // Mock method behaviors
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                advertService.updateAuthenticatedUsersAdvertById(advertId, httpServletRequest, advertRequestForUpdate)
        );
    }

    @Test
    void shouldUpdateAdvertByIdSuccessfully() {
        // Arrange
        Long advertId = 1L;
        AdvertRequestForUpdate advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setTitle("Updated Title");
        advertRequestForUpdate.setAdvertStatus(AdvertStatus.ACTIVATED); // Example new status

        // Mock authenticated admin user
        User authenticatedAdmin = new User();
        authenticatedAdmin.setId(1L);
        authenticatedAdmin.setFirstName("Admin");

        // Mock found advert
        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setTitle("Old Title");
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING); // Old status
        foundAdvert.setUser(authenticatedAdmin);

        // Mock updated advert (after applying changes)
        Advert updatedAdvert = new Advert();
        updatedAdvert.setId(advertId);
        updatedAdvert.setTitle("Updated Title");
        updatedAdvert.setAdvertStatus(AdvertStatus.ACTIVATED); // New status
        updatedAdvert.setSlug("updated-title-slug");

        // Mock HTTP request
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // Mock method behaviors
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedAdmin); // Ensure this returns authenticatedAdmin
        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);
        when(advertMapper.advertRequestToUpdatedAdvert(advertRequestForUpdate, foundAdvert)).thenReturn(updatedAdvert);
        when(advertRepository.save(updatedAdvert)).thenReturn(updatedAdvert);
        when(advertMapper.advertToAdvertResponse(updatedAdvert)).thenReturn(new AdvertResponse());

        // Act
        AdvertResponse response = advertService.updateAdvertById(advertId, advertRequestForUpdate, httpServletRequest);

        // Assert
        assertNotNull(response);
        assertEquals(AdvertStatus.PENDING, response.getAdvertStatus());
        assertEquals("updated-title-slug", updatedAdvert.getSlug()); // Ensure slug was updated
        verify(advertRepository).save(updatedAdvert);

    }

    @Test
    void shouldThrowBadRequestExceptionIfAdvertIsBuiltIn() {
        // Arrange
        Long advertId = 1L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        User authenticatedAdmin = new User();
        authenticatedAdmin.setId(1L);

        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING);
        foundAdvert.setBuiltIn(true); // Advert is built-in

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedAdmin);
        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                advertService.approveAdvert(advertId, httpServletRequest, mock(UpdateAdvertStatusRequest.class))
        );
        assertEquals(ErrorMessages.NOT_PERMITTED_UPDATE, exception.getMessage());
    }

    @Test
    void shouldUpdateAdvertStatusSuccessfully() {
        // Arrange
        Long advertId = 1L;
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setStatus(String.valueOf(AdvertStatus.ACTIVATED)); // New status for the advert

        User authenticatedAdmin = new User();
        authenticatedAdmin.setId(1L);
        authenticatedAdmin.setEmail("admin@example.com");  // Set the email field
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(authenticatedAdmin);

        Advert foundAdvert = new Advert();
        foundAdvert.setId(advertId);
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING); // Old status
        foundAdvert.setUser(authenticatedAdmin);  // Make sure the user is set

        when(methodHelper.findAdvertById(advertId)).thenReturn(foundAdvert);

        Advert updatedAdvert = new Advert();
        updatedAdvert.setId(advertId);
        updatedAdvert.setAdvertStatus(AdvertStatus.ACTIVATED); // New status
        updatedAdvert.setUser(authenticatedAdmin);
        when(advertRepository.save(any(Advert.class))).thenReturn(updatedAdvert);

        // Act
        ResponseMessage<AdvertResponse> response = advertService.approveAdvert(advertId, httpServletRequest, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(SuccessMessages.ADVERT_STATUS_UPDATED, response.getMessage());

        // Capture the argument passed to the save method
        ArgumentCaptor<Advert> advertCaptor = ArgumentCaptor.forClass(Advert.class);
        verify(advertRepository).save(advertCaptor.capture());

        // Ensure the captured Advert has the expected status
        Advert capturedAdvert = advertCaptor.getValue();
        assertEquals(AdvertStatus.ACTIVATED, capturedAdvert.getAdvertStatus()); // Check the status
        assertEquals(advertId, capturedAdvert.getId()); // Ensure the correct advert ID
    }

    @Test
    void shouldSetAdvertStatusSuccessfully() {
        // Arrange
        Advert advert = new Advert();
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();

        // Test valid status: ACTIVATED
        updateRequest.setStatus("ACTIVATED");

        // Act
        Advert updatedAdvert = advertService.setAdvertStatus(advert, updateRequest);

        // Assert
        assertEquals(AdvertStatus.ACTIVATED, updatedAdvert.getAdvertStatus());
    }

    @Test
    void shouldSetAdvertStatusToRejected() {
        // Arrange
        Advert advert = new Advert();
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();

        // Test valid status: REJECTED
        updateRequest.setStatus("REJECTED");

        // Act
        Advert updatedAdvert = advertService.setAdvertStatus(advert, updateRequest);

        // Assert
        assertEquals(AdvertStatus.REJECTED, updatedAdvert.getAdvertStatus());
    }

    @Test
    void shouldSetAdvertStatusToPending() {
        // Arrange
        Advert advert = new Advert();
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();

        // Test valid status: PENDING
        updateRequest.setStatus("PENDING");

        // Act
        Advert updatedAdvert = advertService.setAdvertStatus(advert, updateRequest);

        // Assert
        assertEquals(AdvertStatus.PENDING, updatedAdvert.getAdvertStatus());
    }

    @Test
    void shouldThrowExceptionForInvalidStatus() {
        // Arrange
        Advert advert = new Advert();
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();

        // Test invalid status
        updateRequest.setStatus("INVALID_STATUS");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> advertService.setAdvertStatus(advert, updateRequest));
    }

    @Test
    void shouldSendApprovalEmailWhenAdvertIsActivated() {
        // Arrange
        Advert advert = new Advert();
        User user = new User();
        user.setEmail("user@example.com");
        advert.setUser(user);
        advert.setAdvertStatus(AdvertStatus.ACTIVATED);

        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setStatus("ACTIVATED");

        // Mock email service
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        advertService.sendMail(advert, updateRequest);

        // Assert
        verify(mailService).sendEmail("user@example.com", "Advert Approved", "Your advert has been approved.");
    }


    @Test
    void shouldSendPendingEmailWhenAdvertIsPending() {
        // Arrange
        Advert advert = new Advert();
        User user = new User();
        user.setEmail("user@example.com");
        advert.setUser(user);
        advert.setAdvertStatus(AdvertStatus.PENDING);

        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setStatus("PENDING");
        updateRequest.setRejectMessage("Waiting for approval.");

        // Mock email service
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        advertService.sendMail(advert, updateRequest);

        // Assert
        verify(mailService).sendEmail("user@example.com", "Advert Pending", "Your advert status is changed to pending.\nReason: Waiting for approval.");
    }

    @Test
    void shouldThrowExceptionWhenUserIsNullInSendMail() {
        // Arrange
        Advert advert = new Advert();
        advert.setUser(null); // Set user to null
        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setStatus("PENDING");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> advertService.sendMail(advert, updateRequest));
    }

    @Test
    void shouldReturnUpdatedLogTypeWhenStatusNotChanged() {
        // Arrange
        Advert foundAdvert = new Advert();
        AdvertStatus oldStatus = AdvertStatus.PENDING;
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING); // Status is the same, no change

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.UPDATED, logType); // It should return UPDATED log type
    }

    @Test
    void shouldReturnUpdatedLogTypeWhenStatusChangedToNonSpecialStatus() {
        // Arrange
        Advert foundAdvert = new Advert();
        AdvertStatus oldStatus = AdvertStatus.PENDING;
        foundAdvert.setAdvertStatus(AdvertStatus.PENDING); // Status stays the same

        // Act
        LogType logType = AdvertService.getLogType(foundAdvert, oldStatus);

        // Assert
        assertEquals(LogType.UPDATED, logType); // It should return UPDATED log type
    }

    @Test
    void shouldSendRejectionEmailWhenAdvertStatusIsRejected() {
        // Arrange
        Advert advert = new Advert();
        User user = new User();
        user.setEmail("user@example.com");
        advert.setUser(user);
        advert.setAdvertStatus(AdvertStatus.REJECTED);

        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setRejectMessage("The advert does not meet the quality standards.");

        // Mock email service behavior
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        advertService.sendMail(advert, updateRequest);

        // Assert
        verify(mailService).sendEmail(eq("user@example.com"), eq("Advert Rejected"),
                eq("Your advert has been rejected. \nReason: The advert does not meet the quality standards."));
    }

    @Test
    void shouldSendPendingEmailWhenAdvertStatusIsPending() {
        // Arrange
        Advert advert = new Advert();
        User user = new User();
        user.setEmail("user@example.com");
        advert.setUser(user);
        advert.setAdvertStatus(AdvertStatus.PENDING);

        UpdateAdvertStatusRequest updateRequest = new UpdateAdvertStatusRequest();
        updateRequest.setRejectMessage("The advert is pending review.");

        // Mock email service behavior
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        advertService.sendMail(advert, updateRequest);

        // Assert
        verify(mailService).sendEmail(eq("user@example.com"), eq("Advert Pending"),
                eq("Your advert status is changed to pending.\nReason: The advert is pending review."));
    }
}