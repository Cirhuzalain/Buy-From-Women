package com.nijus.alino.bfwcoopmanagement.loans.ui.fragment;


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
import com.nijus.alino.bfwcoopmanagement.loans.sync.DeleteSyncLoanBkgrnd;
import com.nijus.alino.bfwcoopmanagement.products.sync.DeleteSyncProductBkgrnd;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DeleteLoanDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {
    private  int id_to_delete;
    private String name_product;
    private TextView mesg_textView;
    private Button confirm;
    private long[] tab;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.delete_loan, null);

        mesg_textView = viewContainer.findViewById(R.id.msg);

        //GET values for array  to delete from listview
        tab = getArguments().getLongArray("list_items_to_delete");
       /* String s="";
        for(long t: tab)
        {
            s=s+" - "+ String.valueOf(t);
        }*/


        String print_msg = mesg_textView.getText()+" "+tab.length+" item(s)? ";
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
            //Toast.makeText(getContext(), ""+id_to_delete, Toast.LENGTH_LONG).show();

            if (Utils.isNetworkAvailable(getContext())) {

                //Bundle bundle = new Bundle();
                //bundle.putLongArray("list_items_to_delete",tab);
                //dialogFragment.setArguments(bundle);

                ArrayList<Integer> listsSelectedItem = new ArrayList<Integer>();
                for(long t: tab)
                {
                    listsSelectedItem.add((int) t);
                }



                Intent intent = new Intent(getContext(), DeleteSyncLoanBkgrnd.class);
                intent.putIntegerArrayListExtra("list_items_to_delete", listsSelectedItem);
                //start job service
                getContext().startService(intent);
                dismiss();
            } else {
                Toast.makeText(getContext(), "ERROR, WAIT FOR THE INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){}
    }
}
