package com.team01.realestate.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAdvertResponse {

    private String category;
    private int amount;
}
