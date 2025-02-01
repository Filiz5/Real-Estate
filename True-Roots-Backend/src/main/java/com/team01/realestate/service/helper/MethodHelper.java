package com.team01.realestate.service.helper;

import com.team01.realestate.entity.concretes.business.*;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.repository.business.*;
import com.team01.realestate.repository.business.CategoryPropertyValueRepository;
import com.team01.realestate.repository.user.RoleRepository;
import com.team01.realestate.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DistrictRepository districtRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;
    private final ImageRepository imageRepository;
    private final LogRepository logRepository;
    private final TourRequestRepository tourRequestRepository;
    private final FavoriteRepository favoriteRepository;
    private final AdvertResponse advertResponse;

    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Role findRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ROLE_MESSAGE, roleId)));
    }

    @Transactional
    public Advert findAdvertById(Long advertId) {
        return advertRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE, advertId)));
    }

    @Transactional
    public AdvertType findAdvertTypeById(Long advertTypeId) {
        return advertTypeRepository.findById(advertTypeId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVERT_TYPE_MESSAGE, advertTypeId)));
    }

    @Transactional
    public City findCityById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_CITY_MESSAGE, cityId)));
    }

    @Transactional
    public Country findCountryById(Long countryId) {
        return countryRepository.findById(countryId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_COUNTRY_MESSAGE, countryId)));
    }

    @Transactional
    public District findDistrictById(Long districtId) {
        return districtRepository.findById(districtId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_DISTRICT_MESSAGE, districtId)));
    }

    @Transactional
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_CATEGORY_MESSAGE, categoryId)));
    }

    @Transactional
    public CategoryPropertyKey findCategoryPropertyKeyById(Long keyId) {
        return categoryPropertyKeyRepository.findById(keyId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_CATEGORY_PROPERTY_KEY_MESSAGE, keyId)));
    }

    @Transactional
    public CategoryPropertyValue findCategoryPropertyValueById(Long valueId) {
        return categoryPropertyValueRepository.findById(valueId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_CATEGORY_PROPERTY_VALUE_MESSAGE, valueId)));
    }

    @Transactional
    public Image findImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_IMAGE_MESSAGE, imageId)));
    }

    @Transactional
    public Log findLogById(Long logId) {
        return logRepository.findById(logId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LOG_MESSAGE, logId)));
    }

    @Transactional
    public TourRequest findTourRequestById(Long requestId) {
        return tourRequestRepository.findById(requestId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_TOUR_REQUEST_MESSAGE, requestId)));
    }

    @Transactional
    public Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_FAVORITE_MESSAGE, favoriteId)));
    }

    public User findAuthenticatedUser(HttpServletRequest request) {
        String email = (String) request.getAttribute("username");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, email));
        }
        return user;
    }

    public void isAuthenticatedUser(HttpServletRequest request) {
        String email = (String) request.getAttribute("username");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, email));
        }
    }

    public static String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder generateCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            generateCode.append(characters.charAt(randomIndex));
        }
        return generateCode.toString();
    }

}
