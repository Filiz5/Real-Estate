package com.team01.realestate.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.TourRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tour_requests")
@ToString(exclude = {"advert", "ownerUser", "guestUser"})
@Builder(toBuilder = true)

public class TourRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tour_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tourDate;

    @Column(name = "tour_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tourTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TourRequestStatus tourRequestStatus = TourRequestStatus.PENDING;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "advert_id", nullable = false)
    @JsonIgnore
    private Advert advert;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    @JsonIgnore
    private User ownerUser;

    @ManyToOne
    @JoinColumn(name = "guest_user_id", nullable = false)
    @JsonIgnore
    private User guestUser;

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




