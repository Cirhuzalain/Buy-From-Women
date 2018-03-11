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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;

import java.util.ArrayList;

public class BaselineYieldAdapter extends ArrayAdapter<BaselineYield> {


    public BaselineYieldAdapter(Context context, ArrayList<BaselineYield> baselineYieldArrayList) {
        super(context, R.layout.baseline_yield_coop_item, baselineYieldArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final BaselineYield baselineYield = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.baseline_yield_coop_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.harv_season_id);
            viewHolder.maize = convertView.findViewById(R.id.pic_maize_details3);
            viewHolder.bean = convertView.findViewById(R.id.pic_bean_details3);
            viewHolder.soy = convertView.findViewById(R.id.pic_soy_details3);
            viewHolder.other = convertView.findViewById(R.id.pic_other_details3);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (baselineYield != null) {
            String seasonName = "Harvest Season " + baselineYield.getSeasonName();
            boolean isBean = baselineYield.isBean();
            boolean isMaize = baselineYield.isMaize();
            boolean isSoy = baselineYield.isSoy();
            boolean isOther = baselineYield.isOther();

            viewHolder.seasonId.setText(seasonName);
            viewHolder.maize.setImageResource(isMaize ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.bean.setImageResource(isBean ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.soy.setImageResource(isSoy ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.other.setImageResource(isOther ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView seasonId;
        private ImageView maize;
        private ImageView bean;
        private ImageView soy;
        private ImageView other;
    }
}
