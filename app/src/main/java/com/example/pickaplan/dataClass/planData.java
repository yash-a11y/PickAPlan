package com.example.pickaplan.dataClass;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@SuppressLint("ParcelCreator")
@IgnoreExtraProperties
public class planData implements Parcelable {

    private int brand;
    private String planname;
    private String planprice;
    private String details;

    private int likes;




    public planData(){


    }
    public planData(int brand, String planName, String price, String details) {
        this.brand = brand;
        this.planname = planName;
        this.planprice = price;
        this.details = details;
    }

    public planData(int brand, String planName, String price, String details, int likes) {
        this.brand = brand;
        this.planname = planName;
        this.planprice = price;
        this.details = details;
        this.likes = likes;
    }




    @PropertyName("brand")
    public int getBrand() {
        return brand;
    }

    @PropertyName("brand")
    public void setBrand(int brand) {
        this.brand = brand;
    }

    @PropertyName("planprice")
    public String getPrice() {
        return planprice;
    }

    @PropertyName("planprice")
    public void setPlanPrice(String planprice) {
        this.planprice = planprice;
    }

    @PropertyName("planname")
    public String getPlanName() {
        return planname;
    }

    @PropertyName("likes")
    public void setLikes(int likes) {
        this.likes = likes;
    }

    @PropertyName("likes")
    public int getLikes() {
        return likes;
    }

    @PropertyName("planname")
    public void setPlanName(String planname) {
        this.planname = planname;
    }


    @PropertyName("details")
    public void setDetails(String details) {
        this.details = details;
    }

    @PropertyName("details")
    public String getDetails() {
        return details;
    }

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(brand); // Write the brand field
        dest.writeString(planname); // Write the planname field
        dest.writeString(planprice); // Write the planprice field
        dest.writeString(details); // Write the details field
    }

    // Creator field required for Parcelable
    @Exclude
    public static final Parcelable.Creator<planData> CREATOR = new Parcelable.Creator<planData>() {
        @Exclude
        @Override
        public planData createFromParcel(Parcel in) {
            // Read the data from the Parcel and create a new planData object
            return new planData(in.readInt(), in.readString(), in.readString(), in.readString());
        }

        @Exclude
        @Override
        public planData[] newArray(int size) {
            return new planData[size];
        }


    };



    @Exclude
    public double getPriceAsDouble() {
        // Remove any non-numeric characters (assuming price is in format like "$49.99")
        String numericPrice = planprice.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numericPrice);
    }
}