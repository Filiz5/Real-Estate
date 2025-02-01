package com.team01.realestate.service.impl;

import com.team01.realestate.entity.concretes.business.Image;
import com.team01.realestate.payload.request.business.ImageUpdateRequest;
import com.team01.realestate.payload.request.business.ImageUploadRequest;
import com.team01.realestate.payload.response.business.ImageResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageService {

    List<Image> getImageById(List<Long> ids);

    List<ImageResponse> uploadImages(Long advertId, ImageUploadRequest request);

    void deleteImage(Long imageId);

    ImageResponse updateImageFeaturedStatus(Long imageId);

}
