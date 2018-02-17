package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.PaymentBottomSheetDialogFragment;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.ScheduleBottomSheetDialogFragment;

public class DetailLoanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {
    Long mLoanId;
    private Uri mUri;
    private String name;
    private String mKey;
    public static final String ARG_KEY = "key";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView loan_details;

    private Button schedule,payment, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loan);
        
        getSupportLoaderManager().initLoader(0,null,this);

        //get Coop Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("loanId")) {
            mLoanId = intent.getLongExtra("loanId", 0);
            mUri = BfwContract.Coops.buildCoopUri(mLoanId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_loan);
        toolbar = findViewById(R.id.toolbar_loan);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_edit_loan);
        loan_details = findViewById(R.id.loan_details);
        loan_details.setImageResource(R.mipmap.loan_bg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),UpdateLoanActivity.class);
                intent1.putExtra("loanId", mLoanId);
                startActivity(intent1);
            }
        });

        //les ecouteur sur les button pour appeler les style sheet bottom
        schedule = findViewById(R.id.schedule_);
        payment = findViewById(R.id.payment_);
        save = findViewById(R.id.save_);

        schedule.setOnClickListener(this);
        payment.setOnClickListener(this);
        save.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String coopSelection = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    mUri,
                    null,
                    coopSelection,
                    new String[]{Long.toString(mLoanId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.schedule_: {
                BottomSheetDialogFragment bottomSheetDialogFragment = new ScheduleBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            }
            case R.id.payment_: {
                BottomSheetDialogFragment bottomSheetDialogFragment = new PaymentBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            }
            case R.id.save_: {
                final  View alertLayout = View.inflate(this,R.layout.save_payment_details_dialog,null);

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setView(alertLayout);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                adb.show();
                break;
            }
        }
    }
}


