package com.team01.realestate.service.business;

import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CountryMapper;
import com.team01.realestate.payload.response.business.CountryResponse;
import com.team01.realestate.repository.business.CountryRepository;
import com.team01.realestate.service.impl.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryResponse> getAllCountry() {

        List<CountryResponse> countries = countryRepository.findAll().stream()
                .map(countryMapper::toResponse)
                .collect(Collectors.toList());
        if (countries.isEmpty()) {
            throw new ResourceNotFoundException("Country not found");
        }

        return countries;
    }
}
