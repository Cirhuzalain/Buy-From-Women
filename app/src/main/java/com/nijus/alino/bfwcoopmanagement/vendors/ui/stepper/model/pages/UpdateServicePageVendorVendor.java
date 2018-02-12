package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ServiceAccessVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update.UpdateServiceFragmentVendor;

import java.util.ArrayList;

public class UpdateServicePageVendorVendor extends PageVendorVendor {

    public static final String SERVICE_KEY = "service_key";

    private ServiceAccessVendor serviceAccessVendor = new ServiceAccessVendor();

    public UpdateServicePageVendorVendor(ModelCallbacksVendor callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UpdateServiceFragmentVendor.create(getKey());
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

    public UpdateServicePageVendorVendor setValue(ServiceAccessVendor serviceAccessVendor) {
        mData.putParcelable(SERVICE_KEY, serviceAccessVendor);
        return this;
    }

}
