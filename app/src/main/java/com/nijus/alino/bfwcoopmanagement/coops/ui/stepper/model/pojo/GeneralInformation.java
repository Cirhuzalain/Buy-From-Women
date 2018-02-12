package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class GeneralInformation implements Parcelable {
    private String name;
    private String phone;
    private String mail;
    private String adress;
    private double landSize;


   //constructeur vide et contructeur non vide


    public GeneralInformation() {
    }

    public GeneralInformation(String name, String phone, String mail, String adress, double landSize) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.adress = adress;
        this.landSize = landSize;
    }

    public GeneralInformation(Parcel data) {
        this.name = data.readString();
        this.phone = data.readString();
        this.mail =data.readString();
        this.adress = data.readString();
        this.landSize = data.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(mail);
        parcel.writeString(adress);

        parcel.writeDouble(landSize);

    }
    //setters et getters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getLandSize() {
        return landSize;
    }

    public void setLandSize(double landSize) {
        this.landSize = landSize;
    }

    public static final Creator<GeneralInformation> CREATOR = new Creator<GeneralInformation>() {
        @Override
        public GeneralInformation createFromParcel(Parcel parcel) {
            return new GeneralInformation(parcel);
        }

        @Override
        public GeneralInformation[] newArray(int i) {
            return new GeneralInformation[0];
        }
    };
}
