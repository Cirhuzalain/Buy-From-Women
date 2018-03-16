package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BaselineYield implements Parcelable {
    public static final Creator<BaselineYield> CREATOR = new Creator<BaselineYield>() {
        @Override
        public BaselineYield createFromParcel(Parcel parcel) {
            return new BaselineYield(parcel);
        }

        @Override
        public BaselineYield[] newArray(int i) {
            return new BaselineYield[0];
        }
    };
    private String seasonName;
    private boolean isMaize;
    private boolean isBean;
    private boolean isSoy;
    private boolean isOther;
    private int seasonId;
    private int baselineYieldId;

    public BaselineYield() {
        this.baselineYieldId = 0;
    }

    public BaselineYield(String seasonName, boolean isMaize, boolean isBean, boolean isSoy, boolean isOther) {
        this.seasonName = seasonName;
        this.isMaize = isMaize;
        this.isBean = isBean;
        this.isSoy = isSoy;
        this.isOther = isOther;
    }

    public BaselineYield(Parcel data) {
        this.seasonName = data.readString();
        this.isMaize = data.readByte() != 0;
        this.isBean = data.readByte() != 0;
        this.isSoy = data.readByte() != 0;
        this.isOther = data.readByte() != 0;
        this.seasonId = data.readInt();
        this.baselineYieldId = data.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(seasonName);
        parcel.writeByte((byte) (isMaize ? 1 : 0));
        parcel.writeByte((byte) (isBean ? 1 : 0));
        parcel.writeByte((byte) (isSoy ? 1 : 0));
        parcel.writeByte((byte) (isOther ? 1 : 0));
        parcel.writeInt(seasonId);
        parcel.writeInt(baselineYieldId);

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

    public int getBaselineYieldId() {
        return baselineYieldId;
    }

    public void setBaselineYieldId(int baselineYieldId) {
        this.baselineYieldId = baselineYieldId;
    }

    public boolean isMaize() {
        return isMaize;
    }

    public void setMaize(boolean maize) {
        isMaize = maize;
    }

    public boolean isBean() {
        return isBean;
    }

    public void setBean(boolean bean) {
        isBean = bean;
    }

    public boolean isSoy() {
        return isSoy;
    }

    public void setSoy(boolean soy) {
        isSoy = soy;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }
}
