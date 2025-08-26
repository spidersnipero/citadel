package com.citadel.userservice.repository;

import com.citadel.userservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<Address, UUID> {
    List<Address> findByUserEmail(String email);
}
