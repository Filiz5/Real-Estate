package com.team01.realestate.service.helper;

import com.team01.realestate.entity.concretes.business.*;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.repository.business.*;
import com.team01.realestate.repository.user.RoleRepository;
import com.team01.realestate.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MethodHelperTest {

    @InjectMocks
    private MethodHelper methodHelper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryPropertyKeyRepository categoryPropertyKeyRepository;

    @Mock
    private CategoryPropertyValueRepository categoryPropertyValueRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private TourRequestRepository tourRequestRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private AdvertTypeRepository advertTypeRepository;

    @Mock
    private HttpServletRequest request;

    private User user;
    private Role role;
    private Advert advert;
    private AdvertType advertType;
    private City city;
    private Country country;
    private District district;
    private Category category;

    private CategoryPropertyKey categoryPropertyKey;
    private CategoryPropertyValue categoryPropertyValue;
    private Image image;
    private Log log;
    private TourRequest tourRequest;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        role = new Role();
        role.setId(1L);
        role.setRoleName("Admin");

        advert = new Advert();
        advert.setId(1L);
        advert.setTitle("Sample Advert");

        advertType = new AdvertType();
        advertType.setId(1L);
        advertType.setTitle("Sample Advert Type");

        city = new City();
        city.setId(1L);
        city.setName("Sample City");

        country = new Country();
        country.setId(1L);
        country.setName("Sample Country");

        district = new District();
        district.setId(1L);
        district.setName("Sample District");

        category = new Category();
        category.setId(1L);
        category.setTitle("Sample Category");

        categoryPropertyKey = new CategoryPropertyKey();
        categoryPropertyKey.setId(1L);
        categoryPropertyKey.setName("Sample Key");

        categoryPropertyValue = new CategoryPropertyValue();
        categoryPropertyValue.setId(1L);
        categoryPropertyValue.setValue("Sample Value");

        image = new Image();
        image.setId(1L);
        image.setName("http://example.com/image.jpg");

        log = new Log();
        log.setId(1L);
        log.setDescription("Sample Log");

        tourRequest = new TourRequest();
        tourRequest.setId(1L);

        favorite = new Favorite();
        favorite.setId(1L);
        favorite.setAdvert(advert);
    }

    @Test
    void testFindUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = methodHelper.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findUserById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, 1), exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindRoleById_success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = methodHelper.findRoleById(1L);

        assertNotNull(result);
        assertEquals("Admin", result.getRoleName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindRoleById_notFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findRoleById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_ROLE_MESSAGE, 1), exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByEmail_success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = methodHelper.findUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindUserByEmail_notFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        User result = methodHelper.findUserByEmail("test@example.com");

        assertNull(result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindAdvertById_success() {
        when(advertRepository.findById(1L)).thenReturn(Optional.of(advert));

        Advert result = methodHelper.findAdvertById(1L);

        assertNotNull(result);
        assertEquals("Sample Advert", result.getTitle());
        verify(advertRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAdvertById_notFound() {
        when(advertRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findAdvertById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE, 1), exception.getMessage());
        verify(advertRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAdvertTypeById_success() {
        when(advertTypeRepository.findById(1L)).thenReturn(Optional.of(advertType));

        AdvertType result = methodHelper.findAdvertTypeById(1L);

        assertNotNull(result);
        assertEquals("Sample Advert Type", result.getTitle());
        verify(advertTypeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAdvertTypeById_notFound() {
        when(advertTypeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findAdvertTypeById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_ADVERT_TYPE_MESSAGE, 1), exception.getMessage());
        verify(advertTypeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCityById_success() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City result = methodHelper.findCityById(1L);

        assertNotNull(result);
        assertEquals("Sample City", result.getName());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCityById_notFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findCityById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_CITY_MESSAGE,1), exception.getMessage());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCountryById_success() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Country result = methodHelper.findCountryById(1L);

        assertNotNull(result);
        assertEquals("Sample Country", result.getName());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCountryById_notFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findCountryById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_COUNTRY_MESSAGE,1), exception.getMessage());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindDistrictById_success() {
        when(districtRepository.findById(1L)).thenReturn(Optional.of(district));

        District result = methodHelper.findDistrictById(1L);

        assertNotNull(result);
        assertEquals("Sample District", result.getName());
        verify(districtRepository, times(1)).findById(1L);
    }

    @Test
    void testFindDistrictById_notFound() {
        when(districtRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findDistrictById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_DISTRICT_MESSAGE, 1 ), exception.getMessage());
        verify(districtRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryById_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = methodHelper.findCategoryById(1L);

        assertNotNull(result);
        assertEquals("Sample Category", result.getTitle());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryById_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findCategoryById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_CATEGORY_MESSAGE,1 ), exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryPropertyKeyById_success() {
        when(categoryPropertyKeyRepository.findById(1L)).thenReturn(Optional.of(categoryPropertyKey));

        CategoryPropertyKey result = methodHelper.findCategoryPropertyKeyById(1L);

        assertNotNull(result);
        assertEquals("Sample Key", result.getName());
        verify(categoryPropertyKeyRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryPropertyKeyById_notFound() {
        when(categoryPropertyKeyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findCategoryPropertyKeyById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_CATEGORY_PROPERTY_KEY_MESSAGE, 1), exception.getMessage());
        verify(categoryPropertyKeyRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryPropertyValueById_success() {
        when(categoryPropertyValueRepository.findById(1L)).thenReturn(Optional.of(categoryPropertyValue));

        CategoryPropertyValue result = methodHelper.findCategoryPropertyValueById(1L);

        assertNotNull(result);
        assertEquals("Sample Value", result.getValue());
        verify(categoryPropertyValueRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryPropertyValueById_notFound() {
        when(categoryPropertyValueRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findCategoryPropertyValueById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_CATEGORY_PROPERTY_VALUE_MESSAGE, 1), exception.getMessage());
        verify(categoryPropertyValueRepository, times(1)).findById(1L);
    }

    @Test
    void testFindImageById_success() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        Image result = methodHelper.findImageById(1L);

        assertNotNull(result);
        assertEquals("http://example.com/image.jpg", result.getName());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testFindImageById_notFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findImageById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_IMAGE_MESSAGE,1), exception.getMessage());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testFindLogById_success() {
        when(logRepository.findById(1L)).thenReturn(Optional.of(log));

        Log result = methodHelper.findLogById(1L);

        assertNotNull(result);
        assertEquals("Sample Log", result.getDescription());
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    void testFindLogById_notFound() {
        when(logRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findLogById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_LOG_MESSAGE, 1), exception.getMessage());
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    void testFindTourRequestById_success() {
        when(tourRequestRepository.findById(1L)).thenReturn(Optional.of(tourRequest));

        TourRequest result = methodHelper.findTourRequestById(1L);

        assertNotNull(result);
       // assertEquals("Sample Tour Request", result.get);
        verify(tourRequestRepository, times(1)).findById(1L);
    }

    @Test
    void testFindTourRequestById_notFound() {
        when(tourRequestRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findTourRequestById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_TOUR_REQUEST_MESSAGE, 1), exception.getMessage());
        verify(tourRequestRepository, times(1)).findById(1L);
    }

    @Test
    void testFindFavoriteById_success() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));

        Favorite result = methodHelper.findFavoriteById(1L);

        assertNotNull(result);
        verify(favoriteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindFavoriteById_notFound() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findFavoriteById(1L));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_FAVORITE_MESSAGE, 1), exception.getMessage());
        verify(favoriteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAuthenticatedUser_success() {
        when(request.getAttribute("username")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = methodHelper.findAuthenticatedUser(request);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindAuthenticatedUser_userNotFound() {
        when(request.getAttribute("username")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.findAuthenticatedUser(request));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, "test@example.com"), exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testIsAuthenticatedUser_success() {
        when(request.getAttribute("username")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertDoesNotThrow(() -> methodHelper.isAuthenticatedUser(request));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testIsAuthenticatedUser_userNotFound() {
        when(request.getAttribute("username")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> methodHelper.isAuthenticatedUser(request));

        assertEquals(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, "test@example.com"), exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

}
