package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.controllers.SettingController.ApiResponse;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Users;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    public Transaction createTransaction(Transaction transaction){
        switch (transaction.getTransactionType()) {
            case DEPOSIT:
                depositTransaction(transaction);
                break;
            case WITHDRAW:
                withdrawTransaction(transaction);
                break;
            case PURCHASE:
                purchaseTransaction(transaction);
                break;
            default:
                break;
        }
        transaction.setStatus("PENDING");
        transaction.setUserId(userService.getMyinfo().getId());
        return transactionRepository.save(transaction);
    }

    private void depositTransaction(Transaction transaction){
        Users user = userService.getMyinfo();
        user.setBalance(user.getBalance() + transaction.getAmount());
        userRepository.save(user);
    }

    private void withdrawTransaction(Transaction transaction){
        Users user = userService.getMyinfo();
        if(user.getBalance() < transaction.getAmount()){
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        }
        user.setBalance(user.getBalance() - transaction.getAmount());
        userRepository.save(user);
    }

    private void purchaseTransaction(Transaction transaction){
        // Users user = userService.getMyinfo();
        // if(user.getBalance() < transaction.getAmount()){
        //     throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        // }
        // user.setBalance(user.getBalance() - transaction.getAmount());
        // userRepository.save(user);

        //Xử lí API ngân hàng tại đây
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<Transaction> getTransactionsByUserId(Integer userId){
        return transactionRepository.findAllByUserId(userId);
    }

    // @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public Optional<Transaction> getTransactionById(String transactionId){
        return transactionRepository.findById(transactionId);
    }

    public Integer changeStatus(Transaction transaction, String status){
        transaction.setStatus(status);
        transactionRepository.save(transaction);
        return 1;
    }

    public List<Transaction> getMyTransactions(){
        return transactionRepository.findAllByUserId(userService.getMyinfo().getId());
    }

    public Transaction getMyLastestTransactions(){
        return transactionRepository.findTopByUserIdOrderByTransactionDateDesc(userService.getMyinfo().getId()).get();
    }
}
