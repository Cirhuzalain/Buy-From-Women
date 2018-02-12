package com.nijus.alino.bfwcoopmanagement.products.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;

public class ProductAdapter extends BaseAdapter {

    private final Context mContext;
    private final Product[] mProduct;
    private final boolean mIsProduct;

    public ProductAdapter(Context context, Product[] product, boolean isProduct) {
        this.mContext = context;
        this.mProduct = product;
        this.mIsProduct = isProduct;
    }

    @Override
    public int getCount() {
        return mProduct.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
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

        final Product product = mProduct[i];
        final ImageView imageView = view.findViewById(R.id.productImage);
        final TextView productName = view.findViewById(R.id.productName);
        final TextView productQty = view.findViewById(R.id.productQty);
        final TextView productPrice = view.findViewById(R.id.productPrice);

        if (mIsProduct) {
            imageView.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_local_grocery_store_black_24dp);
        }

        productName.setText(product.getName());
        productPrice.setText(product.getPrice());
        productQty.setText(product.getQuantity());

        return view;
    }
}
