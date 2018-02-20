package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.text.DateFormat;
import java.util.Date;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private TextView mEmptyTextView;

    public ScheduleAdapter(Context mContext, View mEmptyView) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        mEmptyTextView = mEmptyView.findViewById(R.id.recyclerview_empty_schedule);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.txt_loan.setText((position+1)+"");

        holder.nextPayemnt.setText("Next payment "+mCursor.getDouble(mCursor.
                getColumnIndex(BfwContract.LoanLine.COLUMN_NEXT_PAYMENT_AMOUNT))+" RWF");

        holder.interest.setText("("+mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanLine.COLUMN_INTEREST))+"RWF)");

        Long getDate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanLine.COLUMN_PAYMENT_DATE));
        Date start_date = new Date(getDate);
        String date_string = DateFormat.getDateInstance().format(start_date);

        holder.amount_principal.setText("Pay before "+ date_string);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        try {
            //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
            mEmptyTextView.setText(getItemCount() == 0 ? R.string.there_s_no_loan_line: R.string.there_is_loan_line);
        }
        catch (Exception e)
        {

        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView nextPayemnt;
        public final TextView txt_loan,amount_principal,interest;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nextPayemnt = view.findViewById(R.id.sc_next_payment);
            txt_loan = view.findViewById(R.id.txt_loan);
            amount_principal = view.findViewById(R.id.amount_principal);
            interest = view.findViewById(R.id.interest);

        }
    }
}
