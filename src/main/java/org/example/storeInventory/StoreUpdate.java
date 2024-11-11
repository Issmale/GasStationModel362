package org.example.storeInventory;

public class StoreUpdate implements StoreUpdateInterface {
    private String locationID;
    private String productID;
    private String productName;
    private int quantity;
    private double price;
    private String supplier;
    private String applicableLocations;
    private int reorderLevel;

    // Constructor
    public StoreUpdate(String locationID, String productID, String productName, int quantity, double price, String supplier, String applicableLocations, int reorderLevel) {
        this.locationID = locationID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.supplier = supplier;
        this.applicableLocations = applicableLocations;
        this.reorderLevel = reorderLevel;
    }

    // Getters and Setters for each field

    @Override
    public String getLocationID() {
        return locationID;
    }

    @Override
    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

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
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    public String getApplicableLocations() {
        return applicableLocations;
    }

    @Override
    public void setApplicableLocations(String applicableLocations) {
        this.applicableLocations = applicableLocations;
    }

    @Override
    public int getReorderLevel() {
        return reorderLevel;
    }

    @Override
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    // Optionally, add a toString method for easier display of the object
    @Override
    public String toString() {
        return "StoreUpdate{LocationID='" + locationID + "', ProductID='" + productID + "', ProductName='" + productName +
                "', Quantity=" + quantity + ", Price=" + price + ", Supplier='" + supplier +
                "', ApplicableLocations='" + applicableLocations + "', ReorderLevel=" + reorderLevel + "}";
    }
}
