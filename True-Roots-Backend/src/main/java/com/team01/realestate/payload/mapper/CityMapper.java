package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.City;

import com.team01.realestate.payload.response.business.CityResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CityMapper {

    // Entity -> DTO (Response)
    public CityResponse toResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .countryName(city.getCountry().getName())
                .districts(city.getDistricts() != null
                        ? city.getDistricts().stream().map(district -> district.getName()).collect(Collectors.toList())
                        : null)
                .build();


    }
}
