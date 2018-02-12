package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessToInformation implements Parcelable {

    private boolean isAgricultureExtension;
    private boolean isClimateRelatedInformation;
    private boolean isSeed;
    private boolean isOrganicFertilizers;
    private boolean isInorganicFertilizers;
    private boolean isLabour;
    private boolean isWaterPumps;
    private boolean isSpreaderOrSprayer;

    private String harvsetSeason;


    public AccessToInformation() {

    }

    public AccessToInformation(String harvsetSeason, boolean isAgricultureExtension, boolean isClimateRelatedInformation, boolean isSeed,
                               boolean isOrganicFertilizers, boolean isInorganicFertilizers, boolean isLabour,
                               boolean isWaterPumps, boolean isSpreaderOrSprayer){
        this.isAgricultureExtension = isAgricultureExtension;
        this.isClimateRelatedInformation = isClimateRelatedInformation;
        this.isSeed = isSeed;
        this.isOrganicFertilizers = isOrganicFertilizers;
        this.isInorganicFertilizers = isInorganicFertilizers;
        this.isLabour = isLabour;
        this.isWaterPumps = isWaterPumps;
        this.isSpreaderOrSprayer = isSpreaderOrSprayer;

        this.harvsetSeason = harvsetSeason;
    }


    public AccessToInformation(Parcel source) {
        //this.landSize = data.readDouble();
        this.isAgricultureExtension = source.readByte() != 0;
        this.isClimateRelatedInformation = source.readByte() != 0;
        this.isSeed = source.readByte() != 0;
        this.isOrganicFertilizers = source.readByte() != 0;
        this.isInorganicFertilizers = source.readByte() != 0;
        this.isLabour = source.readByte() != 0;
        this.isWaterPumps = source.readByte() != 0;
        this.isSpreaderOrSprayer = source.readByte() != 0;

        this.harvsetSeason = source.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeDouble(landSize);
        parcel.writeString(harvsetSeason);
        parcel.writeByte((byte) (isAgricultureExtension ? 1 : 0));
        parcel.writeByte((byte) (isClimateRelatedInformation ? 1 : 0));
        parcel.writeByte((byte) (isSeed ? 1 : 0));
        parcel.writeByte((byte) (isOrganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isInorganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isLabour ? 1 : 0));
        parcel.writeByte((byte) (isSpreaderOrSprayer ? 1 : 0));

    }

    public String getHarvsetSeason() {
        return harvsetSeason;
    }

    public void setHarvsetSeason(String harvsetSeason) {
        this.harvsetSeason = harvsetSeason;
    }

    public boolean isAgricultureExtension() {
        return isAgricultureExtension;
    }

    public void setAgricultureExtension(boolean agricultureExtension) {
        isAgricultureExtension = agricultureExtension;
    }

    public boolean isClimateRelatedInformation() {
        return isClimateRelatedInformation;
    }

    public void setClimateRelatedInformation(boolean climateRelatedInformation) {
        isClimateRelatedInformation = climateRelatedInformation;
    }

    public boolean isSeed() {
        return isSeed;
    }

    public void setSeed(boolean seed) {
        isSeed = seed;
    }

    public boolean isOrganicFertilizers() {
        return isOrganicFertilizers;
    }

    public void setOrganicFertilizers(boolean organicFertilizers) {
        isOrganicFertilizers = organicFertilizers;
    }

    public boolean isInorganicFertilizers() {
        return isInorganicFertilizers;
    }

    public void setInorganicFertilizers(boolean inorganicFertilizers) {
        isInorganicFertilizers = inorganicFertilizers;
    }

    public boolean isLabour() {
        return isLabour;
    }

    public void setLabour(boolean labour) {
        isLabour = labour;
    }

    public boolean isWaterPumps() {
        return isWaterPumps;
    }

    public void setWaterPumps(boolean waterPumps) {
        isWaterPumps = waterPumps;
    }


    public boolean isSpreaderOrSprayer() {
        return isSpreaderOrSprayer;
    }

    public void setSpreaderOrSprayer(boolean spreaderOrSprayer) {
        isSpreaderOrSprayer = spreaderOrSprayer;
    }

    public static final Creator<AccessToInformation> CREATOR = new Creator<AccessToInformation>() {
        @Override
        public AccessToInformation createFromParcel(Parcel parcel) {
            return new AccessToInformation(parcel);
        }

        @Override
        public AccessToInformation[] newArray(int i) {
            return new AccessToInformation[0];
        }
    };
}
