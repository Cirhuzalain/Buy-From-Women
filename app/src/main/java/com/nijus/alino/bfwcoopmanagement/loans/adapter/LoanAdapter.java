package com.nijus.alino.bfwcoopmanagement.loans.adapter;

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
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    final private Context mContext;
    final private View mEmptyView;
    final private LoanAdapterOnClickHandler mClickHandler;
    final private LoanAdapterOnLongClickHandler mOnLongClickListener;
    private Cursor mCursor;
    private View view;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private SparseBooleanArray itemsValues;
    private boolean reverseAllAnimations = false;
    private static int currentSelectedIndex = -1;
    private int position;

    public LoanAdapter(Context mContext, View mEmptyView, LoanAdapterOnClickHandler mClickHandler,
                       LoanAdapterOnLongClickHandler mOnLongClickListener) {
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
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.farmerImage.setImageResource(R.mipmap.get_loan);

        String institution = "From " + mCursor.getString(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION));
        String amountTot = "" + mCursor.getDouble(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_AMOUNT)) + " RWF";

        holder.mInstitution.setText(institution);
        holder.amount_tot.setText(amountTot);

        Long getDate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_START_DATE));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date start_date = new Date(getDate);
        String date_string = DateFormat.getDateInstance().format(start_date);

        holder.start_date.setText(date_string);
        holder.end_date.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_STATE)));

        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_IS_SYNC)) == 1;
        boolean isUpdate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_IS_UPDATE)) == 1;
        if (isUpdate && isSync) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        }

        applyIconAnimation(holder, position);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
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

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public interface LoanAdapterOnClickHandler {
        void onItemClick(Long farmerId, ViewHolder vh);
    }

    public interface LoanAdapterOnLongClickHandler {
        boolean onLongClick(int position, ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage, imageView;
        public final TextView amount_tot, start_date, end_date;
        public final TextView mInstitution;
        public RelativeLayout iconBack, iconFront, iconContainer;
        public LinearLayout view_foreground;
        private FlipAnimator flipAnimator;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.l_icon);
            imageView = view.findViewById(R.id.loan_sync);
            amount_tot = view.findViewById(R.id.l_name);
            start_date = view.findViewById(R.id.date_start);
            start_date.setEnabled(false);
            end_date = view.findViewById(R.id.date_end);
            mInstitution = view.findViewById(R.id.l_institution);
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
            Long loanColumnIndex = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan._ID));

            mClickHandler.onItemClick(loanColumnIndex, this);
        }

        @Override
        public boolean onLongClick(View view) {
            position = getAdapterPosition();
            mCursor.moveToPosition(position);

            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            mOnLongClickListener.onLongClick(position, this);
            return true;
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }
    }
}
