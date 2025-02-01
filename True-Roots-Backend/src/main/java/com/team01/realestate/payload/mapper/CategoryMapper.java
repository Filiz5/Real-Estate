package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.payload.response.business.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "built_in", target = "builtIn") // built_in alanını builtIn olarak eşleştir
    @Mapping(source = "active", target = "isActive") // built_in alanını builtIn olarak eşleştir
    CategoryResponse toCategoryResponse(Category category);


}