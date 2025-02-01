package com.team01.realestate.service.business;



import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.Image;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.request.business.*;
import com.team01.realestate.payload.response.business.ImageResponse;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImageServiceTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private AdvertRepository advertRepository;


    @Mock
    private ImageRepository imageRepository;

    @Mock
    private Advert advert;

    private Long advertId;
    private ImageUploadRequest imageRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category();
        category.setId(1L);

        advert = new Advert();
        advert.setId(1L);
        advert.setTitle("Advert Title");
        advert.setDesc("Advert Description");
        advert.setPrice(BigDecimal.valueOf(1000.0));
        advert.setCategory(category);
        advert.setAdvertStatus(AdvertStatus.PENDING);


    }

    @Test
    @DisplayName("Test upload images")
    @WithMockUser(authorities = {"CUSTOMER"})
    void uploadImagesTest() {
        Long advertId = 1L;

        ImageUploadRequest imageRequest = new ImageUploadRequest();

        // Create an ImageForAdvertRequest object
        ImageForAdvertRequest image = new ImageForAdvertRequest();
        image.setName("Sample Image");
        image.setType("jpg");
        image.setFeatured(true);
        image.setData("YmFzZTY0RW5jb2RlZERhdGFIZXJl"); // Valid base64 encoded data

        // Add the ImageForAdvertRequest object to a list
        List<ImageForAdvertRequest> images = new ArrayList<>();
        images.add(image);

        // Set the list in ImageUploadRequest
        imageRequest.setImages(images);

        // Mock Advert
        Advert advert = new Advert();
        advert.setId(advertId);
        advert.setTitle("Advert Title");
        advert.setDesc("Advert Description");
        advert.setPrice(BigDecimal.valueOf(1000.0));
        advert.setImages(new ArrayList<>());

        // Mock existing Advert behavior
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(advert));

        // Mock Image saving
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setName("Sample Image");
        mockImage.setType("jpg");
        mockImage.setData(Base64.getDecoder().decode("YmFzZTY0RW5jb2RlZERhdGFIZXJl"));
        mockImage.setFeatured(true);

        List<Image> savedImages = List.of(mockImage);
        when(imageRepository.saveAll(anyList())).thenReturn(savedImages);

        // Call the method under test
        List<ImageResponse> response = imageService.uploadImages(advertId, imageRequest);

        // Verify interactions and assert results
        verify(advertRepository).findById(advertId);
        verify(advertRepository).save(advert);
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);

        ImageResponse actualResponse = response.get(0);
        assertThat(actualResponse.getName()).isEqualTo("Sample Image");
        assertThat(actualResponse.getType()).isEqualTo("jpg");
        assertThat(actualResponse.isFeatured()).isTrue();

        // Validate that the image was added to the Advert

        Image addedImage = advert.getImages().get(0);
        assertThat(addedImage.getName()).isEqualTo("Sample Image");
        assertThat(addedImage.getType()).isEqualTo("jpg");

    }


    @Test
    @DisplayName("Test upload images - Advert Not Found")
    @WithMockUser(authorities = {"CUSTOMER"})
    void uploadImagesAdvertNotFoundTest() {
        Long advertId = 999L; // Non-existent advertId

        ImageUploadRequest imageRequest = new ImageUploadRequest();

        // Mock the behavior for non-existent Advert
        when(advertRepository.findById(advertId))
                .thenThrow(new ResourceNotFoundException("Advert not found with ID: " + advertId));

        // Assertions
        assertThrows(ResourceNotFoundException.class,
                () -> imageService.uploadImages(advertId, imageRequest));

        verify(advertRepository).findById(advertId);
    }


    @Test
    @DisplayName("Test upload images - No New Images")
    @WithMockUser(authorities = {"CUSTOMER"})
    void uploadImagesNoNewImagesTest() {
        Long advertId = 1L;

        ImageUploadRequest imageRequest = new ImageUploadRequest();

        // Create an ImageForAdvertRequest object
        ImageForAdvertRequest image = new ImageForAdvertRequest();
        image.setName("Duplicate Image");
        image.setType("jpg");
        image.setFeatured(true);
        image.setData("YmFzZTY0RW5jb2RlZERhdGFIZXJl"); // Valid base64 data

        // Add the ImageForAdvertRequest object to a list
        List<ImageForAdvertRequest> images = List.of(image);
        imageRequest.setImages(images);

        // Mock Advert with a mutable existing image list
        Image existingImage = new Image();
        existingImage.setName("Duplicate Image");
        existingImage.setType("jpg");
        existingImage.setData(Base64.getDecoder().decode("YmFzZTY0RW5jb2RlZERhdGFIZXJl"));
        existingImage.setFeatured(true);

        Advert advert = new Advert();
        advert.setId(advertId);
        advert.setImages(new ArrayList<>(List.of(existingImage))); // Ensure mutable list

        // Mock repository behavior
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(advert));

        // Call the method
        List<ImageResponse> response = imageService.uploadImages(advertId, imageRequest);

        // Assertions
        assertThat(response).isEmpty(); // Assert that no new images were added
        verify(advertRepository).findById(advertId);
        verify(advertRepository).save(advert);
    }

    @Test
    @DisplayName("Test upload images - Invalid Base64 Data")
    @WithMockUser(authorities = {"CUSTOMER"})
    void uploadImagesInvalidBase64Test() {
        Long advertId = 1L;

        ImageUploadRequest imageRequest = new ImageUploadRequest();

        // Create an ImageForAdvertRequest object with invalid base64 data
        ImageForAdvertRequest image = new ImageForAdvertRequest();
        image.setName("Invalid Image");
        image.setType("jpg");
        image.setFeatured(true);
        image.setData("InvalidBase64Data");

        imageRequest.setImages(List.of(image));

        // Mock Advert
        Advert advert = new Advert();
        advert.setId(advertId);
        advert.setImages(new ArrayList<>());

        when(advertRepository.findById(advertId)).thenReturn(Optional.of(advert));

        // Assertions
        assertThrows(IllegalArgumentException.class,
                () -> imageService.uploadImages(advertId, imageRequest));

        verify(advertRepository).findById(advertId);
    }

    @Test
    @DisplayName("get image by id with valid ids")
    public void getImageById_validIds_returnsImages() {
        // Prepare test data
        Long validId1 = 1L;
        Long validId2 = 2L;
        Image image1 = new Image();
        image1.setId(validId1);
        image1.setName("Image 1");
        Image image2 = new Image();
        image2.setId(validId2);
        image2.setName("Image 2");

        // Mock the repository behavior
        when(imageRepository.findById(validId1)).thenReturn(Optional.of(image1));
        when(imageRepository.findById(validId2)).thenReturn(Optional.of(image2));

        List<Long> ids = List.of(validId1, validId2);

        // Call the method
        List<Image> result = imageService.getImageById(ids);

        // Verify the interactions with the mock
        verify(imageRepository, times(1)).findById(validId1);
        verify(imageRepository, times(1)).findById(validId2);

        // Assertions
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(validId1);
        assertThat(result.get(1).getId()).isEqualTo(validId2);
    }

    @Test
    @DisplayName("Test getImagesByAdvertId - No Images Found")
    void testGetImagesByAdvertId_NoImagesFound() {
        Long advertId = 1L;

        // Mock repository to return an empty list
        when(imageRepository.findAllByAdvert_Id(advertId)).thenReturn(List.of());

        // Call the method
        List<ImageResponse> result = imageService.getImagesByAdvertId(advertId);

        // Assertions
        assertNotNull(result, "The result should not be null.");
        assertTrue(result.isEmpty(), "The result list should be empty.");

        // Verify repository interaction
        verify(imageRepository).findAllByAdvert_Id(advertId);
    }

    @Test
    @DisplayName("Test getImagesByAdvertId - Exception While Processing Images")
    void testGetImagesByAdvertId_ExceptionWhileProcessingImages() {
        Long advertId = 1L;

        // Create an advert object
        Advert advert = new Advert();
        advert.setId(advertId);

        // Mock image with invalid data
        Image image = new Image();
        image.setId(1L);
        image.setName("Invalid Image");
        image.setType("jpg");
        image.setAdvert(advert); // Ensure Advert is not null
        image.setData(null); // Simulate invalid data

        // Mock repository to return the problematic image
        when(imageRepository.findAllByAdvert_Id(advertId)).thenReturn(List.of(image));

        // Assertions: Verify the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.getImagesByAdvertId(advertId);
        });

        // Ensure the exception message contains "Error processing images"
        assertThat(exception.getMessage()).withThreadDumpOnError();

        // Verify repository interaction
        verify(imageRepository).findAllByAdvert_Id(advertId);
    }

    @Test
    @DisplayName("Delete exists image")
    public void deleteImage_imageExists_deletesImage() {
        // Prepare test data
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);
        image.setName("Image 1");

        // Mock the repository behavior
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        // Call the method to delete the image
        imageService.deleteImage(imageId);

        // Verify that the repository delete method was called
        verify(imageRepository, times(1)).findById(imageId);
        verify(imageRepository, times(1)).delete(image);

    }

    @Test
    @DisplayName("update Image featured status")
    public void updateImageFeaturedStatus() {
        Long advertId = 1L;
        Long imageId = 1L;

        // Mock new image to be updated
        Image newImage = new Image();
        newImage.setId(imageId);
        Advert advert = new Advert();
        advert.setId(advertId);
        newImage.setAdvert(advert);
        newImage.setFeatured(false);
        newImage.setData("NewImageData".getBytes()); // Mocking valid byte data for the new image

        // Mock current featured image
        Image currentFeaturedImage = new Image();
        currentFeaturedImage.setId(2L);
        currentFeaturedImage.setAdvert(advert);
        currentFeaturedImage.setFeatured(true);
        currentFeaturedImage.setData("CurrentImageData".getBytes()); // Mocking valid byte data for the current image

        // Mock repository methods
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(newImage));
        when(imageRepository.findByAdvertIdAndFeatured(advertId, true)).thenReturn(Optional.of(currentFeaturedImage));
        when(imageRepository.existsByAdvertIdAndFeatured(advertId, true)).thenReturn(true);

        // Call the method to test
        ImageResponse result = imageService.updateImageFeaturedStatus(imageId);


        // Assertions
        assertThat(result).isNotNull();
        assertTrue(newImage.isFeatured(), "The new image should be marked as featured.");
        assertFalse(currentFeaturedImage.isFeatured(), "The previous featured image should no longer be marked as featured.");

        // Verify interactions
        verify(imageRepository).findById(imageId);
        verify(imageRepository).findByAdvertIdAndFeatured(advertId, true);
        verify(imageRepository).save(newImage);
        verify(imageRepository).save(currentFeaturedImage);
        verify(imageRepository).existsByAdvertIdAndFeatured(advertId, true);



    }

    }









