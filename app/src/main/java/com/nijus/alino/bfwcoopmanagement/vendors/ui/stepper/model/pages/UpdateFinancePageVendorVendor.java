package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update.UpdateFinanceFragmentVendor;

import java.util.ArrayList;

public class UpdateFinancePageVendorVendor extends PageVendorVendor {

    public static final String FINANCE_KEY = "key";

    private FinanceVendor financeVendor = new FinanceVendor();

    public UpdateFinancePageVendorVendor(ModelCallbacksVendor callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UpdateFinanceFragmentVendor.create(getKey());
    }

    @Override
    public Bundle getData() {
        return super.getData();
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    @Override
    void setParentKey(String parentKey) {
        super.setParentKey(parentKey);
    }

    @Override
    public PageVendorVendor findByKey(String key) {
        return super.findByKey(key);
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<PageVendorVendor> dest) {
        super.flattenCurrentPageSequence(dest);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public void resetData(Bundle data) {
        super.resetData(data);
    }

    @Override
    public void notifyDataChanged() {
        super.notifyDataChanged();
    }

    @Override
    public PageVendorVendor setRequired(boolean required) {
        return super.setRequired(required);
    }

    public UpdateFinancePageVendorVendor setValue(FinanceVendor financeVendor) {
        mData.putParcelable(FINANCE_KEY, financeVendor);
        return this;
    }

}
