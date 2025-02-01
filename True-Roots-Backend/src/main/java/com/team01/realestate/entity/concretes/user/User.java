package com.team01.realestate.entity.concretes.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.business.TourRequest;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")

@Builder(toBuilder = true)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;  // Not null, min: 2, max: 30

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;  // Not null, min: 2, max: 30

    @Column(name = "email", nullable = false, unique = true, length = 80)
    private String email;  // Not null, unique, min: 10, max: 80

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;  // Not null, no null constraint

    @Column(name = "password_hash", nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;  // Not null, hashed password

    @Column(name = "reset_password_code", length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String resetPasswordCode;  // Nullable, hash value

    @Column(name = "built_in", nullable = false)
    private Boolean builtIn = false;  // Cannot be deleted or updated, default 0

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createdAt;  // Not null, should use the correct date-time format

    @Column(name = "update_at")
    private LocalDateTime updatedAt;  // Nullable

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Favorite> favorites;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Log> logs;

    @OneToMany(mappedBy = "ownerUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<TourRequest> ownerTourRequests;

    @OneToMany(mappedBy = "guestUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<TourRequest> guestTourRequests;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Advert> adverts;
}
