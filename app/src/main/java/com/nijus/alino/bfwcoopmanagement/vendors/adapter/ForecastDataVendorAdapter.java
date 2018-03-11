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
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ForecastVendor;

import java.util.ArrayList;

public class ForecastDataVendorAdapter extends ArrayAdapter<ForecastVendor> {

    public ForecastDataVendorAdapter(Context context, ArrayList<ForecastVendor> accessToInformationArrayList) {
        super(context, R.layout.forecast_data_item, accessToInformationArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ForecastVendor forecast = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.forecast_data_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.forecast_season_id);

            viewHolder.numLandPlot = convertView.findViewById(R.id.tot_plot_land_maize_prod);
            viewHolder.expectedTotalProductionInKg = convertView.findViewById(R.id.expected_total_production_in_kg);
            viewHolder.expectedSalesOutsideThePpp = convertView.findViewById(R.id.expected_sales_outside_the_ppp);
            viewHolder.expectedPostHarvsetLossInKg = convertView.findViewById(R.id.expected_post_harvset_loss_in_kg);
            viewHolder.totalCoopLandSizeInHa = convertView.findViewById(R.id.total_coop_land_size_in_ha);
            viewHolder.percentCoopLandSize = convertView.findViewById(R.id.percent_coop_land_size);
            viewHolder.currentFtmaCommitementKg = convertView.findViewById(R.id.current_ftma_commitement_kg);
            viewHolder.farmerContributionFtmaCom = convertView.findViewById(R.id.farmer_contribution_ftma_com);
            viewHolder.farmerexpectedminppp = convertView.findViewById(R.id.farmers_expected_min_revenu);
            viewHolder.minimumflowprice = convertView.findViewById(R.id.min_flore_price_per_kg_in_rwf);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (forecast != null) {
            String seasonName = "Harvest Season " + forecast.getSeasonName();
            String landPlotInfo = forecast.getArableLandPlot() + "";
            String mininumPriceInfo = forecast.getMinimumflowprice() + "";
            String expectedInfo = forecast.getFarmerexpectedminppp() + "";

            String prodInfo = forecast.getTotProdKg() + "";
            String salesInfo = forecast.getSalesOutsidePpp() + "";
            String lostInfo = forecast.getPostHarvestLossInKg() + "";
            String coopInfo = forecast.getTotCoopLandSize() + "";
            String farmerPercentCoopInfo = forecast.getFarmerPercentCoopLandSize() + "";
            String currentInfo = forecast.getCurrentPppContrib() + "";
            String farmerInfo = forecast.getFarmerContributionPpp() + "";

            viewHolder.seasonId.setText(seasonName);
            viewHolder.numLandPlot.setText(landPlotInfo);
            viewHolder.minimumflowprice.setText(mininumPriceInfo);
            viewHolder.farmerexpectedminppp.setText(expectedInfo);

            viewHolder.expectedTotalProductionInKg.setText(prodInfo);
            viewHolder.expectedSalesOutsideThePpp.setText(salesInfo);
            viewHolder.expectedPostHarvsetLossInKg.setText(lostInfo);
            viewHolder.totalCoopLandSizeInHa.setText(coopInfo);
            viewHolder.percentCoopLandSize.setText(farmerPercentCoopInfo);
            viewHolder.currentFtmaCommitementKg.setText(currentInfo);
            viewHolder.farmerContributionFtmaCom.setText(farmerInfo);
        }


        return convertView;
    }

    private class ViewHolder {
        private TextView seasonId;
        private TextView numLandPlot;
        private TextView minimumflowprice;
        private TextView farmerexpectedminppp;

        private TextView expectedTotalProductionInKg;
        private TextView expectedSalesOutsideThePpp;
        private TextView expectedPostHarvsetLossInKg;
        private TextView totalCoopLandSizeInHa;
        private TextView percentCoopLandSize;
        private TextView currentFtmaCommitementKg;
        private TextView farmerContributionFtmaCom;
    }
}
