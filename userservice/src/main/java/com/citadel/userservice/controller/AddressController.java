package com.citadel.userservice.controller;

import com.citadel.userservice.DTO.AddressRequestDTO;
import com.citadel.userservice.DTO.AddressResponseDTO;
import com.citadel.userservice.model.Address;
import com.citadel.userservice.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
