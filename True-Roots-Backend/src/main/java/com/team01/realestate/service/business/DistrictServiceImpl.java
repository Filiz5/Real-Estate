package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.DistrictMapper;
import com.team01.realestate.payload.response.business.DistrictResponse;
import com.team01.realestate.repository.business.DistrictRepository;
import com.team01.realestate.service.impl.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    @Override
    public List<DistrictResponse> getAllDistrict() {

        List<District> allDistricts = districtRepository.findAll();
        if (allDistricts.isEmpty()) {
            throw new ResourceNotFoundException("No district found");
        }
        return allDistricts.stream()
                .map(districtMapper::toResponse)
                .collect(Collectors.toList());
    }
}
