package com.nijus.alino.bfwcoopmanagement.coops.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ExpectedYield;

import java.util.ArrayList;

public class ExpectedYieldAdapter extends ArrayAdapter<ExpectedYield> {


    public ExpectedYieldAdapter(Context context, ArrayList<ExpectedYield> expectedYieldArrayList) {
        super(context, R.layout.expected_yield_coop_item, expectedYieldArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ExpectedYield expectedYield = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.expected_yield_coop_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season);

            viewHolder.expectedProductionInMt = convertView.findViewById(R.id.total_expected_coop_production);
            viewHolder.totCoopLandSize = convertView.findViewById(R.id.total_coop_land_size_in_ha);
            viewHolder.expectedTotalProduction = convertView.findViewById(R.id.expected_total_production_in_kg);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (expectedYield != null) {
            String seasonName = "Harvest Season " + expectedYield.getHarvestSeason();
            String expectedProductionInMt = expectedYield.getExpectedCoopProductionInMt() + "";
            String totCoopLandSize = expectedYield.getTotalCoopLandSize() + "";
            String expectedTotalProduction = expectedYield.getExpectedTotalProduction() + "";

            viewHolder.seasonId.setText(seasonName);

            viewHolder.expectedProductionInMt.setText(expectedProductionInMt);
            viewHolder.totCoopLandSize.setText(totCoopLandSize);
            viewHolder.expectedTotalProduction.setText(expectedTotalProduction);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView seasonId;
        private TextView expectedProductionInMt;
        private TextView totCoopLandSize;
        private TextView expectedTotalProduction;
    }
}
