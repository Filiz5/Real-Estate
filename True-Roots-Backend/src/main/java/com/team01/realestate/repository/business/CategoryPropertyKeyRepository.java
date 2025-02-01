package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryPropertyKeyRepository extends JpaRepository <CategoryPropertyKey, Long> {

    // İsteğe bağlı güncelleme metodu
    Optional<CategoryPropertyKey> findById(Long id);


    List<CategoryPropertyKey> findByCategoryId(Long categoryId);
    // Delete all CategoryPropertyKey entities by Category ID where builtIn is false
    @Modifying
    @Transactional
    @Query("DELETE FROM CategoryPropertyKey k WHERE k.category.id = :categoryId AND k.builtIn = false")
    void deleteByCategoryIdAndBuiltInFalse(Long categoryId);

    // Find all CategoryPropertyKey entities by Category ID where builtIn is false
    List<CategoryPropertyKey> findByCategoryIdAndBuiltInFalse(Long categoryId);
}
