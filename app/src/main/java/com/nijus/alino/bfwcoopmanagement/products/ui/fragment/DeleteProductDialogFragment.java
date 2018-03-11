package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;


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
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.products.sync.DeleteSyncProductBkgrnd;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

public class DeleteProductDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {
    private  int id_to_delete;
    private String name_product;
    private TextView mesg_textView;
    private Button confirm;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.delete_product, null);

        mesg_textView = viewContainer.findViewById(R.id.msg);

        //GET values for product to delete from girdview
        //Get product server id
        id_to_delete = getArguments().getInt("id_product");
        //get name of product
        name_product = getArguments().getString("name_product");

        String print_msg = mesg_textView.getText()+" "+name_product+"?";
        mesg_textView.setText(print_msg);

        confirm = viewContainer.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        builder.setView(viewContainer)
                //.setPositiveButton(R.string.msg_ok, this)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {
        try {
            if (Utils.isNetworkAvailable(getContext())) {

                Intent intent = new Intent(getContext(), DeleteSyncProductBkgrnd.class);
                intent.putExtra("product_id", id_to_delete);
                //start job service
                getContext().startService(intent);
                dismiss();
            } else {
                Toast.makeText(getContext(), "ERROR, WAIT FOR THE INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            }
            //getContext().startService(intent);

        }catch (Exception e){}
    }
}
