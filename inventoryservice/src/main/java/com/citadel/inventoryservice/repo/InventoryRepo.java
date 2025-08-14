package com.citadel.inventoryservice.repo;

import com.citadel.inventoryservice.model.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends MongoRepository<InventoryItem,String> {

    Optional<InventoryItem> findBySku(String sku);
}
