package com.team01.realestate.repository.user;

import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.roleType = ?1")
    Role findByEnumRoleEquals(RoleType roleType);
}

