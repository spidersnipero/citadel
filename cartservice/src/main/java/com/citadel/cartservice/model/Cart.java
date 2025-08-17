package com.citadel.cartservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



@RedisHash("carts")
public class Cart implements Serializable {
    @Id
    private String userEmailId;


    private Set<CartItem> cartItems = new HashSet<>();


    public Cart() {
    }

    public Cart(String userEmailId) {
        this.userEmailId = userEmailId;
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

    public void addItem(CartItem item) {
        this.cartItems.add(item);
    }
    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
    }
}
