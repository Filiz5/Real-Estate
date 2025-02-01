package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CityMapper;
import com.team01.realestate.payload.response.business.CityResponse;
import com.team01.realestate.repository.business.CityRepository;
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

class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCity_Success() {
        // Arrange
        City city1 = new City();
        city1.setId(1L);
        city1.setName("City1");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("City2");

        List<City> cityList = Arrays.asList(city1, city2);

        CityResponse cityResponse1 = new CityResponse();
        cityResponse1.setId(1L);
        cityResponse1.setName("City1");
        CityResponse cityResponse2 = new CityResponse();
        cityResponse2.setId(2L);
        cityResponse2.setName("City2");

        when(cityRepository.findAll()).thenReturn(cityList);
        when(cityMapper.toResponse(city1)).thenReturn(cityResponse1);
        when(cityMapper.toResponse(city2)).thenReturn(cityResponse2);

        // Act
        List<CityResponse> result = cityService.getAllCity();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("City1", result.get(0).getName());
        assertEquals("City2", result.get(1).getName());

        verify(cityRepository, times(1)).findAll();
        verify(cityMapper, times(1)).toResponse(city1);
        verify(cityMapper, times(1)).toResponse(city2);
    }

    @Test
    void testGetAllCity_NoCitiesFound() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, cityService::getAllCity);
        assertEquals("No city found", exception.getMessage());

        verify(cityRepository, times(1)).findAll();
        verifyNoInteractions(cityMapper);
    }
}
