package com.banking.models;

public class BankInformation {
    private String branchName;
    private int branchCode;
    private BranchAddress branchAddress;
    private int routingNumber;

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public int getBranchCode() { return branchCode; }
    public void setBranchCode(int branchCode) { this.branchCode = branchCode; }
    public BranchAddress getBranchAddress() { return branchAddress; }
    public void setBranchAddress(BranchAddress branchAddress) { this.branchAddress = branchAddress; }
    public int getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(int routingNumber) { this.routingNumber = routingNumber; }
}