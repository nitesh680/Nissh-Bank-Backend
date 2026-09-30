package com.Niish.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {

    private String type;
    private double amount;
    private double balanceAfterTransaction;
    private LocalDateTime timestamp;
}