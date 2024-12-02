package org.example.Coupon;

public class ConcretePOSSystem implements POSSystem {
    private CouponValidator couponValidator;
    private DiscountApplier discountApplier;
    private TransactionManager transactionManager;
    private SalesRecordManager salesRecordManager;
    private ReceiptPrinter receiptPrinter;

    public ConcretePOSSystem(CouponValidator couponValidator, DiscountApplier discountApplier,
                             TransactionManager transactionManager, SalesRecordManager salesRecordManager,
                             ReceiptPrinter receiptPrinter) {
        this.couponValidator = couponValidator;
        this.discountApplier = discountApplier;
        this.transactionManager = transactionManager;
        this.salesRecordManager = salesRecordManager;
        this.receiptPrinter = receiptPrinter;
    }

    @Override
    public boolean processCoupon(String code) {
        System.out.println("Processing coupon...");
        try {
            if (couponValidator.validateCoupon(code)) {
                System.out.println("Coupon is valid.");
                double discountAmount = discountApplier.applyDiscount(code);
                if (discountAmount > 0) {
                    System.out.printf("Discount of $%.2f applied successfully.%n", discountAmount);
                    return transactionManager.applyDiscount(discountAmount);
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing coupon: " + e.getMessage());
        }
        System.out.println("Failed to process coupon.");
        return false;
    }

    @Override
    public DiscountApplier getDiscountApplier() {
        return discountApplier;
    }

    @Override
    public boolean finalizeSale(double totalAmount) {
        System.out.println("Finalizing sale...");
        try {
            if (transactionManager.processPayment(totalAmount)) {
                System.out.println("Payment processed successfully.");
                salesRecordManager.updateSalesRecord(totalAmount);
                return receiptPrinter.printReceipt(totalAmount);
            }
        } catch (Exception e) {
            System.out.println("Error finalizing sale: " + e.getMessage());
        }
        System.out.println("Failed to finalize sale.");
        return false;
    }
}