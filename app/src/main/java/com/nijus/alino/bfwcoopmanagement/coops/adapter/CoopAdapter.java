package com.nijus.alino.bfwcoopmanagement.coops.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.DetailCoopActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.DetailFarmerActivity;

import java.util.ArrayList;
import java.util.List;

public class CoopAdapter extends RecyclerView.Adapter<CoopAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private CoopAdapterOnClickHandler mClickHandler;
    final private CoopAdapterOnLongClickHandler mLongClickHandler;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;

    public CoopAdapter(Context mContext, View mEmptyView, CoopAdapterOnClickHandler mClickHandler, CoopAdapterOnLongClickHandler mLongClickHandler) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
        this.mLongClickHandler = mLongClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.farmerImage.setImageResource(R.mipmap.coop);
        holder.pic_male.setImageResource(R.mipmap.male);
        holder.pic_female.setImageResource(R.mipmap.female);
        holder.iconBack.setVisibility(View.GONE);

        holder.mUname.setText(mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
        holder.nbr_male.setText("12");
        holder.nbr_female.setText("8");
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
        public final ImageView farmerImage,pic_male,pic_female;
        public final TextView mUname,nbr_male,nbr_female;
        public RelativeLayout iconBack, iconFront,iconContainer;
        public LinearLayout view_foreground;
        public List<Integer> listsSelectedItem = new ArrayList<>();


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.u_icon);
            pic_female = view.findViewById(R.id.pic_male);
            pic_male = view.findViewById(R.id.pic_female);
            mUname = view.findViewById(R.id.u_name);
            nbr_female = view.findViewById(R.id.nbr_female);
            nbr_male = view.findViewById(R.id.nbr_male);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer =  view.findViewById(R.id.icon_container);
            view_foreground =  view.findViewById(R.id.view_foreground);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mCursor.moveToPosition(position);
            int coopColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);
            mClickHandler.onClick(mCursor.getLong(coopColumnIndex), this);

            //si click simple, appel de l'activity  details farmer
            Intent intent = new Intent(mContext, DetailCoopActivity.class);
            intent.putExtra("coopId", coopColumnIndex);
            mContext.startActivity(intent);
        }

        private void resetIconYAxis(View view) {
            if (view.getRotationY() != 0) {
                view.setRotationY(0);
            }
        }

        @Override
        public boolean onLongClick(View view) {

            //annimation et delete un coop agent
            if (!return_if_val_in_array(Integer.valueOf(this.getAdapterPosition()))) {
                this.iconFront.setVisibility(View.GONE);
                this.view_foreground.setBackgroundColor(Color.argb(20, 0, 0, 0));
                resetIconYAxis(this.iconBack);
                this.iconBack.setVisibility(View.VISIBLE);
                this.iconBack.setAlpha(1);
                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, true);

                listsSelectedItem.add(Integer.valueOf(this.getAdapterPosition()));

            } else {
                this.iconBack.setVisibility(View.GONE);
                resetIconYAxis(this.iconFront);
                this.view_foreground.setBackgroundColor(Color.argb(2, 0, 0, 0));
                this.iconFront.setVisibility(View.VISIBLE);
                this.iconFront.setAlpha(1);

                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, false);
                listsSelectedItem.remove(Integer.valueOf(this.getAdapterPosition()));
            }
            return true;
        }
        boolean return_if_val_in_array(int val)
        {
            for (int v : listsSelectedItem){
                if (val == v){
                    return true;
                }
            }
            return false;
        }
    }
}
