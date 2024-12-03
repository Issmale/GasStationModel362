package org.example.Coupon;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class ConcreteReceiptPrinter implements ReceiptPrinter {
    @Override
    public boolean printReceipt(double totalAmount) {
        System.out.println("\n--- Receipt ---");
        System.out.println("Date: " + LocalDateTime.now());
        System.out.printf("Total Amount: $%.2f%n", totalAmount);
        System.out.println("Thank you for your purchase!");
        System.out.println("--------------\n");
        return true;
    }
}
