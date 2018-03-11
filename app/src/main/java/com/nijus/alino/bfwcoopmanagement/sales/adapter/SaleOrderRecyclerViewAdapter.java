package com.nijus.alino.bfwcoopmanagement.sales.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.sales.ui.fragment.SaleOrderFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.dummy.DummyCont;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyCont.DummyItem} and makes a call to the
 * specified {@link SaleOrderFragment.OnListFragmentInteractionListener}.
 */
public class SaleOrderRecyclerViewAdapter extends RecyclerView.Adapter<SaleOrderRecyclerViewAdapter.ViewHolder> {

    private final List<DummyCont.DummyItem> mValues;
    private final SaleOrderFragment.OnListFragmentInteractionListener mListener;

    public SaleOrderRecyclerViewAdapter(List<DummyCont.DummyItem> items, SaleOrderFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mSalesIcon.setImageResource(R.drawable.ic_local_grocery_store_black_24dp);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final ImageView mSalesIcon;
        public DummyCont.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSalesIcon = view.findViewById(R.id.prod_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " ";
        }
    }
}
