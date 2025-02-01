package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.LogType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
@ToString(exclude = {"advert", "user"})
@Builder(toBuilder = true)

public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)  // Enum türü olarak saklanacak
    @Column(nullable = false)
    private LogType log;  // Enum tipinde log değeri

    private String description;

    @ManyToOne
    @JoinColumn(name = "advert_id", nullable = false)
    @JsonIgnore
    private Advert advert;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;  // Not null, zorunlu alan
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
