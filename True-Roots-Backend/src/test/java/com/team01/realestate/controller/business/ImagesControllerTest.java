package com.team01.realestate.controller.business;


import com.team01.realestate.base.TestDataReaderForImages;
import com.team01.realestate.entity.concretes.business.Image;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.ImageUploadRequest;
import com.team01.realestate.payload.response.business.ImageResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.impl.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ImagesControllerTest {

    Logger logger = LoggerFactory.getLogger(ImagesControllerTest.class);

    @InjectMocks
    private ImagesController imagesController;

    @Mock
    private ImageService imageService;


    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test upload images")
    @WithMockUser(authorities = {"CUSTOMER"})
    void uploadImagesTest() {
        Long advertId = 1L;
        ImageUploadRequest imageRequest = new ImageUploadRequest();
        imageRequest.setImages(TestDataReaderForImages.getAdvertRequestPayload().getImages());


        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(1L);
        imageResponse.setName("Sample Image");
        imageResponse.setType("jpg");
        imageResponse.setData("base64EncodedDataHere");
        imageResponse.setFeatured(true);
        imageResponse.setAdvert_id(1L);

        List<ImageResponse> imageResponseList = new ArrayList<>();
        imageResponseList.add(imageResponse);

        ResponseMessage<List<ImageResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.CREATED);
        expectedResponse.setObject(imageResponseList);
        expectedResponse.setMessage("Images uploaded successfully");

        when(imageService.uploadImages(advertId, imageRequest)).thenReturn(imageResponseList);

        ResponseMessage<List<ImageResponse>> actualResponse = imagesController.uploadImages(advertId, imageRequest);

        assertThat(actualResponse.getHttpStatus()).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
        assertThat(actualResponse.getObject().get(0).getData()).isEqualTo(expectedResponse.getObject().get(0).getData());


        // Logging for confirmation
        logger.info("Images uploaded: {}", actualResponse);
    }


    @Test
    @DisplayName("Test get images by ID list")
    void getImagesByImageIdListTest() {
        // Arrange
        List<Long> imageIdList = Arrays.asList(1L, 2L);

        // Prepare mock response
        Image image1 = new Image();
        image1.setId(1L);
        image1.setName("Sample Image 1");
        image1.setType("jpg");
        image1.setData("base64EncodedData1".getBytes());
        image1.setFeatured(false);


        Image image2 = new Image();
        image2.setId(2L);
        image2.setName("Sample Image 2");
        image2.setType("png");
        image2.setData("base64EncodedData2".getBytes());
        image2.setFeatured(true);

        List<Image> imageList = Arrays.asList(image1, image2);

        // Expected Response
        ResponseMessage<List<Image>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setObject(imageList);
        expectedResponse.setMessage(SuccessMessages.IMAGES_FOUND_SUCCESSFULLY);

        // Mock the service
        when(imageService.getImageById(imageIdList)).thenReturn(imageList);

        // Act
        ResponseMessage<List<Image>> actualResponse =  imagesController.getImagesByImageIdList(imageIdList);

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedResponse.getHttpStatus());
        assertThat(actualResponse.getObject()).isEqualTo(expectedResponse.getObject());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());

        logger.info("Retrieved images by ID list : {}", actualResponse);

    }

    @Test
    @DisplayName("Test delete image by ID")
    @WithMockUser(authorities = {"ADMIN"})
    void deleteImageTest() {

        //First uploading image
        Long advertId = 1L;
        ImageUploadRequest imageRequest = new ImageUploadRequest();
        imageRequest.setImages(TestDataReaderForImages.getAdvertRequestPayload().getImages());


        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(1L);
        imageResponse.setName("Sample Image");
        imageResponse.setType("jpg");
        imageResponse.setData("base64EncodedDataHere");
        imageResponse.setFeatured(true);
        imageResponse.setAdvert_id(1L);

        List<ImageResponse> imageResponseList = new ArrayList<>();
        imageResponseList.add(imageResponse);

        ResponseMessage<List<ImageResponse>> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.CREATED);
        expectedResponse.setObject(imageResponseList);
        expectedResponse.setMessage(SuccessMessages.IMAGES_SAVED_SUCCESSFULLY);

        when(imageService.uploadImages(advertId, imageRequest)).thenReturn(imageResponseList);


        //then testing for delete

        doNothing().when(imageService).deleteImage(imageResponse.getId());

        ResponseMessage<String> expectedDeleteResponse = new ResponseMessage<>();
        expectedDeleteResponse.setMessage(SuccessMessages.IMAGES_DELETED_SUCCESSFULLY);
        expectedDeleteResponse.setHttpStatus(HttpStatus.OK);

        // Simulate deleting the image
        ResponseMessage<String> actualDeleteResponse = imagesController.deleteImage(imageResponse.getId());

        // Assert delete response
        assertThat(actualDeleteResponse).isNotNull();
        assertThat(actualDeleteResponse.getHttpStatus()).isEqualTo(expectedDeleteResponse.getHttpStatus());
        assertThat(actualDeleteResponse.getMessage()).isEqualTo(expectedDeleteResponse.getMessage());

        logger.info("Deleted image by ID: {}", actualDeleteResponse);



    }

    @Test
    @DisplayName("Test update image featured status")
    @WithMockUser(authorities = {"ADMIN"})
    void updateImageFeaturedStatusTest() {
        Long imageId = 1L;

        // 1. Prepare image response with updated featured status
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(imageId);
        imageResponse.setName("Sample Image");
        imageResponse.setType("jpg");
        imageResponse.setData("base64EncodedDataHere");
        imageResponse.setFeatured(false);  // Assume featured status is updated
        imageResponse.setAdvert_id(1L);

        // 2. Expected Response Message for update operation
        ResponseMessage<ImageResponse> expectedUpdateResponse = new ResponseMessage<>();
        expectedUpdateResponse.setHttpStatus(HttpStatus.OK);
        expectedUpdateResponse.setObject(imageResponse);
        expectedUpdateResponse.setMessage(SuccessMessages.IMAGES_UPDATED_SUCCESSFULLY);

        // Mock the imageService's update method
        when(imageService.updateImageFeaturedStatus(imageId)).thenReturn(imageResponse);

        // 3. Call the updateImageFeaturedStatus method
        ResponseMessage<ImageResponse> actualUpdateResponse = imagesController.updateImageFeaturedStatus(imageId);

        // Assert that the update response is not null and the values match the expected response
        assertThat(actualUpdateResponse).isNotNull();
        assertThat(actualUpdateResponse.getHttpStatus()).isEqualTo(expectedUpdateResponse.getHttpStatus());
        assertThat(actualUpdateResponse.getObject()).isEqualTo(expectedUpdateResponse.getObject());
        assertThat(actualUpdateResponse.getMessage()).isEqualTo(expectedUpdateResponse.getMessage());

        // Verify the updateImageFeaturedStatus method was called once
        verify(imageService, times(1)).updateImageFeaturedStatus(imageId);

        logger.info(" updated Image Featured Status: {}", actualUpdateResponse);
    }



}














