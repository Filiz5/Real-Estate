package com.team01.realestate.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportResponse {

    private int numberOfCategory;
    private int numberOfAdvert;
    private int numberOfAdvertType;
    private int numberOfTourRequest;
    private int numberOfCustomer;

}
