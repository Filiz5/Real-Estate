package com.team01.realestate.payload;

import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.payload.mapper.CityMapper;
import com.team01.realestate.payload.response.business.CityResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

class CityMapperTest {

    private final CityMapper cityMapper = new CityMapper();

    @Test
    void shouldMapCityToResponseSuccessfully() {
        // Arrange
        Country country = new Country();
        country.setName("Test Country");

        District district1 = new District();
        district1.setName("District 1");
        District district2 = new District();
        district2.setName("District 2");

        City city = new City();
        city.setId(1L);
        city.setName("Test City");
        city.setCountry(country);
        city.setDistricts(List.of(district1, district2));

        // Act
        CityResponse response = cityMapper.toResponse(city);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test City", response.getName());
        assertEquals("Test Country", response.getCountryName());
        assertNotNull(response.getDistricts());
        assertEquals(2, response.getDistricts().size());
        assertTrue(response.getDistricts().contains("District 1"));
        assertTrue(response.getDistricts().contains("District 2"));
    }

    @Test
    void shouldHandleCityWithNoDistrictsSuccessfully() {
        // Arrange
        Country country = new Country();
        country.setName("Test Country");

        City city = new City();
        city.setId(1L);
        city.setName("Test City");
        city.setCountry(country);
        city.setDistricts(null);

        // Act
        CityResponse response = cityMapper.toResponse(city);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test City", response.getName());
        assertEquals("Test Country", response.getCountryName());
        assertNull(response.getDistricts());
    }
}

