package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class InternalInformation implements Parcelable {
    private String chairName;
    private String viceChairName;
    private String secName;

    private boolean isChairGender;
    private boolean isViceChairGender;
    private boolean isSecGender;

    private String chairCell;
    private String viceChairCell;
    private String secCell;

    private int yearRcaRegistration;

   //constructeur vide et contructeur non vide


    public InternalInformation() {}

    public InternalInformation(String chairName, String viceChairName, String secName, boolean isChairGender, boolean isViceChairGender, boolean isSecGender, String chairCell, String viceChairCell, String secCell, int yearRcaRegistration) {
        this.chairName = chairName;
        this.viceChairName = viceChairName;
        this.secName = secName;
        this.isChairGender = isChairGender;
        this.isViceChairGender = isViceChairGender;
        this.isSecGender = isSecGender;
        this.chairCell = chairCell;
        this.viceChairCell = viceChairCell;
        this.secCell = secCell;
        this.yearRcaRegistration = yearRcaRegistration;
}


    public InternalInformation(Parcel data) {
        this.chairName = data.readString();
        this.viceChairName = data.readString();
        this.secName = data.readString();
        this.isChairGender = data.readByte() != 0;
        this.isViceChairGender = data.readByte() != 0;
        this.isSecGender = data.readByte() != 0;
        this.chairCell = data.readString();
        this.viceChairCell = data.readString();
        this.secCell = data.readString();
        this.yearRcaRegistration = data.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chairName);
        parcel.writeString(viceChairName);
        parcel.writeString(secName);
        parcel.writeByte((byte) (isChairGender ? 1 : 0));
        parcel.writeByte((byte) (isViceChairGender  ? 1 : 0));
        parcel.writeByte((byte) (isSecGender  ? 1 : 0));
        parcel.writeString(chairCell );
        parcel.writeString(viceChairCell);
        parcel.writeString(secCell );
        parcel.writeInt(yearRcaRegistration );

    }
   //setters et getters


    public String getChairName() {
        return chairName;
    }

    public void setChairName(String chairName) {
        this.chairName = chairName;
    }

    public String getViceChairName() {
        return viceChairName;
    }

    public void setViceChairName(String viceChairName) {
        this.viceChairName = viceChairName;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public boolean isChairGender() {
        return isChairGender;
    }

    public void setChairGender(boolean chairGender) {
        isChairGender = chairGender;
    }

    public boolean isViceChairGender() {
        return isViceChairGender;
    }

    public void setViceChairGender(boolean viceChairGender) {
        isViceChairGender = viceChairGender;
    }

    public boolean isSecGender() {
        return isSecGender;
    }

    public void setSecGender(boolean secGender) {
        isSecGender = secGender;
    }

    public String getChairCell() {
        return chairCell;
    }

    public void setChairCell(String chairCell) {
        this.chairCell = chairCell;
    }

    public String getViceChairCell() {
        return viceChairCell;
    }

    public void setViceChairCell(String viceChairCell) {
        this.viceChairCell = viceChairCell;
    }

    public String getSecCell() {
        return secCell;
    }

    public void setSecCell(String secCell) {
        this.secCell = secCell;
    }

    public int getYearRcaRegistration() {
        return yearRcaRegistration;
    }

    public void setYearRcaRegistration(int yearRcaRegistration) {
        this.yearRcaRegistration = yearRcaRegistration;
    }

    public static final Creator<InternalInformation> CREATOR = new Creator<InternalInformation>() {
        @Override
        public InternalInformation createFromParcel(Parcel parcel) {
            return new InternalInformation(parcel);
        }

        @Override
        public InternalInformation[] newArray(int i) {
            return new InternalInformation[0];
        }
    };
}
