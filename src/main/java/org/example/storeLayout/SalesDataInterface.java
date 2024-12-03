package org.example.storeLayout;

public interface SalesDataInterface {
    void saveSalesData(String csvFilePath);
    void updateFootTraffic(String product, double salesVolumeChange);
    void editProduct(String product, double salesVolumeChange);
    void addProduct(String product, double salesVolume);
    void loadSalesData(String csvFilePath);
}

