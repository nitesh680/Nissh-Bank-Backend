package com.Niish.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String username;
    private String name;
    private String accountNumber;
    private double balance;
}