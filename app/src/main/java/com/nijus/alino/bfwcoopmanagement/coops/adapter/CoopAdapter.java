package com.nijus.alino.bfwcoopmanagement.coops.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.ArrayList;

public class CoopAdapter extends RecyclerView.Adapter<CoopAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private SparseBooleanArray itemsValues;
    final private CoopAdapterOnClickHandler mClickHandler;
    final private CoopAdapterOnLongClickHandler mOnLongClickListener;
    private boolean reverseAllAnimations = false;
    private static int currentSelectedIndex = -1;

    public CoopAdapter(Context mContext, View mEmptyView, CoopAdapterOnClickHandler mClickHandler, CoopAdapterOnLongClickHandler mOnLongClickListener) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
        this.mOnLongClickListener = mOnLongClickListener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        itemsValues = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.farmerImage.setImageResource(R.mipmap.coop);
        holder.pic_male.setImageResource(R.mipmap.male);
        holder.pic_female.setImageResource(R.mipmap.female);
        holder.iconBack.setVisibility(View.GONE);

        holder.imagedone.setImageResource(R.drawable.ic_done_white_24dp);

        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));

        String address = mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_ADDRESS));
        if (address == null || address.equals("null")) {
            holder.mCoopAddress.setText("");
        } else {
            holder.mCoopAddress.setText(address);
        }


        String maleNumber = "" + mCursor.getInt(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_MALE_COOP)) + "";
        String femaleNumber = "" + mCursor.getInt(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_FEMALE_COOP)) + "";

        holder.nbr_male.setText(maleNumber);
        holder.nbr_female.setText(femaleNumber);

        applyIconAnimation(holder, position);
    }

    private void applyIconAnimation(CoopAdapter.ViewHolder holder, int position) {
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

        mCursor.moveToPosition(pos);

        int id = mCursor.getInt(mCursor.getColumnIndex(BfwContract.Farmer._ID));

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
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public interface CoopAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }

    public interface CoopAdapterOnLongClickHandler {
        void onLongClick(Long farmerId, int position);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage, pic_male, pic_female;
        public final ImageView imagedone;
        public final TextView mUname, nbr_male, nbr_female;
        public final TextView mCoopAddress;
        public RelativeLayout iconBack, iconFront, iconContainer;
        public LinearLayout view_foreground;
        private int position = 0;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            pic_female = view.findViewById(R.id.pic_male);
            pic_male = view.findViewById(R.id.pic_female);
            mUname = view.findViewById(R.id.u_name);
            mCoopAddress = view.findViewById(R.id.coop_add);
            nbr_female = view.findViewById(R.id.nbr_female);

            imagedone = view.findViewById(R.id.image_done);

            nbr_male = view.findViewById(R.id.nbr_male);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer = view.findViewById(R.id.icon_container);
            view_foreground = view.findViewById(R.id.view_foreground);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int coopId = mCursor.getColumnIndex(BfwContract.Coops._ID);
            mClickHandler.onClick(mCursor.getLong(coopId), this);
        }

        @Override
        public boolean onLongClick(View view) {
            position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int coopColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);

            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            mOnLongClickListener.onLongClick(mCursor.getLong(coopColumnIndex), position);
            return true;
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }
    }
}
