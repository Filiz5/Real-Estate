package com.team01.realestate.service.impl;

import com.team01.realestate.payload.response.business.AdvertTypeResponse;

import java.util.List;

public interface AdvertTypeService {

    AdvertTypeResponse getAdvertTypeById(Long id);

    List<AdvertTypeResponse> getAllAdvertType();
}
