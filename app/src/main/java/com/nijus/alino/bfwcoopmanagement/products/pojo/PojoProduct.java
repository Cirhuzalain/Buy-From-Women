package com.nijus.alino.bfwcoopmanagement.products.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class PojoProduct implements Parcelable {
    public static final Parcelable.Creator<PojoProduct> CREATOR = new Parcelable.Creator<PojoProduct>() {
        @Override
        public PojoProduct createFromParcel(Parcel parcel) {
            return new PojoProduct(parcel);
        }

        @Override
        public PojoProduct[] newArray(int i) {
            return new PojoProduct[0];
        }
    };
    private String name;
    private int price;
    private Double quantity;
    private int harvest_season;
    private String grade;
    private int farmer;

    public PojoProduct() {

    }

    public PojoProduct(String name, int price, Double quantity, int harvest_season, String grade, int farmer) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.harvest_season = harvest_season;
        this.grade = grade;
        this.farmer = farmer;
    }

    public PojoProduct(Parcel data) {
        this.name = data.readString();
        this.price = data.readInt();
        this.quantity = data.readDouble();
        this.harvest_season = data.readInt();
        this.grade = data.readString();
        this.farmer = data.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeDouble(quantity);
        parcel.writeInt(harvest_season);
        parcel.writeString(grade);
        parcel.writeInt(farmer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public int getHarvest_season() {
        return harvest_season;
    }

    public void setHarvest_season(int harvest_season) {
        this.harvest_season = harvest_season;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getFarmer() {
        return farmer;
    }

    public void setFarmer(int farmer) {
        this.farmer = farmer;
    }

}
