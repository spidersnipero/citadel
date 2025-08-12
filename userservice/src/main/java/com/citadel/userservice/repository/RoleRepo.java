package com.citadel.userservice.repository;

import com.citadel.userservice.model.Role;
import com.citadel.userservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(RoleName roleName);
}
