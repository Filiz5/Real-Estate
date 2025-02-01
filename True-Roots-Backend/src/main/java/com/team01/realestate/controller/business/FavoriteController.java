package com.team01.realestate.controller.business;


import com.team01.realestate.payload.mapper.FavoriteMapper;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.impl.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * K-01
     * GET
     * HTTP://localhost:8080/favorites/auth + GET
     * It will get authenticated user’s favorites
     * CUSTOMER
     * payload : --
     * response:[{id:3,title:"",...}]
     * requirement:--
     */
//    @GetMapping("/auth")
//    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
//    public ResponseMessage<List<FavoriteResponse>> getAuthenticatedUserFavorites(HttpServletRequest httpServletRequest) {
//
//        List<FavoriteResponse> favorites = favoriteService.getFavoritesForAuthenticatedUser(httpServletRequest);
//        return ResponseMessage.<List<FavoriteResponse>>builder()
//                .object(favorites)
//                .message(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY)
//                .httpStatus(HttpStatus.OK)
//                .build();
//    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<Page<FavoriteResponse>> getAuthenticatedUserFavoritesPageable(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String type) {


        Page<FavoriteResponse> favorites = favoriteService.getAuthenticatedUserFavoritesPageable(query, httpServletRequest, page, size,sort,type);

        return ResponseMessage.<Page<FavoriteResponse>>builder()
                .object(favorites)
                .message(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    /**
     * K-02
     * http://localhost:8080/favorites/admin/23 + GET
     * it wil get user's favorites
     * MANAGER + ADMIN
     * PAYLOAD: user_id
     * RESPONSE:array[advert] [{id:3,title:"",...}]
     * REQUİREMENTS:---
     */
    @GetMapping("/admin/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<List<FavoriteResponse>> getFavoritesForUser(@PathVariable Long userId) {

        List<FavoriteResponse> favorites =
                favoriteService.getFavoritesForUser(userId);

        return ResponseMessage.<List<FavoriteResponse>>builder()
                .object(favorites)
                .message(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();

    }

//    @GetMapping("/admin/{userId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseMessage<Page<FavoriteResponse>> getFavoritesByUserIdPageable(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sort,
//            @RequestParam(defaultValue = "desc") String type) {
//
//        Page<FavoriteResponse> favorites = favoriteService.getFavoritesByUserIdPageable(userId, page, size, sort ,type);
//
//        return ResponseMessage.<Page<FavoriteResponse>>builder()
//                .object(favorites)
//                .message(SuccessMessages.FAVORITES_FOUND_SUCCESSFULLY)
//                .httpStatus(HttpStatus.OK)
//                .build();
//    }


    /**
     * K-03
     * http://localhost:8080/{id}/auth + POST
     * CUSTOMER
     * PAYLOAD:id: advert_id(required)
     * response model: {id:3,title:"something",...}
     * requirements:
     * 1-It should add/remove to/from favorites table
     * 2-If given advertId is found in favorites table, it should be removed otherwise
     * it should be added
     */
    @PostMapping("/{id}/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponse> addOrRemoveFavorite(@PathVariable("id") Long advertId, HttpServletRequest httpServletRequest) {

        AdvertResponse advertResponse = favoriteService.toggleFavorite(advertId, httpServletRequest);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(advertResponse.isDeleted() ? SuccessMessages.ADVERT_DELETED_FROM_FAVORITES_SUCCESSFULLY : SuccessMessages.ADVERT_ADDED_TO_FAVORITES_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * K-04
     * http://localhost:8080/favorites/auth + DELETE
     * CUSTOMER
     * It will remove all favorites of authenticated user
     * PAYLOAD:--
     * RESPONSE:--
     * REQUIREMENTS:
     */
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @DeleteMapping("/auth")
    public ResponseMessage<Void> removeAllFavoritesCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.removeAllFavoritesCustomer(userDetails);

        return ResponseMessage.<Void>builder()
                .message(SuccessMessages.FAVORITES_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * K-05
     * http://localhost:8080/favorites/admin + DELETE
     * ADMIN + MANAGER
     * it will remove all favorites of user
     * payload:---
     * response:---
     * requirements:---
     */

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @DeleteMapping("/admin/{userId}")
    public ResponseMessage<FavoriteResponse> removeAllFavorites(@PathVariable Long userId) {

        favoriteService.removeAllFavoritesForAdmin(userId);

        return ResponseMessage.<FavoriteResponse>builder()
                .message(SuccessMessages.FAVORITES_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * K-06
     * http://localhost:8080/favorites/{id}/admin + DELETE
     * MANAGER ADMIN
     * it will remove a favorite of a user
     * Payload: ---
     * response: ---
     * requirements: ---
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @DeleteMapping("/{favoriteId}/admin")
    public ResponseMessage<FavoriteResponse> removeFavorite(@PathVariable Long favoriteId) {

        favoriteService.removeFavorite(favoriteId);

        return ResponseMessage.<FavoriteResponse>builder()
                .message(SuccessMessages.FAVORITE_DELETED_SUCCESSFULLY + favoriteId)
                .httpStatus(HttpStatus.OK)
                .build();
    }


}
