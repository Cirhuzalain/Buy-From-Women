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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;

import java.util.ArrayList;

public class AccessToInformationAdapter extends ArrayAdapter<AccessToInformation> {

    public AccessToInformationAdapter(Context context, ArrayList<AccessToInformation> accessToInformationArrayList) {
        super(context, R.layout.access_to_information_item, accessToInformationArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final AccessToInformation accessToInformation = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.access_to_information_item, parent, false);

            viewHolder.seasonId = convertView.findViewById(R.id.info_season_id);
            viewHolder.aes = convertView.findViewById(R.id.pic_agri_extension_details2);
            viewHolder.cri = convertView.findViewById(R.id.pic_clim_rel_details2);
            viewHolder.seeds = convertView.findViewById(R.id.pic_seed_details2);
            viewHolder.orgFert = convertView.findViewById(R.id.pic_org_fert_details2);
            viewHolder.inorgFert = convertView.findViewById(R.id.pic_inorg_fert_details2);
            viewHolder.labour = convertView.findViewById(R.id.pic_labour_details2);
            viewHolder.waterPumps = convertView.findViewById(R.id.pic_irr_w_p_details2);
            viewHolder.spreaders = convertView.findViewById(R.id.pic_spread_or_spray_details2);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (accessToInformation != null) {
            String seasonName = "Harvest Season " + accessToInformation.getHarvsetSeason();
            boolean isAes = accessToInformation.isAgricultureExtension();
            boolean isCri = accessToInformation.isClimateRelatedInformation();
            boolean isSeeds = accessToInformation.isSeed();
            boolean isOrgFert = accessToInformation.isOrganicFertilizers();
            boolean isInorgFert = accessToInformation.isInorganicFertilizers();
            boolean isLabour = accessToInformation.isLabour();
            boolean isWP = accessToInformation.isWaterPumps();
            boolean isSp = accessToInformation.isSpreaderOrSprayer();

            viewHolder.seasonId.setText(seasonName);
            viewHolder.aes.setImageResource(isAes ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.cri.setImageResource(isCri ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.seeds.setImageResource(isSeeds ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.orgFert.setImageResource(isOrgFert ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.inorgFert.setImageResource(isInorgFert ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.labour.setImageResource(isLabour ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.waterPumps.setImageResource(isWP ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
            viewHolder.spreaders.setImageResource(isSp ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView seasonId;
        private ImageView aes;
        private ImageView cri;
        private ImageView seeds;
        private ImageView orgFert;
        private ImageView inorgFert;
        private ImageView labour;
        private ImageView waterPumps;
        private ImageView spreaders;

    }
}
