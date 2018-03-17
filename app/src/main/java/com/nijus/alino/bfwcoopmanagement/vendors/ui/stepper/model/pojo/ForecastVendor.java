package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ForecastVendor PageVendorVendor
 */

public class ForecastVendor implements Parcelable {

    public static final Parcelable.Creator<ForecastVendor> CREATOR = new Parcelable.Creator<ForecastVendor>() {
        @Override
        public ForecastVendor createFromParcel(Parcel parcel) {
            return new ForecastVendor(parcel);
        }

        @Override
        public ForecastVendor[] newArray(int i) {
            return new ForecastVendor[0];
        }
    };
    private double arableLandPlot;
    private double totProdKg;
    private double salesOutsidePpp;
    private double postHarvestLossInKg;
    private double totCoopLandSize;
    private double farmerPercentCoopLandSize;
    private double currentPppContrib;
    private double farmerContributionPpp;
    private double farmerexpectedminppp;
    private double minimumflowprice;
    private int harvestSeason;
    private int forecastId;
    private String seasonName;

    public ForecastVendor() {
        this.forecastId = 0;
    }

    public ForecastVendor(double arableLandPlot, double totProdKg, double salesOutsidePpp,
                          double postHarvestLossInKg, double totCoopLandSize, double farmerPercentCoopLandSize,
                          int harvestSeason, double farmerexpectedminppp, double minimumflowprice, double currentPppContrib, double farmerContributionPpp) {
        this.arableLandPlot = arableLandPlot;
        this.totProdKg = totProdKg;
        this.salesOutsidePpp = salesOutsidePpp;
        this.postHarvestLossInKg = postHarvestLossInKg;
        this.totCoopLandSize = totCoopLandSize;
        this.farmerPercentCoopLandSize = farmerPercentCoopLandSize;
        this.harvestSeason = harvestSeason;
        this.farmerexpectedminppp = farmerexpectedminppp;
        this.minimumflowprice = minimumflowprice;
        this.currentPppContrib = currentPppContrib;
        this.farmerContributionPpp = farmerContributionPpp;

    }

    public ForecastVendor(Parcel data) {
        this.arableLandPlot = data.readDouble();
        this.totProdKg = data.readDouble();
        this.salesOutsidePpp = data.readDouble();
        this.postHarvestLossInKg = data.readDouble();
        this.totCoopLandSize = data.readDouble();
        this.farmerPercentCoopLandSize = data.readDouble();
        this.farmerexpectedminppp = data.readDouble();
        this.minimumflowprice = data.readDouble();
        this.currentPppContrib = data.readDouble();
        this.farmerContributionPpp = data.readDouble();
        this.harvestSeason = data.readInt();
        this.forecastId = data.readInt();
        this.seasonName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(arableLandPlot);
        parcel.writeDouble(totProdKg);
        parcel.writeDouble(salesOutsidePpp);
        parcel.writeDouble(postHarvestLossInKg);
        parcel.writeDouble(totCoopLandSize);
        parcel.writeDouble(farmerPercentCoopLandSize);
        parcel.writeDouble(farmerexpectedminppp);
        parcel.writeDouble(minimumflowprice);
        parcel.writeDouble(currentPppContrib);
        parcel.writeDouble(farmerContributionPpp);
        parcel.writeInt(harvestSeason);
        parcel.writeInt(forecastId);
        parcel.writeString(seasonName);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getForecastId() {
        return forecastId;
    }

    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

    public double getCurrentPppContrib() {
        return currentPppContrib;
    }

    public void setCurrentPppContrib(double currentPppContrib) {
        this.currentPppContrib = currentPppContrib;
    }

    public double getFarmerContributionPpp() {
        return farmerContributionPpp;
    }

    public void setFarmerContributionPpp(double farmerContributionPpp) {
        this.farmerContributionPpp = farmerContributionPpp;
    }

    public double getFarmerexpectedminppp() {
        return farmerexpectedminppp;
    }

    public void setFarmerexpectedminppp(double farmerexpectedminppp) {
        this.farmerexpectedminppp = farmerexpectedminppp;
    }

    public double getMinimumflowprice() {
        return minimumflowprice;
    }

    public void setMinimumflowprice(double minimumflowprice) {
        this.minimumflowprice = minimumflowprice;
    }

    public int getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(int harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    public double getArableLandPlot() {
        return arableLandPlot;
    }

    public void setArableLandPlot(double arableLandPlot) {
        this.arableLandPlot = arableLandPlot;
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
}
