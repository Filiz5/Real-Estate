package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DistrictRequest {

    @NotNull(message = "District name cannot be null")
    @Size(min = 2, max = 30, message = "District name must be between 2 and 30 characters")
    private String name;

    @NotNull(message = "City ID cannot be null")
    private Long cityId;
}
