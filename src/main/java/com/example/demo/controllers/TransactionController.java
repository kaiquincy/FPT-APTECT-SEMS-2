package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Transaction;
import com.example.demo.service.TransactionService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(value = "/api/transaction")
@CrossOrigin(origins = "*") //Cho phep front end su dung API
@Slf4j
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    ApiResponse<Transaction> createTransition(@RequestBody Transaction transaction){
        return ApiResponse.<Transaction>builder().result(transactionService.createTransaction(transaction)).build();
    };

    @GetMapping("/getall")
    ApiResponse<List<Transaction>> getAllTransactions(){
        return ApiResponse.<List<Transaction>>builder().result(transactionService.getAllTransactions()).build();
    };

    @GetMapping("/{userId}")
    ApiResponse<List<Transaction>> getTransactionsByUserId(@PathVariable Integer userId){
        return ApiResponse.<List<Transaction>>builder().result(transactionService.getTransactionsByUserId(userId)).build();
    };

    @GetMapping
    ApiResponse<List<Transaction>> getMyTransactions(){
        return ApiResponse.<List<Transaction>>builder().result(transactionService.getMyTransactions()).build();
    };

    @GetMapping("/my-lastest-transaction")
    ApiResponse<Transaction> getMyLastestTransactions(){
        return ApiResponse.<Transaction>builder().result(transactionService.getMyLastestTransactions()).build();
    };
    
}
