package com.nijus.alino.bfwcoopmanagement.loans.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class PojoLoanPayment implements Parcelable {
    private int loan_id;
    private Double amount;
    private long payment_date;

    public PojoLoanPayment() {}

    public PojoLoanPayment(int loan_id, Double amount, long payment_date) {
        this.loan_id = loan_id;
        this.amount = amount;
        this.payment_date = payment_date;
    }

    public PojoLoanPayment(Parcel data) {
        this.loan_id = data.readInt();
        this.payment_date = data.readLong();
        this.amount = data.readDouble();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(loan_id);
        parcel.writeLong(payment_date);
        parcel.writeDouble(amount);
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(long payment_date) {
        this.payment_date = payment_date;
    }

    public static Creator<PojoLoanPayment> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<PojoLoanPayment> CREATOR = new Creator<PojoLoanPayment>() {
        @Override
        public PojoLoanPayment createFromParcel(Parcel parcel) {
            return new PojoLoanPayment(parcel);
        }

        @Override
        public PojoLoanPayment[] newArray(int i) {
            return new PojoLoanPayment[0];
        }
    };

}
