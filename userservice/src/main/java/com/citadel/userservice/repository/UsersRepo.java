package com.citadel.userservice.repository;

import com.citadel.userservice.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {

    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
}
