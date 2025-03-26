package com.example.pickaplan.dataClass;

public class phoneData {
    private String phoneName;
    private String price;
    private String condition;
    private String fullPrice;
    private String imageUrl;

    public phoneData(String price, String phoneName, String condition, String fullPrice, String imageUrl) {
        this.price = price;
        this.phoneName = phoneName;
        this.condition = condition;
        this.fullPrice = fullPrice;
        this.imageUrl = imageUrl;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public String getFullPrice() {
        return fullPrice;
    }

    public String getCondition() {
        return condition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}
