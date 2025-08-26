package com.citadel.cartservice.controller;

import com.citadel.cartservice.DTO.AddItemRequestDTO;
import com.citadel.cartservice.DTO.CartResponseDTO;
import com.citadel.cartservice.DTO.ProductResponseDTO;
import com.citadel.cartservice.DTO.UpdateItemQuantityDTO;
import com.citadel.cartservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Void> addCartItem(@Valid @RequestBody AddItemRequestDTO addItemRequestDTO, Authentication authentication){
        cartService.addCartItem(addItemRequestDTO,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Authentication authentication){
        CartResponseDTO cartResponseDTO = cartService.getCart(authentication);
        return new ResponseEntity<>(cartResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable UUID productId,@Valid @RequestBody UpdateItemQuantityDTO updateItemQuantityDTO, Authentication authentication){
        cartService.updateCartItemQuantity(productId,updateItemQuantityDTO,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID productId,Authentication authentication){
        cartService.deleteCartItem(productId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteCart(Authentication authentication){
        cartService.deleteCart(authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }






}
