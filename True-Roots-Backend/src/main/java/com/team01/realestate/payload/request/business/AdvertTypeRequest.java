package com.team01.realestate.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertTypeRequest {
    private String title;
    private boolean builtIn;
}