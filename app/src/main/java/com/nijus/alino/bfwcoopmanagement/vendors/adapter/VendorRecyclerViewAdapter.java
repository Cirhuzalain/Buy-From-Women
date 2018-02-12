package com.nijus.alino.bfwcoopmanagement.vendors.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.dummy.DummyContents;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContents.DummyItem} and makes a call to the
 * specified {@link VendorFragment.OnListFragmentInteractionListener}.
 */
public class VendorRecyclerViewAdapter extends RecyclerView.Adapter<VendorRecyclerViewAdapter.ViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private VendorAdapterOnClickHandler mClickHandler;
    private Uri mUri;
    //private String id_cursor_to_delete ;



    public VendorRecyclerViewAdapter(Context context, View view, VendorAdapterOnClickHandler vh) {
        mContext = context;
        mEmptyView = view;
        mClickHandler = vh;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.farmerImage.setImageResource(R.drawable.profile);

        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME)));
        holder.mUphone.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE)));

        holder.id_cursor_to_delete = mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer._ID));

        //COLUMN_PHONE

        boolean isSync = mCursor.getLong(mCursor.getColumnIndex(BfwContract.Farmer.COLUMN_IS_SYNC)) == 1;
        if (isSync) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        } else {
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

    //add on 01 febrary 2018
    public void removeItem(int position) {
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position,mCursor.getCount() +1);
    }



    public void restoreItem(View v, int position) {

        //mCursor.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    public interface VendorAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final TextView mUname;
        public final TextView mUphone;
        public final ImageView imageView;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;
        public String id_cursor_to_delete;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            mUname = view.findViewById(R.id.u_name);
            mUphone = view.findViewById(R.id.u_phone);
            imageView = view.findViewById(R.id.u_sync);
            //id_cursor_to_delete = null;

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(this);
            id_cursor_to_delete = null;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int farmerColumnIndex = mCursor.getColumnIndex(BfwContract.Farmer._ID);
            mClickHandler.onClick(mCursor.getLong(farmerColumnIndex), this);
        }

    }
}
