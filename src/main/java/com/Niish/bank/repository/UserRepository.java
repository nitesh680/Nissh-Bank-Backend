package com.Niish.bank.repository;

import com.Niish.bank.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByAccountNumber(String accountNumber);
}