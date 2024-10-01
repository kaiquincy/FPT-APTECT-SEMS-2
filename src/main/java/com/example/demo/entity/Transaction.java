package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.example.demo.enums.TransactionType;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    private String status;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    private Integer productId;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
}
