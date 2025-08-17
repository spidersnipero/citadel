package com.citadel.cartservice.DTO;

import com.citadel.cartservice.model.CartItem;

import java.util.HashSet;
import java.util.Set;

public class CartResponseDTO {

    private String userEmailId;

    private Set<CartItem> cartItems = new HashSet<>();

    public CartResponseDTO(String userEmailId, Set<CartItem> cartItems) {
        this.userEmailId = userEmailId;
        this.cartItems = cartItems;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
