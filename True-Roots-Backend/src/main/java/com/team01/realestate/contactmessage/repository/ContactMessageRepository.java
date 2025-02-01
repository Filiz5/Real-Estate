package com.team01.realestate.contactmessage.repository;

import com.team01.realestate.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    @Query("SELECT c FROM ContactMessage c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.message) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ContactMessage> findByQuery(String query, Pageable pageable);

}

