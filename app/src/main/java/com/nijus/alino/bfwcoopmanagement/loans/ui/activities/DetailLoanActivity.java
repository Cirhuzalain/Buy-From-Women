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
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.PaymentBottomSheetDialogFragment;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.ScheduleBottomSheetDialogFragment;


public class DetailLoanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {
    Long mBuyerId;
    private Uri mUri;
    private String name;
    private String mKey;
    public static final String ARG_KEY = "key";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private Button schedule,payment, save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loan);
        
        getSupportLoaderManager().initLoader(0,null,this);

        //get Coop Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("loanId")) {
            mBuyerId = intent.getLongExtra("loanId", 0);
            mUri = BfwContract.Coops.buildCoopUri(mBuyerId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_loan);
        toolbar = findViewById(R.id.toolbar_loan);
        //toolbar.setTitle(mBuyerId+"");
        //Log.d("DetailCoopActivity",mBuyerId+"");


        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab_edit_loan);
        ImageView imageView = findViewById(R.id.loan_details);
        //imageView.setImageResource(R.mipmap.coopbg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Comming soon",Toast.LENGTH_LONG).show();
                /*Intent intent1 = new Intent(getApplicationContext(),UpdateBuyerActivity.class);
                intent1.putExtra("buyerId", mBuyerId);
                startActivity(intent1);*/
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
                    new String[]{Long.toString(mBuyerId)},
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

            //Affichages des donnees venant dans lka base des donnes


           /* TextView name_ca_details = findViewById(R.id.name_b_details);
            name_ca_details.setText("Name Lastname Buyer");
            TextView phone_ca_details = findViewById(R.id.phone_b_details);
            phone_ca_details.setText("+2501286555");
            *//*TextView coop_ca_details = findViewById(R.id.coop_ca_details);
            coop_ca_details.setText("ici le text");*//*
            TextView mail_ca_details = findViewById(R.id.mail_b_details);
            mail_ca_details.setText("ici @ text");*/

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
               // BottomSheetDialogFragment bottomSheetDialogFragment = new ScheduleBottomSheetDialogFragment();
                ///bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            }
        }
    }
}


