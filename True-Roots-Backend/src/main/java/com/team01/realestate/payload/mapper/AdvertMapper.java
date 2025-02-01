package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.*;

import com.team01.realestate.entity.concretes.user.User;

import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.repository.business.ImageRepository;
import com.team01.realestate.service.business.ImageServiceImpl;
import com.team01.realestate.payload.response.business.ResponseForAdvertType;
import com.team01.realestate.service.helper.MethodHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AdvertMapper {

    private final MethodHelper methodHelper;
    private final ImageServiceImpl imageService; // Inject ImageService
    private final ImageRepository imageRepository;
    //private final ImageRepository imageRepository;

    public Advert advertRequestToAdvert(AdvertRequest advertRequest) {
        // 1. Create Advert entity from AdvertRequest
        Advert advert = Advert.builder()
                .title(advertRequest.getTitle())
                .desc(advertRequest.getDesc())
                .price(advertRequest.getPrice())
                .advertType(methodHelper.findAdvertTypeById(advertRequest.getAdvert_type_id()))
                .country(methodHelper.findCountryById(advertRequest.getCountry_id()))
                .city(methodHelper.findCityById(advertRequest.getCity_id()))
                .district(methodHelper.findDistrictById(advertRequest.getDistrict_id()))
                .category(methodHelper.findCategoryById(advertRequest.getCategory_id()))  // Fetch Category by ID
                .location(advertRequest.getLocation())  // Assuming location is directly passed
                .build();

        // Map and decode images
        List<Image> images = advertRequest.getImages().stream()
                .map(imageRequest -> Image.builder()
                        .name(imageRequest.getName())
                        .type(imageRequest.getType())
                        .data(decodeBase64Image(imageRequest.getData())) // Decode base64
                        .featured(imageRequest.isFeatured())
                        .advert(advert)
                        .build())
                .toList();

        advert.setImages(images);

        // Ensure category has CategoryPropertyKeys (Related keys for the Category)
        Category category = advert.getCategory();
        if (category == null || category.getCategoryPropertyKeys() == null || category.getCategoryPropertyKeys().isEmpty()) {
            throw new IllegalStateException("Category does not have CategoryPropertyKeys.");
        }

        // Convert CategoryPropertyKeys to a Map for fast lookup by ID
        Map<Long, CategoryPropertyKey> propertyKeyMap = category.getCategoryPropertyKeys().stream()
                .collect(Collectors.toMap(CategoryPropertyKey::getId, key -> key));

        // 2. Map the properties from AdvertRequest to CategoryPropertyValues
        List<CategoryPropertyValue> categoryPropertyValues = advertRequest.getProperties()
                .stream()
                .map(property -> {
                    // Get the corresponding CategoryPropertyKey from the category
                    CategoryPropertyKey propertyKey = propertyKeyMap.get(property.getId());

                    if (propertyKey == null) {
                        throw new IllegalArgumentException("CategoryPropertyKey not found for the provided ID: " + property.getId());
                    }

                    // Ensure the property value is not null or empty
                    if (property.getValue() == null || property.getValue().isEmpty()) {
                        throw new IllegalArgumentException("Property value cannot be null or empty for key: " + propertyKey.getId());
                    }

                    // Create a CategoryPropertyValue object and associate it with the Advert and PropertyKey
                    return CategoryPropertyValue.builder()
                            .categoryPropertyKey(propertyKey)  // Associate the property key
                            .value(property.getValue())        // Set the value from the request
                            .advert(advert)                    // Associate the property value with the Advert
                            .build();
                })
                .collect(Collectors.toList());

        // 3. Set the created CategoryPropertyValues to the Advert entity
        advert.setCategoryPropertyValues(categoryPropertyValues);

        return advert;
    }

    private byte[] decodeBase64Image(String base64Image) {
        if (base64Image.startsWith("data:image")) {
            // Strip the prefix (e.g., "data:image/png;base64,")
            String base64Data = base64Image.split(",")[1];
            return Base64.getDecoder().decode(base64Data);
        }
        return Base64.getDecoder().decode(base64Image); // Directly decode if no prefix
    }

    @Transactional
    public AdvertResponse advertToAdvertResponse(Advert advert) {
        List<ImageResponse> imageList = imageService.getImagesByAdvertId(advert.getId());
        CountryResponse countryResponse = toCountryResponse(advert.getCountry());
        CityResponse cityResponse = toCityResponse(advert.getCity());
        DistrictResponse districtResponse = toDistrictResponse(advert.getDistrict());
        List<FavoriteResponse> favoriteResponses = getFavoriteResponses(advert);

        return AdvertResponse.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .desc(advert.getDesc())
                .slug(advert.getSlug())
                .price(advert.getPrice())
                .advertStatus(advert.getAdvertStatus())
                .builtIn(advert.isBuiltIn())
                .isActive(advert.isActive())
                .viewCount(advert.getViewCount())
                .createdAt(advert.getCreatedAt())
                .updatedAt(advert.getUpdatedAt())
                .userId(advert.getUser().getId())
                .advertType(ResponseForAdvertType.builder()
                        .id(advert.getAdvertType().getId())
                        .title(advert.getAdvertType().getTitle())
                        .build())
                .country(countryResponse)
                .city(cityResponse)
                .district(districtResponse)
                .location(advert.getLocation())
                .category(toCategoryResponse(advert))
                .categoryPropertyValues(advert.getCategoryPropertyValues().stream()
                        .map(propertyValue -> CategoryPropertyValue.builder()
                                .id(propertyValue.getId())
                                .value(propertyValue.getValue())
                                .categoryPropertyKey(CategoryPropertyKey.builder()
                                        .id(propertyValue.getCategoryPropertyKey().getId())
                                        .name(propertyValue.getCategoryPropertyKey().getName())
                                        .build())
                                .build())
                        .toList())
                .images(imageList)
                .favorites(favoriteResponses)
                .logs(advert.getLogs())
                .tourRequests(advert.getTourRequests() != null
                        ? advert.getTourRequests().stream()
                        .map(tourRequest -> TourRequestResponse.builder()
                                .id(tourRequest.getId())
                                .tourDate(tourRequest.getTourDate())
                                .tourTime(tourRequest.getTourTime())
                                .tourRequestStatus(tourRequest.getTourRequestStatus())
                                .createdAt(tourRequest.getCreatedAt())
                                .updatedAt(tourRequest.getUpdatedAt())
                                .advertId(tourRequest.getAdvert().getId())
                                .ownerUserId(tourRequest.getOwnerUser().getId())
                                .ownerFirstName(tourRequest.getOwnerUser().getFirstName())
                                .ownerLastName(tourRequest.getOwnerUser().getLastName())
                                .guestUserId(tourRequest.getGuestUser().getId())
                                .guestFirstName(tourRequest.getGuestUser().getFirstName())
                                .guestLastName(tourRequest.getGuestUser().getLastName())
                                .build())
                        .toList()
                        : Collections.emptyList())
                .build();
    }

    public Set<AdvertResponse> advertToAdvertResponse(Set<Advert> adverts) {
        return adverts.stream()
                .map(this::advertToAdvertResponse)
                .collect(Collectors.toSet());
    }

    public CountryResponse toCountryResponse(Country country) {
        if (country == null) return null;
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    public CityResponse toCityResponse(City city) {
        if (city == null) return null;
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    public DistrictResponse toDistrictResponse(District district) {
        if (district == null) return null;
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }

    public CategoryResponse toCategoryResponse(Advert advert) {
        if (advert.getCategory() == null) return null;
        return CategoryResponse.builder()
                .id(advert.getCategory().getId())
                .title(advert.getCategory().getTitle())
                .categoryPropertyKeys(advert.getCategory().getCategoryPropertyKeys().stream()
                        .map(propertyKey -> PropertyKeyResponse.builder()
                                .id(propertyKey.getId())
                                .name(propertyKey.getName())
                                .build())
                        .toList())
                .build();
    }


    public List<FavoriteResponse> getFavoriteResponses(Advert advert) {
        // Favori listesini kontrol et, null ise boş liste dön
        List<Favorite> favorites = advert.getFavorites();
        if (favorites == null) {
            return Collections.emptyList(); // Null ise boş liste dön
        }

        // FavoriteResponse tipinde bir liste oluşturun
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();

        // Favori listesini dönerek her bir favoriyi FavoriteResponse'a dönüştürün
        for (Favorite favorite : favorites) {
            FavoriteResponse response = new FavoriteResponse();
            response.setAdvertId(advert.getId()); // Favoriye ait ilan ID'si

            // Kullanıcının adını methodHelper üzerinden bulun
            User user = methodHelper.findUserById(favorite.getUser().getId());
            response.setUserId(user.getId()); // Kullanıcının ID'sini ayarlayın

            response.setId(favorite.getId()); // Favori ID'si
            favoriteResponses.add(response); // Listeye ekle
        }

        return favoriteResponses; // Listeyi döndür
    }


    public Advert advertRequestToUpdatedAdvert(AdvertRequestForUpdate advertRequestForUpdate, Advert foundAdvert){


        foundAdvert.setTitle(advertRequestForUpdate.getTitle());
        foundAdvert.setDesc(advertRequestForUpdate.getDesc());
        foundAdvert.setPrice(advertRequestForUpdate.getPrice());
        foundAdvert.setAdvertType(methodHelper.findAdvertTypeById(advertRequestForUpdate.getAdvert_type_id()));
        foundAdvert.setCountry(methodHelper.findCountryById(advertRequestForUpdate.getCountry_id()));
        foundAdvert.setCity(methodHelper.findCityById(advertRequestForUpdate.getCity_id()));
        foundAdvert.setDistrict(methodHelper.findDistrictById(advertRequestForUpdate.getDistrict_id()));
        foundAdvert.setCategory(methodHelper.findCategoryById(advertRequestForUpdate.getCategory_id()));  // Fetch Category by ID
        Category category = foundAdvert.getCategory();
        if (category == null || category.getCategoryPropertyKeys() == null || category.getCategoryPropertyKeys().isEmpty()) {
            throw new IllegalStateException("Category does not have CategoryPropertyKeys.");
        }

        // Convert CategoryPropertyKeys to a Map for fast lookup by ID
        Map<Long, CategoryPropertyKey> propertyKeyMap = category.getCategoryPropertyKeys().stream()
                .collect(Collectors.toMap(CategoryPropertyKey::getId, key -> key));

        // 2. Map the properties from AdvertRequest to CategoryPropertyValues
        List<CategoryPropertyValue> categoryPropertyValues = advertRequestForUpdate.getProperties()
                .stream()
                .map(property -> {
                    // Get the corresponding CategoryPropertyKey from the category
                    CategoryPropertyKey propertyKey = propertyKeyMap.get(property.getId());
                    //System.out.println("propertyKey: " + propertyKey);
                    if (propertyKey == null) {
                        throw new IllegalArgumentException("CategoryPropertyKey not found for the provided ID: " + property.getId());
                    }

                    // Ensure the property value is not null or empty
                    if (property.getValue() == null || property.getValue().isEmpty()) {
                        throw new IllegalArgumentException("Property value cannot be null or empty for key: " + propertyKey.getId());
                    }

                    // Create a CategoryPropertyValue object and associate it with the Advert and PropertyKey
                    return CategoryPropertyValue.builder()
                            .categoryPropertyKey(propertyKey)  // Associate the property key
                            .value(property.getValue())        // Set the value from the request
                            .advert(foundAdvert)                    // Associate the property value with the Advert
                            .build();
                })
                .collect(Collectors.toList());

        // 3. Set the created CategoryPropertyValues to the Advert entity
        foundAdvert.setCategoryPropertyValues(categoryPropertyValues);


        return foundAdvert;

    }

    @Transactional
    public AdvertResponseForList advertToAdvertResponseForList(Advert advert) {
        ImageResponse image = getImage(advert);
        CountryResponse countryResponse = toCountryResponse(advert.getCountry());
        CityResponse cityResponse = toCityResponse(advert.getCity());
        DistrictResponse districtResponse = toDistrictResponse(advert.getDistrict());
        List<FavoriteResponse> favoriteResponses = getFavoriteResponses(advert);

        return AdvertResponseForList.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .desc(advert.getDesc())
                .slug(advert.getSlug())
                .price(advert.getPrice())
                .advertStatus(advert.getAdvertStatus())
                .builtIn(advert.isBuiltIn())
                .isActive(advert.isActive())
                .viewCount(advert.getViewCount())
                .createdAt(advert.getCreatedAt())
                .updatedAt(advert.getUpdatedAt())
                .advertType(ResponseForAdvertType.builder()
                        .id(advert.getAdvertType().getId())
                        .title(advert.getAdvertType().getTitle())
                        .build())
                .country(countryResponse)
                .city(cityResponse)
                .district(districtResponse)
                .location(advert.getLocation())
                .category(toCategoryResponse(advert))
                .image(image)
                .favoriteCount(advert.getFavorites()!=null ? advert.getFavorites().size() : 0)
                .tourRequestCount(advert.getTourRequests() != null ? advert.getTourRequests().size() : 0)
                .build();
    }

    private ImageResponse getImage(Advert advert){

        List<ImageResponse> imageList = imageService.getImagesByAdvertId(advert.getId());

        ImageResponse image = imageList.stream().filter(ImageResponse::isFeatured).findFirst().orElse(null);

        if(image == null){
            image = imageList.stream().findFirst().orElse(null);
        }
        return image;
    }
}
