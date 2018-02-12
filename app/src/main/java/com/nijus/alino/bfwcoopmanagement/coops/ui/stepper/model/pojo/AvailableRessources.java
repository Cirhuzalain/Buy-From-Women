package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AvailableRessourcesFragment data
 */

public class AvailableRessources implements Parcelable {
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

    private String texteSafeStorage;
    private String textOtherResourceInfo;

    public AvailableRessources() {
    }

    public AvailableRessources(boolean isOfficeSpace, boolean isMoistureMeter, boolean isWeightingScales, boolean isQualityInput, boolean isTractor, boolean isHarvester, boolean isDryer, boolean isTresher, boolean isSafeStorage, boolean isOtherResourceInfo, String texteSafeStorage, String textOtherResourceInfo) {
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
        this.texteSafeStorage = texteSafeStorage;
        this.textOtherResourceInfo = textOtherResourceInfo;
    }

    public AvailableRessources(Parcel data) {

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

        this.texteSafeStorage = data.readString();
        this.textOtherResourceInfo = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isOfficeSpace  ? 1 : 0));
        parcel.writeByte((byte) (isMoistureMeter  ? 1 : 0));
        parcel.writeByte((byte) (isWeightingScales  ? 1 : 0));
        parcel.writeByte((byte) (isQualityInput  ? 1 : 0));
        parcel.writeByte((byte) (isTractor  ? 1 : 0));
        parcel.writeByte((byte) (isHarvester  ? 1 : 0));
        parcel.writeByte((byte) (isDryer  ? 1 : 0));
        parcel.writeByte((byte) (isTresher  ? 1 : 0));
        parcel.writeByte((byte) (isSafeStorage  ? 1 : 0));
        parcel.writeByte((byte) (isOtherResourceInfo  ? 1 : 0));

        parcel.writeString(texteSafeStorage);
        parcel.writeString(textOtherResourceInfo);

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

    public String getTexteSafeStorage() {
        return texteSafeStorage;
    }

    public void setTexteSafeStorage(String texteSafeStorage) {
        this.texteSafeStorage = texteSafeStorage;
    }

    public String getTextOtherResourceInfo() {
        return textOtherResourceInfo;
    }

    public void setTextOtherResourceInfo(String textOtherResourceInfo) {
        this.textOtherResourceInfo = textOtherResourceInfo;
    }

    public static final Creator<AvailableRessources> CREATOR = new Creator<AvailableRessources>() {
        @Override
        public AvailableRessources createFromParcel(Parcel parcel) {
            return new AvailableRessources(parcel);
        }

        @Override
        public AvailableRessources[] newArray(int i) {
            return new AvailableRessources[0];
        }
    };
}
