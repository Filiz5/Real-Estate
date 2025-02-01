package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team01.realestate.entity.concretes.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;  // Zaman dilimi gerekmiyorsa LocalDateTime

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites")
@Builder(toBuilder = true)
@ToString(exclude = {"user", "advert"})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "advert_id", nullable = false)
    @JsonIgnore
    private Advert advert;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
