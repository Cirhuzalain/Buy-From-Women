package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Land Information Data
 */

public class LandInformationVendor implements Parcelable {

    public static final Parcelable.Creator<LandInformationVendor> CREATOR = new Parcelable.Creator<LandInformationVendor>() {
        @Override
        public LandInformationVendor createFromParcel(Parcel parcel) {
            return new LandInformationVendor(parcel);
        }

        @Override
        public LandInformationVendor[] newArray(int i) {
            return new LandInformationVendor[0];
        }
    };
    private double landSize;
    private double lat;
    private double lng;
    private int harvestSeason;
    private int landId;
    private String seasonName;

    public LandInformationVendor() {
        this.landId = 0;
    }

    public LandInformationVendor(double landSize, double lat, double lng, int harvestSeason) {
        this.landSize = landSize;
        this.lat = lat;
        this.lng = lng;
        this.harvestSeason = harvestSeason;
    }

    public LandInformationVendor(Parcel data) {
        this.landSize = data.readDouble();
        this.lat = data.readDouble();
        this.lng = data.readDouble();
        this.harvestSeason = data.readInt();
        this.landId = data.readInt();
        this.seasonName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeDouble(landSize);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeInt(harvestSeason);
        parcel.writeInt(landId);
        parcel.writeString(seasonName);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getLandId() {
        return landId;
    }

    public void setLandId(int landId) {
        this.landId = landId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(int harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    public double getLandSize() {
        return landSize;
    }

    public void setLandSize(double landSize) {
        this.landSize = landSize;
    }
}
