package com.nijus.alino.bfwcoopmanagement.vendors.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;

import java.util.List;


public class CreateVendorStepper extends FragmentStatePagerAdapter {

    private Context mContext;
    private int mCutOffPage;
    private List<PageVendorVendor> mPageVendorList;

    public CreateVendorStepper(FragmentManager fm, Context context, List<PageVendorVendor> pageVendorList) {
        super(fm);
        Context mContext = context;
        mPageVendorList = pageVendorList;
    }

    public int getCutOffPage() {
        return mCutOffPage;
    }

    public void setCutOffPage(int cutOffPage) {
        if (cutOffPage < 0) {
            cutOffPage = Integer.MAX_VALUE;
        }
        mCutOffPage = cutOffPage;
    }

    @Override
    public Fragment getItem(int position) {
        return mPageVendorList.get(position).createFragment();
    }

    @Override
    public int getCount() {
        if (mPageVendorList == null) {
            return 0;
        }
        return mPageVendorList.size();
    }
}
