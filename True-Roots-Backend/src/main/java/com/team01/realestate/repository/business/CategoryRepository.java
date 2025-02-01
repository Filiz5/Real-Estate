package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug); // Slug'a göre kategori bulma

    @Query("SELECT COUNT(a) > 0 FROM Advert a WHERE a.category.id = :categoryId")
    boolean existsAdvertisementsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT k FROM CategoryPropertyKey k WHERE k.category.id = :categoryId")
    List<CategoryPropertyKey> findPropertyKeysByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND (:q IS NULL OR c.title LIKE %:q%)")
    Page<Category> findActiveCategoriesByQuery(@Param("q") String query, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE (:q IS NULL OR c.title LIKE %:q%)")
    Page<Category> findAllCategoriesByQuery(@Param("q") String query, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.built_in = false")
    void deleteByBuiltInFalse();

    @Modifying
    @Transactional
    @Query("SELECT c FROM Category c WHERE c.built_in = false")
    List<Category> findByBuiltInFalse(); // built_in olmayan kategorileri bulma

    Page<Category> findAll(@Nullable Specification<Category> specification, Pageable pageable);
}