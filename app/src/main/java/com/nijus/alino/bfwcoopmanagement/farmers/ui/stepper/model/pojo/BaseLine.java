package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AvailableRessourcesFragment data
 */

public class BaseLine implements Parcelable {

    private int totProdInKg;
    private int totLostInKg;
    private int totSoldInKg;
    private int totVolumeSoldCoopInKg;
    private int priceSoldToCoopPerKg;
    private int totVolSoldInKg;
    private int priceSoldInKg;


    public BaseLine(){

    }

    public BaseLine(int totProdInKg, int totLostInKg, int totSoldInKg, int totVolumeSoldCoopInKg,
                    int priceSoldToCoopPerKg, int totVolSoldInKg, int priceSoldInKg) {
        this.totProdInKg = totProdInKg;
        this.totLostInKg = totLostInKg;
        this.totSoldInKg = totSoldInKg;
        this.totVolumeSoldCoopInKg = totVolumeSoldCoopInKg;
        this.priceSoldToCoopPerKg = priceSoldToCoopPerKg;
        this.totVolSoldInKg = totVolSoldInKg;
        this.priceSoldInKg = priceSoldInKg;
    }

    public BaseLine(Parcel data) {
        this.totProdInKg = data.readInt();
        this.totLostInKg = data.readInt();
        this.totSoldInKg = data.readInt();
        this.totVolumeSoldCoopInKg = data.readInt();
        this.priceSoldToCoopPerKg = data.readInt();
        this.totVolSoldInKg = data.readInt();
        this.priceSoldInKg = data.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totProdInKg);
        parcel.writeInt(totLostInKg);
        parcel.writeInt(totSoldInKg);
        parcel.writeInt(totVolumeSoldCoopInKg);
        parcel.writeInt(priceSoldToCoopPerKg);
        parcel.writeInt(totVolSoldInKg);
        parcel.writeInt(priceSoldInKg);
    }

    public int getTotProdInKg() {
        return totProdInKg;
    }

    public void setTotProdInKg(int totProdInKg) {
        this.totProdInKg = totProdInKg;
    }

    public int getTotLostInKg() {
        return totLostInKg;
    }

    public void setTotLostInKg(int totLostInKg) {
        this.totLostInKg = totLostInKg;
    }

    public int getTotSoldInKg() {
        return totSoldInKg;
    }

    public void setTotSoldInKg(int totSoldInKg) {
        this.totSoldInKg = totSoldInKg;
    }

    public int getTotVolumeSoldCoopInKg() {
        return totVolumeSoldCoopInKg;
    }

    public void setTotVolumeSoldCoopInKg(int totVolumeSoldCoopInKg) {
        this.totVolumeSoldCoopInKg = totVolumeSoldCoopInKg;
    }

    public int getPriceSoldToCoopPerKg() {
        return priceSoldToCoopPerKg;
    }

    public void setPriceSoldToCoopPerKg(int priceSoldToCoopPerKg) {
        this.priceSoldToCoopPerKg = priceSoldToCoopPerKg;
    }

    public int getTotVolSoldInKg() {
        return totVolSoldInKg;
    }

    public void setTotVolSoldInKg(int totVolSoldInKg) {
        this.totVolSoldInKg = totVolSoldInKg;
    }

    public int getPriceSoldInKg() {
        return priceSoldInKg;
    }

    public void setPriceSoldInKg(int priceSoldInKg) {
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
