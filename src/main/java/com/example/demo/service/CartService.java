package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cart;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CartRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;

    public String addToCart(Cart cart) {
        Cart existCart = cartRepository.findByProductId(cart.getProductId());
        if (existCart != null){
            existCart.setQuantity(existCart.getQuantity() + cart.getQuantity());
            cartRepository.save(existCart);
            return "Update quantity success!";
        } else {
            cart.setUserId(userService.getMyinfo().getId());
            cartRepository.save(cart);
            return "Add item to cart success!";
        }
        // System.out.println(carts);
    }

    public List<Cart> getMyCart(){
        List<Cart> cartItems = cartRepository.findByUserId(userService.getMyinfo().getId());
        return cartItems;
    }

    public String delItem(Integer cartId){
        if(!cartRepository.findById(cartId).isPresent()){
            throw new AppException(ErrorCode.CART_NOT_EXISTED);
        }

        cartRepository.deleteById(cartId);
        return "Delete cart successfully";
    }

}
