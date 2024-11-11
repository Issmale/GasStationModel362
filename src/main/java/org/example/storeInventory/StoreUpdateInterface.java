package org.example.storeInventory;

/**
 * @Author Issmale Bekri
 */

public interface StoreUpdateInterface {
    // Getters and setters for locationID
    String getLocationID();
    void setLocationID(String locationID);

    // Getters and setters for productID
    String getProductID();
    void setProductID(String productID);

    // Getters and setters for productName
    String getProductName();
    void setProductName(String productName);

    // Getters and setters for quantity
    int getQuantity();
    void setQuantity(int quantity);

    // Getters and setters for price
    double getPrice();
    void setPrice(double price);

    // Getters and setters for supplier
    String getSupplier();
    void setSupplier(String supplier);

    // Getters and setters for applicableLocations
    String getApplicableLocations();
    void setApplicableLocations(String applicableLocations);

    // Getters and setters for reorderLevel
    int getReorderLevel();
    void setReorderLevel(int reorderLevel);
}
