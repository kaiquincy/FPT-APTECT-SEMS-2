package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "product_id")
    private Integer productId;
    
    private Integer quantity;


    public Cart() {
    }

    public Cart(Integer id, Integer userId, Integer productId, Integer quantity) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Cart id(Integer id) {
        setId(id);
        return this;
    }

    public Cart userId(Integer userId) {
        setUserId(userId);
        return this;
    }

    public Cart productId(Integer productId) {
        setProductId(productId);
        return this;
    }

    public Cart quantity(Integer quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cart)) {
            return false;
        }
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(userId, cart.userId) && Objects.equals(productId, cart.productId) && Objects.equals(quantity, cart.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", productId='" + getProductId() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }

}
