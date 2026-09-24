package com.banking.models;

public class Transaction {
    private int accountNumber;
    private String txDateTime;
    private String txType;
    private double txAmount;

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

    public double getTxAmount() {
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

    public void setTxAmount(double txAmount) {
        this.txAmount = txAmount;
    }
}