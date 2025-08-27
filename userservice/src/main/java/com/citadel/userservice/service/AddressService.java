package com.citadel.userservice.service;

import com.citadel.userservice.DTO.AddressRequestDTO;
import com.citadel.userservice.DTO.AddressResponseDTO;
import com.citadel.userservice.model.Address;
import com.citadel.userservice.repository.AddressRepo;
import com.citadel.userservice.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UsersRepo usersRepo;

    public void addAddressToUser(AddressRequestDTO addressRequestDTO,Authentication authentication){
        Address newAddress = new Address();

        newAddress.setUser(usersRepo.findByEmail(authentication.getName()).get());

        newAddress.setHouseNo(addressRequestDTO.getHouseNo());
        newAddress.setStreet(addressRequestDTO.getStreet());
        newAddress.setCity(addressRequestDTO.getCity());
        newAddress.setCountry(addressRequestDTO.getCountry());
        newAddress.setPostalCode(addressRequestDTO.getPostalCode());

        addressRepo.save(newAddress);
        return;
    }

    public List<AddressResponseDTO> getAllAddressOfUser(Authentication authentication){
        List<Address> userAddresses = addressRepo.findByUserEmail(authentication.getName());
        return userAddresses.stream().map(this::convertToDTO).toList();
    }

    @PreAuthorize("@addressSecurity.isOwner(authentication, #addressId)")
    public void updateAddressOfUser(UUID addressId,AddressRequestDTO requestDTO){
        Optional<Address> addressOptional  = addressRepo.findById(addressId);
        if(addressOptional.isEmpty()){
            throw new  RuntimeException("No Such address exist with id : "+addressId);
        }
        Address address = addressOptional.get();
        address.setHouseNo(requestDTO.getHouseNo());
        address.setStreet(requestDTO.getStreet());
        address.setCity(requestDTO.getCity());
        address.setPostalCode(requestDTO.getPostalCode());
        address.setCountry(requestDTO.getCountry());

        addressRepo.save(address);
    }

    public void makeAddressDefault(UUID addressId,Authentication authentication){
        List<Address> userAddresses = addressRepo.findByUserEmail(authentication.getName());
        for (Address address:userAddresses){
            address.setDefault(addressId.equals(address.getId()));
        }
        addressRepo.saveAll(userAddresses);
    }

    @PreAuthorize("@addressSecurity.isOwner(authentication, #addressId)")
    public  void deleteAddress(UUID addressId){
        addressRepo.deleteById(addressId);
    }

    private AddressResponseDTO convertToDTO(Address address){
        return new AddressResponseDTO(address.getId(),address.getHouseNo(),
                address.getStreet(),
                address.getCity(),
                address.getCountry(),
                address.getPostalCode(),
                address.getDefault()
                );
    }


}
