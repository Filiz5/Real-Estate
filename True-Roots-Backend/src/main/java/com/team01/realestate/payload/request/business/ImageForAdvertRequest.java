package com.team01.realestate.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageForAdvertRequest {

    private String name;
    private String type;
    private boolean featured;
    private String data;
}
