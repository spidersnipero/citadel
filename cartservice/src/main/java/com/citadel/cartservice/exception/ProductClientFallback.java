package com.citadel.cartservice.exception;

import com.citadel.cartservice.DTO.ProductResponseDTO;
import com.citadel.cartservice.proxy.ProductProxy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductClientFallback implements ProductProxy {

    @Override
    public ProductResponseDTO getProductById(UUID productId) {
        // Log and return a safe default object
        System.err.println("⚠️ Product Service unavailable or Product id not found: " + productId);
        return null; // Or create a placeholder DTO
    }
}

