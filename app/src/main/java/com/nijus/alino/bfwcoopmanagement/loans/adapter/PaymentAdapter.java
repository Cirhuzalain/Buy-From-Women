package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private TextView mEmptyTextView;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;

    public PaymentAdapter(Context mContext, View mEmptyView) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        mEmptyTextView = mEmptyView.findViewById(R.id.recyclerview_empty_payment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.txt_loan.setText((position+1)+"");
        Double np = mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));

        holder.nextPayemnt.setText("Amount payment "+mCursor.getDouble(mCursor.
                getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT))+" RWF");
        //holder.nextPayemnt.setText(np.toString() +  "-- "+ String.valueOf(np) + " -- "+ String.format("%.2f", np) );

        Long getDate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE));
        Date start_date = new Date(getDate);
        String date_string = DateFormat.getDateInstance().format(start_date);

        holder.payment_date.setText("On "+ date_string);


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
            mEmptyTextView.setText(getItemCount() == 0 ? R.string.there_s_no_payment: R.string.there_is_payment);
        }
        catch (Exception e)
        {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView nextPayemnt;
        public final TextView txt_loan,payment_date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nextPayemnt = view.findViewById(R.id.sc_next_payment);
            payment_date = view.findViewById(R.id.l_date);
            txt_loan = view.findViewById(R.id.txt_loan);
        }
    }
}
