package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByIdAndFeatured(Long id, boolean b);

    Optional<Image> findByAdvertIdAndFeatured(Long id, boolean b);

    boolean existsByAdvertIdAndFeatured(Long id, boolean b);

    List<Image> findAllByAdvert_Id(Long advertId);
    // This method will work if 'advertId' is a field in the Image entity
}