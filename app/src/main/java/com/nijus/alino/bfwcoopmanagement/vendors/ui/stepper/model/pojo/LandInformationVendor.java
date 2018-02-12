package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Land Information Data
 */

public class LandInformationVendor implements Parcelable {

    private double landSize;

    public LandInformationVendor() {

    }

    public LandInformationVendor(double landSize) {
        this.landSize = landSize;
    }

    public LandInformationVendor(Parcel data) {
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

    public static final Creator<LandInformationVendor> CREATOR = new Creator<LandInformationVendor>() {
        @Override
        public LandInformationVendor createFromParcel(Parcel parcel) {
            return new LandInformationVendor(parcel);
        }

        @Override
        public LandInformationVendor[] newArray(int i) {
            return new LandInformationVendor[0];
        }
    };
}
