package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.NavigationActivity;
import com.nijus.alino.bfwcoopmanagement.products.ui.activities.ProductActivity;
import com.nijus.alino.bfwcoopmanagement.sales.ui.activities.SaleOrderInfoActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    /**
     * Launching SignInActivity
     *
     * @param button that was clicked
     */
    public void launchSingInActivity(View button) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.checkAlreadyLogin(getApplicationContext())) {

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                    Context.MODE_PRIVATE);
            String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");

            if (groupName.equals("Admin")) {
                Intent intent = new Intent(this, UserProfileActivityAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (groupName.equals("Agent")) {
                Intent intent = new Intent(this, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (groupName.equals("Buyer") || groupName.equals("Vendor")) {
                Intent intent = new Intent(this, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            finish();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_COMMENT = "comment";
        private static final String ARG_SECTION_IMAGE = "img";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int message, int comment, int img) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, message);
            args.putInt(ARG_SECTION_COMMENT, comment);
            args.putInt(ARG_SECTION_IMAGE, img);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ImageView imageView = rootView.findViewById(R.id.home_icon);
            TextView textView = rootView.findViewById(R.id.app_headline);
            TextView textComment = rootView.findViewById(R.id.app_comments);

            textView.setText(getArguments().getInt(ARG_SECTION_NUMBER));
            textComment.setText(getArguments().getInt(ARG_SECTION_COMMENT));
            imageView.setImageResource(getArguments().getInt(ARG_SECTION_IMAGE));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int[] images = {R.drawable.ic_home_1, R.drawable.ic_home_2, R.drawable.ic_home_3};
        private int[] messages = {R.string.message1, R.string.message2, R.string.message3};
        private int[] comments = {R.string.comments1, R.string.comments2, R.string.comments3};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(messages[position], comments[position], images[position]);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
            }
            return null;
        }
    }
}
