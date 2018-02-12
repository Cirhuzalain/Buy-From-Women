package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BaselineSales implements Parcelable {
    private String harvsetSeason;
    private String rgccContractUnderFtma;

    private int qtyAgregatedFromMember;
    private int cycleHarvsetAtPricePerKg;
    private int qtyPurchaseFromNonMember;
    private int nonMemberAtPricePerKg;
    private double qtyOfRgccContract;
    private double qtySoldToRgcc;
    private double pricePerKgSoldToRgcc;
    private double qtySoldOutOfRgcc;
    private double pricePerKkSoldOutFtma;

    private boolean isFormalBuyer;
    private boolean isInformalBuyer;
    private boolean isOther;


   //constructeur vide et contructeur non vide


    public BaselineSales() {
    }

    public BaselineSales(String harvsetSeason, String rgccContractUnderFtma, int qtyAgregatedFromMember, int cycleHarvsetAtPricePerKg, int qtyPurchaseFromNonMember, int nonMemberAtPricePerKg, double qtyOfRgccContract, double qtySoldToRgcc, double pricePerKgSoldToRgcc, double qtySoldOutOfRgcc, double pricePerKkSoldOutFtma, int textOther, boolean isFormalBuyer, boolean isInformalBuyer, boolean isOther) {
        this.harvsetSeason = harvsetSeason;
        this.rgccContractUnderFtma = rgccContractUnderFtma;
        this.qtyAgregatedFromMember = qtyAgregatedFromMember;
        this.cycleHarvsetAtPricePerKg = cycleHarvsetAtPricePerKg;
        this.qtyPurchaseFromNonMember = qtyPurchaseFromNonMember;
        this.nonMemberAtPricePerKg = nonMemberAtPricePerKg;
        this.qtyOfRgccContract = qtyOfRgccContract;
        this.qtySoldToRgcc = qtySoldToRgcc;
        this.pricePerKgSoldToRgcc = pricePerKgSoldToRgcc;
        this.qtySoldOutOfRgcc = qtySoldOutOfRgcc;
        this.pricePerKkSoldOutFtma = pricePerKkSoldOutFtma;
        this.isFormalBuyer = isFormalBuyer;
        this.isInformalBuyer = isInformalBuyer;
        this.isOther = isOther;
    }

    public BaselineSales(Parcel data) {

        this.harvsetSeason =data.readString();
        this.rgccContractUnderFtma =data.readString();
        this.qtyAgregatedFromMember = data.readInt();
        this.cycleHarvsetAtPricePerKg = data.readInt();
        this.qtyPurchaseFromNonMember =data.readInt();
        this.nonMemberAtPricePerKg =data.readInt();
        this.qtyOfRgccContract =data.readDouble();
        this.qtySoldToRgcc =data.readDouble();
        this.pricePerKgSoldToRgcc =data.readDouble();
        this.qtySoldOutOfRgcc =data.readDouble();
        this.pricePerKkSoldOutFtma =data.readDouble();
        this.isFormalBuyer =data.readByte() != 0;
        this.isInformalBuyer =data.readByte() != 0;
        this.isOther =data.readByte() != 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(harvsetSeason);
        parcel.writeString(rgccContractUnderFtma);
        parcel.writeInt(qtyAgregatedFromMember);
        parcel.writeInt(cycleHarvsetAtPricePerKg);
        parcel.writeInt(qtyPurchaseFromNonMember);
        parcel.writeInt(nonMemberAtPricePerKg);
        parcel.writeDouble(qtyOfRgccContract);
        parcel.writeDouble(qtySoldToRgcc);
        parcel.writeDouble(pricePerKgSoldToRgcc );
        parcel.writeDouble(qtySoldOutOfRgcc );
        parcel.writeDouble(pricePerKkSoldOutFtma );
        parcel.writeByte((byte) (isFormalBuyer ? 1 : 0));
        parcel.writeByte((byte) (isInformalBuyer ? 1 : 0));
        parcel.writeByte((byte) (isOther? 1 : 0));
    }
   //setters et getters


    public String getHarvsetSeason() {
        return harvsetSeason;
    }

    public void setHarvsetSeason(String harvsetSeason) {
        this.harvsetSeason = harvsetSeason;
    }

    public String getRgccContractUnderFtma() {
        return rgccContractUnderFtma;
    }

    public void setRgccContractUnderFtma(String rgccContractUnderFtma) {
        this.rgccContractUnderFtma = rgccContractUnderFtma;
    }

    public int getQtyAgregatedFromMember() {
        return qtyAgregatedFromMember;
    }

    public void setQtyAgregatedFromMember(int qtyAgregatedFromMember) {
        this.qtyAgregatedFromMember = qtyAgregatedFromMember;
    }

    public int getCycleHarvsetAtPricePerKg() {
        return cycleHarvsetAtPricePerKg;
    }

    public void setCycleHarvsetAtPricePerKg(int cycleHarvsetAtPricePerKg) {
        this.cycleHarvsetAtPricePerKg = cycleHarvsetAtPricePerKg;
    }

    public int getQtyPurchaseFromNonMember() {
        return qtyPurchaseFromNonMember;
    }

    public void setQtyPurchaseFromNonMember(int qtyPurchaseFromNonMember) {
        this.qtyPurchaseFromNonMember = qtyPurchaseFromNonMember;
    }

    public int getNonMemberAtPricePerKg() {
        return nonMemberAtPricePerKg;
    }

    public void setNonMemberAtPricePerKg(int nonMemberAtPricePerKg) {
        this.nonMemberAtPricePerKg = nonMemberAtPricePerKg;
    }

    public double getQtyOfRgccContract() {
        return qtyOfRgccContract;
    }

    public void setQtyOfRgccContract(double qtyOfRgccContract) {
        this.qtyOfRgccContract = qtyOfRgccContract;
    }

    public double getQtySoldToRgcc() {
        return qtySoldToRgcc;
    }

    public void setQtySoldToRgcc(double qtySoldToRgcc) {
        this.qtySoldToRgcc = qtySoldToRgcc;
    }

    public double getPricePerKgSoldToRgcc() {
        return pricePerKgSoldToRgcc;
    }

    public void setPricePerKgSoldToRgcc(double pricePerKgSoldToRgcc) {
        this.pricePerKgSoldToRgcc = pricePerKgSoldToRgcc;
    }

    public double getQtySoldOutOfRgcc() {
        return qtySoldOutOfRgcc;
    }

    public void setQtySoldOutOfRgcc(double qtySoldOutOfRgcc) {
        this.qtySoldOutOfRgcc = qtySoldOutOfRgcc;
    }

    public double getPricePerKkSoldOutFtma() {
        return pricePerKkSoldOutFtma;
    }

    public void setPricePerKkSoldOutFtma(double pricePerKkSoldOutFtma) {
        this.pricePerKkSoldOutFtma = pricePerKkSoldOutFtma;
    }

    public boolean isFormalBuyer() {
        return isFormalBuyer;
    }

    public void setFormalBuyer(boolean formalBuyer) {
        isFormalBuyer = formalBuyer;
    }

    public boolean isInformalBuyer() {
        return isInformalBuyer;
    }

    public void setInformalBuyer(boolean informalBuyer) {
        isInformalBuyer = informalBuyer;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public static final Creator<BaselineSales> CREATOR = new Creator<BaselineSales>() {
        @Override
        public BaselineSales createFromParcel(Parcel parcel) {
            return new BaselineSales(parcel);
        }

        @Override
        public BaselineSales[] newArray(int i) {
            return new BaselineSales[0];
        }
    };
}
