package com.banking.models;

import java.math.BigDecimal;

public class Transaction {
    private int accountNumber;
    private String txDateTime;
    private String txType;
    private Double txAmount;

    // Getters
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getTxDateTime() {
        return txDateTime;
    }

    public String getTxType() {
        return txType;
    }

    public Double getTxAmount() {
        return txAmount;
    }

    // Setters
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setTxDateTime(String txDateTime) {
        this.txDateTime = txDateTime;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public void setTxAmount(Double txAmount) {
        this.txAmount = txAmount;
    }
}