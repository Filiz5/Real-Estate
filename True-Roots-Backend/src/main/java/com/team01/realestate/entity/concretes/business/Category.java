package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")

@Builder(toBuilder = true)

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // auto number (Primary Key)

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(nullable = false)
    private boolean built_in;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int seq = 0;  // Default 0, defines display order

    @Column(nullable = false, length = 200)
    private String slug;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;  // not null, DateTime

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;  // Nullable DateTime

    @OneToMany(mappedBy = "category")
    private List<Advert> adverts;

    @OneToMany(mappedBy = "category", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CategoryPropertyKey> categoryPropertyKeys;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
