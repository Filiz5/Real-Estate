package com.team01.realestate.controller.business;


import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import org.springframework.security.core.userdetails.UserDetails;
import com.team01.realestate.service.business.FavoriteServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FavoriteControllerTest {

    Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @InjectMocks
    private FavoriteController favoriteController;

    @Mock
    private FavoriteServiceImpl favoriteService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test add or remove favorite")
    @WithMockUser(authorities = {"CUSTOMER"})
    void addOrRemoveFavoriteTest() {
        Long advertId = 1L;


        AdvertResponse advertResponse = new AdvertResponse();
        advertResponse.setId(1L);
        advertResponse.setTitle("Sample Title");
        advertResponse.setDesc("Sample Description");
        advertResponse.setFavorited(true);


        ResponseMessage<AdvertResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(advertResponse);
        expectedResponse.setMessage(SuccessMessages.ADVERT_ADDED_TO_FAVORITES_SUCCESSFULLY);


        when(favoriteService.toggleFavorite(advertId, httpServletRequest)).thenReturn(advertResponse);

        ResponseMessage<AdvertResponse> actualResponse = favoriteController.addOrRemoveFavorite(advertId, httpServletRequest);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
        assertThat(actualResponse.getObject().getFavorites()).isEqualTo(expectedResponse.getObject().getFavorites());

        logger.info("added or removed favorite: {}", actualResponse);

    }

    @Test
    @DisplayName("Test get Authenticated User Favorites Pageable")
    @WithMockUser(authorities = {"CUSTOMER"})
    void getAuthenticatedUserFavoritesPageable() {
        String q = "";
        int page = 0;
        int size = 10;
        String sort = "id";
        String type = "asc";

        Page<FavoriteResponse> favoriteResponses = new PageImpl<>(Collections.emptyList());
        ResponseMessage<Page<FavoriteResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(favoriteResponses);
        expectedResponse.setMessage(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY);


        when(favoriteService.getAuthenticatedUserFavoritesPageable(q,httpServletRequest, page, size, sort, type)).thenReturn(favoriteResponses);

        ResponseMessage<Page<FavoriteResponse>>  actualResponse = favoriteController.getAuthenticatedUserFavoritesPageable(httpServletRequest, q, page, size, sort, type);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());


        logger.info("get Authenticated User favorites by page {}", actualResponse);

    }

    @Test
    @DisplayName("Test get Favorites For User")
    @WithMockUser(authorities = {"ADMIN"})
    void getFavoritesForUser() {
        Long userId = 1L;

        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        ResponseMessage<List<FavoriteResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(favoriteResponses);
        expectedResponse.setMessage(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY);


        when(favoriteService.getFavoritesForUser(userId)).thenReturn(favoriteResponses);

        ResponseMessage<List<FavoriteResponse>>  actualResponse = favoriteController.getFavoritesForUser(userId);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());


        logger.info("get Favorites For User {}", actualResponse);

    }


    @Test
    @DisplayName("Test remove all favorites customer")
    @WithMockUser(authorities = {"CUSTOMER"})
    void removeAllFavoritesCustomer() {
        UserDetails userDetails = mock(UserDetails.class);


        ResponseMessage<Void> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage(SuccessMessages.FAVORITES_DELETED_SUCCESSFULLY);

        doNothing().when(favoriteService).removeAllFavoritesCustomer(userDetails);


      ResponseMessage<Void> actualResponse = favoriteController.removeAllFavoritesCustomer(userDetails);

      assertThat(actualResponse).isNotNull();
      assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
      assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());

        logger.info("removed all favorites customer {}", actualResponse.getMessage());

    }

    @Test
    @DisplayName("Test remove all favorites")
    @WithMockUser(authorities = {"ADMIN"})
    void removeAllFavorites() {
        Long userId = 1L;

        ResponseMessage<FavoriteResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage(SuccessMessages.FAVORITES_DELETED_SUCCESSFULLY);



        doNothing().when(favoriteService).removeAllFavoritesForAdmin(userId);

        ResponseMessage<FavoriteResponse>  actualResponse = favoriteController.removeAllFavorites(userId);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());


        logger.info("removed all favorites {}", actualResponse);

    }


    @Test
    @DisplayName("Test remove favorite")
    @WithMockUser(authorities = {"ADMIN"})
    void removeFavorite() {
        Long favoriteId = 1L;

        ResponseMessage<FavoriteResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage(SuccessMessages.FAVORITE_DELETED_SUCCESSFULLY + favoriteId);



        doNothing().when(favoriteService).removeFavorite(favoriteId);

        ResponseMessage<FavoriteResponse>  actualResponse = favoriteController.removeFavorite(favoriteId);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());


        logger.info("removed all favorites {}", actualResponse);

    }


}