package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.service.helper.MethodHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TourRequestMapper {

    private final MethodHelper methodHelper;
    private final AdvertMapper advertMapper;

    public TourRequest tourRequestRequestToTourRequest(TourRequestRequest tourRequestRequest){

        if(tourRequestRequest == null) {
            return null;
        }

        return TourRequest.builder()

                .tourDate(tourRequestRequest.getTourDate())
                .tourTime(tourRequestRequest.getTourTime())
                .advert(methodHelper.findAdvertById(tourRequestRequest.getAdvertId()))
                .build();
    }

    // Tek bir TourRequest nesnesini dönüştürür
    public TourRequestResponse tourRequestToTourRequestResponse(TourRequest tourRequest) {

        AdvertResponse advertResponse = advertMapper.advertToAdvertResponse(tourRequest.getAdvert());

        return TourRequestResponse.builder()
                .id(tourRequest.getId())
                .tourDate(tourRequest.getTourDate())
                .tourTime(tourRequest.getTourTime())
                .tourRequestStatus(tourRequest.getTourRequestStatus())
                .createdAt(tourRequest.getCreatedAt())
                .updatedAt(tourRequest.getUpdatedAt())
                .advertId(tourRequest.getAdvert().getId())
                .advert(advertResponse)
                .ownerUserId(tourRequest.getOwnerUser().getId())
                .ownerFirstName(tourRequest.getOwnerUser().getFirstName())
                .ownerLastName(tourRequest.getOwnerUser().getLastName())
                .guestUserId(tourRequest.getGuestUser().getId())
                .guestFirstName(tourRequest.getGuestUser().getFirstName())
                .guestLastName(tourRequest.getGuestUser().getLastName())
                .build();
    }

    // Bir Set<TourRequest> koleksiyonunu dönüştürür
    public Set<TourRequestResponse> tourRequestToTourRequestResponse(Set<TourRequest> tourRequests) {
        return tourRequests.stream()
                .map(this::tourRequestToTourRequestResponse) // Mevcut metodu çağırır
                .collect(Collectors.toSet());
    }




}
