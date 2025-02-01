package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_property_keys")
@ToString(exclude = "category")
@Builder(toBuilder = true)

public class CategoryPropertyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 80)
    private String name;

    @NotNull
    private boolean builtIn = false;  // Default value set to false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "categoryPropertyKey", cascade = CascadeType.REMOVE)
    private List<CategoryPropertyValue> categoryPropertyValues;



    //Unit tests için yazıldı....
    public CategoryPropertyKey(long l, String testPropertyKey) {
        this.id = l;
        this.name = testPropertyKey;
    }
}
