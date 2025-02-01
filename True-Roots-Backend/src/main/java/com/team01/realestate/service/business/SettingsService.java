package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.*;
import com.team01.realestate.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SettingsService {
    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DistrictRepository districtRepository;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;

    @Transactional
    public ResponseMessage<String> resetDatabase() {
        try {
            // Delete all adverts
            advertRepository.deleteAll();

            // Delete all District, City, and Country records
            districtRepository.deleteAll();
            cityRepository.deleteAll();
            countryRepository.deleteAll();

            // Delete CategoryPropertyKey and CategoryPropertyValue records for non-built-in Categories
            List<Category> nonBuiltInCategories = categoryRepository.findByBuiltInFalse();
            for (Category category : nonBuiltInCategories) {
                // Find all keys for the category
                List<CategoryPropertyKey> keys = categoryPropertyKeyRepository.findByCategoryIdAndBuiltInFalse(category.getId());
                for (CategoryPropertyKey key : keys) {
                    // Delete all values associated with the key
                    categoryPropertyValueRepository.deleteByCategoryPropertyKeyId(key.getId());
                }
                // Delete all keys for the category
                categoryPropertyKeyRepository.deleteByCategoryIdAndBuiltInFalse(category.getId());
            }

            // Delete non-built-in Category records
            categoryRepository.deleteByBuiltInFalse();

            // Delete other non-built-in entities
            advertTypeRepository.deleteByBuiltInFalse();
            userRepository.deleteByBuiltInFalse();

            // Return success message
            return ResponseMessage.<String>builder()
                    .message(SuccessMessages.DB_RESET_SUCCESSFULLY)
                    .build();
        } catch (Exception e) {
            // Optional logging
            e.printStackTrace();

            // Return error message
            return ResponseMessage.<String>builder()
                    .message(ErrorMessages.DB_RESET_FAILED)
                    .build();
        }
    }
}
