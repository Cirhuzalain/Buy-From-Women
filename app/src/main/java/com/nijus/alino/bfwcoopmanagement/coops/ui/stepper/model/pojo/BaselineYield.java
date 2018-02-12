package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BaselineYield implements Parcelable {
    private String harvestSeason;
    private boolean isMaize;
    private boolean isBean;
    private boolean isSoy;
    private boolean isOther;
    private String otherText;

   //constructeur vide et contructeur non vide


    public BaselineYield() {
    }

    public BaselineYield(String harvestSeason, boolean isMaize, boolean isBean, boolean isSoy, boolean isOther, String otherText) {
        this.harvestSeason = harvestSeason;
        this.isMaize = isMaize;
        this.isBean = isBean;
        this.isSoy = isSoy;
        this.isOther = isOther;
        this.otherText = otherText;
    }

    public BaselineYield(Parcel data) {
        this.harvestSeason = data.readString();
        this.isMaize = data.readByte() != 0;
        this.isBean =data.readByte() != 0;
        this.isSoy = data.readByte() != 0;
        this.isOther = data.readByte() != 0;
        this.otherText = data.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(harvestSeason);
        parcel.writeByte((byte) (isMaize ? 1 : 0));
        parcel.writeByte((byte) (isBean ? 1 : 0));
        parcel.writeByte((byte) (isSoy ? 1 : 0));
        parcel.writeByte((byte) (isOther ? 1 : 0));
        parcel.writeString(otherText);

    }
   //setters et getters


    public String getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(String harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    public boolean isMaize() {
        return isMaize;
    }

    public void setMaize(boolean maize) {
        isMaize = maize;
    }

    public boolean isBean() {
        return isBean;
    }

    public void setBean(boolean bean) {
        isBean = bean;
    }

    public boolean isSoy() {
        return isSoy;
    }

    public void setSoy(boolean soy) {
        isSoy = soy;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public String getOtherText() {
        return otherText;
    }

    public void setOtherText(String otherText) {
        this.otherText = otherText;
    }

    public static final Creator<BaselineYield> CREATOR = new Creator<BaselineYield>() {
        @Override
        public BaselineYield createFromParcel(Parcel parcel) {
            return new BaselineYield(parcel);
        }

        @Override
        public BaselineYield[] newArray(int i) {
            return new BaselineYield[0];
        }
    };
}
