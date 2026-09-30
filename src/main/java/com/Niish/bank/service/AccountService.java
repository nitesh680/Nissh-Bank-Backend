package com.Niish.bank.service;

import com.Niish.bank.exception.AccountNotFoundException;
import com.Niish.bank.exception.InsufficientBalanceException;
import com.Niish.bank.model.Transaction;
import com.Niish.bank.model.User;
import com.Niish.bank.repository.TransactionRepository;
import com.Niish.bank.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(
            UserRepository userRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // =========================
    // DEPOSIT
    // =========================
    public User deposit(String username, double amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccountNotFoundException("User not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than 0");
        }

        double newBalance = user.getBalance() + amount;

        user.setBalance(newBalance);

        User savedUser = userRepository.save(user);

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(user.getAccountNumber());
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(newBalance);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return savedUser;
    }

    // =========================
    // WITHDRAW
    // =========================
    public User withdraw(String username, double amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccountNotFoundException("User not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than 0");
        }

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        double newBalance = user.getBalance() - amount;

        user.setBalance(newBalance);

        User savedUser = userRepository.save(user);

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(user.getAccountNumber());
        transaction.setType("WITHDRAW");
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(newBalance);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return savedUser;
    }

    // =========================
    // CHECK BALANCE
    // =========================
    public User getBalance(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccountNotFoundException("User not found"));
    }

    // =========================
    // GET TRANSACTIONS
    // =========================
    public List<Transaction> getTransactions(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccountNotFoundException("User not found"));

        return transactionRepository
                .findByAccountNumber(user.getAccountNumber());
    }

    // =========================
    // TRANSFER MONEY
    // =========================
    public User transfer(
            String username,
            String recipientAccountNumber,
            double amount) {

        // Find sender
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccountNotFoundException("Sender account not found"));

        // Validate amount
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than 0");
        }

        // Find recipient
        User recipient = userRepository
                .findByAccountNumber(recipientAccountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Recipient account not found"));

        // Prevent sending money to yourself
        if (sender.getAccountNumber()
                .equals(recipient.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "You cannot transfer money to your own account");
        }

        // Check sender balance
        if (sender.getBalance() < amount) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        // =========================
        // UPDATE BALANCES
        // =========================

        double senderNewBalance =
                sender.getBalance() - amount;

        double recipientNewBalance =
                recipient.getBalance() + amount;

        sender.setBalance(senderNewBalance);
        recipient.setBalance(recipientNewBalance);

        // Save both users
        userRepository.save(sender);
        userRepository.save(recipient);

        // =========================
        // SENDER TRANSACTION
        // =========================

        Transaction senderTransaction =
                new Transaction();

        senderTransaction.setAccountNumber(
                sender.getAccountNumber());

        senderTransaction.setType("TRANSFER_SENT");

        senderTransaction.setAmount(amount);

        senderTransaction.setBalanceAfterTransaction(
                senderNewBalance);

        senderTransaction.setTimestamp(
                LocalDateTime.now());

        transactionRepository.save(senderTransaction);

        // =========================
        // RECIPIENT TRANSACTION
        // =========================

        Transaction recipientTransaction =
                new Transaction();

        recipientTransaction.setAccountNumber(
                recipient.getAccountNumber());

        recipientTransaction.setType("TRANSFER_RECEIVED");

        recipientTransaction.setAmount(amount);

        recipientTransaction.setBalanceAfterTransaction(
                recipientNewBalance);

        recipientTransaction.setTimestamp(
                LocalDateTime.now());

        transactionRepository.save(recipientTransaction);

        // Return updated sender
        return sender;
    }
}