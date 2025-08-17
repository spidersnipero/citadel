package com.citadel.cartservice.model;



import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


public class CartItem implements Serializable {

    private UUID productId;
    private String sku;
    private String name;
    private BigDecimal price;

    @Min(value = 1,message = "Quantity can not be less that 1. ")
    private Integer quantity;
    private String imageUrl;

    public CartItem() {
    }

    public CartItem(UUID productId, String sku, String name, BigDecimal price, Integer quantity, String imageUrl) {
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem other = (CartItem) o;
        return productId != null && productId.equals(other.getProductId());
    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
