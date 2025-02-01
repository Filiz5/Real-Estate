package com.team01.realestate.controller.business;

import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.DistrictResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.impl.DistrictService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    /**
     * get all districts
     * @return : http://localhost:8080/districts + get
     */

    @GetMapping
    public ResponseMessage<List<DistrictResponse>> getAllDistrict() {
        List<DistrictResponse> allDistrict = districtService.getAllDistrict();
        return ResponseMessage.<List<DistrictResponse>>builder()
                .message(SuccessMessages.DISTRICTS_FOUND)
                .object(allDistrict)
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
