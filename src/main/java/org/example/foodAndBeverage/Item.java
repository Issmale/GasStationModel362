package org.example.foodAndBeverage;

public class Item {
    private String name;
    private double costPrice;
    private double sellingPrice;

    public Item(String name, double costPrice, double sellingPrice) {
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getName() {
        return name;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setCostPrice(double costPrice) {
        if (costPrice < 0) {
            throw new IllegalArgumentException("Cost price cannot be negative.");
        }
        this.costPrice = costPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        if (sellingPrice < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative.");
        }
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return String.format("Item[name=%s, costPrice=%.2f, sellingPrice=%.2f]", name, costPrice, sellingPrice);
    }
}
