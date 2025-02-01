package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
    public interface AdvertRepository extends JpaRepository<Advert, Long> {
        boolean existsBySlug(String slug);

    @Query("SELECT a FROM Advert a WHERE a.slug = :slug")
    Optional<Advert> findBySlug(@Param("slug") String slug);

    @Query("SELECT a FROM Advert a " +
            "JOIN a.city c " +
            "WHERE (:q IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(a.desc) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
            "AND a.category.isActive = true " +
            "AND (:advertTypeId IS NULL OR a.advertType.id = :advertTypeId) " +
            "AND (:priceStart IS NULL OR a.price >= :priceStart) " +
            "AND (:priceEnd IS NULL OR a.price <= :priceEnd) " +
            "AND (:status IS NULL OR a.advertStatus = :status)")
    Page<Advert> findAdvertsWithParameters(
            @Param("q") String q,
            @Param("categoryId") Long categoryId,
            @Param("advertTypeId") Long advertTypeId,
            @Param("priceStart") BigDecimal priceStart,
            @Param("priceEnd") BigDecimal priceEnd,
            @Param("status") AdvertStatus status,
            Pageable pageable
    );


    @Query("SELECT a FROM Advert a " +
            "WHERE (:q IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(a.desc) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
            "AND (:advertTypeId IS NULL OR a.advertType.id = :advertTypeId) " +
            "AND (:priceStart IS NULL OR a.price >= :priceStart) " +
            "AND (:priceEnd IS NULL OR a.price <= :priceEnd) " +
            "AND (:status IS NULL OR a.advertStatus = :status)")
    Page<Advert> findAdvertsWithParametersForAdmin(
            @Param("q") String q,
            @Param("categoryId") Long categoryId,
            @Param("advertTypeId") Long advertTypeId,
            @Param("priceStart") BigDecimal priceStart,
            @Param("priceEnd") BigDecimal priceEnd,
            @Param("status") AdvertStatus status,
            Pageable pageable
    );

    // Şehir bazında ilan sayısını döndüren özel bir sorgu
//        @Query("SELECT a.city.name AS city, COUNT(a) AS amount FROM Advert a GROUP BY a.city.name")
//        List<Object[]> getAdvertsGroupedByCity();

    @Query("SELECT a.city.name AS city, COUNT(a) AS amount " +
            "FROM Advert a " +
            "WHERE a.advertStatus = 'ACTIVATED' " +
            "GROUP BY a.city.name")
    List<Object[]> getAdvertsGroupedByCity();

//    @Query("SELECT a.category.title AS category, COUNT(a) AS amount FROM Advert a GROUP BY a.category.title")
//    List<Object[]> getAdvertsGroupedByCategory();

    @Query("SELECT a.category.title AS category, COUNT(a) AS amount " +
            "FROM Advert a " +
            "WHERE a.advertStatus = 'ACTIVATED' " +
            "GROUP BY a.category.title")
    List<Object[]> getAdvertsGroupedByCategory();


//    @Query(value = """
//    SELECT a.*\s
//    FROM adverts a\s
//    LEFT JOIN tour_requests t ON a.id = t.advert_id\s
//    GROUP BY a.id, a.view_count, a.title, a.description, a.slug, a.price, a.advert_status, a.built_in, a.is_active,\s
//             a.location, a.created_at, a.updated_at, a.user_id, a.advert_type_id, a.country_id, a.city_id, a.district_id,\s
//             a.category_id\s
//    ORDER BY (3 * COUNT(t.id) + a.view_count) DESC\s
//    LIMIT :limit
//   \s""", nativeQuery = true)
//    List<Advert> findPopularAdverts(@Param("limit") int limit);

    @Query(value = """
    SELECT a.*\s
    FROM adverts a\s
    LEFT JOIN tour_requests t ON a.id = t.advert_id\s
    WHERE a.advert_status = 'ACTIVATED'\s
    GROUP BY a.id, a.view_count, a.title, a.description, a.slug, a.price, a.advert_status, a.built_in, a.is_active,\s
             a.location, a.created_at, a.updated_at, a.user_id, a.advert_type_id, a.country_id, a.city_id, a.district_id,\s
             a.category_id\s
    ORDER BY (3 * COUNT(t.id) + a.view_count) DESC\s
    LIMIT :limit
   \s""", nativeQuery = true)
    List<Advert> findPopularAdverts(@Param("limit") int limit);


    // Corrected native query with plural table name and complete GROUP BY clause
    @Query(value = "SELECT a.* FROM adverts a " +
            "LEFT JOIN tour_requests t ON a.id = t.advert_id " +
            "WHERE a.advert_status = 'ACTIVATED'" +
            "GROUP BY a.id, a.view_count, a.title, a.description, a.slug, a.price, a.advert_status, " +
            "a.built_in, a.is_active, a.location, a.created_at, a.updated_at, a.user_id, " +
            "a.advert_type_id, a.country_id, a.city_id, a.district_id, a.category_id " +
            "ORDER BY COUNT(t.id) DESC LIMIT :limit", nativeQuery = true)
    List<Advert> findPopularAdvertsByTourRequestCount(@Param("limit") int limit);


    //Page<Advert> findByUser(User user, Pageable pageable);

    @Query("SELECT a FROM Advert a " +
            "WHERE a.user.id = :userId " +
            "AND (:q IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(a.desc) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Advert> findUserAdvertsWithParameters(
            @Param("userId") Long userId,
            @Param("q") String q,
            Pageable pageable
    );


    void deleteByBuiltInFalse();


    @Query("SELECT a FROM Advert a " +
            "WHERE a.createdAt >= :date1 " +
            "AND a.createdAt <= :date2 " +
            "AND a.category.id = :categoryId " +
            "AND a.advertType.id = :typeId " +
            "AND a.advertStatus = :status")
    List<Advert> findByFilters(
            @Param("date1") LocalDateTime date1,
            @Param("date2") LocalDateTime date2,
            @Param("categoryId") Long categoryId,
            @Param("typeId") Long typeId,
            @Param("status") AdvertStatus status
    );


}//queryde price kontrol edilecek
