package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.concretes.user.User;
import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adverts")

@Builder(toBuilder = true)

public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "description", length = 300)
    private String desc;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AdvertStatus advertStatus = AdvertStatus.PENDING;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean builtIn = false;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int viewCount = 0;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String location;  // Store Google embed code or map URL

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "advert_type_id", nullable = false)
    @JsonIgnore
    private AdvertType advertType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnore
    private Country country;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    private City city;

    @ManyToOne(optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    @JsonIgnore
    private District district;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoryPropertyValue> categoryPropertyValues;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Log> logs;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TourRequest> tourRequests;

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
