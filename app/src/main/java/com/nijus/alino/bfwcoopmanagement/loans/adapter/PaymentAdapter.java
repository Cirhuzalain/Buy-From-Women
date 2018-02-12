package com.nijus.alino.bfwcoopmanagement.loans.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

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


        //holder.farmerImage.setImageResource(R.drawable.profile);
        //holder.iconBack.setVisibility(View.GONE);

       // holder.mInstitution.setText("From "+mCursor.getString(mCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
        //holder.nextPayemnt.setText("225,000 FRW");
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



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView nextPayemnt;
        public final TextView txt_loan;




        public ViewHolder(View view) {
            super(view);
            mView = view;
            nextPayemnt = view.findViewById(R.id.sc_next_payment);
            txt_loan = view.findViewById(R.id.txt_loan);
            view.setOnClickListener(this);

            //view.setOnClickListener((View.OnClickListener) mContext);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int buyerColumnIndex = mCursor.getColumnIndex(BfwContract.Coops._ID);
            //mClickHandler.onClick(mCursor.getLong(buyerColumnIndex), this);

//            Toast.makeText(mContext, " Click " + position, Toast.LENGTH_LONG).show();

            final  View alertLayout = View.inflate(mContext,R.layout.payment_details_dialog,null);

            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
            adb.setView(alertLayout);
            //adb.setTitle("List");
            //adb.setMessage(name);
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    //dialogInterface.dismiss();
                }
            });
            adb.show();

        }

    }
    //Class action bar pour ajouter les element selectionner dans la toolbar

}
