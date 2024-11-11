package org.example.storeInventory;

import java.util.List;

/**
 * @Author Issmale Bekri
 */

public interface StoreProductInterface {
    String getProductID();
    String getProductName();
    double getPrice();
    String getSupplier();
    List<String> getApplicableLocations();
    int getReorderLevel();  // New method for reorder level

    void setProductID(String productID);
    void setProductName(String productName);
    void setPrice(double price);
    void setSupplier(String supplier);
    void setApplicableLocations(List<String> locations);
    void setReorderLevel(int reorderLevel);  // New setter for reorder level
}
