package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.mapper.FavoriteMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.repository.business.FavoriteRepository;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FavoriteServiceTest {
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdvertMapper advertMapper;

    @Mock
    private MethodHelper methodHelper;

    @Mock
    private PageableHelper pageableHelper;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private UserDetails userDetails;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks before each test
    }

    @AfterEach
    void tearDown() {
        Mockito.clearInvocations(userRepository, favoriteRepository);
        Mockito.reset(userRepository, favoriteRepository);
    }

    @Test
    @DisplayName("Test getFavoritesForAuthenticatedUser - Success")
    void testGetFavoritesForAuthenticatedUser_Success() {
        // Mock authenticated user
        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(mockUser);

        // Mock favorite data
        Advert advert1 = new Advert();
        advert1.setTitle("Advert 1");

        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setAdvert(advert1);

        Advert advert2 = new Advert();
        advert2.setTitle("Advert 2");

        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setAdvert(advert2);

        List<Favorite> mockFavorites = Arrays.asList(favorite1, favorite2);
        when(favoriteRepository.findByUserId(mockUser.getId())).thenReturn(mockFavorites);

        // Mock mapper responses
        FavoriteResponse response1 = new FavoriteResponse();
        FavoriteResponse response2 = new FavoriteResponse();
        when(favoriteMapper.toResponse(favorite1)).thenReturn(response1);
        when(favoriteMapper.toResponse(favorite2)).thenReturn(response2);

        // Call method
        List<FavoriteResponse> result = favoriteService.getFavoritesForAuthenticatedUser(httpServletRequest);

        // Verify and assert
        assertThat(result).hasSize(2).containsExactly(response1, response2);
        verify(methodHelper).findAuthenticatedUser(httpServletRequest);
        verify(favoriteRepository).findByUserId(mockUser.getId());
        verify(favoriteMapper).toResponse(favorite1);
        verify(favoriteMapper).toResponse(favorite2);
    }

    @Test
    @DisplayName("Test getFavoritesForAuthenticatedUser - Advert Not Found")
    void testGetFavoritesForAuthenticatedUser_AdvertNotFound() {
        // Mock authenticated user
        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(mockUser);

        // Mock favorite with invalid advert
        Advert invalidAdvert = new Advert();
        invalidAdvert.setTitle(null); // Advert title is null
        Favorite invalidFavorite = new Favorite();
        invalidFavorite.setId(1L);
        invalidFavorite.setAdvert(invalidAdvert);

        when(favoriteRepository.findByUserId(mockUser.getId())).thenReturn(Collections.singletonList(invalidFavorite));

        // Assert exception
        assertThatThrownBy(() -> favoriteService.getFavoritesForAuthenticatedUser(httpServletRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Advert not found");

        // Verify interactions
        verify(methodHelper).findAuthenticatedUser(httpServletRequest);
        verify(favoriteRepository).findByUserId(mockUser.getId());
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    @DisplayName("Test getAuthenticatedUserFavoritesPageable - Success")
    void testGetAuthenticatedUserFavoritesPageable_Success() {
        String query = "";
        int page = 0;
        int size = 5;
        String sort = "id";
        String type = "asc";

        // Mock pageable
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);

        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(mockUser);

        Advert advert1 = new Advert();
        advert1.setTitle("Advert 1");

        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setAdvert(advert1);

        List<Favorite> mockFavorites = List.of(favorite1);
        Page<Favorite> mockPage = new PageImpl<>(mockFavorites);
        when(favoriteRepository.findByUserId(mockUser.getId(), pageable, query)).thenReturn(mockPage);


        // Mock mapper response
        FavoriteResponse response1 = new FavoriteResponse();
        when(favoriteMapper.toResponse(favorite1)).thenReturn(response1);

        // Call method
        Page<FavoriteResponse> result = favoriteService.getAuthenticatedUserFavoritesPageable(query, httpServletRequest, page, size, sort, type);

        // Verify and assert
        assertThat(result.getContent()).hasSize(1).containsExactly(response1);
        verify(pageableHelper).getPageableWithProperties(page, size, sort, type);
        verify(methodHelper).findAuthenticatedUser(httpServletRequest);
        verify(favoriteRepository).findByUserId(mockUser.getId(), pageable, query);
        verify(favoriteMapper).toResponse(favorite1);
    }

    @Test
    @DisplayName("Test getAuthenticatedUserFavoritesPageable - Advert Not Found")
    void testGetAuthenticatedUserFavoritesPageable_AdvertNotFound() {
        // Mock parameters
        String query = "";
        int page = 0;
        int size = 5;
        String sort = "id";
        String type = "asc";

        // Mock pageable
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);

        // Mock authenticated user
        User mockUser = new User();
        mockUser.setId(1L);
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(mockUser);

        // Mock favorite with invalid advert
        Advert invalidAdvert = new Advert();
        invalidAdvert.setTitle(null); // Advert title is null
        Favorite invalidFavorite = new Favorite();
        invalidFavorite.setId(1L);
        invalidFavorite.setAdvert(invalidAdvert);

        Page<Favorite> mockPage = new PageImpl<>(Collections.singletonList(invalidFavorite));
        when(favoriteRepository.findByUserId(mockUser.getId(), pageable, query)).thenReturn(mockPage);

        // Assert exception
        assertThatThrownBy(() -> favoriteService.getAuthenticatedUserFavoritesPageable(query, httpServletRequest, page, size, sort, type))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Advert not found");

        // Verify interactions
        verify(pageableHelper).getPageableWithProperties(page, size, sort, type);
        verify(methodHelper).findAuthenticatedUser(httpServletRequest);
        verify(favoriteRepository).findByUserId(mockUser.getId(), pageable, query);
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    @DisplayName("Test getFavoritesForUser - Success")
    void testGetFavoritesForUser_Success() {
        // Mock userId
        Long userId = 1L;

        // Mock favorite data
        Advert advert1 = new Advert();
        advert1.setTitle("Advert 1");
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setAdvert(advert1);

        Advert advert2 = new Advert();
        advert2.setTitle("Advert 2");
        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setAdvert(advert2);

        List<Favorite> mockFavorites = List.of(favorite1, favorite2);
        when(favoriteRepository.findByUserId(userId)).thenReturn(mockFavorites);

        // Mock mapper response
        FavoriteResponse response1 = new FavoriteResponse();
        FavoriteResponse response2 = new FavoriteResponse();
        when(favoriteMapper.toResponse(favorite1)).thenReturn(response1);
        when(favoriteMapper.toResponse(favorite2)).thenReturn(response2);

        // Call method
        List<FavoriteResponse> result = favoriteService.getFavoritesForUser(userId);

        // Verify and assert
        assertThat(result).hasSize(2).containsExactly(response1, response2);
        verify(favoriteRepository).findByUserId(userId);
        verify(favoriteMapper).toResponse(favorite1);
        verify(favoriteMapper).toResponse(favorite2);
    }

    @Test
    @DisplayName("Test getFavoritesForUser - Advert Not Found")
    void testGetFavoritesForUser_AdvertNotFound() {
        // Mock userId
        Long userId = 1L;

        // Mock favorite with invalid advert
        Advert invalidAdvert = new Advert();
        invalidAdvert.setTitle(null); // Advert title is null
        Favorite invalidFavorite = new Favorite();
        invalidFavorite.setId(1L);
        invalidFavorite.setAdvert(invalidAdvert);

        List<Favorite> mockFavorites = List.of(invalidFavorite);
        when(favoriteRepository.findByUserId(userId)).thenReturn(mockFavorites);

        // Assert exception
        assertThatThrownBy(() -> favoriteService.getFavoritesForUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Advert not found");

        // Verify interactions
        verify(favoriteRepository).findByUserId(userId);
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    @DisplayName("Test getFavoritesByUserIdPageable - Success")
    void testGetFavoritesByUserIdPageable_Success() {
        String query = "";
        int page = 0;
        int size = 5;
        String sort = "id";
        String type = "asc";

        Long userId = 1L;

        // Mock pageable
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);


        Advert advert1 = new Advert();
        advert1.setTitle("Advert 1");

        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setAdvert(advert1);

        List<Favorite> mockFavorites = List.of(favorite1);
        Page<Favorite> mockPage = new PageImpl<>(mockFavorites);
        when(favoriteRepository.findByUserId(userId, pageable, query)).thenReturn(mockPage);


        // Mock mapper response
        FavoriteResponse response1 = new FavoriteResponse();
        when(favoriteMapper.toResponse(favorite1)).thenReturn(response1);

        // Call method
        Page<FavoriteResponse> result = favoriteService.getFavoritesByUserIdPageable(query, userId, page, size, sort, type);

        // Verify and assert
        assertThat(result.getContent()).hasSize(1).containsExactly(response1);
        verify(pageableHelper).getPageableWithProperties(page, size, sort, type);
        verify(favoriteRepository).findByUserId(userId, pageable, query);
        verify(favoriteMapper).toResponse(favorite1);
    }

    @Test
    @DisplayName("Test getFavoritesByUserIdPageable - Advert Not Found")
    void testGetFavoritesByUserIdPageable_AdvertNotFound() {
        // Mock parameters
        String query = "";
        int page = 0;
        int size = 5;
        String sort = "id";
        String type = "asc";

        Long userId = 1L;

        // Mock pageable
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(page, size, sort, type)).thenReturn(pageable);


        // Mock favorite with invalid advert
        Advert invalidAdvert = new Advert();
        invalidAdvert.setTitle(null); // Advert title is null
        Favorite invalidFavorite = new Favorite();
        invalidFavorite.setId(1L);
        invalidFavorite.setAdvert(invalidAdvert);

        Page<Favorite> mockPage = new PageImpl<>(Collections.singletonList(invalidFavorite));
        when(favoriteRepository.findByUserId(userId, pageable, query)).thenReturn(mockPage);

        // Assert exception
        assertThatThrownBy(() -> favoriteService.getFavoritesByUserIdPageable(query, userId, page, size, sort, type))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Advert not found");

        // Verify interactions
        verify(pageableHelper).getPageableWithProperties(page, size, sort, type);
        verify(favoriteRepository).findByUserId(userId, pageable, query);
        verifyNoInteractions(favoriteMapper);
    }

    @Test
    @DisplayName("Test toggleFavorite - Add to Favorites")
    void testToggleFavorite_AddToFavorites() {
        // Mock data
        Long advertId = 1L;

        User foundUser = new User();
        foundUser.setId(1L);

        Advert advert = new Advert();
        advert.setId(advertId);
        advert.setTitle("Test Advert");

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(foundUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(advert);
        when(favoriteRepository.findByUserAndAdvert(foundUser, advert)).thenReturn(Optional.empty());
        when(favoriteRepository.existsByUserAndAdvert(foundUser, advert)).thenReturn(true);

        // Call method
        AdvertResponse response = favoriteService.toggleFavorite(advertId, httpServletRequest);

        // Verify and assert
        assertThat(response.getId()).isEqualTo(advertId);
        assertThat(response.getTitle()).isEqualTo("Test Advert");
        assertThat(response.isFavorited()).isTrue();

        verify(favoriteRepository).save(any(Favorite.class));
        verify(favoriteRepository).existsByUserAndAdvert(foundUser, advert);
    }

    @Test
    @DisplayName("Test toggleFavorite - Remove from Favorites")
    void testToggleFavorite_RemoveFromFavorites() {
        // Mock data
        Long advertId = 1L;

        User foundUser = new User();
        foundUser.setId(1L);

        Advert advert = new Advert();
        advert.setId(advertId);
        advert.setTitle("Test Advert");

        Favorite favorite = Favorite.builder()
                .user(foundUser)
                .advert(advert)
                .createdAt(LocalDateTime.now())
                .build();

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(foundUser);
        when(methodHelper.findAdvertById(advertId)).thenReturn(advert);
        when(favoriteRepository.findByUserAndAdvert(foundUser, advert)).thenReturn(Optional.of(favorite));
        when(favoriteRepository.existsByUserAndAdvert(foundUser, advert)).thenReturn(false);

        // Call method
        AdvertResponse response = favoriteService.toggleFavorite(advertId, httpServletRequest);

        // Verify and assert
        assertThat(response.getId()).isEqualTo(advertId);
        assertThat(response.getTitle()).isEqualTo("Test Advert");
        assertThat(response.isFavorited()).isFalse();

        verify(favoriteRepository).delete(favorite);
        verify(favoriteRepository).existsByUserAndAdvert(foundUser, advert);
    }

    @Test
    @DisplayName("Test toggleFavorite - User Not Found")
    void testToggleFavorite_UserNotFound() {
        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(null);

        assertThatThrownBy(() -> favoriteService.toggleFavorite(1L, httpServletRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Kullanıcı bulunamadı.");
    }

    @Test
    @DisplayName("Test toggleFavorite - Advert Not Found")
    void testToggleFavorite_AdvertNotFound() {
        User foundUser = new User();
        foundUser.setId(1L);

        when(methodHelper.findAuthenticatedUser(httpServletRequest)).thenReturn(foundUser);
        when(methodHelper.findAdvertById(1L)).thenReturn(null);

        assertThatThrownBy(() -> favoriteService.toggleFavorite(1L, httpServletRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("İlan bulunamadı.");
    }

    @Test
    @DisplayName("Test removeAllFavoritesCustomer - Success")
    void testRemoveAllFavoritesCustomer_Success() {
        // Mock user details
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Mock user
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        when(userRepository.findByEmailOptional("test@example.com")).thenReturn(Optional.of(mockUser));

        // Call method
        favoriteService.removeAllFavoritesCustomer(userDetails);

        // Verify repository calls
        verify(userRepository).findByEmailOptional("test@example.com");
        verify(favoriteRepository).deleteAllByUserId(1L);
    }

    @Test
    @DisplayName("Test removeAllFavoritesCustomer - User Not Found")
    void testRemoveAllFavoritesCustomer_UserNotFound() {
        // Mock user details
        when(userDetails.getUsername()).thenReturn("unknown@example.com");

        // Mock user not found
        when(userRepository.findByEmailOptional("unknown@example.com")).thenReturn(Optional.empty());

        // Assert exception
        assertThatThrownBy(() -> favoriteService.removeAllFavoritesCustomer(userDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(ErrorMessages.NOT_FOUND_USER_MESSAGE + "unknown@example.com");

        // Verify repository calls
        verify(userRepository).findByEmailOptional("unknown@example.com");
        verifyNoInteractions(favoriteRepository);
    }

    @Test
    @DisplayName("Test removeAllFavoritesForAdmin - Success")
    void testRemoveAllFavoritesForAdmin_Success() {
        // Arrange
        Long userId = 1L;

        // Mock the user
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        favoriteService.removeAllFavoritesForAdmin(userId);

        // Assert
        verify(userRepository).findById(userId); // Ensure user lookup
        verify(favoriteRepository).deleteAllByUserId(userId); // Ensure favorites are deleted

    }

    @Test
    @DisplayName("Test removeAllFavoritesForAdmin - User Not Found")
    void testRemoveAllFavoritesForAdmin_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> favoriteService.removeAllFavoritesForAdmin(userId)
        );

        assertEquals(ErrorMessages.NOT_FOUND_USER_MESSAGE + userId, exception.getMessage());
        verify(userRepository).findById(userId);
        verifyNoInteractions(favoriteRepository);
    }

    @Test
    @DisplayName("Test removeFavorite - Success")
    void testFavorite_Success() {
        // Mock data
        Long favoriteId = 1L;

        // Mock a valid favorite
        Favorite favorite = new Favorite();
        favorite.setId(favoriteId);

        // Mock repository behavior
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        // Call the method
        favoriteService.removeFavorite(favoriteId);

        // Verify repository interactions
        verify(favoriteRepository).findById(favoriteId);
        verify(favoriteRepository).delete(favorite);
    }

    @Test
    @DisplayName("Test removeFavorite - Favorite Not Found")
    void testRemoveFavorite_FavoriteNotFound() {
        // Mock data
        Long favoriteId = 1L;

        // Mock repository behavior to return an empty Optional (favorite not found)
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        // Call the method and assert exception is thrown
        assertThatThrownBy(() -> favoriteService.removeFavorite(favoriteId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(ErrorMessages.NOT_FOUND_FAVORITE_MESSAGE + favoriteId);

        // Verify repository interactions
        verify(favoriteRepository).findById(favoriteId); // Verify findById was called
        verify(favoriteRepository, never()).delete(any(Favorite.class)); // Ensure delete is never called
    }
}




