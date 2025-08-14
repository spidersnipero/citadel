package com.citadel.inventoryservice.service;

import com.citadel.inventoryservice.DTO.InventoryDTO;
import com.citadel.inventoryservice.DTO.ProductResponseDTO;
import com.citadel.inventoryservice.model.InventoryItem;
import com.citadel.inventoryservice.proxy.ProductProxy;
import com.citadel.inventoryservice.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ProductProxy proxy;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @inventorySecurity.isOwner(authentication, #sku)")
    public void addInventory(InventoryDTO inventory,String sku){
        if(inventoryRepo.existsById(sku)){
            throw new RuntimeException("Item with SKU "+sku+" already exist.");
        }
        InventoryItem item = new InventoryItem(sku, inventory.getQuantity());
        item.setCreatedAt(new Date().toInstant());
        inventoryRepo.save(item);
        return;
    }

    public InventoryDTO getInventory(String sku){
        InventoryItem inventory = inventoryRepo.findBySku(sku).orElseThrow(()->new RuntimeException("Item with SKU "+sku+" does not exist."));
        return convertToResponseDTO(inventory);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @inventorySecurity.isOwner(authentication, #sku)")
    public ProductResponseDTO getProductBySku(String sku){
        return proxy.getProductBySku(sku);
    }


    private InventoryDTO convertToResponseDTO(InventoryItem inventory){
        InventoryDTO inventoryDTO = new InventoryDTO(inventory.getSku(), inventory.getQuantity());
        return inventoryDTO;
    }
}
