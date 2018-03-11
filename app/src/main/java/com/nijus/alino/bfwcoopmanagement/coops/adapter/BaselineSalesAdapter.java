package com.nijus.alino.bfwcoopmanagement.coops.adapter;


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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;

import java.util.ArrayList;

public class BaselineSalesAdapter extends ArrayAdapter<BaselineSales> {

    public BaselineSalesAdapter(Context context, ArrayList<BaselineSales> baselineSalesArrayList) {
        super(context, R.layout.baseline_sales_coop_item, baselineSalesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final BaselineSales baselineSales = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.baseline_sales_coop_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season);

            viewHolder.qty_agregated_from_members = convertView.findViewById(R.id.qty_agregated_from_members);
            viewHolder.cycle_h_at_price_per_kg = convertView.findViewById(R.id.cycle_h_at_price_per_kg);
            viewHolder.qty_purchased_from_non_members = convertView.findViewById(R.id.qty_purchaced_from_non_members);
            viewHolder.non_member_purchase_at_price_per_kg = convertView.findViewById(R.id.non_member_purchase_at_price_per_kg);
            viewHolder.qty_of_rgcc_contact = convertView.findViewById(R.id.rgcc_contact_under_ftma);
            viewHolder.qty_sold_to_rgcc = convertView.findViewById(R.id.qty_of_rgcc_contact);
            viewHolder.price_per_kg_sold_to_rgcc = convertView.findViewById(R.id.price_per_kg_sold_to_rgcc);
            viewHolder.qty_sold_outside_rgcc = convertView.findViewById(R.id.qty_sold_outside_rgcc);
            viewHolder.price_per_kg_sold_outside_ftma = convertView.findViewById(R.id.price_per_kg_sold_outside_ftma);

            viewHolder.formal_buyer = convertView.findViewById(R.id.formal_buyer);
            viewHolder.informal_buyer = convertView.findViewById(R.id.informal_buyer);
            viewHolder.other = convertView.findViewById(R.id.other_formal_buyer);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (baselineSales != null) {
            String seasonName = "Harvest Season " + baselineSales.getSeasonName();

            String qty_agregated_from_members = baselineSales.getQtyAgregatedFromMember() + "";
            String cycle_h_at_price_per_kg = baselineSales.getCycleHarvsetAtPricePerKg() + "";
            String qty_purchased_from_non_members = baselineSales.getQtyPurchaseFromNonMember() + "";
            String non_member_purchase_at_price_per_kg = baselineSales.getNonMemberAtPricePerKg() + "";
            String qty_of_rgcc_contact = baselineSales.getQtyOfRgccContract() + "";
            String qty_sold_to_rgcc = baselineSales.getQtySoldToRgcc() + "";
            String price_per_kg_sold_to_rgcc = baselineSales.getPricePerKgSoldToRgcc() + "";
            String qty_sold_outside_rgcc = baselineSales.getQtySoldOutOfRgcc() + "";
            String price_per_kg_sold_outside_ftma = baselineSales.getPricePerKkSoldOutFtma() + "";


            boolean formalBuyer = baselineSales.isFormalBuyer();
            boolean informalBuyer = baselineSales.isInformalBuyer();
            boolean isOther = baselineSales.isOther();

            viewHolder.seasonId.setText(seasonName);

            viewHolder.qty_agregated_from_members.setText(qty_agregated_from_members);
            viewHolder.cycle_h_at_price_per_kg.setText(cycle_h_at_price_per_kg);
            viewHolder.qty_purchased_from_non_members.setText(qty_purchased_from_non_members);
            viewHolder.non_member_purchase_at_price_per_kg.setText(non_member_purchase_at_price_per_kg);
            viewHolder.qty_of_rgcc_contact.setText(qty_of_rgcc_contact);
            viewHolder.qty_sold_to_rgcc.setText(qty_sold_to_rgcc);
            viewHolder.price_per_kg_sold_to_rgcc.setText(price_per_kg_sold_to_rgcc);
            viewHolder.qty_sold_outside_rgcc.setText(qty_sold_outside_rgcc);
            viewHolder.price_per_kg_sold_outside_ftma.setText(price_per_kg_sold_outside_ftma);

            viewHolder.formal_buyer.setImageResource(formalBuyer ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.informal_buyer.setImageResource(informalBuyer ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.other.setImageResource(isOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
        }
        return convertView;
    }


    private class ViewHolder {

        private TextView seasonId;

        private TextView qty_agregated_from_members;
        private TextView cycle_h_at_price_per_kg;
        private TextView qty_purchased_from_non_members;
        private TextView non_member_purchase_at_price_per_kg;
        private TextView qty_of_rgcc_contact;
        private TextView qty_sold_to_rgcc;
        private TextView price_per_kg_sold_to_rgcc;
        private TextView qty_sold_outside_rgcc;
        private TextView price_per_kg_sold_outside_ftma;

        private ImageView formal_buyer;
        private ImageView informal_buyer;
        private ImageView other;
    }
}
