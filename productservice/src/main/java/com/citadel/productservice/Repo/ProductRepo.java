package com.citadel.productservice.Repo;

import com.citadel.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepo  extends JpaRepository<Product, UUID> {


    @Query(
            value = "SELECT * FROM products WHERE search_vector @@ to_tsquery('english', :query)",
            countQuery = "SELECT count(*) FROM products WHERE search_vector @@ to_tsquery('english', :query)",
            nativeQuery = true
    )
    Page<Product> searchByText(@Param("query") String query, Pageable pageable);

    List<Product> findBySku(String sku);
}