package com.team01.realestate.payload.request.business;


import com.team01.realestate.entity.enums.AdvertStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAdvertStatusRequest {
    @NotNull
    private String status;
    private String rejectMessage;
}
