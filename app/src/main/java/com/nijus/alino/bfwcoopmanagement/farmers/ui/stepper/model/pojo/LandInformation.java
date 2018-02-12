package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Land Information Data
 */

public class LandInformation implements Parcelable {

    private double landSize;

    public LandInformation() {

    }

    public LandInformation(double landSize) {
        this.landSize = landSize;
    }

    public LandInformation(Parcel data) {
        this.landSize = data.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(landSize);
    }

    public double getLandSize() {
        return landSize;
    }

    public void setLandSize(double landSize) {
        this.landSize = landSize;
    }

    public static final Parcelable.Creator<LandInformation> CREATOR = new Parcelable.Creator<LandInformation>() {
        @Override
        public LandInformation createFromParcel(Parcel parcel) {
            return new LandInformation(parcel);
        }

        @Override
        public LandInformation[] newArray(int i) {
            return new LandInformation[0];
        }
    };
}
