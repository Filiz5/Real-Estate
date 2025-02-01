package com.team01.realestate.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUploadRequest {

    List<ImageForAdvertRequest> images;
//    @NotEmpty(message = "At least one image file is required")
//    private List<MultipartFile> files;
}
