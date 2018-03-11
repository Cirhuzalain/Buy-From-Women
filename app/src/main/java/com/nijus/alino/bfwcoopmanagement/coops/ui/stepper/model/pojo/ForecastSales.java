package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class ForecastSales implements Parcelable {

    private String seasonName;
    private String minFloorPerGrade;
    private String grade;

    private int commitedContractQty;

    private boolean rgcc;
    private boolean prodev;
    private boolean sarura;
    private boolean aif;
    private boolean eax;
    private boolean none;
    private boolean other;

    private int seasonId;
    private int forecastId;


    public ForecastSales() {
        this.forecastId = 0;
    }

    public ForecastSales(String seasonName, String minFloorPerGrade, String grade, int commitedContractQty, boolean rgcc, boolean prodev, boolean sarura, boolean aif, boolean eax, boolean none, boolean other) {
        this.seasonName = seasonName;
        this.minFloorPerGrade = minFloorPerGrade;
        this.grade = grade;
        this.commitedContractQty = commitedContractQty;
        this.rgcc = rgcc;
        this.prodev = prodev;
        this.sarura = sarura;
        this.aif = aif;
        this.eax = eax;
        this.none = none;
        this.other = other;
    }

    public ForecastSales(Parcel data) {
        this.seasonName = data.readString();
        this.minFloorPerGrade = data.readString();
        this.grade = data.readString();
        this.commitedContractQty = data.readInt();
        this.rgcc = data.readByte() != 0;
        this.prodev = data.readByte() != 0;
        this.sarura = data.readByte() != 0;
        this.aif = data.readByte() != 0;
        this.eax = data.readByte() != 0;
        this.none = data.readByte() != 0;
        this.other = data.readByte() != 0;
        this.seasonId = data.readInt();
        this.forecastId = data.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(seasonName);
        parcel.writeString(minFloorPerGrade);
        parcel.writeString(grade);
        parcel.writeInt(commitedContractQty);
        parcel.writeByte((byte) (rgcc ? 1 : 0));
        parcel.writeByte((byte) (prodev ? 1 : 0));
        parcel.writeByte((byte) (sarura ? 1 : 0));
        parcel.writeByte((byte) (aif ? 1 : 0));
        parcel.writeByte((byte) (eax ? 1 : 0));
        parcel.writeByte((byte) (none ? 1 : 0));
        parcel.writeByte((byte) (other ? 1 : 0));
        parcel.writeInt(seasonId);
        parcel.writeInt(forecastId);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getForecastId() {
        return forecastId;
    }

    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

    public String getMinFloorPerGrade() {
        return minFloorPerGrade;
    }

    public void setMinFloorPerGrade(String minFloorPerGrade) {
        this.minFloorPerGrade = minFloorPerGrade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCommitedContractQty() {
        return commitedContractQty;
    }

    public void setCommitedContractQty(int commitedContractQty) {
        this.commitedContractQty = commitedContractQty;
    }

    public boolean isRgcc() {
        return rgcc;
    }

    public void setRgcc(boolean rgcc) {
        this.rgcc = rgcc;
    }

    public boolean isProdev() {
        return prodev;
    }

    public void setProdev(boolean prodev) {
        this.prodev = prodev;
    }

    public boolean isSarura() {
        return sarura;
    }

    public void setSarura(boolean sarura) {
        this.sarura = sarura;
    }

    public boolean isAif() {
        return aif;
    }

    public void setAif(boolean aif) {
        this.aif = aif;
    }

    public boolean isEax() {
        return eax;
    }

    public void setEax(boolean eax) {
        this.eax = eax;
    }

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public static final Creator<ForecastSales> CREATOR = new Creator<ForecastSales>() {
        @Override
        public ForecastSales createFromParcel(Parcel parcel) {
            return new ForecastSales(parcel);
        }

        @Override
        public ForecastSales[] newArray(int i) {
            return new ForecastSales[0];
        }
    };
}
