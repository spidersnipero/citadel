package com.citadel.userservice.controller;

import com.citadel.userservice.DTO.AddressRequestDTO;
import com.citadel.userservice.DTO.AddressResponseDTO;
import com.citadel.userservice.model.Address;
import com.citadel.userservice.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/me/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Void> addAddressToUser(@Valid @RequestBody AddressRequestDTO requestDTO, Authentication authentication){
        addressService.addAddressToUser(requestDTO,authentication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddressOfUser(Authentication authentication){
        List<AddressResponseDTO> userAddresses = addressService.getAllAddressOfUser(authentication);
        return new ResponseEntity<>(userAddresses,HttpStatus.OK);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Void>  updateAddress(@PathVariable UUID addressId,@Valid @RequestBody AddressRequestDTO requestDTO){
        addressService.updateAddressOfUser(addressId,requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/make-default/{addressId}")
    public ResponseEntity<Void>  makeAddressDefault(@PathVariable UUID addressId,Authentication authentication){
        addressService.makeAddressDefault(addressId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void>  deleteAddress(@PathVariable UUID addressId){
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
