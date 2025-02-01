package com.team01.realestate.payload.response.business;

import com.team01.realestate.entity.enums.TourRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TourRequestResponse {

    private Long id;
    private LocalDate tourDate;
    private LocalTime tourTime;
    private TourRequestStatus tourRequestStatus = TourRequestStatus.PENDING;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long advertId;
    private AdvertResponse advert;
    private Long ownerUserId;
    private String ownerFirstName;
    private String ownerLastName;
    private Long guestUserId;
    private String guestFirstName;
    private String guestLastName;

}
