package com.nijus.alino.bfwcoopmanagement.buyers.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.buyers.pojo.PojoBuyer;
import com.nijus.alino.bfwcoopmanagement.buyers.sync.UpdateBuyer;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateBuyerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Button update_buyer;
    private  long id_buyer_long;
    private int buyer_local_id, buyer_id_from_server;
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_buyer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        getSupportLoaderManager().initLoader(0,null,this);


        name = findViewById(R.id.name_b);
        phone = findViewById(R.id.name_phone_b);
        email = findViewById(R.id.address_mail_b);

        update_buyer = findViewById(R.id.update_buyer);
        update_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Before to update Buyer, Check if all fields required are not empty
                 **/
                if (!isValidString(String.valueOf(name.getText()))) {
                    name.setError(getString(R.string.error_invalid_product_name));
                }
                if (!isValidString(String.valueOf(email.getText()))) {
                    email.setError(getString(R.string.error_invalid_email));
                }
                if (TextUtils.isEmpty(phone.getText())) {
                    phone.setError(getString(R.string.error_field_required));
                }

                if (isValidString(String.valueOf(name.getText()))
                        && isValidString(String.valueOf(email.getText()))
                        && !TextUtils.isEmpty(phone.getText()))
                {

                    //Toast.makeText(getApplicationContext()," Commingg soon",Toast.LENGTH_LONG).show();
                    PojoBuyer pojoBuyer = new PojoBuyer();
                    pojoBuyer.setName(String.valueOf(name.getText()));
                    pojoBuyer.setMail(String.valueOf(email.getText()));
                    pojoBuyer.setPhone(String.valueOf(phone.getText()));

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("buyer",pojoBuyer);

                    Intent intent = new Intent(getApplicationContext(), UpdateBuyer.class);
                    intent.putExtra("buyer_id", buyer_id_from_server);
                    intent.putExtra("buyer_data", bundle);
                    startService(intent);
                }
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            id_buyer_long = extras.getLong("buyerId");
        } else {
            // handle case
        }

        //Toast.makeText(this," Get "+id_loan_long,Toast.LENGTH_LONG).show();
        String loanSelection = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer._ID + " =  ? ";//TABLE SERVER ID
        return new CursorLoader(
                this,
                BfwContract.Buyer.CONTENT_URI,
                null,
                loanSelection,
                new String[]{Long.toString(id_buyer_long)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            buyer_id_from_server =  data.getInt(data.getColumnIndex(BfwContract.Buyer._ID));
            buyer_local_id = data.getInt(data.getColumnIndex(BfwContract.Buyer._ID));

            name.setText(data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_NAME))+"");
            phone.setText(""+data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_PHONE)));
            email.setText(""+data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_EMAIL)));
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
        if (syncDataEvent.isSuccess()){
            Toast.makeText(this,syncDataEvent.getMessage(),Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        }
        else {
            Toast.makeText(this,syncDataEvent.getMessage(),Toast.LENGTH_LONG).show();
            //onSupportNavigateUp();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess()){
            Toast.makeText(this,saveDataEvent.getMessage(),Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        }
        else {
            Toast.makeText(this,saveDataEvent.getMessage(),Toast.LENGTH_LONG).show();
            //onSupportNavigateUp();
        }
    }
}
