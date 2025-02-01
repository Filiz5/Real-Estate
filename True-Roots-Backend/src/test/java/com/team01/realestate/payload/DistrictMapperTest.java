package com.team01.realestate.payload;

import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.payload.mapper.DistrictMapper;
import com.team01.realestate.payload.request.business.DistrictRequest;
import com.team01.realestate.payload.response.business.DistrictResponse;
import org.junit.jupiter.api.Test;

class DistrictMapperTest {

    private final DistrictMapper districtMapper = new DistrictMapper();

    @Test
    void shouldMapDistrictToResponseSuccessfully() {
        // Arrange
        City city = new City();
        city.setName("Test City");

        District district = new District();
        district.setId(1L);
        district.setName("Test District");
        district.setCity(city);

        // Act
        DistrictResponse response = districtMapper.toResponse(district);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test District", response.getName());
        assertEquals("Test City", response.getCityName());
    }

    @Test
    void shouldMapDistrictRequestToEntitySuccessfully() {
        // Arrange
        DistrictRequest districtRequest = new DistrictRequest();
        districtRequest.setName("Test District");

        // Act
        District district = districtMapper.toEntity(districtRequest);

        // Assert
        assertNotNull(district);
        assertNull(district.getId()); // ID ataması yapılmadığından null beklenir
        assertEquals("Test District", district.getName());
    }
}

