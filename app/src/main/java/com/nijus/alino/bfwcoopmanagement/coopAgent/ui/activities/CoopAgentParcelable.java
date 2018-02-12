package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.os.Parcel;
import android.os.Parcelable;


public class CoopAgentParcelable implements Parcelable {
    private String name;
    private String phone;
    private String mail;
    private String coop;


   //constructeur vide et contructeur non vide
    public CoopAgentParcelable() {
    }

    public CoopAgentParcelable(String name, String phone, String mail, String coop) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.coop = coop;
    }

    public CoopAgentParcelable(Parcel data) {
        this.name = data.readString();
        this.phone = data.readString();
        this.mail =data.readString();
        this.coop = data.readString();

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
        parcel.writeString(coop);


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

    public String getCoop() {
        return coop;
    }

    public void setCoop(String coop) {
        this.coop = coop;
    }

    public static final Creator<CoopAgentParcelable> CREATOR = new Creator<CoopAgentParcelable>() {
        @Override
        public CoopAgentParcelable createFromParcel(Parcel parcel) {
            return new CoopAgentParcelable(parcel);
        }

        @Override
        public CoopAgentParcelable[] newArray(int i) {
            return new CoopAgentParcelable[0];
        }
    };
}
