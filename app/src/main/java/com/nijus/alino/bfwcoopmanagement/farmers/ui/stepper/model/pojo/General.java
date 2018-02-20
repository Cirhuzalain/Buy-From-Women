package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * GeneralVendor PageVendorVendor Data
 */

public class General implements Parcelable {

    private String name;
    private String coopsName;
    private int coopId;
    private String address;
    private String phoneNumber;
    private boolean gender;

    public General() {

    }

    public General(String name, boolean gender, String coop, String phoneNumber, String address, int coopId) {
        this.name = name;
        this.gender = gender;
        this.coopsName = coop;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.coopId = coopId;
    }

    public General(Parcel data) {
        this.name = data.readString();
        this.coopsName = data.readString();
        this.phoneNumber = data.readString();
        this.address = data.readString();
        this.gender = data.readByte() != 0;
        this.coopId = data.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(coopsName);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeInt(coopId);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getCoopsName() {
        return coopsName;
    }

    public boolean isGender() {
        return gender;
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

    public void setCoopsName(String coop) {
        this.coopsName = coop;
    }

    public static final Parcelable.Creator<General> CREATOR = new Parcelable.Creator<General>() {
        @Override
        public General createFromParcel(Parcel parcel) {
            return new General(parcel);
        }

        @Override
        public General[] newArray(int i) {
            return new General[0];
        }
    };
}
