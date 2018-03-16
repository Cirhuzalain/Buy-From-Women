package com.nijus.alino.bfwcoopmanagement.purchases.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.dummy.DummyCont;
import com.nijus.alino.bfwcoopmanagement.purchases.ui.fragment.PurchaseOrderListFragment;

import java.util.List;

public class PurchaseOrderAdapter extends RecyclerView.Adapter<PurchaseOrderAdapter.ViewHolder> {
    private final List<DummyCont.DummyPurchase> mValues;
    private final PurchaseOrderListFragment.OnListFragmentInteractionListener mListener;

    public PurchaseOrderAdapter(List<DummyCont.DummyPurchase> items, PurchaseOrderListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public PurchaseOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_purchase_order_item, parent, false);
        return new PurchaseOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PurchaseOrderAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mProdIcon.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSeason;
        public final TextView mVendor;
        public final TextView mDate;
        public final TextView mCoop;
        public final ImageView mProdIcon;
        public DummyCont.DummyPurchase mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSeason = view.findViewById(R.id.po_harv_season);
            mVendor = view.findViewById(R.id.po_vendor);
            mDate = view.findViewById(R.id.po_date);
            mCoop = view.findViewById(R.id.po_coop);
            mProdIcon = view.findViewById(R.id.prod_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCoop.getText() + "'";
        }
    }
}
