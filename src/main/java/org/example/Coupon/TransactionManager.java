package org.example.Coupon;

public interface TransactionManager {
    boolean updateTotal(double discountAmount);
    boolean processPayment(double amount) throws Exception;
    double getCurrentTotal();
    void setCurrentTotal(double total);
    boolean applyDiscount(double discountAmount); // Add this method
}