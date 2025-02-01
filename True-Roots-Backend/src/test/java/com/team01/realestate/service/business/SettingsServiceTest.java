package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.*;
import com.team01.realestate.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SettingsServiceTest {

    @InjectMocks
    private SettingsService settingsService;

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private AdvertTypeRepository advertTypeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private CategoryPropertyKeyRepository categoryPropertyKeyRepository;

    @Mock
    private CategoryPropertyValueRepository categoryPropertyValueRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResetDatabase_success() {
        // Mocking non-built-in categories
        Category mockCategory = new Category();
        mockCategory.setId(1L);
        when(categoryRepository.findByBuiltInFalse()).thenReturn(List.of(mockCategory));

        // Mocking category property keys
        CategoryPropertyKey mockKey = new CategoryPropertyKey();
        mockKey.setId(1L);
        when(categoryPropertyKeyRepository.findByCategoryIdAndBuiltInFalse(1L)).thenReturn(List.of(mockKey));

        // Executing the method
        ResponseMessage<String> response = settingsService.resetDatabase();

        // Verifications
        verify(advertRepository, times(1)).deleteAll();
        verify(districtRepository, times(1)).deleteAll();
        verify(cityRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).deleteAll();
        verify(categoryPropertyValueRepository, times(1)).deleteByCategoryPropertyKeyId(1L);
        verify(categoryPropertyKeyRepository, times(1)).deleteByCategoryIdAndBuiltInFalse(1L);
        verify(categoryRepository, times(1)).deleteByBuiltInFalse();
        verify(advertTypeRepository, times(1)).deleteByBuiltInFalse();
        verify(userRepository, times(1)).deleteByBuiltInFalse();

        // Assertion
        assertEquals(SuccessMessages.DB_RESET_SUCCESSFULLY, response.getMessage());
    }

    @Test
    void testResetDatabase_failure() {
        // Mocking an exception
        doThrow(new RuntimeException("Test exception")).when(advertRepository).deleteAll();

        // Executing the method and verifying the exception
        ResponseMessage<String> response = settingsService.resetDatabase();

        // Verifications
        verify(advertRepository, times(1)).deleteAll();

        // Assertions
        assertEquals(ErrorMessages.DB_RESET_FAILED, response.getMessage());
    }
}

