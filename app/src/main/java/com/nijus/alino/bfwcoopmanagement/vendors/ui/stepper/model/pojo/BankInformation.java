package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bank Information data
 */

public class BankInformation implements Parcelable {

    public static final Creator<BankInformation> CREATOR = new Creator<BankInformation>() {
        @Override
        public BankInformation createFromParcel(Parcel parcel) {
            return new BankInformation(parcel);
        }

        @Override
        public BankInformation[] newArray(int i) {
            return new BankInformation[0];
        }
    };
    private String accountNumber;
    private String bankName;

    public BankInformation() {

    }

    public BankInformation(String accountNumber, String bankName) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    public BankInformation(Parcel data) {
        this.accountNumber = data.readString();
        this.bankName = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accountNumber);
        parcel.writeString(bankName);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
