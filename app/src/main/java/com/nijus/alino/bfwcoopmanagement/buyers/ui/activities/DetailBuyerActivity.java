package com.nijus.alino.bfwcoopmanagement.buyers.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class DetailBuyerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_KEY = "key";
    private Long mBuyerId;
    private Uri mUri;
    private String name, phone, mail;
    private String mKey;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView buyer_details, gen_info_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buyer);

        EventBus.getDefault().register(this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("buyerId")) {
            mBuyerId = intent.getLongExtra("buyerId", 0);
            mUri = BfwContract.Buyer.buildBuyerUri(mBuyerId);
        }

        getSupportLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = findViewById(R.id.fab_edit_buyer);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), UpdateBuyerActivity.class);
                intent1.putExtra("buyerId", mBuyerId);
                startActivity(intent1);
            }
        });


        //get Coop Id
        collapsingToolbarLayout = findViewById(R.id.name_bayer);
        toolbar = findViewById(R.id.toolbar_buyer);

        buyer_details = findViewById(R.id.buyer_details);
        buyer_details.setImageResource(R.mipmap.buyer_bg);

        gen_info_pic = findViewById(R.id.gen_info_pic);
        gen_info_pic.setImageResource(R.mipmap.male);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String buyerSelection = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    BfwContract.Buyer.CONTENT_URI,
                    null,
                    buyerSelection,
                    new String[]{Long.toString(mBuyerId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            int vendor_id = data.getInt(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID));
            name = data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_NAME));
            phone = data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_PHONE));
            mail = data.getString(data.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_EMAIL));
            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);

            //Get data from database
            TextView name_ca_details = findViewById(R.id.name_b_details);
            name_ca_details.setText(name);
            TextView phone_ca_details = findViewById(R.id.phone_b_details);
            phone_ca_details.setText(phone);

            TextView mail_ca_details = findViewById(R.id.mail_b_details);
            mail_ca_details.setText(mail);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        getSupportLoaderManager().restartLoader(0, null, this);
    }
}


