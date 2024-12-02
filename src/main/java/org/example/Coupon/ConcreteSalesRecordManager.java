package org.example.Coupon;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class ConcreteSalesRecordManager implements SalesRecordManager {
    @Override
    public void updateSalesRecord(double totalAmount) {
        System.out.println("Updating sales record...");
        System.out.println("Date: " + LocalDateTime.now());
        System.out.printf("Total Amount: $%.2f%n", totalAmount);
        System.out.println("Sales record updated successfully.");
    }
}