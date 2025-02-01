package com.team01.realestate.payload.response.business;

import com.team01.realestate.entity.concretes.business.AdvertType;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdvertTypeResponse {
    private Long id;
    private String title;
    private boolean builtIn;

//    public static AdvertTypeResponse fromEntity(AdvertType advertType) {
//        AdvertTypeResponse dto = new AdvertTypeResponse();
//        dto.setId(advertType.getId());
//        dto.setTitle(advertType.getTitle());
//        return dto;
//    }
    public static AdvertTypeResponse fromEntity(AdvertType advertType) {
        return new AdvertTypeResponse(advertType.getId(), advertType.getTitle(), advertType.isBuiltIn());
    }

    public static AdvertType toEntity(AdvertTypeRequest request) {
        AdvertType advertType = new AdvertType();
        advertType.setTitle(request.getTitle());
        advertType.setBuiltIn(request.isBuiltIn());
        return advertType;
    }
}


