package org.example.storeLayout;

public interface StoreManagerInterface {
    boolean optimizeStoreLayout(String storeID);
    void reviewCurrentLayout(StoreLayout storeLayout, SalesData salesData, TrafficData trafficData);
    boolean adjustLayout(String storeID, StoreLayout adjustedLayout);
    boolean confirmNewLayout(String storeID);
    boolean resolveLayoutConflict(String storeID, String conflictDetails);
}

