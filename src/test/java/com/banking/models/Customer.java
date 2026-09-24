package com.banking.models;

public class Customer {
    private String firstName;
    private String lastName;
    private String middleName;
    private String customerNumber;
    private String status;
    private CustomerAddress customerAddress;
    private ContactDetails contactDetails;

    // Generate Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber2) { this.customerNumber = customerNumber2; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public CustomerAddress getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(CustomerAddress customerAddress) { this.customerAddress = customerAddress; }
    public ContactDetails getContactDetails() { return contactDetails; }
    public void setContactDetails(ContactDetails contactDetails) { this.contactDetails = contactDetails; }
}