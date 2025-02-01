package com.team01.realestate.controller.business;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.CityRepository;
import com.team01.realestate.repository.business.CountryRepository;
import com.team01.realestate.repository.business.DistrictRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

class LocationControllerTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private AdvertRepository advertRepository;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testImportData_Success() {
        // Arrange
        Country country = new Country();
        country.setName("TestCountry");

        City city = new City();
        city.setName("TestCity");
        city.setDistricts(new ArrayList<>());
        country.setCities(List.of(city));

        when(countryRepository.save(any(Country.class))).thenReturn(country);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        // Act
        ResponseMessage<String> response = locationController.importData(List.of(country));

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Successfully imported", response.getMessage());
        verify(countryRepository, times(1)).save(country);
        verify(cityRepository, times(1)).save(city);
        verify(districtRepository, never()).save(any());
    }

    @Test
    void testImportData_DuplicateCountryError() {
        // Arrange
        Country country = new Country();
        country.setName("TestCountry");

        when(countryRepository.save(any(Country.class)))
                .thenThrow(new RuntimeException("duplicate key value violates unique constraint Key (name)=(TestCountry)"));

        // Act
        ResponseMessage<String> response = locationController.importData(List.of(country));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertTrue(response.getMessage().contains("Error: The country 'TestCountry' already exists in the database."));
    }

    @Test
    void testImportData_GenericError() {
        // Arrange
        Country country = new Country();
        country.setName("TestCountry");

        when(countryRepository.save(any(Country.class)))
                .thenThrow(new RuntimeException("Unexpected database error"));

        // Act
        ResponseMessage<String> response = locationController.importData(List.of(country));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("An error occurred while importing data.", response.getMessage());
    }

    @Test
    void testDeleteAllCountriesCitiesDistricts_Success() {
        // Act
        ResponseMessage<String> response = locationController.deleteAllCountriesCitiesDistricts();

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        verify(districtRepository, times(1)).deleteAll();
        verify(cityRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteAllCountriesCitiesDistricts_Error() {
        // Arrange
        doThrow(new RuntimeException("Foreign key constraint violation"))
                .when(districtRepository).deleteAll();

        // Act
        ResponseMessage<String> response = locationController.deleteAllCountriesCitiesDistricts();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertTrue(response.getMessage().contains("An error occurred while deleting data"));
        verify(districtRepository, times(1)).deleteAll();
    }

    @Test
    void testParseDatabaseException_DuplicateKeyError() {
        // Arrange
        String errorMessage = "duplicate key value violates unique constraint Key (name)=(TestCountry)";
        Exception exception = new RuntimeException(errorMessage);

        // Act
        String result = locationController.parseDatabaseException(exception);

        // Assert
        assertEquals("Error: The country 'TestCountry' already exists in the database.", result);
    }

    @Test
    void testParseDatabaseException_GenericError() {
        // Arrange
        String errorMessage = "Unexpected database error";
        Exception exception = new RuntimeException(errorMessage);

        // Act
        String result = locationController.parseDatabaseException(exception);

        // Assert
        assertEquals("An error occurred while importing data.", result);
    }

    @Test
    void testImportData_SaveDistrictsAndLinkToCity() {
        // Arrange
        Country country = new Country();
        country.setName("TestCountry");

        City city = new City();
        city.setName("TestCity");

        District district1 = new District();
        district1.setName("District1");

        District district2 = new District();
        district2.setName("District2");

        city.setDistricts(List.of(district1, district2));
        country.setCities(List.of(city));

        when(countryRepository.save(any(Country.class))).thenReturn(country);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        when(districtRepository.save(any(District.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Return the district passed in.

        // Act
        ResponseMessage<String> response = locationController.importData(List.of(country));

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Successfully imported", response.getMessage());

        verify(countryRepository, times(1)).save(country);
        verify(cityRepository, times(1)).save(city);
        verify(districtRepository, times(2)).save(any(District.class));

        // Check that districts are linked to the city
        assertEquals(city, district1.getCity());
        assertEquals(city, district2.getCity());
    }

}

