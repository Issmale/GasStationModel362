package org.example.storeLayout;

public class StoreManager implements StoreManagerInterface{
    @Override
    public boolean optimizeStoreLayout(String storeID) {
        return false;
    }

    @Override
    public void reviewCurrentLayout(StoreLayout storeLayout, SalesData salesData, TrafficData trafficData) {

    }

    @Override
    public boolean adjustLayout(String storeID, StoreLayout adjustedLayout) {
        return false;
    }

    @Override
    public boolean confirmNewLayout(String storeID) {
        return false;
    }

    @Override
    public boolean resolveLayoutConflict(String storeID, String conflictDetails) {
        return false;
    }
}
