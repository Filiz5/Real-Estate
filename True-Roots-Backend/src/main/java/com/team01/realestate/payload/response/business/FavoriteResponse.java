package com.team01.realestate.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team01.realestate.entity.concretes.business.Advert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponse {

    private Long id;
    private String advertTitle;
    private Long advertId;
    private Long userId;
    private AdvertResponse advert;
}
