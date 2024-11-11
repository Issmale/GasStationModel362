package org.example.adjustFuel;

import java.util.Date;

public class FuelPriceUpdate implements FuelPriceUpdateInterface {
    private String fuelType;
    private double oldPrice;
    private double newPrice;
    private Date timestamp;

    public FuelPriceUpdate(String fuelType, double oldPrice, double newPrice) {
        this.fuelType = fuelType;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.timestamp = new Date();
    }

    @Override
    public String getFuelType() { return fuelType; }
    @Override
    public double getOldPrice() { return oldPrice; }
    @Override
    public double getNewPrice() { return newPrice; }
    @Override
    public Date getTimestamp() { return timestamp; }
}