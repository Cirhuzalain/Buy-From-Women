package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Service Data
 */

public class ServiceAccess implements Parcelable {

    public static final Parcelable.Creator<ServiceAccess> CREATOR = new Parcelable.Creator<ServiceAccess>() {
        @Override
        public ServiceAccess createFromParcel(Parcel parcel) {
            return new ServiceAccess(parcel);
        }

        @Override
        public ServiceAccess[] newArray(int i) {
            return new ServiceAccess[0];
        }
    };
    private boolean isTractor;
    private boolean isHarvester;
    private boolean isDryer;
    private boolean isTresher;
    private boolean isSafeStorage;
    private boolean isOtherResourceInfo;
    private boolean isDam;
    private boolean isWell;
    private boolean isBoreHole;
    private boolean isPipeBorne;
    private boolean isRiverStream;
    private boolean isIrrigation;
    private boolean hasNoWaterSource;
    private boolean isOtherInfo;
    private String storageDetails;
    private String newResourcesDetails;
    private String mainWaterSourceDetails;
    private String seasonName;

    public ServiceAccess() {

    }

    public ServiceAccess(boolean isTractor, boolean isHarvester,
                         boolean isDryer, boolean isTresher, boolean isSafeStorage, boolean isOtherResourceInfo,
                         boolean isDam, boolean isWell, boolean isBoreHole, boolean isPipeBorne, boolean isRiverStream,
                         boolean isIrrigation, boolean hasNoWaterSource, boolean isOtherInfo, String storageDetails,
                         String newResourcesDetails, String mainWaterSourceDetails) {

        this.isTractor = isTractor;
        this.isHarvester = isHarvester;
        this.isDryer = isDryer;
        this.isTresher = isTresher;
        this.isSafeStorage = isSafeStorage;
        this.isOtherResourceInfo = isOtherResourceInfo;
        this.isDam = isDam;
        this.isWell = isWell;
        this.isBoreHole = isBoreHole;
        this.isPipeBorne = isPipeBorne;
        this.isRiverStream = isRiverStream;
        this.isIrrigation = isIrrigation;
        this.hasNoWaterSource = hasNoWaterSource;
        this.isOtherInfo = isOtherInfo;
        this.storageDetails = storageDetails;
        this.newResourcesDetails = newResourcesDetails;
        this.mainWaterSourceDetails = mainWaterSourceDetails;
    }

    public ServiceAccess(Parcel source) {

        this.isTractor = source.readByte() != 0;
        this.isHarvester = source.readByte() != 0;
        this.isDryer = source.readByte() != 0;
        this.isTresher = source.readByte() != 0;
        this.isSafeStorage = source.readByte() != 0;
        this.isOtherResourceInfo = source.readByte() != 0;
        this.isDam = source.readByte() != 0;
        this.isWell = source.readByte() != 0;
        this.isBoreHole = source.readByte() != 0;
        this.isPipeBorne = source.readByte() != 0;
        this.isRiverStream = source.readByte() != 0;
        this.isIrrigation = source.readByte() != 0;
        this.hasNoWaterSource = source.readByte() != 0;
        this.isOtherInfo = source.readByte() != 0;
        this.storageDetails = source.readString();
        this.mainWaterSourceDetails = source.readString();
        this.newResourcesDetails = source.readString();
        this.seasonName = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeByte((byte) (isTractor ? 1 : 0));
        parcel.writeByte((byte) (isHarvester ? 1 : 0));
        parcel.writeByte((byte) (isDryer ? 1 : 0));
        parcel.writeByte((byte) (isTresher ? 1 : 0));
        parcel.writeByte((byte) (isSafeStorage ? 1 : 0));
        parcel.writeByte((byte) (isOtherResourceInfo ? 1 : 0));
        parcel.writeByte((byte) (isDam ? 1 : 0));
        parcel.writeByte((byte) (isWell ? 1 : 0));
        parcel.writeByte((byte) (isBoreHole ? 1 : 0));
        parcel.writeByte((byte) (isPipeBorne ? 1 : 0));
        parcel.writeByte((byte) (isRiverStream ? 1 : 0));
        parcel.writeByte((byte) (isIrrigation ? 1 : 0));
        parcel.writeByte((byte) (hasNoWaterSource ? 1 : 0));
        parcel.writeByte((byte) (isOtherInfo ? 1 : 0));
        parcel.writeString(storageDetails);
        parcel.writeString(mainWaterSourceDetails);
        parcel.writeString(newResourcesDetails);
        parcel.writeString(seasonName);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getStorageDetails() {
        return storageDetails;
    }

    public void setStorageDetails(String storageDetails) {
        this.storageDetails = storageDetails;
    }

    public String getNewResourcesDetails() {
        return newResourcesDetails;
    }

    public void setNewResourcesDetails(String newResourcesDetails) {
        this.newResourcesDetails = newResourcesDetails;
    }

    public String getMainWaterSourceDetails() {
        return mainWaterSourceDetails;
    }

    public void setMainWaterSourceDetails(String mainWaterSourceDetails) {
        this.mainWaterSourceDetails = mainWaterSourceDetails;
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

    public boolean isDam() {
        return isDam;
    }

    public void setDam(boolean dam) {
        isDam = dam;
    }

    public boolean isWell() {
        return isWell;
    }

    public void setWell(boolean well) {
        isWell = well;
    }

    public boolean isBoreHole() {
        return isBoreHole;
    }

    public void setBoreHole(boolean boreHole) {
        isBoreHole = boreHole;
    }

    public boolean isPipeBorne() {
        return isPipeBorne;
    }

    public void setPipeBorne(boolean pipeBorne) {
        isPipeBorne = pipeBorne;
    }

    public boolean isRiverStream() {
        return isRiverStream;
    }

    public void setRiverStream(boolean riverStream) {
        isRiverStream = riverStream;
    }

    public boolean isIrrigation() {
        return isIrrigation;
    }

    public void setIrrigation(boolean irrigation) {
        isIrrigation = irrigation;
    }

    public boolean isHasNoWaterSource() {
        return hasNoWaterSource;
    }

    public void setHasNoWaterSource(boolean hasNoWaterSource) {
        this.hasNoWaterSource = hasNoWaterSource;
    }

    public boolean isOtherInfo() {
        return isOtherInfo;
    }

    public void setOtherInfo(boolean otherInfo) {
        isOtherInfo = otherInfo;
    }
}
