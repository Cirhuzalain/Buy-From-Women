package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateLoanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save_loan;
    private EditText ed_date_value;
    private Button date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_loan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save_loan = findViewById(R.id.save_loan);
        ed_date_value = findViewById(R.id.ed_date_value);
        date = findViewById(R.id.date_selected);

        save_loan.setOnClickListener(this);
        date.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View view) {
        if(view==save_loan){
        Toast.makeText(this,"Comming soon",Toast.LENGTH_LONG).show();}
        else if(view == date){

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
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.YEAR, 2000);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
            calendar.set(Calendar.YEAR, 2030);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year

            dialog.show();
        }
    }
}
