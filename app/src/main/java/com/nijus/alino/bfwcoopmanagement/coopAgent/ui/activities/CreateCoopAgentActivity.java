package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

public class CreateCoopAgentActivity extends AppCompatActivity {
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;
    private Spinner coop;
    private Button create_coop_agent_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coop_agent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name_ca);
        phone = findViewById(R.id.name_phone_ca);
        email = findViewById(R.id.address_mail_ca);
        coop = findViewById(R.id.spinner_ca);

        create_coop_agent_btn = findViewById(R.id.create_coop_agent);
        create_coop_agent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent.putExtra("coop_agent", new CoopAgentParcelable(,phone.getText().toString(),
                        //email.getText().toString(),coop.getSelectedItem().toString()));
/*
                Bundle data = getIntent().getExtras();
                CoopAgentParcelable coopAgentParcelable = (CoopAgentParcelable) data.getParcelable("coop_agent");*/

                Toast.makeText(getApplicationContext()," enregister coop agent ",Toast.LENGTH_LONG).show();
            }
        });


        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




}
