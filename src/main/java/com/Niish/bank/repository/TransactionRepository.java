package com.Niish.bank.repository;

import com.Niish.bank.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository
        extends MongoRepository<Transaction, String> {

    List<Transaction> findByAccountNumber(String accountNumber);
}