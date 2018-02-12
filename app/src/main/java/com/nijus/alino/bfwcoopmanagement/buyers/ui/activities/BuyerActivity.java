package com.nijus.alino.bfwcoopmanagement.buyers.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

public class BuyerActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        FloatingActionButton fab = findViewById(R.id.fab_edit_buyer);
        ImageView imageView = findViewById(R.id.buyer_details);
        imageView.setImageResource(R.drawable.bg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"eloko",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(getApplicationContext(),UpdateBuyerActivity.class);
                intent1.putExtra("buyerId", 1);
                startActivity(intent1);
            }
        });

        String name = "Buyer name";
        collapsingToolbarLayout = findViewById(R.id.name_bayer);
        toolbar = findViewById(R.id.toolbar_buyer);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle(name);
        //collapsingToolbarLayout.setTitle(name);

        //Affichages des donnees venant dans lka base des donnes

        TextView name_ca_details = findViewById(R.id.name_b_details);
        name_ca_details.setText("Name Lastname Buyer");
        TextView phone_ca_details = findViewById(R.id.phone_b_details);
        phone_ca_details.setText("+2501286555");
            /*TextView coop_ca_details = findViewById(R.id.coop_ca_details);
            coop_ca_details.setText("ici le text");*/
        TextView mail_ca_details = findViewById(R.id.mail_b_details);
        mail_ca_details.setText("mailbuyer@text.com");

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
