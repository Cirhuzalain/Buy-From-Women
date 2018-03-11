package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessToInformationVendor implements Parcelable {

    private boolean isAgricultureExtension;
    private boolean isClimateRelatedInformation;
    private boolean isSeed;
    private boolean isOrganicFertilizers;
    private boolean isInorganicFertilizers;
    private boolean isLabour;
    private boolean isWaterPumps;
    private boolean isSpreaderOrSprayer;

    private int accessInfoId;
    private int harvestSeason;
    private String seasonName;

    public AccessToInformationVendor() {this.accessInfoId = 0;}
    public AccessToInformationVendor(boolean isAgricultureExtension, boolean isClimateRelatedInformation, boolean isSeed,
                                     boolean isOrganicFertilizers, boolean isInorganicFertilizers, boolean isLabour,
                                     boolean isWaterPumps, boolean isSpreaderOrSprayer, int harvestSeason) {
        this.isAgricultureExtension = isAgricultureExtension;
        this.isClimateRelatedInformation = isClimateRelatedInformation;
        this.isSeed = isSeed;
        this.isOrganicFertilizers = isOrganicFertilizers;
        this.isInorganicFertilizers = isInorganicFertilizers;
        this.isLabour = isLabour;
        this.isWaterPumps = isWaterPumps;
        this.isSpreaderOrSprayer = isSpreaderOrSprayer;
        this.harvestSeason = harvestSeason;
    }

    public AccessToInformationVendor(Parcel source) {
        this.isAgricultureExtension = source.readByte() != 0;
        this.isClimateRelatedInformation = source.readByte() != 0;
        this.isSeed = source.readByte() != 0;
        this.isOrganicFertilizers = source.readByte() != 0;
        this.isInorganicFertilizers = source.readByte() != 0;
        this.isLabour = source.readByte() != 0;
        this.isWaterPumps = source.readByte() != 0;
        this.isSpreaderOrSprayer = source.readByte() != 0;
        this.accessInfoId = source.readInt();
        this.harvestSeason = source.readInt();
        this.seasonName = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeByte((byte) (isAgricultureExtension ? 1 : 0));
        parcel.writeByte((byte) (isClimateRelatedInformation ? 1 : 0));
        parcel.writeByte((byte) (isSeed ? 1 : 0));
        parcel.writeByte((byte) (isOrganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isInorganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isLabour ? 1 : 0));
        parcel.writeByte((byte) (isWaterPumps ? 1 : 0));
        parcel.writeByte((byte) (isSpreaderOrSprayer ? 1 : 0));
        parcel.writeInt(accessInfoId);
        parcel.writeInt(harvestSeason);
        parcel.writeString(seasonName);

    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getAccessInfoId() {
        return accessInfoId;
    }

    public void setAccessInfoId(int accessInfoId) {
        this.accessInfoId = accessInfoId;
    }

    public int getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(int harvestSeason) {
        this.harvestSeason = harvestSeason;
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


    public static final Creator<AccessToInformationVendor> CREATOR = new Creator<AccessToInformationVendor>() {
        @Override
        public AccessToInformationVendor createFromParcel(Parcel parcel) {
            return new AccessToInformationVendor(parcel);
        }

        @Override
        public AccessToInformationVendor[] newArray(int i) {
            return new AccessToInformationVendor[0];
        }
    };
}
