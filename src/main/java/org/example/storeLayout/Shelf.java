package org.example.storeLayout;

public class Shelf implements ShelfInterface{
    private String shelfID;
    private String side;
    private String product;
    private int spaceAvailable;

    public Shelf(String shelfID, String side, String product, int spaceAvailable) {
        this.shelfID = shelfID;
        this.side = side;
        this.product = product;
        this.spaceAvailable = spaceAvailable;
    }

    @Override
    public String getShelfID() {
        return shelfID;
    }
    public void setSide(String side) {
        this.side = side;  // Update the side of the shelf
    }

    @Override
    public String getSide() {
        return side;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public int getSpaceAvailable() {
        return spaceAvailable;
    }

    @Override
    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public void setSpaceAvailable(int spaceAvailable) {
        this.spaceAvailable = spaceAvailable;
    }

    @Override
    public String toString() {
        return "ShelfID: " + shelfID + ", Side: " + side + ", Product: " + product + ", Space Available: " + spaceAvailable;
    }

}
