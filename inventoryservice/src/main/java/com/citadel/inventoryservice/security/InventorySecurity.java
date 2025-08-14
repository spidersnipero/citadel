package com.citadel.inventoryservice.security;

import com.citadel.inventoryservice.DTO.ProductResponseDTO;
import com.citadel.inventoryservice.proxy.ProductProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component("inventorySecurity")
public class InventorySecurity {

    @Autowired
    private ProductProxy proxy;

    public boolean isOwner(Authentication authentication, String  sku){
        String currentUserEmail = authentication.getName();
        ProductResponseDTO productItem = proxy.getProductBySku(sku);
        return productItem.getCreatorEmail().equals(authentication.getName());
    }

}
