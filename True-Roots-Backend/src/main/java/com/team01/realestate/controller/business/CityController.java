package com.team01.realestate.controller.business;

import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.CityResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.impl.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    /**
     * method: GET
     * @Returns: http://localhost:8080/cities
     */

    @GetMapping
    public ResponseMessage<List<CityResponse>> getAllCities() {
        List<CityResponse> allCity = cityService.getAllCity();
        return ResponseMessage.<List<CityResponse>>builder()
                .message(SuccessMessages.CITIES_FOUND)
                .object(allCity)
                .httpStatus(HttpStatus.OK)
                .build();
    }




}
