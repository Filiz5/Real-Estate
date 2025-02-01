package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Image;

import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.ImageMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.request.business.ImageUploadRequest;
import com.team01.realestate.payload.response.business.ImageResponse;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.ImageRepository;
import com.team01.realestate.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AdvertRepository advertRepository;
    private final ImageMapper imageMapper;


    @Override
    @Transactional
    public List<Image> getImageById(List<Long> ids) {
        List<Image> imageList = new ArrayList<>();
        for (Long id : ids) {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(ErrorMessages.IMAGES_NOT_FOUND_BY_ID, id)));
            imageList.add(image);
        }
        return imageList;
    }

    @Transactional
    public List<ImageResponse> getImagesByAdvertId(Long advertId) {

        List<Image> images = imageRepository.findAllByAdvert_Id(advertId);

        // Listeyi kontrol et
        if (images.isEmpty()) {
            System.out.println("No images found for advertId: " + advertId);
        }

        // LOB veri yüklemeden önce kontrol edin
        try {
            return images.stream()
                    .map(ImageMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error processing images: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ImageResponse> uploadImages(Long advertId, ImageUploadRequest request) {

        Advert advert = advertRepository.findById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE + advertId));

        List<Image> existingImages = advert.getImages();

        // Map existing images to a Set for faster duplicate checking
        Set<String> existingImageSignatures = existingImages.stream()
                .map(image -> image.getName() + image.getType() + Arrays.toString(image.getData()))
                .collect(Collectors.toSet());

        // Decode and build new images
        List<Image> newImages = request.getImages().stream()
                .map(imageRequest -> Image.builder()
                        .name(imageRequest.getName())
                        .type(imageRequest.getType())
                        .data(decodeBase64Image(imageRequest.getData()))
                        .featured(imageRequest.isFeatured())
                        .advert(advert) // Set the relationship
                        .build())
                .filter(image -> !existingImageSignatures.contains(
                        image.getName() + image.getType() + Arrays.toString(image.getData())))
                .toList();

        // Add new images to the Advert's image list
        advert.getImages().addAll(newImages);

        // Save the Advert (cascades to Images)
        advertRepository.save(advert);
        return newImages.stream()
                .map(ImageMapper::toResponse)
                .toList();
    }

    private byte[] decodeBase64Image(String base64Image) {
        if (base64Image.startsWith("data:image")) {
            // Strip the prefix (e.g., "data:image/png;base64,")
            String base64Data = base64Image.split(",")[1];
            return Base64.getDecoder().decode(base64Data);
        }
        return Base64.getDecoder().decode(base64Image); // Directly decode if no prefix
    }


    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.IMAGES_NOT_FOUND_BY_ID + imageId));
        imageRepository.delete(image);

    }

    @Override
    @Transactional
    public ImageResponse updateImageFeaturedStatus(Long imageId) {

        // Fetch the image by id
        Image newFeaturedImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.IMAGES_NOT_FOUND_BY_ID + imageId));

        // Fetch the current featured image for the same advert, if any
        Optional<Image> currentFeaturedImage = imageRepository.findByAdvertIdAndFeatured(newFeaturedImage.getAdvert().getId(), true);

        // If there's already a featured image for the same advert, set it to false
        if (currentFeaturedImage.isPresent() && !currentFeaturedImage.get().getId().equals(newFeaturedImage.getId())) {
            Image current = currentFeaturedImage.get();
            current.setFeatured(false);
            imageRepository.save(current);
        }

        // Set the new image as featured
        newFeaturedImage.setFeatured(true);
        imageRepository.save(newFeaturedImage);

        // Verify that at least one image is featured for the advert
        boolean isAnyFeatured = imageRepository.existsByAdvertIdAndFeatured(newFeaturedImage.getAdvert().getId(), true);
        if (!isAnyFeatured) {
            throw new IllegalStateException("Each advert must have at least one featured image.");
        }

        // Map and return the updated image as a response
        return ImageMapper.toResponse(newFeaturedImage);
    }

}
