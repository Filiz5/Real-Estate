package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "districts")

@Builder(toBuilder = true)

public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonIgnore
    private City city; // Her ilçe bir şehre aittir

    @OneToMany(mappedBy = "district")
    private List<Advert> adverts; // Bir ilçe birden fazla ilan içerir
}


