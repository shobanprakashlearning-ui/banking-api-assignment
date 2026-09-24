package com.banking.models;

public class AccountPayload {
    private long accountNumber;
    private BankInformation bankInformation;
    private String accountStatus;
    private String accountType;
    private double accountBalance;
    private String accountCreated;

    public long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(long accountNumber) { this.accountNumber = accountNumber; }
    public BankInformation getBankInformation() { return bankInformation; }
    public void setBankInformation(BankInformation bankInformation) { this.bankInformation = bankInformation; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }
    public String getAccountCreated() { return accountCreated; }
    public void setAccountCreated(String accountCreated) { this.accountCreated = accountCreated; }
}