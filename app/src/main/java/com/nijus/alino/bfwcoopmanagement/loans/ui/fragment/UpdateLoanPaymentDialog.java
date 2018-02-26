package com.nijus.alino.bfwcoopmanagement.loans.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.loans.sync.DeleteSyncLoanBkgrnd;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateLoanPayment;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.Date;


public class UpdateLoanPaymentDialog extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {
    private  long  id_to_delete;
    private String name_product;
    private TextView mesg_textView;
    private Button confirm;
    private long[] tab;


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View alertLayout = getActivity().getLayoutInflater().inflate(R.layout.save_payment_details_dialog, null);

        //GET values for array  to delete from listview
        id_to_delete = getArguments().getLong("id_loan");
        final AutoCompleteTextView amount_pay = alertLayout.findViewById(R.id.amount_pay);
        final TextView title =  alertLayout.findViewById(R.id.title);
        title.setText("UPDATE");

        builder.setView(alertLayout)
                //.setPositiveButton(R.string.msg_ok, this)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();


       /* Double amount_tot = mCursor.getDouble(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));
        final long id_loan = mCursor.getLong(mCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_LOAN_ID));
        final View alertLayout = View.inflate(mContext, R.layout.save_payment_details_dialog, null);*/
       /*
        amount_pay.setText(String.valueOf(amount_tot));
        final Button confirm = alertLayout.findViewById(R.id.save_pay);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,"cancel",Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(amount_pay.getText())) {
                    amount_pay.setError(mContext.getString(R.string.error_field_required));
                }
                else
                {
                    PojoLoanPayment pojoLoanPayment = new PojoLoanPayment();
                    pojoLoanPayment.setAmount(Double.valueOf(amount_pay.getText().toString()));
                    Date start_date_date = new Date();

                    pojoLoanPayment.setPayment_date(start_date_date.getTime());
                    pojoLoanPayment.setLoan_id((int)id_loan);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("loan_payment", pojoLoanPayment);

                    Intent intent = new Intent(mContext, UpdateLoanPayment.class);
                    intent.putExtra("loan_payment_data", bundle);
                    //mContext.startService(intent);
                }
                Toast.makeText(mContext, "bree1",Toast.LENGTH_LONG).show();


            }
        });

        confirm.setText("Update loan payment");*/

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {

    }
}
