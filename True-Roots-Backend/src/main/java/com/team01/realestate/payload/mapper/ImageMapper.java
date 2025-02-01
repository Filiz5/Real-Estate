package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.Image;
import com.team01.realestate.payload.response.business.ImageResponse;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class ImageMapper {

    public static ImageResponse toResponse(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .name(image.getName())
                .type(image.getType())
                .featured(image.isFeatured())
                .advert_id(image.getAdvert().getId()) // Advert ID'yi ekliyoruz
                .data("data:"+ image.getType()+";base64,"+Base64.getEncoder().encodeToString(image.getData()))
                .build();
    }

    public static Image toEntity(ImageResponse imageResponse) {
        return Image.builder()
                .id(imageResponse.getId())
                .name(imageResponse.getName())
                .type(imageResponse.getType())
                .featured(imageResponse.isFeatured())
                .build();
    }

    public Image mapMultiPartFileToImage(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String contentType = tika.detect(file.getBytes());

        return Image.builder()
                .data(file.getBytes())
                .name(file.getOriginalFilename())
                .type(contentType)
                .featured(false)
                .build();
    }


}
