package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Available Resources Fragment data
 */

public class AvailableResources implements Parcelable {
    public static final Creator<AvailableResources> CREATOR = new Creator<AvailableResources>() {
        @Override
        public AvailableResources createFromParcel(Parcel parcel) {
            return new AvailableResources(parcel);
        }

        @Override
        public AvailableResources[] newArray(int i) {
            return new AvailableResources[0];
        }
    };
    private boolean isOfficeSpace;
    private boolean isMoistureMeter;
    private boolean isWeightingScales;
    private boolean isQualityInput;
    private boolean isTractor;
    private boolean isHarvester;
    private boolean isDryer;
    private boolean isTresher;
    private boolean isSafeStorage;
    private boolean isOtherResourceInfo;
    private String textSafeStorage;
    private String textOtherResourceInfo;
    private String seasonName;

    public AvailableResources() {
    }

    public AvailableResources(boolean isOfficeSpace, boolean isMoistureMeter, boolean isWeightingScales, boolean isQualityInput, boolean isTractor, boolean isHarvester, boolean isDryer, boolean isTresher, boolean isSafeStorage, boolean isOtherResourceInfo, String texteSafeStorage, String textOtherResourceInfo) {
        this.isOfficeSpace = isOfficeSpace;
        this.isMoistureMeter = isMoistureMeter;
        this.isWeightingScales = isWeightingScales;
        this.isQualityInput = isQualityInput;
        this.isTractor = isTractor;
        this.isHarvester = isHarvester;
        this.isDryer = isDryer;
        this.isTresher = isTresher;
        this.isSafeStorage = isSafeStorage;
        this.isOtherResourceInfo = isOtherResourceInfo;
        this.textSafeStorage = texteSafeStorage;
        this.textOtherResourceInfo = textOtherResourceInfo;
    }

    public AvailableResources(Parcel data) {

        this.isOfficeSpace = data.readByte() != 0;
        this.isMoistureMeter = data.readByte() != 0;
        this.isWeightingScales = data.readByte() != 0;
        this.isQualityInput = data.readByte() != 0;
        this.isTractor = data.readByte() != 0;
        this.isHarvester = data.readByte() != 0;
        this.isDryer = data.readByte() != 0;
        this.isTresher = data.readByte() != 0;
        this.isSafeStorage = data.readByte() != 0;
        this.isOtherResourceInfo = data.readByte() != 0;

        this.textSafeStorage = data.readString();
        this.textOtherResourceInfo = data.readString();
        this.seasonName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isOfficeSpace ? 1 : 0));
        parcel.writeByte((byte) (isMoistureMeter ? 1 : 0));
        parcel.writeByte((byte) (isWeightingScales ? 1 : 0));
        parcel.writeByte((byte) (isQualityInput ? 1 : 0));
        parcel.writeByte((byte) (isTractor ? 1 : 0));
        parcel.writeByte((byte) (isHarvester ? 1 : 0));
        parcel.writeByte((byte) (isDryer ? 1 : 0));
        parcel.writeByte((byte) (isTresher ? 1 : 0));
        parcel.writeByte((byte) (isSafeStorage ? 1 : 0));
        parcel.writeByte((byte) (isOtherResourceInfo ? 1 : 0));

        parcel.writeString(textSafeStorage);
        parcel.writeString(textOtherResourceInfo);
        parcel.writeString(seasonName);

    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public boolean isOfficeSpace() {
        return isOfficeSpace;
    }

    public void setOfficeSpace(boolean officeSpace) {
        isOfficeSpace = officeSpace;
    }

    public boolean isMoistureMeter() {
        return isMoistureMeter;
    }

    public void setMoistureMeter(boolean moistureMeter) {
        isMoistureMeter = moistureMeter;
    }

    public boolean isWeightingScales() {
        return isWeightingScales;
    }

    public void setWeightingScales(boolean weightingScales) {
        isWeightingScales = weightingScales;
    }

    public boolean isQualityInput() {
        return isQualityInput;
    }

    public void setQualityInput(boolean qualityInput) {
        isQualityInput = qualityInput;
    }

    public boolean isTractor() {
        return isTractor;
    }

    public void setTractor(boolean tractor) {
        isTractor = tractor;
    }

    public boolean isHarvester() {
        return isHarvester;
    }

    public void setHarvester(boolean harvester) {
        isHarvester = harvester;
    }

    public boolean isDryer() {
        return isDryer;
    }

    public void setDryer(boolean dryer) {
        isDryer = dryer;
    }

    public boolean isTresher() {
        return isTresher;
    }

    public void setTresher(boolean tresher) {
        isTresher = tresher;
    }

    public boolean isSafeStorage() {
        return isSafeStorage;
    }

    public void setSafeStorage(boolean safeStorage) {
        isSafeStorage = safeStorage;
    }

    public boolean isOtherResourceInfo() {
        return isOtherResourceInfo;
    }

    public void setOtherResourceInfo(boolean otherResourceInfo) {
        isOtherResourceInfo = otherResourceInfo;
    }

    public String getTextSafeStorage() {
        return textSafeStorage;
    }

    public void setTextSafeStorage(String texteSafeStorage) {
        this.textSafeStorage = texteSafeStorage;
    }

    public String getTextOtherResourceInfo() {
        return textOtherResourceInfo;
    }

    public void setTextOtherResourceInfo(String textOtherResourceInfo) {
        this.textOtherResourceInfo = textOtherResourceInfo;
    }
}
