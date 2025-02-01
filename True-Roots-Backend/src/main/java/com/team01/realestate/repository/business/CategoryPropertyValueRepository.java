package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.CategoryPropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryPropertyValueRepository extends JpaRepository<CategoryPropertyValue, Long> {
    void deleteByCategoryPropertyKeyId(Long propertyKeyId);
}
