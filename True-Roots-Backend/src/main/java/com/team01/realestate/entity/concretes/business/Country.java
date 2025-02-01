package com.team01.realestate.entity.concretes.business;

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
@Table(name = "countries")

@Builder(toBuilder = true)

public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @OneToMany(mappedBy = "country", orphanRemoval = true)
    private List<City> cities; // Bir ülke birden fazla şehir içerir

    @OneToMany(mappedBy = "country")
    private List<Advert> adverts; // Bir ülke birden fazla şehir içerir
}


