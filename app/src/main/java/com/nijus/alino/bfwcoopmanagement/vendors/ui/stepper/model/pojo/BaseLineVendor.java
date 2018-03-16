package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AvailableResourcesFragment data
 */

public class BaseLineVendor implements Parcelable {


    public static final Parcelable.Creator<BaseLineVendor> CREATOR = new Parcelable.Creator<BaseLineVendor>() {
        @Override
        public BaseLineVendor createFromParcel(Parcel parcel) {
            return new BaseLineVendor(parcel);
        }

        @Override
        public BaseLineVendor[] newArray(int i) {
            return new BaseLineVendor[0];
        }
    };
    private Double totProdInKg;
    private Double totLostInKg;
    private Double totSoldInKg;
    private Double totVolumeSoldCoopInKg;
    private Double priceSoldToCoopPerKg;
    private Double totVolSoldInKg;
    private Double priceSoldInKg;
    private int harvestSeason;
    private int baselineId;
    private String seasonName;

    public BaseLineVendor() {
        this.baselineId = 0;
    }

    public BaseLineVendor(Double totProdInKg, Double totLostInKg, Double totSoldInKg, Double totVolumeSoldCoopInKg,
                          Double priceSoldToCoopPerKg, Double totVolSoldInKg, Double priceSoldInKg, int harvestSeason) {
        this.totProdInKg = totProdInKg;
        this.totLostInKg = totLostInKg;
        this.totSoldInKg = totSoldInKg;
        this.totVolumeSoldCoopInKg = totVolumeSoldCoopInKg;
        this.priceSoldToCoopPerKg = priceSoldToCoopPerKg;
        this.totVolSoldInKg = totVolSoldInKg;
        this.priceSoldInKg = priceSoldInKg;
        this.harvestSeason = harvestSeason;
    }

    public BaseLineVendor(Parcel data) {
        this.totProdInKg = data.readDouble();
        this.totLostInKg = data.readDouble();
        this.totSoldInKg = data.readDouble();
        this.totVolumeSoldCoopInKg = data.readDouble();
        this.priceSoldToCoopPerKg = data.readDouble();
        this.totVolSoldInKg = data.readDouble();
        this.priceSoldInKg = data.readDouble();
        this.harvestSeason = data.readInt();
        this.baselineId = data.readInt();
        this.seasonName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(totProdInKg);
        parcel.writeDouble(totLostInKg);
        parcel.writeDouble(totSoldInKg);
        parcel.writeDouble(totVolumeSoldCoopInKg);
        parcel.writeDouble(priceSoldToCoopPerKg);
        parcel.writeDouble(totVolSoldInKg);
        parcel.writeDouble(priceSoldInKg);
        parcel.writeInt(harvestSeason);
        parcel.writeInt(baselineId);
        parcel.writeString(seasonName);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(int baselineId) {
        this.baselineId = baselineId;
    }

    public int getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(int harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    public Double getTotProdInKg() {
        return totProdInKg;
    }

    public void setTotProdInKg(Double totProdInKg) {
        this.totProdInKg = totProdInKg;
    }

    public Double getTotLostInKg() {
        return totLostInKg;
    }

    public void setTotLostInKg(Double totLostInKg) {
        this.totLostInKg = totLostInKg;
    }

    public Double getTotSoldInKg() {
        return totSoldInKg;
    }

    public void setTotSoldInKg(Double totSoldInKg) {
        this.totSoldInKg = totSoldInKg;
    }

    public Double getTotVolumeSoldCoopInKg() {
        return totVolumeSoldCoopInKg;
    }

    public void setTotVolumeSoldCoopInKg(Double totVolumeSoldCoopInKg) {
        this.totVolumeSoldCoopInKg = totVolumeSoldCoopInKg;
    }

    public Double getPriceSoldToCoopPerKg() {
        return priceSoldToCoopPerKg;
    }

    public void setPriceSoldToCoopPerKg(Double priceSoldToCoopPerKg) {
        this.priceSoldToCoopPerKg = priceSoldToCoopPerKg;
    }

    public Double getTotVolSoldInKg() {
        return totVolSoldInKg;
    }

    public void setTotVolSoldInKg(Double totVolSoldInKg) {
        this.totVolSoldInKg = totVolSoldInKg;
    }

    public Double getPriceSoldInKg() {
        return priceSoldInKg;
    }

    public void setPriceSoldInKg(Double priceSoldInKg) {
        this.priceSoldInKg = priceSoldInKg;
    }
}
