package com.nijus.alino.bfwcoopmanagement.coopAgent.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillain-B on 27/02/2018.
 */

public class PojoAgent implements Parcelable{
    private String name;
    private String phone;
    private String mail;
    private int coop;

    public PojoAgent() {
    }

    public PojoAgent(String name, String phone, String mail, int coop) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.coop = coop;
    }

    public PojoAgent(Parcel data) {
        this.name = data.readString();
        this.phone = data.readString();
        this.mail = data.readString();
        this.coop = data.readInt();
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
        parcel.writeInt(coop);
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

    public int getCoop() {
        return coop;
    }

    public void setCoop(int coop) {
        this.coop = coop;
    }

    public static final Parcelable.Creator<PojoAgent> CREATOR = new Parcelable.Creator<PojoAgent>() {
        @Override
        public PojoAgent createFromParcel(Parcel parcel) {
            return new PojoAgent(parcel);
        }

        @Override
        public PojoAgent[] newArray(int i) {
            return new PojoAgent[0];
        }
    };
}
