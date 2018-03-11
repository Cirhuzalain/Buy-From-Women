package com.nijus.alino.bfwcoopmanagement.products.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;

public class ProductAdapter extends BaseAdapter {
    private Cursor mCursor;
    private final Context mContext;
    private View mEmptyView;
    private final boolean mIsProduct;
    private CardView cardView;

    public ProductAdapter(Context context, Product[] product, boolean isProduct) {
        this.mContext = context;
        this.mIsProduct = isProduct;
    }

    public ProductAdapter(Context context, View mEmptyView, boolean isProduct) {
        this.mContext = context;
        this.mIsProduct = isProduct;
        this.mEmptyView = mEmptyView;
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public String getName(int i) {
        mCursor.moveToPosition(i);
        String n = mCursor.getString(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME));
        return n;
    }

    public int getServerProductId(int i) {
        mCursor.moveToPosition(i);
        int id = mCursor.getInt(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_SERVER_ID));
        return id;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.product, null);
        }

        mCursor.moveToPosition(i);

        final ImageView imageView = view.findViewById(R.id.productImage);
        final TextView productName = view.findViewById(R.id.productName);
        final TextView productQty = view.findViewById(R.id.productQty);
        final TextView productPrice = view.findViewById(R.id.productPrice);
        cardView = view.findViewById(R.id.card_view);

        if (mIsProduct) {
            imageView.setImageResource(R.mipmap.farmer_bg);
        } else {
            imageView.setImageResource(R.mipmap.farmer_bg);
        }

        final View scrim = view.findViewById(R.id.scrim);

        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_IS_SYNC)) == 1;
        boolean isUpdate = mCursor.getLong(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_IS_UPDATE)) == 1;
        if (isUpdate && isSync) {
            scrim.setBackgroundResource(R.drawable.srim_success);
        } else {
            scrim.setBackgroundResource(R.drawable.srim_error);
        }
        Double qty = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_VENDOR_QTY)));
        productName.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME))
                .toUpperCase());
        productPrice.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRICE)) + " RWF");
        productQty.setText(String.valueOf(qty) + " KG");

        return view;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        try {
            mEmptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {

        }
    }

    public CardView selectedCardView(int i) {
        mCursor.moveToPosition(i);

        notifyDataSetChanged();
        return cardView;
    }

}
