package com.team01.realestate.payload.response.business;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    //private List<Category> categories; // Kategori listesi

    private Long id; // Kategori ID'si
    private String title; // Kategori başlığı
    private String icon; // Kategori simgesi
    private boolean builtIn; // Kategori varsayılan mı
    private int seq; // Sıralama numarası
    private String slug; // Kategori URL'si
    private boolean isActive; // Kategori aktif mi
    private List<PropertyKeyResponse> categoryPropertyKeys; // Kategori özellik anahtarları
    private LocalDateTime createdAt; // Oluşturulma tarihi
    private LocalDateTime updatedAt; // Güncellenme tarihi
}

