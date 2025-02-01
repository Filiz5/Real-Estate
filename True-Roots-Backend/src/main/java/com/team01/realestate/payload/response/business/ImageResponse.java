package com.team01.realestate.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    private Long id;
    private String name;
    private String type;
    private boolean featured;
    private Long advert_id;
    private String data;


    }

