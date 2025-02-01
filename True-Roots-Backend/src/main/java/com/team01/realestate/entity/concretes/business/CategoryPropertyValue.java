package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_property_values")
@ToString(exclude = {"categoryPropertyKey", "advert"})
@Builder(toBuilder = true)

public class CategoryPropertyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String value;

    @ManyToOne
    @JoinColumn(name = "category_property_key_id")
    @JsonProperty("categoryPropertyKey")
    private CategoryPropertyKey categoryPropertyKey;

    @ManyToOne
    @JoinColumn(name = "advert_id")
    @JsonIgnore
    private Advert advert;
}
