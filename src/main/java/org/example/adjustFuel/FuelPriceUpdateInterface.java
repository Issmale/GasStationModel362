package org.example.adjustFuel;

import java.util.Date;

public interface FuelPriceUpdateInterface {
    String getFuelType();
    double getOldPrice();
    double getNewPrice();
    Date getTimestamp();
}
