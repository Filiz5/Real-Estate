package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.payload.request.business.DistrictRequest;
import com.team01.realestate.payload.response.business.DistrictResponse;
import org.springframework.stereotype.Component;

@Component
public class DistrictMapper {

    public DistrictResponse toResponse(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .cityName(district.getCity().getName())
                .build();
    }

    public District toEntity(DistrictRequest districtRequest) {

        District district = new District();
        district.setName(districtRequest.getName());
        return district;
    }
}
