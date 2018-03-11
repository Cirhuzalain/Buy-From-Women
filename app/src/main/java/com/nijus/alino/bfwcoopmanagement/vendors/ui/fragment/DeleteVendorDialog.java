package com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.sync.DeleteVendorService;

import java.util.ArrayList;

public class DeleteVendorDialog extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    private ArrayList<Integer> vendorsId;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.delete_vendor, null);

        TextView msgTextView = viewContainer.findViewById(R.id.msg);

        Bundle bundle = getArguments();
        if (bundle != null) {
            vendorsId = getArguments().getIntegerArrayList("vendor_ids");


            String print_msg = msgTextView.getText() + " " + vendorsId.size() + " item(s) ? ";
            msgTextView.setText(print_msg);
        }

        Button confirm = viewContainer.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        builder.setView(viewContainer)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), DeleteVendorService.class);
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("vendor_del", vendorsId);
        intent.putExtra("vendor_data", bundle);
        getContext().startService(intent);
        dismiss();
    }
}
