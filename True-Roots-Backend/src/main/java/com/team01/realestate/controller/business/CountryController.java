package com.team01.realestate.controller.business;

import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.CountryResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.impl.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    /**
     *method: get
     * @return http://localhost:8080/countries
     */

    @GetMapping
    public ResponseMessage<List<CountryResponse>> getAllCountries() {
        List<CountryResponse> allCountry = countryService.getAllCountry();
        return ResponseMessage.<List<CountryResponse>>builder()
                .message(SuccessMessages.COUNTRIES_FOUND)
                .object(allCountry)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
