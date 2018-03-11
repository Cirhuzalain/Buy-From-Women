package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.pojo.PojoAgent;
import com.nijus.alino.bfwcoopmanagement.coopAgent.sync.UpdateAgent;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UpdateCoopAgent extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;
    private Spinner coop;
    private Button create_coop_agent_btn;
    private int agent_id_from_server;
    private Long id_agent_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coop_agent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        getSupportLoaderManager().initLoader(0, null, this);

        name = findViewById(R.id.name_ca);
        phone = findViewById(R.id.name_phone_ca);
        email = findViewById(R.id.address_mail_ca);
        coop = findViewById(R.id.spinner_ca);
        populateSpinnerCoops();

        create_coop_agent_btn = findViewById(R.id.create_coop_agent);
        create_coop_agent_btn.setOnClickListener(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            Intent intent = new Intent(getApplicationContext(), UpdateAgent.class);
            intent.putExtra("agent_id", agent_id_from_server);
            intent.putExtra("agent_data", bundle);
            startService(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_agent_long = extras.getLong("coopAgentId");
        } else {
            // handle case
        }

        //Toast.makeText(this," Get "+id_loan_long,Toast.LENGTH_LONG).show();
        String loanSelection = BfwContract.CoopAgent.TABLE_NAME + "." +
                BfwContract.CoopAgent._ID + " =  ? ";//TABLE SERVER ID
        return new CursorLoader(
                this,
                BfwContract.CoopAgent.CONTENT_URI,
                null,
                loanSelection,
                new String[]{Long.toString(id_agent_long)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            agent_id_from_server = data.getInt(data.getColumnIndex(BfwContract.CoopAgent._ID));
            name.setText(data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_NAME)) + "");
            phone.setText("" + data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_PHONE)));
            email.setText("" + data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_EMAIL)));

            setSpinnerItemByIdCoop(coop, data.getInt(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_COOP_ID)));
        }
    }

    public void setSpinnerItemByIdCoop(Spinner spinner, int _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            int id = value.getInt(value.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));
            if (id == _id) {
                spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean isValidString(String word) {
        return word.length() >= 3;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        } else {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            //onSupportNavigateUp();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess()) {
            Toast.makeText(this, saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        } else {
            Toast.makeText(this, saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            //onSupportNavigateUp();
        }
    }
}
