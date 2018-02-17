package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.UpdateCoop;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.riyagayasen.easyaccordion.AccordionExpansionCollapseListener;
import com.riyagayasen.easyaccordion.AccordionView;

public class CoopAgentActivity extends BaseActivity  {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coop_agent);

        FloatingActionButton fab = findViewById(R.id.fab_edit_coop_agent);

        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),UpdateCoop.class);
                intent1.putExtra("coopId", 1);
                startActivity(intent1);
            }
        });

        TextView name_c_details = findViewById(R.id.name_c_details);
        name_c_details.setText("ici le text");
        TextView phone_c_details = findViewById(R.id.phone_c_details);
        phone_c_details.setText("ici le text");
        TextView address_c_details = findViewById(R.id.address_c_details);
        address_c_details.setText("ici le text");
        TextView mail_c_details = findViewById(R.id.mail_c_details);
        mail_c_details.setText("ici le text");

        TextView internal_f_details = findViewById(R.id.internal_f_details);
        internal_f_details.setText("ici le text");
        TextView chair_name_c_details = findViewById(R.id.chair_name_c_details);
        chair_name_c_details.setText("ici le text");
        TextView chair_gender_c_details = findViewById(R.id.chair_gender_c_details);
        chair_gender_c_details.setText("ici le text");
        TextView chair_cel_c_details = findViewById(R.id.chair_cel_c_details);
        chair_cel_c_details.setText("ici le text");
        TextView v_chair_name_c_details = findViewById(R.id.v_chair_name_c_details);
        v_chair_name_c_details.setText("ici le text");
        TextView v_chair_gender_c_details = findViewById(R.id.v_chair_gender_c_details);
        v_chair_gender_c_details.setText("ici le text");
        TextView v_chair_cel_c_details = findViewById(R.id.v_chair_cel_c_details);
        v_chair_cel_c_details.setText("ici le text");
        TextView sec_name_c_details = findViewById(R.id.sec_name_c_details);
        sec_name_c_details.setText("ici le text");
        TextView sec_gender_c_details = findViewById(R.id.sec_gender_c_details);
        sec_gender_c_details.setText("ici le text");
        TextView sec_cel_c_details = findViewById(R.id.sec_cel_c_details);
        sec_cel_c_details.setText("ici le text");
        TextView year_rca_registration = findViewById(R.id.year_rca_registration);
        year_rca_registration.setText("ici le text");
           /* TextView membership_f_details = findViewById(R.id.membership_f_details);
            membership_f_details.setText("ici le text");*/
        TextView tot_land_c_details = findViewById(R.id.tot_land_c_details);
        tot_land_c_details.setText("ici le text");
        TextView measured_total_land_size = findViewById(R.id.measured_total_land_size);
        measured_total_land_size.setText("ici le text");
        TextView male_in_coop = findViewById(R.id.male_in_coop);
        male_in_coop.setText("ici le text");
        TextView female_in_coop = findViewById(R.id.female_in_coop);
        female_in_coop.setText("ici le text");
        TextView total_members = findViewById(R.id.total_members);
        total_members.setText("ici le text");
           /* TextView membership_f_details = findViewById(R.id.membership_f_details);
            membership_f_details.setText("ici le text");*/

        //LES IMAGES ICI
        ImageView gen_info_pic =findViewById(R.id.gen_info_pic);
        gen_info_pic.setImageResource(R.drawable.profile);
        ImageView internal_pic_c_details =findViewById(R.id.internal_pic_c_details);
        internal_pic_c_details.setImageResource(R.drawable.profile);
        ImageView pic_office_space_details =findViewById(R.id.pic_office_space_details);
        pic_office_space_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_moisture_meter_details =findViewById(R.id.pic_moisture_meter_details);
        pic_moisture_meter_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_weighting_scales_details =findViewById(R.id.pic_weighting_scales_details);
        pic_weighting_scales_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_qlt_inputs_details =findViewById(R.id.pic_qlt_inputs_details);
        pic_qlt_inputs_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_tractors_details =findViewById(R.id.pic_tractors_details);
        pic_tractors_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_harvs_details =findViewById(R.id.pic_harvs_details);
        pic_harvs_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_dryer_details =findViewById(R.id.pic_dryer_details);
        pic_dryer_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_thresher_details =findViewById(R.id.pic_thresher_details);
        pic_thresher_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_safe_storage_facilities_details =findViewById(R.id.pic_safe_storage_facilities_details);
        pic_safe_storage_facilities_details.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_other_details =findViewById(R.id.pic_other_details);
        pic_other_details.setImageResource(R.mipmap.icon_sm_error);

        //ACCORDION DETAILS FOR COOPS
        //1. ACCESS TO INFORMATION
        final AccordionView access_info_accordion = findViewById(R.id.access_info_accordion_c);
        access_info_accordion.setHeadingString("ACCESS TO INFORMATION");

        //les images access to info
        ImageView pic_agri_extension_details2 = findViewById(R.id.pic_agri_extension_details2);
        pic_agri_extension_details2.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_clim_rel_details2 = findViewById(R.id.pic_clim_rel_details2);
        pic_clim_rel_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_seed_details2 = findViewById(R.id.pic_seed_details2);
        pic_seed_details2.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_org_fert_details2 = findViewById(R.id.pic_org_fert_details2);
        pic_org_fert_details2.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_inorg_fert_details2 = findViewById(R.id.pic_inorg_fert_details2);
        pic_inorg_fert_details2.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_labour_details2 = findViewById(R.id.pic_labour_details2);
        pic_labour_details2.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_irr_w_p_details2 = findViewById(R.id.pic_irr_w_p_details2);
        pic_irr_w_p_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_spread_or_spray_details2 = findViewById(R.id.pic_spread_or_spray_details2);
        pic_spread_or_spray_details2.setImageResource(R.mipmap.icon_sm_error);

        //2. FORECAST SALES
        final AccordionView forecast_sales = findViewById(R.id.forecast_s_accordion);
        forecast_sales.setHeadingString("FORECAST SALES");

        //les images du forescast
        ImageView pic_rgcc_details2 = findViewById(R.id.pic_rgcc_details2);
        pic_rgcc_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_prodev_details2 = findViewById(R.id.pic_prodev_details2);
        pic_prodev_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_sasura_details2 = findViewById(R.id.pic_sasura_details2);
        pic_sasura_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_aif_details2 = findViewById(R.id.pic_aif_details2);
        pic_aif_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_eax_details2 = findViewById(R.id.pic_eax_details2);
        pic_eax_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_none_details2 = findViewById(R.id.pic_none_details2);
        pic_none_details2.setImageResource(R.mipmap.icon_sm_ok);
        ImageView pic_other_details2 = findViewById(R.id.pic_other_details2);
        pic_other_details2.setImageResource(R.mipmap.icon_sm_ok);

        //LES TEXT DU FIORECAST SALES
        TextView harverst_s_forecast_sales = findViewById(R.id.harverst_s_forecast_sales);
        harverst_s_forecast_sales.setText("text ici");

        TextView commired_contract_qty_details2 = findViewById(R.id.commired_contract_qty_details2);
        commired_contract_qty_details2.setText("text ici");
        TextView grade_details2 = findViewById(R.id.grade_details2);
        grade_details2.setText("text ici");
        TextView min_floor_per_grade_details2 = findViewById(R.id.min_floor_per_grade_details2);
        min_floor_per_grade_details2.setText("text ici");

        //3. BASELINE YIELDS
        final AccordionView baseline_y = findViewById(R.id.baseline_y_accordion);
        baseline_y.setHeadingString("BASELINE YIELD");
        ImageView pic_maize_details3 = findViewById(R.id.pic_maize_details3);
        pic_maize_details3.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_bean_details3 = findViewById(R.id.pic_bean_details3);
        pic_bean_details3.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_soy_details3 = findViewById(R.id.pic_soy_details3);
        pic_soy_details3.setImageResource(R.mipmap.icon_sm_error);
        ImageView pic_other_details3 = findViewById(R.id.pic_other_details3);
        pic_other_details3.setImageResource(R.mipmap.icon_sm_error);

        //4. BASELINE SALES
        final AccordionView baseline_s = findViewById(R.id.baseline_s_accordion);
        baseline_s.setHeadingString("BASELINE SALES");



        TextView qty_agregated_from_members = findViewById(R.id.qty_agregated_from_members);
        qty_agregated_from_members.setText("504");
        TextView cycle_h_at_price_per_kg = findViewById(R.id.cycle_h_at_price_per_kg);
        cycle_h_at_price_per_kg.setText("504");
        TextView qty_purchaced_from_non_members = findViewById(R.id.qty_purchaced_from_non_members);
        qty_purchaced_from_non_members.setText("504");
        TextView non_member_purchase_at_price_per_kg = findViewById(R.id.non_member_purchase_at_price_per_kg);
        non_member_purchase_at_price_per_kg.setText("504");
        TextView rgcc_contact_under_ftma = findViewById(R.id.rgcc_contact_under_ftma);
        rgcc_contact_under_ftma.setText("504");
        TextView qty_of_rgcc_contact = findViewById(R.id.qty_of_rgcc_contact);
        qty_of_rgcc_contact.setText("504");
        TextView price_per_kg_sold_to_rgcc = findViewById(R.id.price_per_kg_sold_to_rgcc);
        price_per_kg_sold_to_rgcc.setText("504");
        TextView qty_slod_outside_rgcc = findViewById(R.id.qty_slod_outside_rgcc);
        qty_slod_outside_rgcc.setText("504");

        ImageView formal_buyer = findViewById(R.id.formal_buyer);
        formal_buyer.setImageResource(R.mipmap.icon_sm_error);
        ImageView informal_buyer = findViewById(R.id.informal_buyer);
        informal_buyer.setImageResource(R.mipmap.icon_sm_error);
        ImageView other_formal_buyer = findViewById(R.id.other_formal_buyer);
        other_formal_buyer.setImageResource(R.mipmap.icon_sm_error);

        TextView price_per_kg_sold_outside_ftma = findViewById(R.id.price_per_kg_sold_outside_ftma);
        price_per_kg_sold_outside_ftma.setText("504");


        //5. BASELINE FINANCE
        final AccordionView baseline_fin = findViewById(R.id.baseline_fin_accordion);
        baseline_fin.setHeadingString("BASELINE FINANCE INFO");



        TextView input_loan_amount = findViewById(R.id.input_loan_amount);
        input_loan_amount.setText("45.5");
        TextView input_loan_interest_rate = findViewById(R.id.input_loan_interest_rate);
        input_loan_interest_rate.setText("45.5");
        TextView input_loan_duration = findViewById(R.id.input_loan_duration);
        input_loan_duration.setText("45.5");

        TextView input_loan_disbursement_method = findViewById(R.id.input_loan_disbursement_method);
        input_loan_disbursement_method.setText("45.5");
        TextView aggrgation_post_harvset_loan = findViewById(R.id.aggrgation_post_harvset_loan);
        aggrgation_post_harvset_loan.setText("45.5");

        TextView aggrgation_post_harvset_loan_amount = findViewById(R.id.aggrgation_post_harvset_loan_amount);
        aggrgation_post_harvset_loan_amount.setText("45.5");
        TextView agg_post_harvset_loan_interest = findViewById(R.id.agg_post_harvset_loan_interest);
        agg_post_harvset_loan_interest.setText("45.5");
        TextView agg_post_harvset_loan_duration = findViewById(R.id.agg_post_harvset_loan_duration);
        agg_post_harvset_loan_duration.setText("45.5");

        TextView agg_post_har_laon_disbrsmnt_met = findViewById(R.id.agg_post_har_laon_disbrsmnt_met);
        agg_post_har_laon_disbrsmnt_met.setText("45.5");


        ImageView bank_input_loan = findViewById(R.id.bank_input_loan);
        bank_input_loan.setImageResource(R.mipmap.icon_sm_error);
        ImageView coop_input_loan = findViewById(R.id.coop_input_loan);
        coop_input_loan.setImageResource(R.mipmap.icon_sm_error);
        ImageView sacco_input_loan = findViewById(R.id.sacco_input_loan);
        sacco_input_loan.setImageResource(R.mipmap.icon_sm_error);
        ImageView other_input_loan = findViewById(R.id.other_input_loan);
        other_input_loan.setImageResource(R.mipmap.icon_sm_error);
        ImageView labour_input_loan_purpose = findViewById(R.id.labour_input_loan_purpose);
        labour_input_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView seed_input_loan_purpose = findViewById(R.id.seed_input_loan_purpose);
        seed_input_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView input_input_loan_purpose = findViewById(R.id.input_input_loan_purpose);
        input_input_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView machinery_input_loan_purpose = findViewById(R.id.machinery_input_loan_purpose);
        machinery_input_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView other_input_loan_purpose = findViewById(R.id.other_input_loan_purpose);
        other_input_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView bank_aggregation_prov = findViewById(R.id.bank_aggregation_prov);
        bank_aggregation_prov.setImageResource(R.mipmap.icon_sm_error);
        ImageView coop_aggregation_prov = findViewById(R.id.coop_aggregation_prov);
        coop_aggregation_prov.setImageResource(R.mipmap.icon_sm_error);
        ImageView sacco_aggregation_prov = findViewById(R.id.sacco_aggregation_prov);
        sacco_aggregation_prov.setImageResource(R.mipmap.icon_sm_error);
        ImageView other_aggregation_prov = findViewById(R.id.other_aggregation_prov);
        other_aggregation_prov.setImageResource(R.mipmap.icon_sm_error);
        ImageView labour_agg_harv_loan_purpose = findViewById(R.id.labour_agg_harv_loan_purpose);
        labour_agg_harv_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView input_agg_harv_loan_purpose = findViewById(R.id.input_agg_harv_loan_purpose);
        input_agg_harv_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView machinery_agg_harv_loan_purpose = findViewById(R.id.machinery_agg_harv_loan_purpose);
        machinery_agg_harv_loan_purpose.setImageResource(R.mipmap.icon_sm_error);
        ImageView other_agg_harv_loan_purpose = findViewById(R.id.other_agg_harv_loan_purpose);
        other_agg_harv_loan_purpose.setImageResource(R.mipmap.icon_sm_error);

        //6 expected yield
        final AccordionView expected_yiel = findViewById(R.id.expected_y_accordion);
        expected_yiel.setHeadingString("EXPECTED YIELD");

        TextView total_expected_coop_production = findViewById(R.id.total_expected_coop_production);
        total_expected_coop_production.setText("78.5");
        TextView total_coop_land_size_in_ha = findViewById(R.id.total_coop_land_size_in_ha);
        total_coop_land_size_in_ha.setText("78.5");
        TextView expected_total_production_in_kg = findViewById(R.id.expected_total_production_in_kg);
        expected_total_production_in_kg.setText("78.5");


        //ADD LISTENER TO ACCORDIONS
        access_info_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                access_info_accordion.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                access_info_accordion.setHeadingBackGround(R.color.default_color);
            }
        });

        forecast_sales.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                forecast_sales.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                forecast_sales.setHeadingBackGround(R.color.default_color);
            }
        });

        baseline_y.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                baseline_y.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                baseline_y.setHeadingBackGround(R.color.default_color);
            }
        });

        baseline_s.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                baseline_s.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                baseline_s.setHeadingBackGround(R.color.default_color);
            }
        });
        baseline_fin.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                baseline_fin.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                baseline_fin.setHeadingBackGround(R.color.default_color);
            }
        });

        expected_yiel.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
            @Override
            public void onExpanded(AccordionView view) {
                expected_yiel.setHeadingBackGround(R.color.bg_detail);
            }
            @Override
            public void onCollapsed(AccordionView view) {
                expected_yiel.setHeadingBackGround(R.color.default_color);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = super.getDrawerLayout();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
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
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).trim();
        }
    }

}
