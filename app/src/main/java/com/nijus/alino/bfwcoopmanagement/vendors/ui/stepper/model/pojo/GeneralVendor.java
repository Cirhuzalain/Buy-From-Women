package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * GeneralVendor PageVendorVendor Data
 */

public class GeneralVendor implements Parcelable {

    public static final Creator<GeneralVendor> CREATOR = new Creator<GeneralVendor>() {
        @Override
        public GeneralVendor createFromParcel(Parcel parcel) {
            return new GeneralVendor(parcel);
        }

        @Override
        public GeneralVendor[] newArray(int i) {
            return new GeneralVendor[0];
        }
    };
    private String name;
    private String address;
    private String phoneNumber;
    private boolean gender;

    public GeneralVendor() {

    }

    public GeneralVendor(String name, boolean gender, String phoneNumber, String address) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public GeneralVendor(Parcel data) {
        this.name = data.readString();
        this.phoneNumber = data.readString();
        this.address = data.readString();
        this.gender = data.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeByte((byte) (gender ? 1 : 0));

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
