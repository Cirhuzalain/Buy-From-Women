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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;

import java.util.ArrayList;

public class ForecastSalesAdapter extends ArrayAdapter<ForecastSales> {


    public ForecastSalesAdapter(Context context, ArrayList<ForecastSales> forecastSalesArrayList) {
        super(context, R.layout.forecast_sales_coop_item, forecastSalesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ForecastSales forecastSales = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.forecast_sales_coop_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season_sales);

            viewHolder.rgcc = convertView.findViewById(R.id.pic_rgcc_details2);
            viewHolder.prodev = convertView.findViewById(R.id.pic_prodev_details2);
            viewHolder.sarura = convertView.findViewById(R.id.pic_sasura_details2);
            viewHolder.aif = convertView.findViewById(R.id.pic_aif_details2);
            viewHolder.eax = convertView.findViewById(R.id.pic_eax_details2);
            viewHolder.none = convertView.findViewById(R.id.pic_none_details2);
            viewHolder.other = convertView.findViewById(R.id.pic_other_details2);

            viewHolder.contractQuantity = convertView.findViewById(R.id.committed_contract_qty_details2);
            viewHolder.grade = convertView.findViewById(R.id.grade_details2);
            viewHolder.minimumFloorPerGrade = convertView.findViewById(R.id.min_floor_per_grade_details2);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (forecastSales != null) {
            String seasonName = "Harvest Season " + forecastSales.getSeasonName();
            String contractquantity = forecastSales.getCommitedContractQty() + "";
            String grade = forecastSales.getGrade();
            String minimumFlooGrade = forecastSales.getMinFloorPerGrade();

            boolean rgcc = forecastSales.isRgcc();
            boolean prodev = forecastSales.isProdev();
            boolean sarura = forecastSales.isSarura();
            boolean aif = forecastSales.isAif();
            boolean eax = forecastSales.isEax();
            boolean none = forecastSales.isNone();
            boolean other = forecastSales.isOther();

            viewHolder.seasonId.setText(seasonName);
            viewHolder.rgcc.setImageResource(rgcc ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.prodev.setImageResource(prodev ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.sarura.setImageResource(sarura ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.aif.setImageResource(aif ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.eax.setImageResource(eax ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.none.setImageResource(none ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.other.setImageResource(other ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            viewHolder.contractQuantity.setText(contractquantity);
            viewHolder.grade.setText(grade);
            viewHolder.minimumFloorPerGrade.setText(minimumFlooGrade);
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView seasonId;

        private ImageView rgcc;
        private ImageView prodev;
        private ImageView sarura;
        private ImageView aif;
        private ImageView eax;
        private ImageView none;
        private ImageView other;

        private TextView contractQuantity;
        private TextView grade;
        private TextView minimumFloorPerGrade;

    }
}
