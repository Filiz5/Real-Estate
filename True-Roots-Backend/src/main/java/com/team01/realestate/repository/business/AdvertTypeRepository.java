package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.AdvertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertTypeRepository extends JpaRepository<AdvertType, Long> {
    void deleteByBuiltInFalse();

//
//    @Query("SELECT at FROM AdvertType at")
//    Page<AdvertType> getAllAdvertTypes(Pageable pageable);
}