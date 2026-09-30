package com.Niish.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoneyRequest {

    @NotNull
    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private Double amount;
}