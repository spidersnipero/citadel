package com.citadel.cartservice.service;

import com.citadel.cartservice.DTO.AddItemRequestDTO;
import com.citadel.cartservice.DTO.CartResponseDTO;
import com.citadel.cartservice.DTO.ProductResponseDTO;
import com.citadel.cartservice.DTO.UpdateItemQuantityDTO;
import com.citadel.cartservice.model.Cart;
import com.citadel.cartservice.model.CartItem;
import com.citadel.cartservice.proxy.ProductProxy;
import com.citadel.cartservice.repo.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductProxy productProxy;

    public void addCartItem(AddItemRequestDTO itemDTO, Authentication authentication){

        ProductResponseDTO productItem = productProxy.getProductById(itemDTO.getProductId());

        if(productItem==null) {
            throw new RuntimeException("⚠️ Product Service unavailable or Product id not found: " + itemDTO.getProductId());
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProductId(productItem.getId());
        newCartItem.setName(productItem.getName());
        newCartItem.setSku(productItem.getSku());
        newCartItem.setPrice(productItem.getPrice());
        newCartItem.setImageUrl(!productItem.getImageUrls().isEmpty() ?productItem.getImageUrls().get(0):null);

        newCartItem.setQuantity(itemDTO.getQuantity());

        if(authentication.getName()==null){
            throw new RuntimeException("⚠️ User not authenticated");
        }

       Cart userCart = cartRepo.findById(authentication.getName()).orElse(new Cart(authentication.getName()));
       userCart.addItem(newCartItem);

       cartRepo.save(userCart);
    }

    public CartResponseDTO getCart(Authentication authentication){
        if(authentication.getName()==null){
            throw new RuntimeException("⚠️ User not authenticated");
        }
        if(!cartRepo.existsById(authentication.getName())){
            Cart emptyCart = new Cart(authentication.getName());
            Cart savedCart = cartRepo.save(emptyCart);
            return convertToDTO(savedCart);
        }
        Cart userCart = cartRepo.findById(authentication.getName()).get();
        return convertToDTO(userCart);
    }

    public void updateCartItemQuantity(UUID productId,UpdateItemQuantityDTO itemQuantityDTO,Authentication authentication){
        if(authentication.getName()==null){
            throw new RuntimeException("⚠️ User not authenticated");
        }
        Cart userCart = cartRepo.findById(authentication.getName()).orElseThrow(()-> new RuntimeException("⚠️ User not authenticated"));
        CartItem item = userCart.getCartItems().stream().filter(cartItem -> cartItem.getProductId().equals(productId)).
                findFirst().
                orElseThrow(()-> new RuntimeException("⚠️ Product with product id : "+productId+" not found in cart"));
        if(itemQuantityDTO.getQuantity()<1){
            userCart.getCartItems().remove(item);
        }
        else{
            item.setQuantity(itemQuantityDTO.getQuantity());
        }
        cartRepo.save(userCart);
    }

    public void deleteCartItem(UUID productId,Authentication authentication){
        if(authentication.getName()==null){
            throw new RuntimeException("⚠️ User not authenticated");
        }
        Cart userCart = cartRepo.findById(authentication.getName()).orElseThrow(()-> new RuntimeException("⚠️ User not authenticated"));
        CartItem item = userCart.getCartItems().stream().filter(cartItem -> cartItem.getProductId().equals(productId)).
                findFirst().
                orElse(null);
        if(item!=null) {
            userCart.getCartItems().remove(item);
            cartRepo.save(userCart);
        }
    }

    public void deleteCart(Authentication authentication){
        if(authentication.getName()==null){
            throw new RuntimeException("⚠️ User not authenticated");
        }
        Cart userCart = cartRepo.findById(authentication.getName()).orElseThrow(()-> new RuntimeException("⚠️ User not authenticated"));
        cartRepo.delete(userCart);
    }

    private CartResponseDTO convertToDTO(Cart cart){
        return new CartResponseDTO(cart.getUserEmailId(),cart.getCartItems());
    }
}
