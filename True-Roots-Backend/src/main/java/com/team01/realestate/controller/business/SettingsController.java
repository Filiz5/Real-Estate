package com.team01.realestate.controller.business;

import com.team01.realestate.payload.request.user.LoginRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.service.business.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;
    // http://localhost:8080/settings/db-reset + POST
    @PostMapping("/db-reset") //X01
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<String> resetDatabase() {
        return settingsService.resetDatabase();
    }



}
