package com.nijus.alino.bfwcoopmanagement.vendors.adapter;

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
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;

import java.util.ArrayList;

public class FinanceDataVendorAdapter extends ArrayAdapter<FinanceVendor> {

    public FinanceDataVendorAdapter(Context context, ArrayList<FinanceVendor> finances) {
        super(context, R.layout.finance_data_item, finances);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final FinanceVendor finance = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.finance_data_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.finance_season_id);
            viewHolder.outstandingLoan = convertView.findViewById(R.id.pic_outstanding_loan);
            viewHolder.totLoanAmount = convertView.findViewById(R.id.tot_vol_side_sold);
            viewHolder.totOutstanding = convertView.findViewById(R.id.tot_outstanding);
            viewHolder.interestRate = convertView.findViewById(R.id.interest_rate);
            viewHolder.duration = convertView.findViewById(R.id.duration_month);
            viewHolder.loanProvider = convertView.findViewById(R.id.loan_provider);

            viewHolder.input = convertView.findViewById(R.id.input);
            viewHolder.aggregation = convertView.findViewById(R.id.aggregation);
            viewHolder.waterSource = convertView.findViewById(R.id.other_fin);
            viewHolder.mobileMoneyAccount = convertView.findViewById(R.id.mobile_m_account);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (finance != null) {
            String seasonName = "Harvest Season " + finance.getSeasonName();
            String loanAmount = finance.getTotLoanAmount() + "";
            String outstanding = finance.getTotOutstanding() + "";
            String interestrate = finance.getInterestRate() + "";
            String durationInMonth = finance.getDurationInMonth() + "";
            String lProvider = finance.getLoanProvider();

            boolean isOutstandingLoan = finance.isOutstandingLoan();
            boolean hasMobileMoneyAccount = finance.isHasMobileMoneyAccount();

            boolean isInput = finance.isInput();
            boolean isAggregation = finance.isAggregation();
            boolean isOtherLp = finance.isOtherLp();

            viewHolder.seasonId.setText(seasonName);

            viewHolder.totLoanAmount.setText(loanAmount);
            viewHolder.totOutstanding.setText(outstanding);
            viewHolder.interestRate.setText(interestrate);
            viewHolder.duration.setText(durationInMonth);
            viewHolder.loanProvider.setText(lProvider);

            viewHolder.input.setImageResource(isInput ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.aggregation.setImageResource(isAggregation ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.waterSource.setImageResource(isOtherLp ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.mobileMoneyAccount.setImageResource(hasMobileMoneyAccount ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.outstandingLoan.setImageResource(isOutstandingLoan ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView seasonId;
        private TextView totLoanAmount;
        private TextView totOutstanding;
        private TextView interestRate;
        private TextView duration;
        private TextView loanProvider;
        private ImageView input;
        private ImageView aggregation;
        private ImageView waterSource;
        private ImageView mobileMoneyAccount;
        private ImageView outstandingLoan;


    }
}
