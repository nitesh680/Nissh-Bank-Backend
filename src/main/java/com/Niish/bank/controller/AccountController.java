package com.Niish.bank.controller;

import com.Niish.bank.dto.MoneyRequest;
import com.Niish.bank.dto.TransactionResponse;
import com.Niish.bank.dto.TransferRequest;
import com.Niish.bank.dto.UserResponse;
import com.Niish.bank.model.Transaction;
import com.Niish.bank.model.User;
import com.Niish.bank.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // =========================
    // DEPOSIT
    // =========================
    @PostMapping("/deposit")
    public ResponseEntity<UserResponse> deposit(
            @Valid @RequestBody MoneyRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = accountService.deposit(
                username,
                request.getAmount()
        );

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getAccountNumber(),
                user.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // WITHDRAW
    // =========================
    @PostMapping("/withdraw")
    public ResponseEntity<UserResponse> withdraw(
            @Valid @RequestBody MoneyRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = accountService.withdraw(
                username,
                request.getAmount()
        );

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getAccountNumber(),
                user.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // CHECK BALANCE
    // =========================
    @GetMapping("/balance")
    public ResponseEntity<UserResponse> getBalance(
            Authentication authentication) {

        String username = authentication.getName();

        User user = accountService.getBalance(username);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getAccountNumber(),
                user.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // TRANSACTIONS
    // =========================
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            Authentication authentication) {

        String username = authentication.getName();

        List<Transaction> transactions =
                accountService.getTransactions(username);

        List<TransactionResponse> response =
                transactions.stream()
                        .map(transaction ->
                                new TransactionResponse(
                                        transaction.getType(),
                                        transaction.getAmount(),
                                        transaction.getBalanceAfterTransaction(),
                                        transaction.getTimestamp()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =========================
    // TRANSFER MONEY
    // =========================
    @PostMapping("/transfer")
    public ResponseEntity<UserResponse> transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = accountService.transfer(
                username,
                request.getRecipientAccountNumber(),
                request.getAmount()
        );

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getAccountNumber(),
                user.getBalance()
        );

        return ResponseEntity.ok(response);
    }
}