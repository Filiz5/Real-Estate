package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
@ToString(exclude = "advert")
@Builder(toBuilder = true)

public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", columnDefinition = "oid")
    private byte[] data;

    @Column(nullable = false)  // Name, Not Null
    private String name;

    @Column
    private String type;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean featured = false;

    @ManyToOne
    @JoinColumn(name = "advert_id", nullable = false)
    @JsonIgnore
    private Advert advert;
}
