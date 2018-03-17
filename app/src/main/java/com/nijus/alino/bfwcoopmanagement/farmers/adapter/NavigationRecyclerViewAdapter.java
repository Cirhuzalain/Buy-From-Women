package com.nijus.alino.bfwcoopmanagement.farmers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.pojo.Farmer;

import java.util.ArrayList;
import java.util.List;

public class NavigationRecyclerViewAdapter extends RecyclerView.Adapter<NavigationRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static int currentSelectedIndex = -1;
    final private Context mContext;
    final private View mEmptyView;
    final private FarmerAdapterOnClickHandler mClickHandler;
    final private FarmerAdapterOnLongClickListener mOnLongClickListener;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private SparseBooleanArray itemsValues;
    private List<Farmer> farmerList;
    private List<Farmer> farmerFilterList;
    private boolean reverseAllAnimations = false;

    public NavigationRecyclerViewAdapter(Context context, View view, FarmerAdapterOnClickHandler vh, FarmerAdapterOnLongClickListener vLong) {
        mContext = context;
        mEmptyView = view;
        mClickHandler = vh;
        mOnLongClickListener = vLong;

        farmerFilterList = new ArrayList<>();
        farmerList = new ArrayList<>();

        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        itemsValues = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Farmer farmer = farmerFilterList.get(position);

        holder.farmerImage.setImageResource(R.mipmap.male);

        String phone = farmer.getPhone();
        String name = farmer.getName();
        boolean isSync = farmer.isSync();
        if (phone == null || phone.equals("null")) {
            holder.mUphone.setText("");
        } else {
            holder.mUphone.setText(phone);
        }

        holder.mUname.setText(name);
        holder.imagedone.setImageResource(R.drawable.ic_done_white_24dp);


        if (isSync) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        }

        applyIconAnimation(holder, position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    farmerFilterList = farmerList;
                } else {
                    List<Farmer> filteredList = new ArrayList<>();
                    for (Farmer row : farmerList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    farmerFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = farmerFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                farmerFilterList = (ArrayList<Farmer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            holder.resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            holder.resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        itemsValues.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items =
                new ArrayList<>(itemsValues.size());
        for (int i = 0; i < itemsValues.size(); i++) {
            items.add(itemsValues.keyAt(i));
        }
        return items;
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;

        final Farmer farmer = farmerFilterList.get(pos);

        int id = farmer.getFarmerId();

        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
            itemsValues.delete(id);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
            itemsValues.put(id, true);
        }
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        if (farmerFilterList == null) return 0;
        return farmerFilterList.size();
    }

    public void swapCursor(List<Farmer> farmerList1) {
        farmerList = farmerList1;
        farmerFilterList = farmerList1;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public interface FarmerAdapterOnClickHandler {
        void onClick(int farmerId, ViewHolder vh);
    }

    public interface FarmerAdapterOnLongClickListener {
        void onLongClick(long item, long position, NavigationRecyclerViewAdapter.ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final TextView mUname;
        public final TextView mUphone;
        public final ImageView imageView, imagedone;
        public LinearLayout viewForeground;
        public RelativeLayout iconBack, iconFront, iconContainer;
        private int position = 0;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            mUname = view.findViewById(R.id.u_name);
            mUphone = view.findViewById(R.id.u_phone);
            imageView = view.findViewById(R.id.u_sync);

            viewForeground = view.findViewById(R.id.view_foreground);

            imagedone = view.findViewById(R.id.image_done);

            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer = view.findViewById(R.id.icon_container);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            final Farmer farmer = farmerFilterList.get(position);

            mClickHandler.onClick(farmer.getFarmerId(), this);
        }

        @Override
        public boolean onLongClick(View view) {

            position = getAdapterPosition();

            final Farmer farmer = farmerFilterList.get(position);

            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            mOnLongClickListener.onLongClick(farmer.getFarmerId(), position, this);
            return true;
        }

        public void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }

    }
}
