package com.example.pickaplan.dataClass;

public class planData {

    private int brand;
    private String planname;
    private String planprice;

    private String details;

    public planData(int brand, String planName, String price, String details) {
        this.brand = brand;
        this.planname = planName;
        this.planprice = price;
        this.details = details;
    }

    public int getBrand() {
        return brand;
    }

    public String getPrice() {
        return planprice;
    }

    public String getPlanName() {
        return planname;
    }

    public String getDetails() {
        return details;
    }

    public double getPriceAsDouble() {
        // Remove any non-numeric characters (assuming price is in format like "$49.99")
        String numericPrice = planprice.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numericPrice);
    }
}
