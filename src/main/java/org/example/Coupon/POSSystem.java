package org.example.Coupon;

public interface POSSystem {
    boolean processCoupon(String code);
    boolean finalizeSale(double totalAmount);
    DiscountApplier getDiscountApplier();
}