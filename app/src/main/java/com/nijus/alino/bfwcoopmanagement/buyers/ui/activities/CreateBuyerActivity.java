package com.nijus.alino.bfwcoopmanagement.buyers.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;

public class CreateBuyerActivity extends AppCompatActivity {
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;
    private Button create_buyer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_buyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        name = findViewById(R.id.name_b);
        phone = findViewById(R.id.name_phone_b);
        email = findViewById(R.id.address_mail_b);

        create_buyer_btn = findViewById(R.id.create_b);
        create_buyer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent.putExtra("coop_agent", new CoopAgentParcelable(,phone.getText().toString(),
                //email.getText().toString(),coop.getSelectedItem().toString()));
/*
                Bundle data = getIntent().getExtras();
                CoopAgentParcelable coopAgentParcelable = (CoopAgentParcelable) data.getParcelable("coop_agent");*/

                Toast.makeText(getApplicationContext()," enregister buyer",Toast.LENGTH_LONG).show();
            }
        });


        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
