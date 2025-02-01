package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    List<Favorite> findByUserId(Long id);

    // Corrected method name
   // Page<Favorite> findByUserId(Long id, Pageable pageable);

        @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND (:query IS NULL OR f.advert.title LIKE %:query%)")
        Page<Favorite> findByUserId(@Param("userId") Long userId, Pageable pageable, @Param("query") String query);



    Favorite findByAdvertId(Long advertId);

    void deleteAllByUserId(Long id);

    Optional<Favorite> findByUserAndAdvert(User user, Advert advert);

    boolean existsByUserAndAdvert(User user, Advert advert);
}

