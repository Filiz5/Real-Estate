package com.team01.realestate.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.payload.response.abstracts.BaseUserResponse;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseWithOtherEntities extends BaseUserResponse {

    private List<String> userRoles; // Sadece rol adlarını tutar
    List<TourRequestResponse> tourRequests;
    List<FavoriteResponse> favorites;
    List<Log> logs;
    Set<AdvertResponse> adverts;


}
