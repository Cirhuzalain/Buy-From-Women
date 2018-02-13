package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ForecastVendor PageVendorVendor
 */

public class ForecastVendor implements Parcelable {

    private double arableLandPlot;
    private double landFarmerSize;
    private double totProdKg;
    private double salesOutsidePpp;
    private double postHarvestLossInKg;
    private double totCoopLandSize;
    private double farmerPercentCoopLandSize;

    public ForecastVendor() {

    }

    public ForecastVendor(double arableLandPlot, double landFarmerSize, double totProdKg, double salesOutsidePpp,
                          double postHarvestLossInKg, double totCoopLandSize, double farmerPercentCoopLandSize) {
        this.arableLandPlot = arableLandPlot;
        this.landFarmerSize = landFarmerSize;
        this.totProdKg = totProdKg;
        this.salesOutsidePpp = salesOutsidePpp;
        this.postHarvestLossInKg = postHarvestLossInKg;
        this.totCoopLandSize = totCoopLandSize;
        this.farmerPercentCoopLandSize = farmerPercentCoopLandSize;
    }

    public ForecastVendor(Parcel data) {
        this.arableLandPlot = data.readDouble();
        this.landFarmerSize = data.readDouble();
        this.totProdKg = data.readDouble();
        this.salesOutsidePpp = data.readDouble();
        this.postHarvestLossInKg = data.readDouble();
        this.totCoopLandSize = data.readDouble();
        this.farmerPercentCoopLandSize = data.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(arableLandPlot);
        parcel.writeDouble(landFarmerSize);
        parcel.writeDouble(totProdKg);
        parcel.writeDouble(salesOutsidePpp);
        parcel.writeDouble(postHarvestLossInKg);
        parcel.writeDouble(totCoopLandSize);
        parcel.writeDouble(farmerPercentCoopLandSize);
    }

    public double getArableLandPlot() {
        return arableLandPlot;
    }

    public void setArableLandPlot(double arableLandPlot) {
        this.arableLandPlot = arableLandPlot;
    }

    public double getLandFarmerSize() {
        return landFarmerSize;
    }

    public void setLandFarmerSize(double landFarmerSize) {
        this.landFarmerSize = landFarmerSize;
    }

    public double getTotProdKg() {
        return totProdKg;
    }

    public void setTotProdKg(double totProdKg) {
        this.totProdKg = totProdKg;
    }

    public double getSalesOutsidePpp() {
        return salesOutsidePpp;
    }

    public void setSalesOutsidePpp(double salesOutsidePpp) {
        this.salesOutsidePpp = salesOutsidePpp;
    }

    public double getPostHarvestLossInKg() {
        return postHarvestLossInKg;
    }

    public void setPostHarvestLossInKg(double postHarvestLossInKg) {
        this.postHarvestLossInKg = postHarvestLossInKg;
    }

    public double getTotCoopLandSize() {
        return totCoopLandSize;
    }

    public void setTotCoopLandSize(double totCoopLandSize) {
        this.totCoopLandSize = totCoopLandSize;
    }

    public double getFarmerPercentCoopLandSize() {
        return farmerPercentCoopLandSize;
    }

    public void setFarmerPercentCoopLandSize(double farmerPercentCoopLandSize) {
        this.farmerPercentCoopLandSize = farmerPercentCoopLandSize;
    }

    public static final Creator<ForecastVendor> CREATOR = new Creator<ForecastVendor>() {
        @Override
        public ForecastVendor createFromParcel(Parcel parcel) {
            return new ForecastVendor(parcel);
        }

        @Override
        public ForecastVendor[] newArray(int i) {
            return new ForecastVendor[0];
        }
    };
}