package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Cart;
import java.util.List;


public interface CartRepository  extends JpaRepository<Cart, Integer>{
    List<Cart> findByUserId(Integer userId);
    Cart findByProductId(Integer productId);
};