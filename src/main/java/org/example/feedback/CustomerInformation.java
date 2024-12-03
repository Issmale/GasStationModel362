package org.example.feedback;

public class CustomerInformation {
    private String customerID;
    private String name;
    private String email;

    // Constructor
    public CustomerInformation(String customerID, String name, String email) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
