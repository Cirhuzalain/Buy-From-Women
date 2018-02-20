package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;

import java.util.Calendar;

public class UpdateLoanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save_loan;
    private EditText ed_date_value;
    private Button date;
    private int jr, mois, annee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_loan);

        save_loan = findViewById(R.id.save_loan);
        ed_date_value = findViewById(R.id.ed_date_value);
        date = findViewById(R.id.date_selected);

        save_loan.setOnClickListener(this);
        date.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View view) {
        if (view == save_loan) {
            Toast.makeText(this, "Comming soon", Toast.LENGTH_LONG).show();
        } else if (view == date) {
            final Calendar c = Calendar.getInstance();
            jr = c.get(Calendar.DAY_OF_MONTH);
            mois = c.get(Calendar.MONTH);
            annee = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    ed_date_value.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    , jr, mois, annee);
            datePickerDialog.show();
        }
    }
}
