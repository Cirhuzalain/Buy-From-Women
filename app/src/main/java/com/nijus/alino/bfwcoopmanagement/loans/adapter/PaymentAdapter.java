package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.loans.sync.DeleteSyncLoanPaymentBkgrnd;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateLoanPayment;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import java.text.DateFormat;
import java.util.Date;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    final private Context mContext;
    final private View mEmptyView;
    final private TextView mEmptyTextView;
    private Cursor mCursor;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private AlertDialog.Builder adb_update, adb_del;
    private AlertDialog alertDialog_update, alertDialog_del;

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
        holder.txt_loan.setText((position + 1) + "");
        Double np = mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));

        holder.nextPayemnt.setText("Amount payment " + mCursor.getDouble(mCursor.
                getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT)) + " RWF");
        //holder.nextPayemnt.setText(np.toString() +  "-- "+ String.valueOf(np) + " -- "+ String.format("%.2f", np) );

        Long getDate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE));
        Date start_date = new Date(getDate);
        String date_string = DateFormat.getDateInstance().format(start_date);

        holder.payment_date.setText("On " + date_string);

        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_IS_SYNC)) == 1;
        boolean isUpdate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_IS_UPDATE)) == 1;
        if (isUpdate && isSync) {
            holder.isSync_icon.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        } else {
            holder.isSync_icon.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        }
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
            mEmptyTextView.setText(getItemCount() == 0 ? R.string.there_s_no_payment : R.string.there_is_payment);
        } catch (Exception e) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public final View mView;
        public final TextView nextPayemnt;
        public final TextView txt_loan, payment_date;
        public ImageView isSync_icon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nextPayemnt = view.findViewById(R.id.sc_next_payment);
            isSync_icon = view.findViewById(R.id.loan_payment_sync);
            payment_date = view.findViewById(R.id.l_date);
            txt_loan = view.findViewById(R.id.txt_loan);

            /**
             * Set listeners to this recycleview to edit and to delete it
             * */
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            Double amount_tot = mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));
            final long id_loan = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_LOAN_ID));
            final long id_loanPayment_local = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment._ID));
            final View alertLayout = View.inflate(mContext, R.layout.save_payment_details_dialog, null);
            final AutoCompleteTextView amount_pay = alertLayout.findViewById(R.id.amount_pay);
            final TextView title = alertLayout.findViewById(R.id.title);
            title.setText("UPDATE");
            amount_pay.setText(String.valueOf(amount_tot));
            final Button confirm = alertLayout.findViewById(R.id.save_pay);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(amount_pay.getText())) {
                        amount_pay.setError(mContext.getString(R.string.error_field_required));
                    } else {
                        PojoLoanPayment pojoLoanPayment = new PojoLoanPayment();
                        pojoLoanPayment.setAmount(Double.valueOf(amount_pay.getText().toString()));
                        Date start_date_date = new Date();

                        pojoLoanPayment.setPayment_date(start_date_date.getTime());
                        pojoLoanPayment.setLoan_id((int) id_loan);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("loan_payment", pojoLoanPayment);

                        Intent intent = new Intent(mContext, UpdateLoanPayment.class);
                        intent.putExtra("loan_payment_data", bundle);
                        intent.putExtra("loan_payment_id", id_loanPayment_local);

                        mContext.startService(intent);
                        alertDialog_update.dismiss();
                    }
                }
            });

            confirm.setText("Update loan payment");
            adb_update = new AlertDialog.Builder(mContext);
            adb_update.setView(alertLayout);
            adb_update.setPositiveButton("Cancel", null);
            alertDialog_update = adb_update.show();

        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            Double amount_tot = mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));
            final long id_loan = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_LOAN_ID));
            final int id_loan_to_delete = mCursor.getInt(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_SERVER_ID));
            final View alertLayout = View.inflate(mContext, R.layout.save_payment_details_dialog, null);
            final AutoCompleteTextView amount_pay = alertLayout.findViewById(R.id.amount_pay);
            final TextView details = alertLayout.findViewById(R.id.details);
            final TextView title = alertLayout.findViewById(R.id.title);
            title.setText("DELETE");
            title.setBackgroundResource(R.color.colorAccent);
            details.setText("Are you sure you want to remove this " + amount_tot + " RWF payment?");
            details.setGravity(View.TEXT_ALIGNMENT_CENTER);
            amount_pay.setText(String.valueOf(amount_tot));
            amount_pay.setVisibility(View.GONE);

            final Button confirm = alertLayout.findViewById(R.id.save_pay);

            confirm.setText("Delete loan payment");
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (Utils.isNetworkAvailable(mContext)) {

                            Intent intent = new Intent(mContext, DeleteSyncLoanPaymentBkgrnd.class);
                            intent.putExtra("loan_id", id_loan_to_delete);
                            //start job service
                            mContext.startService(intent);
                            alertDialog_del.dismiss();

                        } else {
                            Toast.makeText(mContext, "ERROR, WAIT FOR THE INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                    }
                }
            });
            adb_del = new AlertDialog.Builder(mContext);
            adb_del.setView(alertLayout);
            adb_del.setPositiveButton("Cancel", null);
            alertDialog_del = adb_del.show();
            return true;
        }
    }
}
