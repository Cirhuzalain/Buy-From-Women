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
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;


public class DetailBuyerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Long mBuyerId;
    private Uri mUri;
    private String name;
    private String mKey;
    public static final String ARG_KEY = "key";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buyer);
        
        getSupportLoaderManager().initLoader(0,null,this);

        //get Coop Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("buyerId")) {
            mBuyerId = intent.getLongExtra("buyerId", 0);
            mUri = BfwContract.Coops.buildCoopUri(mBuyerId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_bayer);
        toolbar = findViewById(R.id.toolbar_buyer);
        //toolbar.setTitle(mBuyerId+"");
        //Log.d("DetailCoopActivity",mBuyerId+"");


        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab_edit_buyer);
        ImageView imageView = findViewById(R.id.buyer_details);
        //imageView.setImageResource(R.mipmap.coopbg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"eloko",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(getApplicationContext(),UpdateBuyerActivity.class);
                intent1.putExtra("buyerId", mBuyerId);
                startActivity(intent1);
            }
        });

        //AFFICHAGE DES DETAILS DU COOPERATIVE
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String coopSelection = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    mUri,
                    null,
                    coopSelection,
                    new String[]{Long.toString(mBuyerId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);

            //Affichages des donnees venant dans lka base des donnes


            TextView name_ca_details = findViewById(R.id.name_b_details);
            name_ca_details.setText("Name Lastname Buyer");
            TextView phone_ca_details = findViewById(R.id.phone_b_details);
            phone_ca_details.setText("+2501286555");
            /*TextView coop_ca_details = findViewById(R.id.coop_ca_details);
            coop_ca_details.setText("ici le text");*/
            TextView mail_ca_details = findViewById(R.id.mail_b_details);
            mail_ca_details.setText("ici @ text");

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


