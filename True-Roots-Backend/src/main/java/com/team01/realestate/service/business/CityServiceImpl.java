package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CityMapper;
import com.team01.realestate.payload.response.business.CityResponse;
import com.team01.realestate.repository.business.CityRepository;
import com.team01.realestate.service.impl.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityResponse> getAllCity() {
        List<City> allCities = cityRepository.findAll();
        if (allCities.isEmpty()) {
            throw new ResourceNotFoundException("No city found");
        }


        return allCities.stream().map(cityMapper::toResponse).collect(Collectors.toList());
    }
}
