package com.nijus.alino.bfwcoopmanagement.buyers.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillain-B on 26/02/2018.
 */

public class PojoBuyer implements Parcelable{
    private String name;
    private String phone;
    private String mail;

    public PojoBuyer() {
    }

    public PojoBuyer(String name, String phone, String mail) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
    }

    public PojoBuyer(Parcel data) {
        this.name = data.readString();
        this.phone = data.readString();
        this.mail = data.readString();
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
    }

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

    public static final Parcelable.Creator<PojoBuyer> CREATOR = new Parcelable.Creator<PojoBuyer>() {
        @Override
        public PojoBuyer createFromParcel(Parcel parcel) {
            return new PojoBuyer(parcel);
        }

        @Override
        public PojoBuyer[] newArray(int i) {
            return new PojoBuyer[0];
        }
    };
}
