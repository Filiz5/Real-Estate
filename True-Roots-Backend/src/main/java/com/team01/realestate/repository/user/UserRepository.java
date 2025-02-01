package com.team01.realestate.repository.user;

import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    User findByEmail(@Param("email") String username);

    @Query("SELECT u FROM User u  WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);

    User findByResetPasswordCode(String resetPasswordCode);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleType = ?1")
    long countByRoleType(RoleType roleType);

    /**
     * Retrieves users based on the query parameter (first name, last name, email, or phone) and pagination.
     *
     * @param query    the search term (first name, last name, email, or phone) for filtering users.
     * @param pageable pagination and sorting information.
     * @return a paginated list of users matching the query.
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> findUsersByQuery(String query, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailOptional(@Param("email")String email);

    void deleteByBuiltInFalse();

    @Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.roleType = :roleType")
    Set<User> findUsersQueryForReports(@Param("roleType") RoleType roleType);

}
