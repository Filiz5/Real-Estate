package com.team01.realestate.controller.business;

import com.team01.realestate.entity.concretes.business.Image;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.ImageUpdateRequest;
import com.team01.realestate.payload.request.business.ImageUploadRequest;
import com.team01.realestate.payload.response.business.ImageResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.ImageRepository;
import com.team01.realestate.service.impl.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImagesController {

    private final ImageService imageService;

    /**
     * I-01
     * {get} /{id}
     * Image by id
    http://localhost:8080/images?imageIdList=1,2,3     
    * user: anonymous
     */
    @GetMapping
    public ResponseMessage<List<Image>> getImagesByImageIdList(@RequestParam(value = "imageIdList") List<Long> imageIdList) {
        List<Image> imageList = imageService.getImageById(imageIdList);
        return ResponseMessage.<List<Image>>builder()
                .object(imageList)
                .message(SuccessMessages.IMAGES_FOUND_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /***
     *
     * I-02
     * http://localhost:8080/images/:advertId
     *  {put} /{id}
     * It will upload image(s) of a product
     * Payload
     * (path & body)
     * advert_id: not null
     * Images
     * :[ File, File, File …
     * Response
     * ( Arr[ image_id] )
     * Requirements
     * -----
     */
    @PutMapping("/{advertId}/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'CUSTOMER')")
    public ResponseMessage<List<ImageResponse>>
    uploadImages(@PathVariable Long advertId, @Valid @RequestBody ImageUploadRequest request) {
       //System.out.println(request.getImages());
        List<ImageResponse> imageResponse = imageService.uploadImages(advertId, request);

        return ResponseMessage.<List<ImageResponse>>builder()
                .object(imageResponse)
                .message(SuccessMessages.IMAGES_SAVED_SUCCESSFULLY)
                .httpStatus(HttpStatus.CREATED).build();
    }


    /***
     * I-03
     * delete image
     * {delete} /{id}
     * /images/5,56,22,56,7
     * HTTP/localhost:8080/images/:imageId + 200 OK
     * PAYLOAD : IMAGE_ID NOT NULL
     * users: admin, manager, customer
     */

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER','CUSTOMER')")
    public ResponseMessage<String> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseMessage.<String>builder()
                .message(SuccessMessages.IMAGES_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    /***
     * I-04
     * put image
     * {put} /{id}
     * /images/:imageId
     * HTTP/localhost:8080/images/:imageId + 200 OK
     * it will set the featured field of an image
     * PAYLOAD : IMAGE_ID NOT NULL
     * RESPONSE :Image
     * REQUIREMENTS: An advert can have only one featured image
     */

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER','CUSTOMER')")
    @PutMapping("/{imageId}")
    public ResponseMessage<ImageResponse> updateImageFeaturedStatus(@PathVariable Long imageId) {

        ImageResponse imageResponse = imageService.updateImageFeaturedStatus(imageId);

        return ResponseMessage.<ImageResponse>builder()
                .object(imageResponse)
                .message(SuccessMessages.IMAGES_UPDATED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();

    }

}
