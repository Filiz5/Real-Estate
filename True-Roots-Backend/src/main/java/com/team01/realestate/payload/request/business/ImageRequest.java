package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequest {

    @NotBlank(message = "Image name cannot be blank")
    @Size(max = 255, message = "Image name must be less than 255 characters")
    private String name;

    @NotNull(message = "Featured field cannot be null")
    private boolean featured;

    @NotNull(message = "Advert id cannot be null")
    private Long advertId;
}

