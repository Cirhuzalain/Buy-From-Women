package com.nijus.alino.bfwcoopmanagement.loans.adapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
//import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.DetailBuyerActivity;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.ui.activities.DetailLoanActivity;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    private Cursor mCursor;
    final private Context mContext;
    final private View mEmptyView;
    final private LoanAdapterOnClickHandler mClickHandler;
    final private LoanAdapterOnLongClickHandler mLongClickHandler;

    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;


    public LoanAdapter(Context mContext, View mEmptyView, LoanAdapterOnClickHandler mClickHandler, LoanAdapterOnLongClickHandler mLongClickHandler) {
        this.mContext = mContext;
        this.mEmptyView = mEmptyView;
        this.mClickHandler = mClickHandler;
        this.mLongClickHandler = mLongClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        holder.farmerImage.setImageResource(R.drawable.profile);
        //holder.iconBack.setVisibility(View.GONE);

        holder.mInstitution.setText("From "+mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
        holder.mUname.setText("225,000 FRW");

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

    public interface  LoanAdapterOnClickHandler {
        void onClick(Long farmerId, ViewHolder vh);
    }

    public interface LoanAdapterOnLongClickHandler {
        boolean onLongClick(Long farmerId, ViewHolder vh);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final ImageView farmerImage;
        public final TextView mUname;
        public final TextView mInstitution;
        public RelativeLayout iconBack, iconFront, iconContainer;
        public LinearLayout view_foreground;
        //lisste des coop agent deja selectionner
        public List<Integer> listsSelectedItem = new ArrayList<>();

        private ActionModeCallback actionModeCallback;
        private ActionMode actionMode;
        private int selectionned;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            farmerImage = view.findViewById(R.id.l_icon);
            mUname = view.findViewById(R.id.l_name);
            mInstitution = view.findViewById(R.id.l_institution);
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
            int buyerColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);
            mClickHandler.onClick(mCursor.getLong(buyerColumnIndex), this);

            //Toast.makeText(mContext, " ++Click " + position, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mContext, DetailLoanActivity.class);
            intent.putExtra("loanId", buyerColumnIndex);
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
            if (!listsSelectedItem.contains(Integer.valueOf(this.getAdapterPosition()))) {
                //actionMode =((AppCompatActivity)mContext).startSupportActionMode(actionModeCallback);

                //actionMode = mContext.startSupportActionMode(actionModeCallback);
                this.iconFront.setVisibility(View.GONE);
                this.view_foreground.setBackgroundColor(Color.argb(20, 0, 0, 0));
                resetIconYAxis(this.iconBack);
                this.iconBack.setVisibility(View.VISIBLE);
                this.iconBack.setAlpha(1);
                //if (currentSelectedIndex == vh.getAdapterPosition()) {
                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, true);
                //resetCurrentIndex();
                //istsSelectedItem.forEach(int a as i);
                listsSelectedItem.add(Integer.valueOf(this.getAdapterPosition()));
                Toast.makeText(mContext.getApplicationContext()," ADDED "+Integer.valueOf(this.getAdapterPosition()), Toast.LENGTH_LONG).show();

            } else {
                this.iconBack.setVisibility(View.GONE);
                resetIconYAxis(this.iconFront);
                this.view_foreground.setBackgroundColor(Color.argb(2, 0, 0, 0));
                this.iconFront.setVisibility(View.VISIBLE);
                this.iconFront.setAlpha(1);

                FlipAnimator.flipView(mContext.getApplicationContext(), this.iconBack, this.iconFront, false);
                listsSelectedItem.remove(Integer.valueOf(this.getAdapterPosition()));
                //Toast.makeText(this," REMOVE "+Integer.valueOf(vh.getAdapterPosition()), Toast.LENGTH_LONG).show();
            }
            return true;
        }

    }
    //Class action bar pour ajouter les element selectionner dans la toolbar

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected coop agent
                    //deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mAdapter.clearSelections();
            //swipeRefreshLayout.setEnabled(true);
            //actionMode = null;
            //recyclerView.post(new Runnable() {
            //@Override
            //public void run() {
            //mAdapter.resetAnimationIndex();
            // mAdapter.notifyDataSetChanged();
            //}
            //});
        }
    }
}