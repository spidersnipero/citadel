package com.citadel.cartservice.repo;

import com.citadel.cartservice.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CartRepo extends CrudRepository<Cart, String> {
}
