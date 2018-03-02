package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

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
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;


public class DetailCoopAgentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Long mCoopAgentId;
    private Uri mUri;
    private int coopId,coop;
    private String name, phone, mail;
    private String mKey;
    public static final String ARG_KEY = "key";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView coop_image_details, gen_info_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_coopagent);

        //get Coop Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("coopAgentId")) {
            mCoopAgentId = intent.getLongExtra("coopAgentId", 0);
            mUri = BfwContract.CoopAgent.buildAgentUri(mCoopAgentId);
        }

        getSupportLoaderManager().initLoader(0,null,this);

        collapsingToolbarLayout = findViewById(R.id.name_coopAgent);
        toolbar = findViewById(R.id.toolbar_coop);

        setSupportActionBar(toolbar);

        coop_image_details = findViewById(R.id.coop_image_details);
        coop_image_details.setImageResource(R.mipmap.agent_bg);

        gen_info_pic = findViewById(R.id.gen_info_pic);
        gen_info_pic.setImageResource(R.mipmap.male);

        FloatingActionButton fab = findViewById(R.id.fab_edit_coop);
        ImageView imageView = findViewById(R.id.coop_image_details);
        imageView.setImageResource(R.mipmap.agent_bg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),UpdateCoopAgent.class);
                intent1.putExtra("coopAgentId", mCoopAgentId);
                startActivity(intent1);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String coopSelection = BfwContract.CoopAgent.TABLE_NAME + "." +
                BfwContract.CoopAgent._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    BfwContract.CoopAgent.CONTENT_URI,
                    null,
                    coopSelection,
                    new String[]{Long.toString(mCoopAgentId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_NAME));
            phone = data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_PHONE));
            coop = data.getInt(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_COOP_ID));
            mail = data.getString(data.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_EMAIL));
            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);

            //Set data from database

            TextView name_ca_details = findViewById(R.id.name_ca_details);
            name_ca_details.setText(name);
            TextView phone_ca_details = findViewById(R.id.phone_ca_details);
            phone_ca_details.setText(phone);
            TextView mail_ca_details = findViewById(R.id.mail_ca_details);
            mail_ca_details.setText(mail);

            /**Get the real name of the cooperative**/

            Cursor cursor = null;
            int dataCount;
            String namecoop = "";

            String selectionCoop = BfwContract.Coops.TABLE_NAME + "." +
                    BfwContract.Coops.COLUMN_COOP_SERVER_ID + " =  ? ";

            try {
                cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, selectionCoop,
                        new String[]{Long.toString(coop)}, null);
                if (cursor != null) {
                    dataCount = cursor.getCount();
                    while (cursor.moveToNext()) {
                        namecoop = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
                    }
                }
            }
            catch (Exception e){

            }

            TextView coop_ca_details = findViewById(R.id.coop_ca_details);
            coop_ca_details.setText(namecoop);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


