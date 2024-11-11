package org.example.fuelInventory;


/**
 * @Author Issmale Bekri
 */

public class FuelUpdate implements FuelUpdateInterface {
    private String fuelType;
    private double quantity;
    private double reorderLevel;

    public FuelUpdate(String fuelType, double quantity, double reorderLevel) {
        this.fuelType = fuelType;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String getFuelType() {
        return fuelType;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public double getReorderLevel() {
        return reorderLevel;
    }

    @Override
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "FuelType: " + fuelType + ", Quantity: " + quantity + ", Reorder Level: " + reorderLevel;
    }
}
