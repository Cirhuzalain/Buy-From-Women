package com.nijus.alino.bfwcoopmanagement.buyers.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
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

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.ViewHolder> {
    private static int currentSelectedIndex = -1;
    final private Context mContext;
    final private View mEmptyView;
    final private BuyerAdapterOnClickHandler mClickHandler;
    final private BuyerAdapterOnLongClickHandler mLongClickHandler;
    private Cursor mCursor;
    private LinearLayoutManager mLinearLayoutManager;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private SparseBooleanArray itemsValues;
    private boolean reverseAllAnimations = false;

    public BuyerAdapter(Context mContext, View mEmptyView, BuyerAdapterOnClickHandler mClickHandler, BuyerAdapterOnLongClickHandler mLongClickHandler,
                        LinearLayoutManager linearLayoutManager) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
        this.mLongClickHandler = mLongClickHandler;

        mLinearLayoutManager = linearLayoutManager;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        itemsValues = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buyer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_NAME)));
        holder.mPhone.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_PHONE)));
        holder.farmerImage.setImageResource(R.mipmap.male);

        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Buyer.COLUMN_IS_SYNC)) == 1;
        boolean isUpdate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Buyer.COLUMN_IS_UPDATE)) == 1;
        if (isUpdate && isSync) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        }

        applyIconAnimation(holder, position);

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

        mCursor.moveToPosition(pos);

        int id = mCursor.getInt(mCursor.getColumnIndex(BfwContract.Buyer._ID));

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

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public interface BuyerAdapterOnClickHandler {
        void onClick(Long buyerId, ViewHolder vh);
    }

    public interface BuyerAdapterOnLongClickHandler {
        void onLongClick(long buyerId, long position, BuyerAdapter.ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final ImageView imagedone, imageView;
        public final TextView mUname, mPhone;
        public RelativeLayout iconBack, iconFront, iconContainer;
        public LinearLayout view_foreground;

        private int position = 0;
        private boolean isVisible;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            imagedone = view.findViewById(R.id.image_done);
            imageView = view.findViewById(R.id.buyer_sync);
            imagedone.setImageResource(R.drawable.ic_done_white_24dp);

            mUname = view.findViewById(R.id.b_name);
            mPhone = view.findViewById(R.id.b_phone);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer = view.findViewById(R.id.icon_container);
            iconContainer = view.findViewById(R.id.icon_container);
            view_foreground = view.findViewById(R.id.view_foreground);

            isVisible = false;

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            Long buyerColumnIndex = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Buyer._ID));

            mClickHandler.onClick(buyerColumnIndex, this);
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            position = getAdapterPosition();

            mCursor.moveToPosition(position);
            Long buyerColumnIndex = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Buyer._ID));

            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            mLongClickHandler.onLongClick(buyerColumnIndex, position, this);
            return true;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }
    }
}
