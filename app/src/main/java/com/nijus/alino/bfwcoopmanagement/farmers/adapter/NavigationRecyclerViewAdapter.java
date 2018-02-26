package com.nijus.alino.bfwcoopmanagement.farmers.adapter;

import android.content.Context;
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

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.ArrayList;
import java.util.List;

public class NavigationRecyclerViewAdapter extends RecyclerView.Adapter<NavigationRecyclerViewAdapter.ViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private FarmerAdapterOnClickHandler mClickHandler;
    public List<Integer> listsSelectedItem = new ArrayList<>();

    public NavigationRecyclerViewAdapter(Context context, View view, FarmerAdapterOnClickHandler vh) {
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

        holder.farmerImage.setImageResource(R.mipmap.male);

        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME)));
        holder.mUphone.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE)));

        holder.id_cursor_to_delete = mCursor.getString(mCursor.getColumnIndex(BfwContract.Farmer._ID));

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

    public interface FarmerAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final TextView mUname;
        public final TextView mUphone;
        public final ImageView imageView, imagedone;
        public LinearLayout viewForeground;
        public String id_cursor_to_delete;
        public RelativeLayout iconBack, iconFront, iconContainer;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            mUname = view.findViewById(R.id.u_name);
            mUphone = view.findViewById(R.id.u_phone);
            imageView = view.findViewById(R.id.u_sync);

            viewForeground = view.findViewById(R.id.view_foreground);

            imagedone = view.findViewById(R.id.image_done);
            imagedone.setImageResource(R.drawable.ic_done_white_24dp);

            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer = view.findViewById(R.id.icon_container);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int farmerColumnIndex = mCursor.getColumnIndex(BfwContract.Farmer._ID);
            mClickHandler.onClick(mCursor.getLong(farmerColumnIndex), this);
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }

        @Override
        public boolean onLongClick(View view) {

            if (!return_if_val_in_array(Integer.valueOf(this.getAdapterPosition()))) {
                this.iconFront.setVisibility(View.GONE);
                this.viewForeground.setBackgroundColor(Color.argb(20, 0, 0, 0));
                resetIconYAxis(this.iconBack);
                this.iconBack.setVisibility(View.VISIBLE);
                this.iconBack.setAlpha(1);
                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, true);

                listsSelectedItem.add(Integer.valueOf(this.getAdapterPosition()));

            } else {
                this.iconBack.setVisibility(View.GONE);
                resetIconYAxis(this.iconFront);
                this.viewForeground.setBackgroundColor(Color.argb(2, 0, 0, 0));
                this.iconFront.setVisibility(View.VISIBLE);
                this.iconFront.setAlpha(1);

                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, false);
                listsSelectedItem.remove(Integer.valueOf(this.getAdapterPosition()));
            }
            return true;
        }

        boolean return_if_val_in_array(int val) {
            for (int v : listsSelectedItem) {
                if (val == v) {
                    return true;
                }
            }
            return false;
        }
    }
}
