package com.nijus.alino.bfwcoopmanagement.coops.adapter;


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
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
//import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

public class CoopAdapter extends RecyclerView.Adapter<CoopAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private CoopAdapterOnClickHandler mClickHandler;
    final private CoopAdapterOnLongClickHandler mLongClickHandler;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;

   /* public CoopAdapter(Context context, View view, CoopAdapterOnClickHandler vh) {
        mContext = context;
        mEmptyView = view;
        mClickHandler = vh;
    }*/

    public CoopAdapter(Context mContext, View mEmptyView, CoopAdapterOnClickHandler mClickHandler, CoopAdapterOnLongClickHandler mLongClickHandler) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
        this.mLongClickHandler = mLongClickHandler;


    }
    /*public CoopAdapter(Context context, View view, CoopAdapterOnLongClickHandler vh) {
        mContext = context;
        mEmptyView = view;
        mLongClickHandler = vh;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        holder.farmerImage.setImageResource(R.drawable.ic_coops_24dp);
        holder.iconBack.setVisibility(View.GONE);

        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
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

    public interface CoopAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }
    public interface CoopAdapterOnLongClickHandler {
        void onLongClick(Long farmerId, ViewHolder vh);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final TextView mUname;
        public RelativeLayout iconBack, iconFront,iconContainer;
        public LinearLayout view_foreground;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            mUname = view.findViewById(R.id.u_name);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view_foreground = (LinearLayout) view.findViewById(R.id.view_foreground);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int coopColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);
            mClickHandler.onClick(mCursor.getLong(coopColumnIndex), this);

            //Toast.makeText(mContext," ok",Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onLongClick(View view) {
            //Toast.makeText(mContext," ok2",Toast.LENGTH_LONG).show();
            int position = getAdapterPosition();
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            mCursor.moveToPosition(position);
            int coopColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);
            mLongClickHandler.onLongClick(mCursor.getLong(coopColumnIndex), this);
            return false;
        }
    }
}
