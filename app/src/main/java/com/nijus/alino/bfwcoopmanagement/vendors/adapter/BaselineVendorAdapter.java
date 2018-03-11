package com.nijus.alino.bfwcoopmanagement.vendors.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BaseLineVendor;

import java.util.ArrayList;

public class BaselineVendorAdapter extends ArrayAdapter<BaseLineVendor> {

    public BaselineVendorAdapter(Context context, ArrayList<BaseLineVendor> accessToInformationArrayList) {
        super(context, R.layout.baseline_farmer_item, accessToInformationArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final BaseLineVendor baseLine = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.baseline_farmer_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.baseline_season_id);
            viewHolder.totProdKg = convertView.findViewById(R.id.total_production_in_kg);
            viewHolder.totLostKg = convertView.findViewById(R.id.tot_lost);
            viewHolder.totSoldKg = convertView.findViewById(R.id.total_sold_in_kg);
            viewHolder.totVolSoldCoops = convertView.findViewById(R.id.tot_vol_sold_coop);
            viewHolder.priceSoldToCoop = convertView.findViewById(R.id.price_sold_to_the_coop_per_kg);
            viewHolder.totVolSoldKg = convertView.findViewById(R.id.tot_vol_side_sold);
            viewHolder.priceSoldKg = convertView.findViewById(R.id.price_sold_kg);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (baseLine != null) {
            String seasonName = "Harvest Season " + baseLine.getSeasonName();
            String prodInfo = baseLine.getTotProdInKg() + "";
            String soldInfo = baseLine.getTotSoldInKg() + "";
            String lostInfo = baseLine.getTotLostInKg() + "";
            String volCoopInfo = baseLine.getTotVolumeSoldCoopInKg() + "";
            String priceCoopInfo = baseLine.getPriceSoldToCoopPerKg() + "";
            String totVolSoldInfo = baseLine.getTotVolSoldInKg() + "";
            String priceSoldInfo = baseLine.getPriceSoldInKg() + "";
            viewHolder.seasonId.setText(seasonName);

            viewHolder.totProdKg.setText(prodInfo);
            viewHolder.totLostKg.setText(lostInfo);
            viewHolder.totSoldKg.setText(soldInfo);
            viewHolder.totVolSoldCoops.setText(volCoopInfo);
            viewHolder.priceSoldToCoop.setText(priceCoopInfo);
            viewHolder.totVolSoldKg.setText(totVolSoldInfo);
            viewHolder.priceSoldKg.setText(priceSoldInfo);
        }


        return convertView;
    }

    private class ViewHolder {

        private TextView seasonId;
        private TextView totProdKg;
        private TextView totLostKg;
        private TextView totSoldKg;
        private TextView totVolSoldCoops;
        private TextView priceSoldToCoop;
        private TextView totVolSoldKg;
        private TextView priceSoldKg;
    }

}
