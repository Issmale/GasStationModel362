package org.example.Coupon;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

public class ConcreteCouponValidator implements CouponValidator {
    private Map<String, Double> validCoupons;

    public ConcreteCouponValidator() {
        validCoupons = new HashMap<>();
        validCoupons.put("SUMMER10", 0.10);
        validCoupons.put("WELCOME20", 0.20);
    }

    @Override
    public boolean validateCoupon(String code) throws Exception {
        if (code == null || code.isEmpty()) {
            throw new Exception("Invalid coupon code");
        }
        return validCoupons.containsKey(code.toUpperCase());
    }

    public double getDiscountPercentage(String code) {
        return validCoupons.getOrDefault(code.toUpperCase(), 0.0);
    }
}