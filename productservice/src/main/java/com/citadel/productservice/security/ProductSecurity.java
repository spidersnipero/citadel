package com.citadel.productservice.security;

import com.citadel.productservice.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("productSecurity")
public class ProductSecurity {

    @Autowired
    private ProductRepo productRepo;

    public boolean isOwner(Authentication authentication, UUID productId){
        String currentUserEmail = authentication.getName();
        return productRepo.findById(productId).map(product -> product.getCreatorEmail().equals(authentication.getName())).orElse(false);
    }

}
