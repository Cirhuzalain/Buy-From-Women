package com.nijus.alino.bfwcoopmanagement.farmers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.LandInformation;

import java.util.ArrayList;

public class LandFarmerDataAdapter extends ArrayAdapter<LandInformation> {

    public LandFarmerDataAdapter(Context context, ArrayList<LandInformation> accessToInformationArrayList) {
        super(context, R.layout.farmer_land_data_item, accessToInformationArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final LandInformation landInformation = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.farmer_land_data_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season_id);
            viewHolder.plotSize = convertView.findViewById(R.id.plot_size);
            viewHolder.lat = convertView.findViewById(R.id.latitude);
            viewHolder.lng = convertView.findViewById(R.id.longitude);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (landInformation != null) {
            String seasonName = "Harvest Season " + landInformation.getSeasonName();
            String plotInfo = landInformation.getLandSize() + "";
            String latInfo = landInformation.getLat() + "";
            String lngInfo = landInformation.getLng() + "";

            viewHolder.seasonId.setText(seasonName);
            viewHolder.plotSize.setText(plotInfo);
            viewHolder.lat.setText(latInfo);
            viewHolder.lng.setText(lngInfo);
        }


        return convertView;
    }

    private class ViewHolder {

        private TextView seasonId;
        private TextView plotSize;
        private TextView lat;
        private TextView lng;
    }

}
