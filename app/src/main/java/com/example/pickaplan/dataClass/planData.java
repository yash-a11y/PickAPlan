package com.example.pickaplan.dataClass;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("ParcelCreator")
public class planData implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(brand); // Write the brand field
        dest.writeString(planname); // Write the planname field
        dest.writeString(planprice); // Write the planprice field
        dest.writeString(details); // Write the details field
    }

    // Creator field required for Parcelable
    public static final Parcelable.Creator<planData> CREATOR = new Parcelable.Creator<planData>() {
        @Override
        public planData createFromParcel(Parcel in) {
            // Read the data from the Parcel and create a new planData object
            return new planData(in.readInt(), in.readString(), in.readString(), in.readString());
        }

        @Override
        public planData[] newArray(int size) {
            return new planData[size];
        }
    };
}
