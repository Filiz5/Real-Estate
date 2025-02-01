package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateRequest {

    private Long id;

    @NotNull(message = "Featured field is required")
    private Boolean featured;
}
