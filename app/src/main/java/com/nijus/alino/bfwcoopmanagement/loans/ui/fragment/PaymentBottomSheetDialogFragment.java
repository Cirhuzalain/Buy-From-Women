package com.nijus.alino.bfwcoopmanagement.loans.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.adapter.PaymentAdapter;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateLoanPayment;

public class PaymentBottomSheetDialogFragment extends BottomSheetDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {
    private PaymentAdapter loanRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private long id_long, id_loan;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (slideOffset < 0) {dismiss();
            //Toast.makeText(getContext()," "+slideOffset, Toast.LENGTH_LONG).show();
            }
        }

    };
    //@SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //super.setupDialog(dialog,style);
        //getSupportLoaderManager().initLoader(0,null,this);
        getLoaderManager().initLoader(0,null,this);

        id_loan = getArguments().getLong("id_loan");
        //Toast.makeText(getContext(),id_loan+"",Toast.LENGTH_LONG).show();

        View contentView = View.inflate(getContext(), R.layout.payment_fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Make responsive bottom sheet
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.padding_bottom_sheet)/2;
        params.setMargins(width,0,width,0);

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View emptyView = contentView.findViewById(R.id.recyclerview_empty_payment);
        //Context context = this;
        RecyclerView recyclerView = contentView.findViewById(R.id.payment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loanRecyclerViewAdapter = new PaymentAdapter(getContext(), emptyView);

        mRefreshData = contentView.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(loanRecyclerViewAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        id_loan = getArguments().getLong("id_loan");
        String loanPaymentSelection = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment.COLUMN_LOAN_ID + " =  ? ";
         return new CursorLoader(
                    getContext(),
                    BfwContract.LoanPayment.CONTENT_URI,
                    null,
                    loanPaymentSelection,
                    new String[]{Long.toString(id_loan)},
                    null
            );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loanRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loanRecyclerViewAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(0, null, this);
        //getSupportLoaderManager().restartLoader(0,null,this);

    }
    public void back2(PojoLoanPayment p) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("loan_payment", p);
        Intent intent = new Intent(getContext(), UpdateLoanPayment.class);
        intent.putExtra("loan_payment_data", bundle);
        getContext().startService(intent);
    }
}
