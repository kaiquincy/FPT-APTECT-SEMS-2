package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Cart;
import com.example.demo.service.CartService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "/api/cart")
@CrossOrigin(origins = "http://localhost:3000") //Cho phep front end su dung API
@Slf4j
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping
    public ApiResponse<List<Cart>> getMyCart() {
        ApiResponse<List<Cart>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(cartService.getMyCart());

        return apiResponse;
    }

    @PostMapping
    public ApiResponse<String> addToCart(@RequestBody Cart item) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setResult(cartService.addToCart(item));
        return apiResponse;
    }

    @DeleteMapping("/{cartId}")
    public ApiResponse<String> deleteCart(@PathVariable int cartId){
        ApiResponse<String> apiResponse = new ApiResponse<>();

        cartService.delItem(cartId);

        apiResponse.setResult("Cart delete successfully!");
        return apiResponse;
    }
    
    
}
