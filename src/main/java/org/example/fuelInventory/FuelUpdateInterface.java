// FuelUpdateInterface.java
package org.example.fuelInventory;

/**
 * @Author Issmale Bekri
 */

public interface FuelUpdateInterface {
    String getFuelType();
    double getQuantity();
    double getReorderLevel();
    void setQuantity(double quantity);
}
