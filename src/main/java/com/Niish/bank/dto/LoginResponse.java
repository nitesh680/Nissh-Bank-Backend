package com.Niish.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private String username;
    private String accountNumber;
    private double balance;
    private String token;
}