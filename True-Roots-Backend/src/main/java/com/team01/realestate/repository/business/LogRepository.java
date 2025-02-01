package com.team01.realestate.repository.business;

import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.business.TourRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {


        @Query("SELECT l FROM Log l " +
                "JOIN FETCH l.user u " +
                "JOIN FETCH l.advert a " +
                "WHERE u.id = :userId " +
                "ORDER BY l.createdAt DESC")
        List<Log> findLogsByUserId(@Param("userId") Long userId);


}
