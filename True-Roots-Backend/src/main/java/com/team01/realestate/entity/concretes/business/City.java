package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")

@Builder(toBuilder = true)

public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;  // Max length 30, not null

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country; // Her şehir bir ülkeye aittir

    @OneToMany(mappedBy = "city", orphanRemoval = true)
    private List<District> districts; // Bir şehir birden fazla bölge içerir

    @OneToMany(mappedBy = "city")
    private List<Advert> adverts; // Bir şehir birden fazla ilan içerir
}
