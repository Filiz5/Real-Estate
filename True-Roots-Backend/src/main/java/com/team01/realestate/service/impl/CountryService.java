package com.team01.realestate.service.impl;

import com.team01.realestate.payload.response.business.CountryResponse;

import java.util.List;

public interface CountryService {
    List<CountryResponse> getAllCountry();
   // CountryResponse createCountry(CountryRequest countryRequest);
}
