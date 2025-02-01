package com.team01.realestate.payload;

import com.team01.realestate.entity.concretes.business.*;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.request.business.ImageForAdvertRequest;
import com.team01.realestate.payload.request.business.ImageRequest;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.repository.business.ImageRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.business.ImageServiceImpl;
import com.team01.realestate.payload.mapper.AdvertMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdvertMapperTest {

    @Mock
    private MethodHelper methodHelper;

    @Mock
    private ImageServiceImpl imageService;

    @Mock
    private ImageRepository imageRepository;

    private AdvertMapper advertMapper;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create the AdvertMapper instance with mocked dependencies
        advertMapper = new AdvertMapper(methodHelper, imageService, imageRepository);
    }

    @Test
    public void testAdvertRequestToAdvert_validRequest() {
        // Prepare mock responses for methodHelper methods
        when(methodHelper.findAdvertTypeById(anyLong())).thenReturn(new AdvertType());
        when(methodHelper.findCountryById(anyLong())).thenReturn(new Country());
        when(methodHelper.findCityById(anyLong())).thenReturn(new City());
        when(methodHelper.findDistrictById(anyLong())).thenReturn(new District());
        when(methodHelper.findCategoryById(anyLong())).thenReturn(new Category());

        // Mocking Category and its CategoryPropertyKeys
        Category categoryMock = mock(Category.class);
        CategoryPropertyKey categoryPropertyKeyMock = mock(CategoryPropertyKey.class);
        when(categoryMock.getCategoryPropertyKeys()).thenReturn(Collections.singletonList(categoryPropertyKeyMock));
        when(methodHelper.findCategoryById(anyLong())).thenReturn(categoryMock);

        // Prepare a valid AdvertRequest with non-null images list
        AdvertRequest advertRequest = new AdvertRequest();
        advertRequest.setTitle("Sample Advert");
        advertRequest.setDesc("Sample description");
        advertRequest.setPrice(BigDecimal.valueOf(1000));
        advertRequest.setAdvert_type_id(1L);  // Example ID
        advertRequest.setCountry_id(1L);      // Example ID
        advertRequest.setCity_id(1L);        // Example ID
        advertRequest.setDistrict_id(1L);    // Example ID
        advertRequest.setCategory_id(1L);    // Example ID

        // Mock a valid image request
        ImageForAdvertRequest imageRequest = new ImageForAdvertRequest();
        imageRequest.setName("Image1");
        imageRequest.setType("jpg");
        imageRequest.setData("iVBORw0KGgoAAAANSUhEUgAAAOEAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");  // Shortened Base64 string for image
        imageRequest.setFeatured(true);

        advertRequest.setImages(Collections.singletonList(imageRequest));

        // Initialize properties list (can be empty or contain mock properties)
        advertRequest.setProperties(Collections.emptyList()); // or you can add mock property items

        // Perform the test
        Advert advert = advertMapper.advertRequestToAdvert(advertRequest);

        // Assertions
        assertNotNull(advert);
        assertEquals("Sample Advert", advert.getTitle());
        assertEquals(BigDecimal.valueOf(1000), advert.getPrice());
        verify(methodHelper, times(1)).findAdvertTypeById(1L);
        verify(methodHelper, times(1)).findCountryById(1L);
        verify(methodHelper, times(1)).findCityById(1L);
        verify(methodHelper, times(1)).findDistrictById(1L);
        verify(methodHelper, times(1)).findCategoryById(1L);
    }


    @Test
    public void testToCountryResponse_validCountry() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getId()).thenReturn(1L);
        when(country.getName()).thenReturn("Country Name");

        // Act
        CountryResponse countryResponse = advertMapper.toCountryResponse(country);

        // Assert
        assertNotNull(countryResponse);
        assertEquals(1L, countryResponse.getId().longValue());
        assertEquals("Country Name", countryResponse.getName());
    }

    @Test
    public void testToCityResponse_validCity() {
        // Arrange
        City city = mock(City.class);
        when(city.getId()).thenReturn(1L);
        when(city.getName()).thenReturn("City Name");

        // Act
        CityResponse cityResponse = advertMapper.toCityResponse(city);

        // Assert
        assertNotNull(cityResponse);
        assertEquals(1L, cityResponse.getId().longValue());
        assertEquals("City Name", cityResponse.getName());
    }

    @Test
    public void testToDistrictResponse_validDistrict() {
        // Arrange
        District district = mock(District.class);
        when(district.getId()).thenReturn(1L);
        when(district.getName()).thenReturn("District Name");

        // Act
        DistrictResponse districtResponse = advertMapper.toDistrictResponse(district);

        // Assert
        assertNotNull(districtResponse);
        assertEquals(1L, districtResponse.getId().longValue());
        assertEquals("District Name", districtResponse.getName());
    }

    @Test
    public void testToCategoryResponse_validAdvert() {
        // Arrange
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(1L);
        when(category.getTitle()).thenReturn("Category Title");

        CategoryPropertyKey propertyKey = mock(CategoryPropertyKey.class);
        when(propertyKey.getId()).thenReturn(1L);
        when(propertyKey.getName()).thenReturn("Property Key Name");
        when(category.getCategoryPropertyKeys()).thenReturn(Collections.singletonList(propertyKey));

        Advert advert = mock(Advert.class);
        when(advert.getCategory()).thenReturn(category);

        // Act
        CategoryResponse categoryResponse = advertMapper.toCategoryResponse(advert);

        // Assert
        assertNotNull(categoryResponse);
        assertEquals(1L, categoryResponse.getId().longValue());
        assertEquals("Category Title", categoryResponse.getTitle());
        assertEquals(1, categoryResponse.getCategoryPropertyKeys().size());
        assertEquals(1L, categoryResponse.getCategoryPropertyKeys().get(0).getId().longValue());
        assertEquals("Property Key Name", categoryResponse.getCategoryPropertyKeys().get(0).getName());
    }

    @Test
    public void testAdvertRequestToUpdatedAdvert_missingCategoryPropertyKeys() {
        // Arrange
        AdvertRequestForUpdate advertRequestForUpdate = mock(AdvertRequestForUpdate.class);
        Advert foundAdvert = mock(Advert.class);
        Category categoryMock = mock(Category.class);

        // Mocking the scenario where category does not have CategoryPropertyKeys
        when(advertRequestForUpdate.getTitle()).thenReturn("Updated Title");
        when(advertRequestForUpdate.getDesc()).thenReturn("Updated Description");
        when(advertRequestForUpdate.getPrice()).thenReturn(BigDecimal.valueOf(1000.0));
        when(advertRequestForUpdate.getCategory_id()).thenReturn(1L);
        when(methodHelper.findCategoryById(1L)).thenReturn(categoryMock);
        when(categoryMock.getCategoryPropertyKeys()).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            advertMapper.advertRequestToUpdatedAdvert(advertRequestForUpdate, foundAdvert);
        });

        assertEquals("Category does not have CategoryPropertyKeys.", thrown.getMessage());
    }

    @Test
    void testGetFavoriteResponses_withNoFavorites() {
        // Arrange
        Advert advert = mock(Advert.class);
        MethodHelper methodHelper = mock(MethodHelper.class);

        // Mock the Advert's getFavorites to return null (no favorites)
        when(advert.getFavorites()).thenReturn(Collections.emptyList());

        // Act
        List<FavoriteResponse> favoriteResponses = advertMapper.getFavoriteResponses(advert);

        // Assert
        assertNotNull(favoriteResponses);
        assertTrue(favoriteResponses.isEmpty());  // Empty list if no favorites
    }

}