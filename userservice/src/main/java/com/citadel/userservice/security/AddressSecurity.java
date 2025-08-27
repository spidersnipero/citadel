package com.citadel.userservice.security;


import com.citadel.userservice.model.Address;
import com.citadel.userservice.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("addressSecurity")
public class AddressSecurity {

    @Autowired
    private AddressRepo addressRepo;

    public boolean isOwner(Authentication authentication, UUID addressId){
        String currentUserEmail = authentication.getName();
        Optional<Address> addressOptional = addressRepo.findById(addressId);
        return addressOptional.filter(address -> addressRepo.findByUserEmail(currentUserEmail).contains(address)).isPresent();
    }

}
