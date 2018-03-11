package com.nijus.alino.bfwcoopmanagement.buyers.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.buyers.pojo.PojoBuyer;
import com.nijus.alino.bfwcoopmanagement.buyers.sync.AddBuyer;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CreateBuyerActivity extends AppCompatActivity {
    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView email;
    private Button create_buyer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_buyer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        name = findViewById(R.id.name_b);
        phone = findViewById(R.id.name_phone_b);
        email = findViewById(R.id.address_mail_b);

        create_buyer_btn = findViewById(R.id.create_b);
        create_buyer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Before to create Buyer, Check if all fields required are not empty
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
                        && !TextUtils.isEmpty(phone.getText())) {
                    PojoBuyer pojoBuyer = new PojoBuyer();
                    pojoBuyer.setName(String.valueOf(name.getText()));
                    pojoBuyer.setMail(String.valueOf(email.getText()));
                    pojoBuyer.setPhone(String.valueOf(phone.getText()));

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Buyer", pojoBuyer);

                    Intent intent = new Intent(getApplicationContext(), AddBuyer.class);
                    intent.putExtra("Buyer_data", bundle);
                    startService(intent);
                }
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
