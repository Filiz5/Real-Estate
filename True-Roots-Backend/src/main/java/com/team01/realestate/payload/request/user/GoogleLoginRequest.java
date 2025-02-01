package com.team01.realestate.payload.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleLoginRequest {

    @NotNull(message = "email must not be empty")
    private String email;

    @NotNull(message = "First name must not be empty")
    private String name;

    @NotNull(message = "Last name must not be empty")
    private String lastName;

    private String idToken;
}
