package org.example.Coupon;

public class ConcreteTransactionManager implements TransactionManager {
    private double currentTotal;

    public ConcreteTransactionManager(double initialTotal) {
        this.currentTotal = initialTotal;
    }

    public void setCurrentTotal(double total) {
        this.currentTotal = total;
    }

    @Override
    public boolean updateTotal(double newTotal) {
        this.currentTotal = newTotal;
        System.out.printf("Updated total: $%.2f%n", currentTotal);
        return true;
    }

    @Override
    public boolean applyDiscount(double discountAmount) {
        if (discountAmount > currentTotal) {
            System.out.println("Discount amount exceeds total. Cannot apply.");
            return false;
        }
        currentTotal -= discountAmount;
        System.out.printf("Discount applied. New total: $%.2f%n", currentTotal);
        return true;
    }

    @Override
    public boolean processPayment(double amount) throws Exception {
        if (Math.abs(amount - currentTotal) > 0.01) {
            throw new Exception("Payment amount does not match the current total");
        }
        System.out.printf("Processing payment of $%.2f%n", amount);
        return true;
    }

    public double getCurrentTotal() {
        return currentTotal;
    }
}