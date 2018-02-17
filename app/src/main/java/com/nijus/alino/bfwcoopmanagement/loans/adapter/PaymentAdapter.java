package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;

    public PaymentAdapter(Context mContext, View mEmptyView) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.txt_loan.setText((position+1)+"");
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        try {
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        }
        catch (Exception e)
        {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView nextPayemnt;
        public final TextView txt_loan;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nextPayemnt = view.findViewById(R.id.sc_next_payment);
            txt_loan = view.findViewById(R.id.txt_loan);
        }
    }
}
