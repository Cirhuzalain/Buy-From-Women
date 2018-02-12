package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BlankInfoGuy implements Parcelable {

    /*  private String accountNumber
        private boolean isOfficeSpace;
        private String bankName;
        private int totProdInKg;
        private double interestRate;

*/


   //constructeur vide et contructeur non vide

    public BlankInfoGuy(Parcel data) {
        /*
        this.totProdInKg = data.readInt();
        this.isOfficeSpace = data.readByte() != 0;
        this.accountNumber = data.readString();
        this.bankName = data.readString();
        this.interestRate = data.readDouble();
*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       /*
        parcel.writeInt(totProdInKg);
        parcel.writeByte((byte) (isOfficeSpace  ? 1 : 0));
        parcel.writeString(accountNumber);
        parcel.writeString(bankName);
        parcel.writeDouble(interestRate);
*/
    }
   //setters et getters

    public static final Creator<BlankInfoGuy> CREATOR = new Creator<BlankInfoGuy>() {
        @Override
        public BlankInfoGuy createFromParcel(Parcel parcel) {
            return new BlankInfoGuy(parcel);
        }

        @Override
        public BlankInfoGuy[] newArray(int i) {
            return new BlankInfoGuy[0];
        }
    };
}
