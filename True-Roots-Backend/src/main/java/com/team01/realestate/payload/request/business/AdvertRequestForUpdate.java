package com.team01.realestate.payload.request.business;


import com.team01.realestate.entity.concretes.business.*;
import com.team01.realestate.entity.enums.AdvertStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdvertRequestForUpdate {


    @NotNull(message = "Please enter title")
    @Size(min = 5, max = 150, message = "Title should be at least 5 (max 150) chars")
    private String title;

    @Nullable
    @Size(max = 300, message = "Desc should be at most 300 chars")
    private String desc;

    @NotNull(message = "Please enter price")
    private BigDecimal price;

    private AdvertStatus advertStatus;

    @NotNull(message = "Please enter advert type number")
    private Long advert_type_id; //id

    @NotNull (message = "Please enter country code")
    private Long country_id; //id

    @NotNull(message = "Please enter city code")
    private Long city_id; //id

    @NotNull(message = "Please enter district code")
    private Long district_id;//id

    @NotNull (message = "Please enter catgory code")
    private Long category_id; //id

    private List<CategoryPropertyValue> properties;

    private String location;

    @NotNull
    private boolean isActive;

}
