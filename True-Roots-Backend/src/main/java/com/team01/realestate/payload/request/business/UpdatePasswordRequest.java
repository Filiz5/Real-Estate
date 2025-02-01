package com.team01.realestate.payload.request.business;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message="Please Provide Old Password")
    private String oldPassword;

    @NotBlank(message="Please Provide New Password")
    private String newPassword;

}