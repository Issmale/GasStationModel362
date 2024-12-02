package org.example.Coupon;

public class ConcreteCashier implements Cashier {
    private POSSystem posSystem;

    public ConcreteCashier(POSSystem posSystem) {
        this.posSystem = posSystem;
    }

    @Override
    public boolean enterCouponCode(String code) {
        System.out.println("Cashier enters coupon code: " + code);
        return posSystem.processCoupon(code);
    }

    @Override
    public POSSystem getPosSystem() {
        return posSystem;
    }
}
