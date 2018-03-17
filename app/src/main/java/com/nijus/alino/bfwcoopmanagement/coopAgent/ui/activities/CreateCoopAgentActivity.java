package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.pojo.PojoAgent;
import com.nijus.alino.bfwcoopmanagement.coopAgent.sync.AddAgent;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CreateCoopAgentActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;
    private Spinner coop;
    private Button create_coop_agent_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coop_agent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        name = findViewById(R.id.name_ca);
        phone = findViewById(R.id.name_phone_ca);
        email = findViewById(R.id.address_mail_ca);
        coop = findViewById(R.id.spinner_ca);
        populateSpinnerCoops();

        create_coop_agent_btn = findViewById(R.id.create_coop_agent);
        create_coop_agent_btn.setOnClickListener(this);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void populateSpinnerCoops() {
        String[] fromColumns = {BfwContract.Coops.COLUMN_COOP_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = this.getContentResolver().query(
                BfwContract.Coops.CONTENT_URI,
                null,
                null,
                null,
                null
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
            coop.setAdapter(adapter);
        }
    }

    private boolean isValidString(String word) {
        return word.length() >= 3;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess()) {
            Toast.makeText(getApplicationContext(), saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        } else {
            Toast.makeText(this, saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        } else {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {

        int coop_spiner_id;

        // Check if value's length entered is > 3 char .
        if (!isValidString(String.valueOf(name.getText()))) {
            name.setError(getString(R.string.error_invalid_product_name));
        }

        // Check for a valid qty
        if (TextUtils.isEmpty(phone.getText())) {
            phone.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(email.getText())) {
            email.setError(getString(R.string.error_field_required));
        }

        if (isValidString(String.valueOf(name.getText())) && !TextUtils.isEmpty(name.getText())
                && !TextUtils.isEmpty(email.getText())) {

            Cursor cursor = (Cursor) coop.getSelectedItem();
            coop_spiner_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));

            PojoAgent pojoAgent = new PojoAgent();
            pojoAgent.setCoop(coop_spiner_id);
            pojoAgent.setName(String.valueOf(name.getText()));
            pojoAgent.setMail(String.valueOf(email.getText()));
            pojoAgent.setPhone(String.valueOf(phone.getText()));

            Bundle bundle = new Bundle();
            bundle.putParcelable("agent", pojoAgent);

            Intent intent = new Intent(getApplicationContext(), AddAgent.class);
            intent.putExtra("agent_data", bundle);
            startService(intent);
        }
    }
}
