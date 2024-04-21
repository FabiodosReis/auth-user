package com.application.security.domain.repository;

import com.application.security.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(String name);

    @Query(value =
            "SELECT " +
                    "r.id, " +
                    "r.name " +
                    "FROM user_roles ur " +
                    "JOIN role r ON r.id = ur.roleId " +
                    "WHERE ur.userId = :userId", nativeQuery = true)
    List<Role> findByUserId(@Param("userId") String userId);
}
