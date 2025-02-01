package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {

    @NotBlank(message = "City name cannot be blank")
    @Size(max = 30, message = "City name must be less than 30 characters")
    private String name;

    @NotNull(message = "Country ID cannot be null")
    private Long countryId;
}
