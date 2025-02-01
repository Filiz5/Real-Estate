package com.team01.realestate.payload;

import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.payload.mapper.CountryMapper;
import com.team01.realestate.payload.request.business.CountryRequest;
import com.team01.realestate.payload.response.business.CountryResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

class CountryMapperTest {

    private final CountryMapper countryMapper = new CountryMapper();

    @Test
    void shouldMapCountryToResponseSuccessfully() {
        // Arrange
        City city1 = new City();
        city1.setName("City 1");

        City city2 = new City();
        city2.setName("City 2");

        Country country = new Country();
        country.setId(1L);
        country.setName("Test Country");
        country.setCities(List.of(city1, city2));

        // Act
        CountryResponse response = countryMapper.toResponse(country);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Country", response.getName());
        assertNotNull(response.getCities());
        assertEquals(2, response.getCities().size());
        assertTrue(response.getCities().contains("City 1"));
        assertTrue(response.getCities().contains("City 2"));
    }

    @Test
    void shouldHandleCountryWithNoCitiesSuccessfully() {
        // Arrange
        Country country = new Country();
        country.setId(1L);
        country.setName("Test Country");
        country.setCities(null);

        // Act
        CountryResponse response = countryMapper.toResponse(country);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Country", response.getName());
        assertNull(response.getCities());
    }

    @Test
    void shouldMapCountryRequestToEntitySuccessfully() {
        // Arrange
        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setName("Test Country");

        // Act
        Country country = countryMapper.toEntity(countryRequest);

        // Assert
        assertNotNull(country);
        assertNull(country.getId()); // ID henüz atanmamış olmalı
        assertEquals("Test Country", country.getName());
        assertNull(country.getCities()); // Şehirler varsayılan olarak null olmalı
    }
}

