package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {
}
