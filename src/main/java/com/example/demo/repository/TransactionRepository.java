package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{
    List<Transaction> findAllByUserId(Integer userId);
    Optional<Transaction> findTopByUserIdOrderByTransactionDateDesc(Integer userId);
}