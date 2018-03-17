package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * DemographicVendor Data
 */

public class Demographic implements Parcelable {

    public static final Parcelable.Creator<Demographic> CREATOR = new Parcelable.Creator<Demographic>() {
        @Override
        public Demographic createFromParcel(Parcel parcel) {
            return new Demographic(parcel);
        }

        @Override
        public Demographic[] newArray(int i) {
            return new Demographic[0];
        }
    };
    private boolean isHouseHoldHead;
    private int houseHoldMember;
    private String spouseFirstName;
    private String spouseLastName;
    private String cellPhoneAlt;
    private String cellCarrier;
    private String seasonName;

    public Demographic() {

    }

    public Demographic(boolean isHouseHold, int houseHoldMember, String spouseFirstName, String spouseLastName,
                       String cellPhoneAlt, String cellCarrier) {
        this.isHouseHoldHead = isHouseHold;
        this.houseHoldMember = houseHoldMember;
        this.spouseFirstName = spouseFirstName;
        this.spouseLastName = spouseLastName;
        this.cellPhoneAlt = cellPhoneAlt;
        this.cellCarrier = cellCarrier;
    }

    public Demographic(Parcel data) {
        this.isHouseHoldHead = data.readByte() != 0;
        this.houseHoldMember = data.readInt();
        this.spouseFirstName = data.readString();
        this.spouseLastName = data.readString();
        this.cellPhoneAlt = data.readString();
        this.cellCarrier = data.readString();
        this.seasonName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isHouseHoldHead ? 1 : 0));
        parcel.writeInt(houseHoldMember);
        parcel.writeString(spouseFirstName);
        parcel.writeString(spouseLastName);
        parcel.writeString(cellPhoneAlt);
        parcel.writeString(cellCarrier);
        parcel.writeString(seasonName);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public boolean isHouseHoldHead() {
        return isHouseHoldHead;
    }

    public void setHouseHoldHead(boolean houseHoldHead) {
        isHouseHoldHead = houseHoldHead;
    }

    public boolean isHouseHold() {
        return isHouseHoldHead;
    }

    public void setHouseHold(boolean houseHold) {
        isHouseHoldHead = houseHold;
    }

    public int getHouseHoldMember() {
        return houseHoldMember;
    }

    public void setHouseHoldMember(int houseHoldMember) {
        this.houseHoldMember = houseHoldMember;
    }

    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getCellPhoneAlt() {
        return cellPhoneAlt;
    }

    public void setCellPhoneAlt(String cellPhoneAlt) {
        this.cellPhoneAlt = cellPhoneAlt;
    }

    public String getCellCarrier() {
        return cellCarrier;
    }

    public void setCellCarrier(String cellCarrier) {
        this.cellCarrier = cellCarrier;
    }
}
