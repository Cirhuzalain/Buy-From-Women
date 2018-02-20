package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AvailableRessourcesFragment data
 */

public class BaseLine implements Parcelable {

    private Double totProdInKg;
    private Double totLostInKg;
    private Double totSoldInKg;
    private Double totVolumeSoldCoopInKg;
    private Double priceSoldToCoopPerKg;
    private Double totVolSoldInKg;
    private Double priceSoldInKg;
    private int harvestSeason;


    public BaseLine() {

    }

    public BaseLine(Double totProdInKg, Double totLostInKg, Double totSoldInKg, Double totVolumeSoldCoopInKg,
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

    public BaseLine(Parcel data) {
        this.totProdInKg = data.readDouble();
        this.totLostInKg = data.readDouble();
        this.totSoldInKg = data.readDouble();
        this.totVolumeSoldCoopInKg = data.readDouble();
        this.priceSoldToCoopPerKg = data.readDouble();
        this.totVolSoldInKg = data.readDouble();
        this.priceSoldInKg = data.readDouble();
        this.harvestSeason = data.readInt();
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

    public static final Parcelable.Creator<BaseLine> CREATOR = new Parcelable.Creator<BaseLine>() {
        @Override
        public BaseLine createFromParcel(Parcel parcel) {
            return new BaseLine(parcel);
        }

        @Override
        public BaseLine[] newArray(int i) {
            return new BaseLine[0];
        }
    };
}
