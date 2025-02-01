package com.team01.realestate.controller.user;

import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.UpdatePasswordRequest;
import com.team01.realestate.payload.request.user.*;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.service.user.AuthenticationService;
import com.team01.realestate.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // http://localhost:8080/login + POST
    @PostMapping("/login") //F01
    public ResponseMessage<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @PostMapping("/google-login")
    public ResponseMessage<LoginResponse> googleLogin(@RequestBody @Valid GoogleLoginRequest loginRequest) {
        System.out.println("Google Login Request: " + loginRequest);
        return authenticationService.googleAuthenticateUser(loginRequest);
    }

    // http://localhost:8080/register + POST
    @PostMapping("/register") //F02
    public ResponseMessage<UserResponse> register(@RequestBody @Valid UserRequest userRequestForRegister) {
        return authenticationService.register(userRequestForRegister);
    }

    // http://localhost:8080/users/auth + PATCH
    @PatchMapping("/users/auth") // F07 -> Update Password
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER','CUSTOMER')")
    public ResponseMessage<String>updateAuthenticatedUsersPassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                                         HttpServletRequest request){
        authenticationService.updateAuthenticatedUsersPassword(updatePasswordRequest, request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE; // paylod.messages
        return ResponseMessage.<String>builder()
                .message(response)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // http://localhost:8080/forgot-password + POST
    @PostMapping("/forgot-password") //F03
    public ResponseMessage<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.generateResetCodeAndSendEmail(forgotPasswordRequest.getEmail());
        return ResponseMessage.<Void>builder().build();
    }

    // http://localhost:8080/reset-password+ POST
    @PostMapping("/reset-password") //F04
    public ResponseMessage<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseMessage.<Void>builder().build();
    }
}
