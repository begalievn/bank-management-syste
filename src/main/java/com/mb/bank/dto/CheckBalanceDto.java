package com.mb.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckBalanceDto {

    private String account;
    private String fio;
    private double amount;
    private int result;
    private String message;

    public CheckBalanceDto(String message) {
        this.message = message;
    }
}
