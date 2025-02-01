package com.team01.realestate.entity.concretes.business;
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
@Table(name = "advert_types")
@Builder(toBuilder = true)
public class AdvertType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private boolean builtIn;

    @OneToMany(mappedBy = "advertType")
    private List<Advert> adverts;
}
