package org.example.Coupon;

public class ConcreteDiscountApplier implements DiscountApplier {
    private ConcreteCouponValidator couponValidator;
    private TransactionManager transactionManager;

    public ConcreteDiscountApplier(ConcreteCouponValidator couponValidator, TransactionManager transactionManager) {
        this.couponValidator = couponValidator;
        this.transactionManager = transactionManager;
    }

    @Override
    public double applyDiscount(String code) throws Exception {
        double discountPercentage = couponValidator.getDiscountPercentage(code);
        if (discountPercentage > 0) {
            double currentTotal = transactionManager.getCurrentTotal();
            double discountAmount = currentTotal * discountPercentage;
            System.out.printf("Applying discount of %.0f%% for coupon: %s%n", discountPercentage * 100, code);
            return Math.round(discountAmount * 100.0) / 100.0;
        }
        throw new Exception("Invalid discount for coupon: " + code);
    }
}