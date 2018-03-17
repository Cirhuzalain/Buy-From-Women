package com.nijus.alino.bfwcoopmanagement.loans.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;


public class UpdateLoanPaymentDialog extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {
    private long id_to_delete;
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
        final TextView title = alertLayout.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.update));

        builder.setView(alertLayout)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {

    }
}
