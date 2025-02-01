package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryRequest {

    @NotBlank(message = "Country name cannot be blank")
    @Size(max = 30, message = "Country name must be less than 30 characters")
    private String name;
}
