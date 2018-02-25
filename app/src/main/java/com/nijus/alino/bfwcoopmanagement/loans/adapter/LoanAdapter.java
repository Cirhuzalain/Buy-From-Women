package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.ui.activities.DetailLoanActivity;
import com.nijus.alino.bfwcoopmanagement.loans.ui.activities.SelectedItems;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    final private Context mContext;
    final private View mEmptyView;
    final private LoanAdapterOnClickHandler mClickHandler;
    public List<Long> listsSelectedItem = new ArrayList<Long>();
    private Cursor mCursor;
    private View view;

    public LoanAdapter(Context mContext, View mEmptyView, LoanAdapterOnClickHandler mClickHandler,
                       LoanAdapterOnLongClickHandler mLongClickHandler) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
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
        //holder.iconBack.setVisibility(View.GONE);

        holder.mInstitution.setText("From " + mCursor.getString(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION)));
        holder.amount_tot.setText("" + mCursor.getDouble(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_AMOUNT)) + " RWF");

        Long getDate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_START_DATE));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date start_date = new Date(getDate);
        String date_string = DateFormat.getDateInstance().format(start_date);

        holder.start_date.setText(date_string);
        holder.end_date.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_STATE)));


        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_IS_SYNC)) == 1;
        boolean isUpdate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan.COLUMN_IS_UPDATE))==1;
        if (isUpdate && isSync) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        }
        else  {
            holder.imageView.setImageResource(R.drawable.ic_cloud_upload_black_24dp);
        }
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

    public void reset() {
        ViewHolder h = new ViewHolder(view);
        //h.resetAll(1);
        TextView v =(TextView) h.resetAll(1);
        //TextView amount_tot = view.findViewById(R.id.l_name);
        String s = (String) v.getText();

        //ImageView f = v.findViewById(R.id.l_icon);
        //f.setImageResource(R.mipmap.calendar_one);
    }

    public interface LoanAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }

    public interface LoanAdapterOnLongClickHandler {
        boolean onLongClick(Long farmerId, ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage, imageView;
        public final TextView amount_tot, start_date, end_date;
        public final TextView mInstitution;
        public RelativeLayout iconBack, iconFront, iconContainer;
        public LinearLayout view_foreground;
        private FlipAnimator flipAnimator;
        //lisste des LOANS deja selectionner


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
            int c = mCursor.getColumnIndex(BfwContract.Loan._ID);
            Long loanColumnIndex = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan._ID));

            Intent intent = new Intent(mContext, DetailLoanActivity.class);
            intent.putExtra("loanId", loanColumnIndex);
            mContext.startActivity(intent);
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }


        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int c = mCursor.getColumnIndex(BfwContract.Loan._ID);
            Long loanColumnIndex = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Loan._ID));
            //annimation and delete and loan agent

            if (!return_if_val_in_array(loanColumnIndex)) {
                this.iconFront.setVisibility(View.GONE);
                this.view_foreground.setBackgroundColor(Color.argb(20, 0, 0, 0));
                resetIconYAxis(this.iconBack);
                this.iconBack.setVisibility(View.VISIBLE);
                this.iconBack.setAlpha(1);
                flipAnimator.flipView(mContext, this.iconBack, this.iconFront, true);
                listsSelectedItem.add(loanColumnIndex);
                EventBus.getDefault().post(new SelectedItems(listsSelectedItem));

            } else {
                this.iconBack.setVisibility(View.GONE);
                resetIconYAxis(this.iconFront);
                this.view_foreground.setBackgroundColor(Color.argb(2, 0, 0, 0));
                this.iconFront.setVisibility(View.VISIBLE);
                this.iconFront.setAlpha(1);

                flipAnimator.flipView(mContext, this.iconBack, this.iconFront, false);
                listsSelectedItem.remove(loanColumnIndex);
                EventBus.getDefault().post(new SelectedItems(listsSelectedItem));
            }
            return true;
        }

        public View resetAll(int i) {
            mCursor.moveToPosition(i);
            int c = mCursor.getColumnIndex(BfwContract.Loan._ID);

            //int position = getAdapterPosition();

            //mCursor.moveToPosition(i);
            Toast.makeText(mContext,"Amount " +c,Toast.LENGTH_LONG).show();


            return this.amount_tot;
        }

        boolean return_if_val_in_array(Long val) {
            //Toast.makeText(mContext,return_count()+" selected",Toast.LENGTH_LONG).show();
            for (Long v : listsSelectedItem) {
                if (val == v) {
                    return true;
                }
            }
            return false;
        }
    }
}
