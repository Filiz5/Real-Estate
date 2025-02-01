package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FavoriteMapper {

    private final AdvertMapper advertMapper;

    // Tek bir Favorite nesnesini FavoriteResponse'e dönüştürür
    public FavoriteResponse toResponse(Favorite favorite) {
        AdvertResponse advertResponse = advertMapper.advertToAdvertResponse(favorite.getAdvert());

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .advert(advertResponse)
                .build();
    }

    // Bir Set<Favorite> koleksiyonunu Set<FavoriteResponse>'e dönüştürür
    //public Set<FavoriteResponse> toResponse(Set<Favorite> favorites) {
      //  return favorites.stream()
        //        .map(this::toResponse) // Mevcut toResponse metodunu çağırır
          //      .collect(Collectors.toSet());
  //  }
}
