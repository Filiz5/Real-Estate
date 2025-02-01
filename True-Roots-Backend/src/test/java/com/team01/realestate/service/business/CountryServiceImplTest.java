package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CountryMapper;
import com.team01.realestate.payload.response.business.CountryResponse;
import com.team01.realestate.repository.business.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCountry_Success() {
        // Arrange
        Country country1 = new Country();
        country1.setId(1L);
        country1.setName("Country1");

        Country country2 = new Country();
        country2.setId(2L);
        country2.setName("Country2");

        List<Country> countryList = Arrays.asList(country1, country2);

        CountryResponse countryResponse1 = new CountryResponse();
        countryResponse1.setId(1L);
        countryResponse1.setName("Country1");
        CountryResponse countryResponse2 = new CountryResponse();
        countryResponse2.setId(2L);
        countryResponse2.setName("Country2");

        when(countryRepository.findAll()).thenReturn(countryList);
        when(countryMapper.toResponse(country1)).thenReturn(countryResponse1);
        when(countryMapper.toResponse(country2)).thenReturn(countryResponse2);

        // Act
        List<CountryResponse> result = countryService.getAllCountry();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Country1", result.get(0).getName());
        assertEquals("Country2", result.get(1).getName());

        verify(countryRepository, times(1)).findAll();
        verify(countryMapper, times(1)).toResponse(country1);
        verify(countryMapper, times(1)).toResponse(country2);
    }

    @Test
    void testGetAllCountry_NoCountriesFound() {
        // Arrange
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, countryService::getAllCountry);
        assertEquals("Country not found", exception.getMessage());

        verify(countryRepository, times(1)).findAll();
        verifyNoInteractions(countryMapper);
    }
}
