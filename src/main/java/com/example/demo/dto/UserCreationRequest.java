package com.example.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreationRequest {

    @NotNull(message = "USERNAME_NULL")
    @Size(min = 5, max = 15, message = "USERNAME_INVALID")
    private String username;
    @NotBlank(message = "EMAIL_BLANK")
    private String email;
    @NotBlank(message = "FULLNAME_BLANK")
    private String fullName;
    private String role;
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password_hash;
    private Double balance;


    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPassword_hash() {
        return password_hash;
    }
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    
}
