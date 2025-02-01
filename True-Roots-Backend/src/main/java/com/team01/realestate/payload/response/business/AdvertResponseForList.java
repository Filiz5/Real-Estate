package com.team01.realestate.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import com.team01.realestate.entity.concretes.business.CategoryPropertyValue;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.enums.AdvertStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertResponseForList {

    private Long id;
    private String title;
    private String desc;
    private String slug;
    private BigDecimal price;
    private AdvertStatus advertStatus = AdvertStatus.PENDING;
    private boolean builtIn = false;
    private boolean isActive = true;
    private int viewCount = 0;
    private String location;  // Store Google embed code or map URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ResponseForAdvertType advertType;
    private CountryResponse country;
    private CityResponse city;
    private DistrictResponse district;
    private CategoryResponse category;
    private ImageResponse image;
    private int favoriteCount;
    private int tourRequestCount;
    private boolean isDeleted;
    private boolean isFavorited;

}


