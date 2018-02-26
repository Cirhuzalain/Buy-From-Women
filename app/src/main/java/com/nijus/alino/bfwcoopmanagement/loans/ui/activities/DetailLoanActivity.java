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
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoan;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.loans.sync.AddLoan;
import com.nijus.alino.bfwcoopmanagement.loans.sync.AddPayment;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateLoanPayment;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.PaymentBottomSheetDialogFragment;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.ScheduleBottomSheetDialogFragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class DetailLoanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener{
    public static final String ARG_KEY = "key";
    Long mLoanId;
    private Uri mUri;
    private String name;
    private String mKey;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView loan_details, gen_info_pic;
    private TextView name_b_details, start_date, loan_purpose,
            principal_amount, interest_rate, duration_month,
            fin_inst_spinner;

    private Button schedule, payment, save;

    private long loan_id, loan_id_from_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loan);

        getSupportLoaderManager().initLoader(0, null, this);

        //get Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("loanId")) {
            mLoanId = intent.getLongExtra("loanId", 0);
            mUri = BfwContract.Loan.buildLoanUri(mLoanId);
        }

        FloatingActionButton fab = findViewById(R.id.fab_edit_loan);
        loan_details = findViewById(R.id.loan_details);
        loan_details.setImageResource(R.mipmap.loan_bg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateLoanActivity.class);
                intent.putExtra("loanId", loan_id_from_server);
                startActivity(intent);
            }
        });

        gen_info_pic = findViewById(R.id.gen_info_pic);


        name_b_details = findViewById(R.id.name_b_details);
        start_date = findViewById(R.id.phone_b_details);
        loan_purpose = findViewById(R.id.mail_b_details);
        principal_amount = findViewById(R.id.principal_amount);
        interest_rate = findViewById(R.id.interest_rate);
        duration_month = findViewById(R.id.duration_month);
        fin_inst_spinner = findViewById(R.id.fin_inst_spinner);

        //Listeners to buttons to call bottom sheets
        schedule = findViewById(R.id.schedule_);
        payment = findViewById(R.id.payment_);
        save = findViewById(R.id.save_);

        schedule.setOnClickListener(this);
        payment.setOnClickListener(this);
        save.setOnClickListener(this);

        collapsingToolbarLayout = findViewById(R.id.name_loan);
        toolbar = findViewById(R.id.toolbar_loan);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String loanSelection = BfwContract.Loan.TABLE_NAME + "." +
                BfwContract.Loan._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    mUri,
                    null,
                    loanSelection,
                    new String[]{Long.toString(mLoanId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            int vendor_id = data.getInt(data.getColumnIndex(BfwContract.Loan.COLUMN_FARMER_ID));
            loan_id = data.getInt(data.getColumnIndex(BfwContract.Loan._ID));
            loan_id_from_server = data.getLong(data.getColumnIndex(BfwContract.Loan.COLUMN_SERVER_ID));
            /**
             * Get the real name of farmer
             * **/
            String vendorSelect = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer._ID + " =  ? ";

            Cursor cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null,
                    vendorSelect,
                    new String[]{Long.toString(vendor_id)},
                    null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    name = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
                }
            }

            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);
            gen_info_pic.setImageResource(R.mipmap.male);
            name_b_details.setText(name);

            Long getDate = data.getLong(data.getColumnIndex(BfwContract.Loan.COLUMN_START_DATE));
            Date start_date_date = new Date(getDate);
            String date_string = DateFormat.getDateInstance().format(start_date_date);

            start_date.setText(date_string);

            loan_purpose.setText(data.getString(data.getColumnIndex(BfwContract.Loan.COLUMN_PURPOSE)));
            principal_amount.setText("" + data.getDouble(data.getColumnIndex(BfwContract.Loan.COLUMN_AMOUNT)));
            interest_rate.setText("" + data.getDouble(data.getColumnIndex(BfwContract.Loan.COLUMN_INTEREST_RATE)));
            duration_month.setText("" + data.getDouble(data.getColumnIndex(BfwContract.Loan.COLUMN_DURATION)));
            fin_inst_spinner.setText(data.getString(data.getColumnIndex(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule_: {
                Bundle bundle = new Bundle();
                bundle.putLong("id_loan", loan_id); // set Fragmentclass Arguments
                BottomSheetDialogFragment bottomSheetDialogFragment = new ScheduleBottomSheetDialogFragment();
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                break;
            }
            case R.id.payment_: {
                Bundle bundle = new Bundle();
                bundle.putLong("id_loan", loan_id); // set Fragmentclass Arguments
                BottomSheetDialogFragment bottomSheetDialogFragment = new PaymentBottomSheetDialogFragment();
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                break;
            }
            case R.id.save_: {
                final View alertLayout = View.inflate(this, R.layout.save_payment_details_dialog, null);
                final AutoCompleteTextView amount_pay = alertLayout.findViewById(R.id.amount_pay);
                Button confirm = alertLayout.findViewById(R.id.save_pay);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(amount_pay.getText())) {
                            amount_pay.setError(getString(R.string.error_field_required));
                        }
                        else
                        {
                            PojoLoanPayment pojoLoanPayment = new PojoLoanPayment();
                            pojoLoanPayment.setAmount(Double.valueOf(amount_pay.getText().toString()));
                            Date start_date_date = new Date();

                            pojoLoanPayment.setPayment_date(start_date_date.getTime());
                            pojoLoanPayment.setLoan_id((int)loan_id);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("loan_payment", pojoLoanPayment);

                            Intent intent = new Intent(getBaseContext(), AddPayment.class);
                            intent.putExtra("loan_payment_data", bundle);
                            getBaseContext().startService(intent);
                        }

                        Toast.makeText(getApplicationContext(), "bree",Toast.LENGTH_LONG).show();
                    }
                });


                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setView(alertLayout);

                adb.setPositiveButton("Cancel",null);

                /*adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });*/
                adb.show();
                break;
            }
        }
    }

}


