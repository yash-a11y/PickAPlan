package com.example.pickaplan.dataClass;

public class planData {

    private int brand;
    private String planName;
    private String price;

    private String details;

    public planData(int brand, String planName, String price, String details) {
        this.brand = brand;
        this.planName = planName;
        this.price = price;
        this.details = details;
    }

    public int getBrand() {
        return brand;
    }

    public String getPrice() {
        return price;
    }

    public String getPlanName() {
        return planName;
    }

    public String getDetails() {
        return details;
    }
}
