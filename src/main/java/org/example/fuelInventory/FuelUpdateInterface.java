// FuelUpdateInterface.java
package org.example.fuelInventory;

public interface FuelUpdateInterface {
    String getFuelType();
    double getQuantity();
    double getReorderLevel();
    void setQuantity(double quantity);
}
