package org.example.storeInventory;

import java.util.List;

public class StoreProduct implements StoreProductInterface {
    private String productID;
    private String productName;
    private double price;
    private String supplier;
    private List<String> applicableLocations;
    private int reorderLevel;  // New field for reorder level

    // Constructor
    public StoreProduct(String productID, String productName, double price, String supplier, List<String> applicableLocations, int reorderLevel) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.supplier = supplier;
        this.applicableLocations = applicableLocations;
        this.reorderLevel = reorderLevel;  // Initialize reorder level
    }

    // Implementing StoreProductInterface methods

    @Override
    public String getProductID() {
        return productID;
    }

    @Override
    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getSupplier() {
        return supplier;
    }

    @Override
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public List<String> getApplicableLocations() {
        return applicableLocations;
    }

    @Override
    public void setApplicableLocations(List<String> applicableLocations) {
        this.applicableLocations = applicableLocations;
    }

    @Override
    public int getReorderLevel() {  // Getter for reorder level
        return reorderLevel;
    }

    @Override
    public void setReorderLevel(int reorderLevel) {  // Setter for reorder level
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String toString() {
        return "StoreProduct{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", supplier='" + supplier + '\'' +
                ", applicableLocations=" + applicableLocations +
                ", reorderLevel=" + reorderLevel +  // Include reorder level in toString
                '}';
    }
}
