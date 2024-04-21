package com.application.security.domain.repository;

import com.application.security.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("FROM User u WHERE u.email = :email")
    UserDetails findByLogin(String email);
    @Query(value = "SELECT u.* FROM user u WHERE u.email = :email AND u.id <> :id LIMIT 1", nativeQuery = true)
    Optional<User> findByEmailAndId(String email, String id);

   }
