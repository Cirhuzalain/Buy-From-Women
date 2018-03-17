package com.nijus.alino.bfwcoopmanagement.loans.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class PojoLoan implements Parcelable {

    public static final Creator<PojoLoan> CREATOR = new Creator<PojoLoan>() {
        @Override
        public PojoLoan createFromParcel(Parcel parcel) {
            return new PojoLoan(parcel);
        }

        @Override
        public PojoLoan[] newArray(int i) {
            return new PojoLoan[0];
        }
    };
    private int farmer_id;
    private int coop_id;
    private int vendor_id;
    private Long start_date;
    private Double amount;
    private Double interest_rate;
    private Double duration;
    private String purpose;
    private String financial_institution;

    public PojoLoan() {

    }

    public PojoLoan(int farmer_id, int coop_id, int vendor_id, Long start_date, Double amount,
                    Double interest_rate, Double duration, String purpose, String financial_institution) {
        this.farmer_id = farmer_id;
        this.coop_id = coop_id;
        this.vendor_id = vendor_id;
        this.start_date = start_date;
        this.amount = amount;
        this.interest_rate = interest_rate;
        this.duration = duration;
        this.purpose = purpose;
        this.financial_institution = financial_institution;
    }

    public PojoLoan(Parcel data) {
        this.farmer_id = data.readInt();
        this.coop_id = data.readInt();
        this.vendor_id = data.readInt();
        this.start_date = data.readLong();
        this.amount = data.readDouble();
        this.interest_rate = data.readDouble();
        this.duration = data.readDouble();
        this.purpose = data.readString();
        this.financial_institution = data.readString();

    }

    public static Creator<PojoLoan> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(farmer_id);
        parcel.writeInt(coop_id);
        parcel.writeInt(vendor_id);
        parcel.writeLong(start_date);
        parcel.writeDouble(amount);
        parcel.writeDouble(interest_rate);
        parcel.writeDouble(duration);
        parcel.writeString(purpose);
        parcel.writeString(financial_institution);
    }

    public int getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(int farmer_id) {
        this.farmer_id = farmer_id;
    }

    public int getCoop_id() {
        return coop_id;
    }

    public void setCoop_id(int coop_id) {
        this.coop_id = coop_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long start_date) {
        this.start_date = start_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(Double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getFinancial_institution() {
        return financial_institution;
    }

    public void setFinancial_institution(String financial_institution) {
        this.financial_institution = financial_institution;
    }

}
