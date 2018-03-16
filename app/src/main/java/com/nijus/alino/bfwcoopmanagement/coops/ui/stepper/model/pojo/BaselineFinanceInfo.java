package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class BaselineFinanceInfo implements Parcelable {
    public static final Creator<BaselineFinanceInfo> CREATOR = new Creator<BaselineFinanceInfo>() {
        @Override
        public BaselineFinanceInfo createFromParcel(Parcel parcel) {
            return new BaselineFinanceInfo(parcel);
        }

        @Override
        public BaselineFinanceInfo[] newArray(int i) {
            return new BaselineFinanceInfo[0];
        }
    };
    private String seasonName;
    private String input_loan;
    private boolean isInput_loan_prov_bank;
    private boolean isInput_loan_prov_cooperative;
    private boolean isInput_loan_prov_sacco;
    private boolean isInput_loan_prov_other;
    private double input_loan_amount;
    private double input_loan_interest_rate;
    private int input_loan_duration;
    private boolean sInput_loan_purpose_labour;
    private boolean sInput_loan_purpose_seed;
    private boolean sInput_loan_purpose_input;
    private boolean sInput_loan_purpose_machinery;
    private boolean sInput_loan_purpose_other;
    private boolean isInput_prov_in_kind;
    private boolean isCash_provided_purchase_inputs;
    private String aggrgation_post_harvset_loan;
    private boolean isAgg_post_harv_loan_prov_bank;
    private boolean isAgg_post_harv_loan_prov_cooperative;
    private boolean isAgg_post_harv_loan_prov_sacco;
    private boolean isAgg_post_harv_loan_prov_other;
    private double aggrgation_post_harvset_amount;
    private double aggrgation_post_harvset_loan_interest;
    private int aggrgation_post_harvset_loan_duration;
    private boolean isAgg_post_harv_loan_purpose_labour;
    private boolean isAgg_post_harv_loan_purpose_input;
    private boolean isAgg_post_harv_loan_purpose_machinery;
    private boolean isAgg_post_harv_loan_purpose_other;
    private String aggrgation_post_harvset_laon_disbursement_method;
    private int baselineFinanceInfoId;
    private int seasonId;

    public BaselineFinanceInfo() {
        this.baselineFinanceInfoId = 0;
    }

    public BaselineFinanceInfo(String seasonName, String input_loan, boolean isInput_loan_prov_bank,
                               boolean isInput_loan_prov_cooperative, boolean isInput_loan_prov_sacco,
                               boolean isInput_loan_prov_other, double input_loan_amount,
                               double input_loan_interest_rate, int input_loan_duration,
                               boolean sInput_loan_purpose_labour, boolean sInput_loan_purpose_seed,
                               boolean sInput_loan_purpose_input, boolean sInput_loan_purpose_machinery,
                               boolean sInput_loan_purpose_other, boolean isInput_prov_in_kind,
                               boolean isCash_provided_purchase_inputs,
                               String aggrgation_post_harvset_loan,
                               boolean isAgg_post_harv_loan_prov_bank,
                               boolean isAgg_post_harv_loan_prov_cooperative,
                               boolean isAgg_post_harv_loan_prov_sacco,
                               boolean isAgg_post_harv_loan_prov_other,
                               double aggrgation_post_harvset_amount,
                               double aggrgation_post_harvset_loan_interest,
                               int aggrgation_post_harvset_loan_duration,
                               boolean isAgg_post_harv_loan_purpose_labour,
                               boolean isAgg_post_harv_loan_purpose_input,
                               boolean isAgg_post_harv_loan_purpose_machinery,
                               boolean isAgg_post_harv_loan_purpose_other,
                               String aggrgation_post_harvset_laon_disbursement_method) {
        this.seasonName = seasonName;
        this.input_loan = input_loan;
        this.isInput_loan_prov_bank = isInput_loan_prov_bank;
        this.isInput_loan_prov_cooperative = isInput_loan_prov_cooperative;
        this.isInput_loan_prov_sacco = isInput_loan_prov_sacco;
        this.isInput_loan_prov_other = isInput_loan_prov_other;
        this.input_loan_amount = input_loan_amount;
        this.input_loan_interest_rate = input_loan_interest_rate;
        this.input_loan_duration = input_loan_duration;
        this.sInput_loan_purpose_labour = sInput_loan_purpose_labour;
        this.sInput_loan_purpose_seed = sInput_loan_purpose_seed;
        this.sInput_loan_purpose_input = sInput_loan_purpose_input;
        this.sInput_loan_purpose_machinery = sInput_loan_purpose_machinery;
        this.sInput_loan_purpose_other = sInput_loan_purpose_other;
        this.isInput_prov_in_kind = isInput_prov_in_kind;
        this.isCash_provided_purchase_inputs = isCash_provided_purchase_inputs;
        this.aggrgation_post_harvset_loan = aggrgation_post_harvset_loan;
        this.isAgg_post_harv_loan_prov_bank = isAgg_post_harv_loan_prov_bank;
        this.isAgg_post_harv_loan_prov_cooperative = isAgg_post_harv_loan_prov_cooperative;
        this.isAgg_post_harv_loan_prov_sacco = isAgg_post_harv_loan_prov_sacco;
        this.isAgg_post_harv_loan_prov_other = isAgg_post_harv_loan_prov_other;
        this.aggrgation_post_harvset_amount = aggrgation_post_harvset_amount;
        this.aggrgation_post_harvset_loan_interest = aggrgation_post_harvset_loan_interest;
        this.aggrgation_post_harvset_loan_duration = aggrgation_post_harvset_loan_duration;
        this.isAgg_post_harv_loan_purpose_labour = isAgg_post_harv_loan_purpose_labour;
        this.isAgg_post_harv_loan_purpose_input = isAgg_post_harv_loan_purpose_input;
        this.isAgg_post_harv_loan_purpose_machinery = isAgg_post_harv_loan_purpose_machinery;
        this.isAgg_post_harv_loan_purpose_other = isAgg_post_harv_loan_purpose_other;
        this.aggrgation_post_harvset_laon_disbursement_method = aggrgation_post_harvset_laon_disbursement_method;
    }

    public BaselineFinanceInfo(Parcel data) {

        this.seasonName = data.readString();
        this.input_loan = data.readString();

        this.isInput_loan_prov_bank = data.readByte() != 0;
        this.isInput_loan_prov_cooperative = data.readByte() != 0;
        this.isInput_loan_prov_sacco = data.readByte() != 0;
        this.isInput_loan_prov_other = data.readByte() != 0;

        this.input_loan_amount = data.readDouble();
        this.input_loan_interest_rate = data.readDouble();
        this.input_loan_duration = data.readInt();

        this.sInput_loan_purpose_labour = data.readByte() != 0;
        this.sInput_loan_purpose_seed = data.readByte() != 0;
        this.sInput_loan_purpose_input = data.readByte() != 0;
        this.sInput_loan_purpose_machinery = data.readByte() != 0;
        this.sInput_loan_purpose_other = data.readByte() != 0;

        this.isInput_prov_in_kind = data.readByte() != 0;
        this.isCash_provided_purchase_inputs = data.readByte() != 0;

        this.aggrgation_post_harvset_loan = data.readString();

        this.isAgg_post_harv_loan_prov_bank = data.readByte() != 0;
        this.isAgg_post_harv_loan_prov_cooperative = data.readByte() != 0;
        this.isAgg_post_harv_loan_prov_sacco = data.readByte() != 0;
        this.isAgg_post_harv_loan_prov_other = data.readByte() != 0;

        this.aggrgation_post_harvset_amount = data.readDouble();
        this.aggrgation_post_harvset_loan_interest = data.readDouble();
        this.aggrgation_post_harvset_loan_duration = data.readInt();

        this.isAgg_post_harv_loan_purpose_labour = data.readByte() != 0;
        this.isAgg_post_harv_loan_purpose_input = data.readByte() != 0;
        this.isAgg_post_harv_loan_purpose_machinery = data.readByte() != 0;
        this.isAgg_post_harv_loan_purpose_other = data.readByte() != 0;

        this.aggrgation_post_harvset_laon_disbursement_method = data.readString();
        this.baselineFinanceInfoId = data.readInt();
        this.seasonId = data.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(seasonName);
        parcel.writeString(input_loan);

        parcel.writeByte((byte) (isInput_loan_prov_bank ? 1 : 0));
        parcel.writeByte((byte) (isInput_loan_prov_cooperative ? 1 : 0));
        parcel.writeByte((byte) (isInput_loan_prov_sacco ? 1 : 0));
        parcel.writeByte((byte) (isInput_loan_prov_other ? 1 : 0));

        parcel.writeDouble(input_loan_amount);
        parcel.writeDouble(input_loan_interest_rate);
        parcel.writeInt(input_loan_duration);

        parcel.writeByte((byte) (sInput_loan_purpose_labour ? 1 : 0));
        parcel.writeByte((byte) (sInput_loan_purpose_seed ? 1 : 0));
        parcel.writeByte((byte) (sInput_loan_purpose_input ? 1 : 0));
        parcel.writeByte((byte) (sInput_loan_purpose_machinery ? 1 : 0));
        parcel.writeByte((byte) (sInput_loan_purpose_other ? 1 : 0));

        parcel.writeByte((byte) (isInput_prov_in_kind ? 1 : 0));
        parcel.writeByte((byte) (isCash_provided_purchase_inputs ? 1 : 0));

        parcel.writeString(aggrgation_post_harvset_loan);

        parcel.writeByte((byte) (isAgg_post_harv_loan_prov_bank ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_prov_cooperative ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_prov_sacco ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_prov_other ? 1 : 0));

        parcel.writeDouble(aggrgation_post_harvset_amount);
        parcel.writeDouble(aggrgation_post_harvset_loan_interest);
        parcel.writeInt(aggrgation_post_harvset_loan_duration);

        parcel.writeByte((byte) (isAgg_post_harv_loan_purpose_labour ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_purpose_input ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_purpose_machinery ? 1 : 0));
        parcel.writeByte((byte) (isAgg_post_harv_loan_purpose_other ? 1 : 0));

        parcel.writeString(aggrgation_post_harvset_laon_disbursement_method);
        parcel.writeInt(baselineFinanceInfoId);
        parcel.writeInt(seasonId);
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getBaselineFinanceInfoId() {
        return baselineFinanceInfoId;
    }

    public void setBaselineFinanceInfoId(int baselineFinanceInfoId) {
        this.baselineFinanceInfoId = baselineFinanceInfoId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getInput_loan() {
        return input_loan;
    }

    public void setInput_loan(String input_loan) {
        this.input_loan = input_loan;
    }

    public boolean isInput_loan_prov_bank() {
        return isInput_loan_prov_bank;
    }

    public void setInput_loan_prov_bank(boolean input_loan_prov_bank) {
        isInput_loan_prov_bank = input_loan_prov_bank;
    }

    public boolean isInput_loan_prov_cooperative() {
        return isInput_loan_prov_cooperative;
    }

    public void setInput_loan_prov_cooperative(boolean input_loan_prov_cooperative) {
        isInput_loan_prov_cooperative = input_loan_prov_cooperative;
    }

    public boolean isInput_loan_prov_sacco() {
        return isInput_loan_prov_sacco;
    }

    public void setInput_loan_prov_sacco(boolean input_loan_prov_sacco) {
        isInput_loan_prov_sacco = input_loan_prov_sacco;
    }

    public boolean isInput_loan_prov_other() {
        return isInput_loan_prov_other;
    }

    public void setInput_loan_prov_other(boolean input_loan_prov_other) {
        isInput_loan_prov_other = input_loan_prov_other;
    }

    public double getInput_loan_amount() {
        return input_loan_amount;
    }

    public void setInput_loan_amount(double input_loan_amount) {
        this.input_loan_amount = input_loan_amount;
    }

    public double getInput_loan_interest_rate() {
        return input_loan_interest_rate;
    }

    public void setInput_loan_interest_rate(double input_loan_interest_rate) {
        this.input_loan_interest_rate = input_loan_interest_rate;
    }

    public int getInput_loan_duration() {
        return input_loan_duration;
    }

    public void setInput_loan_duration(int input_loan_duration) {
        this.input_loan_duration = input_loan_duration;
    }

    public boolean issInput_loan_purpose_labour() {
        return sInput_loan_purpose_labour;
    }

    public void setsInput_loan_purpose_labour(boolean sInput_loan_purpose_labour) {
        this.sInput_loan_purpose_labour = sInput_loan_purpose_labour;
    }

    public boolean issInput_loan_purpose_seed() {
        return sInput_loan_purpose_seed;
    }

    public void setsInput_loan_purpose_seed(boolean sInput_loan_purpose_seed) {
        this.sInput_loan_purpose_seed = sInput_loan_purpose_seed;
    }

    public boolean issInput_loan_purpose_input() {
        return sInput_loan_purpose_input;
    }

    public void setsInput_loan_purpose_input(boolean sInput_loan_purpose_input) {
        this.sInput_loan_purpose_input = sInput_loan_purpose_input;
    }

    public boolean issInput_loan_purpose_machinery() {
        return sInput_loan_purpose_machinery;
    }

    public void setsInput_loan_purpose_machinery(boolean sInput_loan_purpose_machinery) {
        this.sInput_loan_purpose_machinery = sInput_loan_purpose_machinery;
    }

    public boolean issInput_loan_purpose_other() {
        return sInput_loan_purpose_other;
    }

    public void setsInput_loan_purpose_other(boolean sInput_loan_purpose_other) {
        this.sInput_loan_purpose_other = sInput_loan_purpose_other;
    }

    public boolean isInput_prov_in_kind() {
        return isInput_prov_in_kind;
    }

    public void setInput_prov_in_kind(boolean input_prov_in_kind) {
        isInput_prov_in_kind = input_prov_in_kind;
    }

    public boolean isCash_provided_purchase_inputs() {
        return isCash_provided_purchase_inputs;
    }

    public void setCash_provided_purchase_inputs(boolean cash_provided_purchase_inputs) {
        isCash_provided_purchase_inputs = cash_provided_purchase_inputs;
    }

    public String getAggrgation_post_harvset_loan() {
        return aggrgation_post_harvset_loan;
    }

    public void setAggrgation_post_harvset_loan(String aggrgation_post_harvset_loan) {
        this.aggrgation_post_harvset_loan = aggrgation_post_harvset_loan;
    }

    public boolean isAgg_post_harv_loan_prov_bank() {
        return isAgg_post_harv_loan_prov_bank;
    }

    public void setAgg_post_harv_loan_prov_bank(boolean agg_post_harv_loan_prov_bank) {
        isAgg_post_harv_loan_prov_bank = agg_post_harv_loan_prov_bank;
    }

    public boolean isAgg_post_harv_loan_prov_cooperative() {
        return isAgg_post_harv_loan_prov_cooperative;
    }

    public void setAgg_post_harv_loan_prov_cooperative(boolean agg_post_harv_loan_prov_cooperative) {
        isAgg_post_harv_loan_prov_cooperative = agg_post_harv_loan_prov_cooperative;
    }

    public boolean isAgg_post_harv_loan_prov_sacco() {
        return isAgg_post_harv_loan_prov_sacco;
    }

    public void setAgg_post_harv_loan_prov_sacco(boolean agg_post_harv_loan_prov_sacco) {
        isAgg_post_harv_loan_prov_sacco = agg_post_harv_loan_prov_sacco;
    }

    public boolean isAgg_post_harv_loan_prov_other() {
        return isAgg_post_harv_loan_prov_other;
    }

    public void setAgg_post_harv_loan_prov_other(boolean agg_post_harv_loan_prov_other) {
        isAgg_post_harv_loan_prov_other = agg_post_harv_loan_prov_other;
    }

    public double getAggrgation_post_harvset_amount() {
        return aggrgation_post_harvset_amount;
    }

    public void setAggrgation_post_harvset_amount(double aggrgation_post_harvset_amount) {
        this.aggrgation_post_harvset_amount = aggrgation_post_harvset_amount;
    }

    public double getAggrgation_post_harvset_loan_interest() {
        return aggrgation_post_harvset_loan_interest;
    }

    public void setAggrgation_post_harvset_loan_interest(double aggrgation_post_harvset_loan_interest) {
        this.aggrgation_post_harvset_loan_interest = aggrgation_post_harvset_loan_interest;
    }

    public int getAggrgation_post_harvset_loan_duration() {
        return aggrgation_post_harvset_loan_duration;
    }

    public void setAggrgation_post_harvset_loan_duration(int aggrgation_post_harvset_loan_duration) {
        this.aggrgation_post_harvset_loan_duration = aggrgation_post_harvset_loan_duration;
    }

    public boolean isAgg_post_harv_loan_purpose_labour() {
        return isAgg_post_harv_loan_purpose_labour;
    }

    public void setAgg_post_harv_loan_purpose_labour(boolean agg_post_harv_loan_purpose_labour) {
        isAgg_post_harv_loan_purpose_labour = agg_post_harv_loan_purpose_labour;
    }

    public boolean isAgg_post_harv_loan_purpose_input() {
        return isAgg_post_harv_loan_purpose_input;
    }

    public void setAgg_post_harv_loan_purpose_input(boolean agg_post_harv_loan_purpose_input) {
        isAgg_post_harv_loan_purpose_input = agg_post_harv_loan_purpose_input;
    }

    public boolean isAgg_post_harv_loan_purpose_machinery() {
        return isAgg_post_harv_loan_purpose_machinery;
    }

    public void setAgg_post_harv_loan_purpose_machinery(boolean agg_post_harv_loan_purpose_machinery) {
        isAgg_post_harv_loan_purpose_machinery = agg_post_harv_loan_purpose_machinery;
    }

    public boolean isAgg_post_harv_loan_purpose_other() {
        return isAgg_post_harv_loan_purpose_other;
    }

    public void setAgg_post_harv_loan_purpose_other(boolean agg_post_harv_loan_purpose_other) {
        isAgg_post_harv_loan_purpose_other = agg_post_harv_loan_purpose_other;
    }

    public String getAggrgation_post_harvset_laon_disbursement_method() {
        return aggrgation_post_harvset_laon_disbursement_method;
    }

    public void setAggrgation_post_harvset_laon_disbursement_method(String aggrgation_post_harvset_laon_disbursement_method) {
        this.aggrgation_post_harvset_laon_disbursement_method = aggrgation_post_harvset_laon_disbursement_method;
    }
}
