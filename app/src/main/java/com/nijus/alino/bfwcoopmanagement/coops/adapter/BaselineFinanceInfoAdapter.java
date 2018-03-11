package com.nijus.alino.bfwcoopmanagement.coops.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;

import java.util.ArrayList;

public class BaselineFinanceInfoAdapter extends ArrayAdapter<BaselineFinanceInfo> {

    public BaselineFinanceInfoAdapter(Context context, ArrayList<BaselineFinanceInfo> forecastSalesArrayList) {
        super(context, R.layout.baseline_finance_info_coop_item, forecastSalesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final BaselineFinanceInfo baselineFinanceInfo = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.baseline_finance_info_coop_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season_id);

            viewHolder.inputLoan = convertView.findViewById(R.id.input_loan);
            viewHolder.input_loan_prov_bank = convertView.findViewById(R.id.bank_input_loan);
            viewHolder.input_loan_prov_cooperative = convertView.findViewById(R.id.coop_input_loan);
            viewHolder.input_loan_prov_sacco = convertView.findViewById(R.id.sacco_input_loan);
            viewHolder.input_loan_prov_other = convertView.findViewById(R.id.other_input_loan);

            viewHolder.input_loan_amount = convertView.findViewById(R.id.input_loan_amount);
            viewHolder.input_loan_interest_rate = convertView.findViewById(R.id.input_loan_interest_rate);
            viewHolder.input_loan_duration = convertView.findViewById(R.id.input_loan_duration);

            viewHolder.input_loan_purpose_labour = convertView.findViewById(R.id.labour_input_loan_purpose);
            viewHolder.input_loan_purpose_seed = convertView.findViewById(R.id.seed_input_loan_purpose);
            viewHolder.input_loan_purpose_input = convertView.findViewById(R.id.input_input_loan_purpose);
            viewHolder.input_loan_purpose_machinery = convertView.findViewById(R.id.machinery_input_loan_purpose);
            viewHolder.input_loan_purpose_other = convertView.findViewById(R.id.other_input_loan_purpose);


            viewHolder.input_loan_disbursement_method = convertView.findViewById(R.id.input_loan_disbursement_method);
            viewHolder.aggrgation_post_harvset_loan = convertView.findViewById(R.id.aggrgation_post_harvset_loan);

            viewHolder.agg_post_harv_loan_prov_bank = convertView.findViewById(R.id.bank_aggregation_prov);
            viewHolder.agg_post_harv_loan_prov_cooperative = convertView.findViewById(R.id.coop_aggregation_prov);
            viewHolder.agg_post_harv_loan_prov_sacco = convertView.findViewById(R.id.sacco_aggregation_prov);
            viewHolder.agg_post_harv_loan_prov_other = convertView.findViewById(R.id.other_aggregation_prov);


            viewHolder.aggrgation_post_harvset_amount = convertView.findViewById(R.id.aggrgation_post_harvset_loan_amount);
            viewHolder.aggrgation_post_harvset_loan_interest = convertView.findViewById(R.id.agg_post_harvset_loan_interest);
            viewHolder.aggrgation_post_harvset_loan_duration = convertView.findViewById(R.id.agg_post_harvset_loan_duration);


            viewHolder.agg_post_harv_loan_purpose_labour = convertView.findViewById(R.id.labour_agg_harv_loan_purpose);
            viewHolder.agg_post_harv_loan_purpose_input = convertView.findViewById(R.id.input_agg_harv_loan_purpose);
            viewHolder.agg_post_harv_loan_purpose_machinery = convertView.findViewById(R.id.machinery_agg_harv_loan_purpose);
            viewHolder.agg_post_harv_loan_purpose_other = convertView.findViewById(R.id.other_agg_harv_loan_purpose);


            viewHolder.aggrgation_post_harvset_laon_disbursement_method = convertView.findViewById(R.id.agg_post_har_laon_disbrsmnt_met);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (baselineFinanceInfo != null) {

            String seasonName = "Harvest Season " + baselineFinanceInfo.getSeasonName();
            String inputLoanProvider = baselineFinanceInfo.getInput_loan();

            String inputLoanAmount = baselineFinanceInfo.getInput_loan_amount() + "";
            String inputLoanInterest = baselineFinanceInfo.getInput_loan_interest_rate() + "";
            String inputLoanDuration = baselineFinanceInfo.getInput_loan_duration() + "";

            String inputLoanDisburseMethod = baselineFinanceInfo.isCash_provided_purchase_inputs() ? "cash" : "kind";
            String aggregPostHarvestLoan = baselineFinanceInfo.getAggrgation_post_harvset_loan();

            String aggregationPostHarvestLoanAmount = baselineFinanceInfo.getAggrgation_post_harvset_amount() + "";
            String aggregationPostHarvestInterestRate = baselineFinanceInfo.getAggrgation_post_harvset_loan_interest() + "";
            String aggregationPostHarvestDuration = baselineFinanceInfo.getAggrgation_post_harvset_loan_duration() + "";

            String aggregationPostHarvestLoanDisMethod = baselineFinanceInfo.getAggrgation_post_harvset_laon_disbursement_method() + "";


            boolean isInputLoanProviderBank = baselineFinanceInfo.isInput_loan_prov_bank();
            boolean isInputLoanProviderCooperative = baselineFinanceInfo.isInput_loan_prov_cooperative();
            boolean isInputLoanProviderSacco = baselineFinanceInfo.isInput_loan_prov_sacco();
            boolean isInputLoanProviderOther = baselineFinanceInfo.isInput_loan_prov_other();

            boolean isInputLoanPurposeLabour = baselineFinanceInfo.issInput_loan_purpose_labour();
            boolean isInputLoanPurposeSeeds = baselineFinanceInfo.issInput_loan_purpose_seed();
            boolean isInputLoanPurposeInput = baselineFinanceInfo.issInput_loan_purpose_input();
            boolean isInputLoanPurposeMachinery = baselineFinanceInfo.issInput_loan_purpose_machinery();
            boolean isInputLoanPurposeOther = baselineFinanceInfo.issInput_loan_purpose_other();

            boolean isAggregationLoanProviderBank = baselineFinanceInfo.isAgg_post_harv_loan_prov_bank();
            boolean isAggregationLoanProviderCooperative = baselineFinanceInfo.isAgg_post_harv_loan_prov_cooperative();
            boolean isAggregationLoanProviderSacco = baselineFinanceInfo.isAgg_post_harv_loan_prov_sacco();
            boolean isAggOther = baselineFinanceInfo.isAgg_post_harv_loan_prov_other();

            boolean isAggLabour = baselineFinanceInfo.isAgg_post_harv_loan_purpose_labour();
            boolean isAggInput = baselineFinanceInfo.isAgg_post_harv_loan_purpose_input();
            boolean isAggMachinery = baselineFinanceInfo.isAgg_post_harv_loan_purpose_machinery();
            boolean isPsAggOther = baselineFinanceInfo.isAgg_post_harv_loan_prov_other();

            viewHolder.seasonId.setText(seasonName);

            viewHolder.input_loan_prov_bank.setImageResource(isInputLoanProviderBank ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_prov_cooperative.setImageResource(isInputLoanProviderCooperative ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_prov_sacco.setImageResource(isInputLoanProviderSacco ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_prov_other.setImageResource(isInputLoanProviderOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            viewHolder.input_loan_purpose_labour.setImageResource(isInputLoanPurposeLabour ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_purpose_seed.setImageResource(isInputLoanPurposeSeeds ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_purpose_input.setImageResource(isInputLoanPurposeInput ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_purpose_machinery.setImageResource(isInputLoanPurposeMachinery ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.input_loan_purpose_other.setImageResource(isInputLoanPurposeOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            viewHolder.agg_post_harv_loan_prov_bank.setImageResource(isAggregationLoanProviderBank ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_prov_cooperative.setImageResource(isAggregationLoanProviderCooperative ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_prov_sacco.setImageResource(isAggregationLoanProviderSacco ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_prov_other.setImageResource(isAggOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            viewHolder.agg_post_harv_loan_purpose_labour.setImageResource(isAggLabour ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_purpose_input.setImageResource(isAggInput ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_purpose_machinery.setImageResource(isAggMachinery ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.agg_post_harv_loan_purpose_other.setImageResource(isPsAggOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            viewHolder.inputLoan.setText(inputLoanProvider);

            viewHolder.input_loan_amount.setText(inputLoanAmount);
            viewHolder.input_loan_interest_rate.setText(inputLoanInterest);
            viewHolder.input_loan_duration.setText(inputLoanDuration);

            viewHolder.input_loan_disbursement_method.setText(inputLoanDisburseMethod);
            viewHolder.aggrgation_post_harvset_loan.setText(aggregPostHarvestLoan);

            viewHolder.aggrgation_post_harvset_amount.setText(aggregationPostHarvestLoanAmount);
            viewHolder.aggrgation_post_harvset_loan_interest.setText(aggregationPostHarvestInterestRate);
            viewHolder.aggrgation_post_harvset_loan_duration.setText(aggregationPostHarvestDuration);

            viewHolder.aggrgation_post_harvset_laon_disbursement_method.setText(aggregationPostHarvestLoanDisMethod);
        }
        return convertView;
    }


    private class ViewHolder {

        private TextView seasonId;
        private TextView inputLoan;

        private ImageView input_loan_prov_bank;
        private ImageView input_loan_prov_cooperative;
        private ImageView input_loan_prov_sacco;
        private ImageView input_loan_prov_other;

        private TextView input_loan_amount;
        private TextView input_loan_interest_rate;
        private TextView input_loan_duration;

        private ImageView input_loan_purpose_labour;
        private ImageView input_loan_purpose_seed;
        private ImageView input_loan_purpose_input;
        private ImageView input_loan_purpose_machinery;
        private ImageView input_loan_purpose_other;

        private TextView input_loan_disbursement_method;

        private TextView aggrgation_post_harvset_loan;

        private ImageView agg_post_harv_loan_prov_bank;
        private ImageView agg_post_harv_loan_prov_cooperative;
        private ImageView agg_post_harv_loan_prov_sacco;
        private ImageView agg_post_harv_loan_prov_other;

        private TextView aggrgation_post_harvset_amount;
        private TextView aggrgation_post_harvset_loan_interest;
        private TextView aggrgation_post_harvset_loan_duration;

        private ImageView agg_post_harv_loan_purpose_labour;
        private ImageView agg_post_harv_loan_purpose_input;
        private ImageView agg_post_harv_loan_purpose_machinery;
        private ImageView agg_post_harv_loan_purpose_other;

        private TextView aggrgation_post_harvset_laon_disbursement_method;
    }
}
