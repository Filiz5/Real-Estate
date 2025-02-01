package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.payload.response.business.CategoryResponse;
import com.team01.realestate.payload.response.business.PropertyKeyResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MapCategory {

    public CategoryResponse mapCategoryToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .icon(category.getIcon())
                .builtIn(category.isBuilt_in())
                .seq(category.getSeq())
                .slug(category.getSlug())
                .isActive(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .categoryPropertyKeys(category.getCategoryPropertyKeys().stream().map(t -> PropertyKeyResponse.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .build()).collect(Collectors.toList()))
                .build();

    }

}
