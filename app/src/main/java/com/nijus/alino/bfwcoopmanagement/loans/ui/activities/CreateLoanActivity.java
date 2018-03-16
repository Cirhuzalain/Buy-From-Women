package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoan;
import com.nijus.alino.bfwcoopmanagement.loans.sync.AddLoan;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateLoanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save_loan;
    private EditText ed_date_value;
    private Button date;
    private Spinner loan_orign_spinner, fin_inst_spinner, purpose_loan;
    private AutoCompleteTextView principal_amount, interest_rate, duration_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_loan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save_loan = findViewById(R.id.save_loan);
        ed_date_value = findViewById(R.id.ed_date_value);
        date = findViewById(R.id.date_selected);
        loan_orign_spinner = findViewById(R.id.loan_orign_spinner);
        fin_inst_spinner = findViewById(R.id.fin_inst_spinner);
        purpose_loan = findViewById(R.id.purpose_loan_spinner);
        principal_amount = findViewById(R.id.principal_amount);
        interest_rate = findViewById(R.id.interest_rate);
        duration_month = findViewById(R.id.duration_month);


        save_loan.setOnClickListener(this);
        date.setOnClickListener(this);

        populateSpinnerFarmer();

        ArrayAdapter<CharSequence> adapter_microfin = ArrayAdapter.createFromResource(this,
                R.array.fin_institution, android.R.layout.simple_spinner_item);
        adapter_microfin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fin_inst_spinner.setAdapter(adapter_microfin);

        ArrayAdapter<CharSequence> adapter_purpose_loan = ArrayAdapter.createFromResource(this,
                R.array.purpose_loan, android.R.layout.simple_spinner_item);
        adapter_purpose_loan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose_loan.setAdapter(adapter_purpose_loan);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void populateSpinnerFarmer() {
        String[] fromColumns = {BfwContract.Farmer.COLUMN_NAME};
        // View IDs to map the columns (fetched above) into
        int[] toViews = {android.R.id.text1};
        Cursor cursor = getContentResolver().query(
                BfwContract.Farmer.CONTENT_URI, null, null, null, null
        );
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, // context
                    android.R.layout.simple_spinner_item, // layout file
                    cursor, // DB cursor
                    fromColumns, // data to bind to the UI
                    toViews, // views that'll represent the data from `fromColumns`
                    0
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Create the list view and bind the adapter
            loan_orign_spinner.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == save_loan) {
            int farmer_spiner_id;
            String fin_inst;
            String purpose_loan_string;
            Date start_date = null;

            if (TextUtils.isEmpty(ed_date_value.getText())) {
                ed_date_value.setError(getString(R.string.error_field_required));
            }

            if (TextUtils.isEmpty(principal_amount.getText())) {
                principal_amount.setError(getString(R.string.error_field_required));
            }
            if (TextUtils.isEmpty(interest_rate.getText())) {
                interest_rate.setError(getString(R.string.error_field_required));
            }
            if (TextUtils.isEmpty(duration_month.getText())) {
                duration_month.setError(getString(R.string.error_field_required));
            }

            if (!TextUtils.isEmpty(ed_date_value.getText()) && !TextUtils.isEmpty(principal_amount.getText())
                    && !TextUtils.isEmpty(interest_rate.getText()) && !TextUtils.isEmpty(duration_month.getText())) {
                //Ready to save loan's data
                Cursor cursor = (Cursor) loan_orign_spinner.getSelectedItem();
                farmer_spiner_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer._ID));

                //get date in long
                String date_start_string = ed_date_value.getText().toString();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                try {
                    start_date = df.parse(date_start_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                fin_inst = (String) fin_inst_spinner.getSelectedItem();
                purpose_loan_string = (String) purpose_loan.getSelectedItem();
                PojoLoan pojoLoan = new PojoLoan();
                pojoLoan.setFarmer_id(farmer_spiner_id);
                pojoLoan.setPurpose(purpose_loan_string);
                pojoLoan.setAmount(Double.valueOf(principal_amount.getText().toString()));
                pojoLoan.setInterest_rate(Double.valueOf(interest_rate.getText().toString()));
                pojoLoan.setDuration(Double.valueOf(duration_month.getText().toString()));
                pojoLoan.setStart_date(start_date.getTime());
                pojoLoan.setFinancial_institution(fin_inst);

                Bundle bundle = new Bundle();
                bundle.putParcelable("loan", pojoLoan);

                Intent intent = new Intent(this, AddLoan.class);
                intent.putExtra("loan_data", bundle);
                this.startService(intent);
                finish();
            }

        } else if (view == date) {
            //select the loan start date
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, (month));
                    calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    ed_date_value.setText(sdf.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.YEAR, 2000);
            // Used to hide previous date,month and year
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.set(Calendar.YEAR, 2030);
            // Used to hide future date,month and year
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            dialog.show();
        }
    }

    private boolean isValidString(String word) {
        return word.length() >= 3;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess()) {
            startActivity(new Intent(getApplicationContext(), LoanActivity.class));
            Toast.makeText(this, saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            startActivity(new Intent(getApplicationContext(), LoanActivity.class));
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
