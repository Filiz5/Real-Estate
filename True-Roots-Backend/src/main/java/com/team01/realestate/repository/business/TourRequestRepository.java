package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.TourRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TourRequestRepository extends JpaRepository<TourRequest, Long> {


    @Query("SELECT tr FROM TourRequest tr " +
            "JOIN FETCH tr.advert a " +
            "WHERE (tr.guestUser.email = :email) " +
            "AND (:query IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY tr.tourDate ASC")
    Page<TourRequest> findTourRequestsByAuthUser(@Param("email") String email,
                                                  @Param("query") String query,
                                                  Pageable pageable);


    @Query("SELECT tr FROM TourRequest tr " +
            "WHERE (:query IS NULL OR " +
            "LOWER(tr.advert.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.advert.user.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.advert.user.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.guestUser.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.guestUser.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.advert.city.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.advert.district.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tr.advert.country.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY tr.createdAt DESC")
    Page<TourRequest> findAllByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT t FROM TourRequest t " +
            "WHERE t.createdAt >= :date1 " +
            "AND t.createdAt <= :date2 " +
            "AND t.tourRequestStatus = :status")
    List<TourRequest> findByFilters(
            @Param("date1") LocalDate date1,
            @Param("date2") LocalDate date2,
            @Param("status") TourRequestStatus status
    );

    @Query("SELECT COUNT(t) > 0 FROM TourRequest t " +
            "WHERE t.guestUser.id = :guestUserId " +
            "AND t.tourDate = :tourDate " +
            "AND ((t.tourTime >= :tourTime AND t.tourTime < :tourTimePlusOneHour) " +
            "OR (t.tourTime < :tourTime AND t.tourTime >= :tourTimeMinusOneHour))")
    boolean existsByGuestUserAndTourDateWithinHour(Long guestUserId, LocalDate tourDate, LocalTime tourTime, LocalTime tourTimePlusOneHour, LocalTime tourTimeMinusOneHour);


    @Query("SELECT tr FROM TourRequest tr " +
            "JOIN FETCH tr.advert a " +
            "WHERE (tr.ownerUser.id = :id OR tr.guestUser.id = :id) " +
            "ORDER BY tr.tourDate ASC")
    List<TourRequest> findTourRequestsByUserId(@Param("id") Long id);
}

