package com.team01.realestate.payload.request.business;

import lombok.Data;

@Data
public class CategoryRequest {
    private String title; // Kategori başlığı
    private String icon; // Kategori simgesi
    private boolean builtIn; // Kategori varsayılan mı
    private int seq; // Sıralama numarası
    private String slug; // Kategori URL'si
    private boolean isActive; // Kategori aktif mi

}