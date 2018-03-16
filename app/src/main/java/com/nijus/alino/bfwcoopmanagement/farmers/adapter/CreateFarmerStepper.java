package com.nijus.alino.bfwcoopmanagement.farmers.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;

import java.util.List;


public class CreateFarmerStepper extends FragmentStatePagerAdapter {

    private Context mContext;
    private int mCutOffPage;
    private List<Page> mPageList;

    public CreateFarmerStepper(FragmentManager fm, Context context, List<Page> pageList) {
        super(fm);
        Context mContext = context;
        mPageList = pageList;
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
        return mPageList.get(position).createFragment();
    }

    @Override
    public int getCount() {
        if (mPageList == null) {
            return 0;
        }
        return mPageList.size();
    }
}
