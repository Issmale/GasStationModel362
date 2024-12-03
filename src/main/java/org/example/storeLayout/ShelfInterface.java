package org.example.storeLayout;

public interface ShelfInterface {
    void setSpaceAvailable(int spaceAvailable);
    void setProduct(String product);
    int getSpaceAvailable();
    String getProduct();
    String getSide();
    String getShelfID();
}

