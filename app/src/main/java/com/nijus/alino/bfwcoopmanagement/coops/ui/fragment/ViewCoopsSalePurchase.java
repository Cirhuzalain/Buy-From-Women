package com.nijus.alino.bfwcoopmanagement.coops.ui.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.CoopProfileActivity;
import com.nijus.alino.bfwcoopmanagement.purchases.ui.activities.PurchaseOrderActivity;
import com.nijus.alino.bfwcoopmanagement.sales.ui.activities.SaleOrderInfoActivity;

public class ViewCoopsSalePurchase extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {


    private Button viewSale;
    private Button viewPurchase;
    private Button viewCoopProfile;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.view_coop_sale_purchase, null);

        viewSale = viewContainer.findViewById(R.id.view_coop_sale);
        viewPurchase = viewContainer.findViewById(R.id.view_coop_purchase);
        viewCoopProfile = viewContainer.findViewById(R.id.view_coop_profile);

        viewSale.setOnClickListener(this);
        viewPurchase.setOnClickListener(this);
        viewCoopProfile.setOnClickListener(this);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewContainer)
                .setNegativeButton(R.string.msg_cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_coop_purchase) {
            startActivity(new Intent(getActivity(), PurchaseOrderActivity.class));
        } else if (view.getId() == R.id.view_coop_sale) {
            startActivity(new Intent(getActivity(), SaleOrderInfoActivity.class));
        } else {
            startActivity(new Intent(getActivity(), CoopProfileActivity.class));
        }
    }
}
