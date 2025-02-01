package com.team01.realestate.service.impl;

import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FavoriteService {


    List<FavoriteResponse> getFavoritesForAuthenticatedUser(HttpServletRequest httpServletRequest);

    List<FavoriteResponse> getFavoritesForUser(Long userId);


    AdvertResponse toggleFavorite(Long advertId, HttpServletRequest httpServletRequest);

    void removeAllFavoritesCustomer(UserDetails userDetails);


    void removeAllFavoritesForAdmin(Long userId);

    void removeFavorite(Long id);


    Page<FavoriteResponse> getFavoritesByUserIdPageable(String query, Long userId, int page, int size , String sort, String type);

    Page<FavoriteResponse> getAuthenticatedUserFavoritesPageable(String query, HttpServletRequest httpServletRequest, int page, int size, String sort, String type);
}