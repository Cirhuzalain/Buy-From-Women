package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BankInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.BankFragment;

import java.util.ArrayList;

public class BankPage extends Page {

    private BankInformation bankInformation = new BankInformation();
    public static final String BANK_KEY = "key";

    public BankPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return BankFragment.create(getKey());
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
    public Page findByKey(String key) {
        return super.findByKey(key);
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
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
    public Page setRequired(boolean required) {
        return super.setRequired(required);
    }

    public BankPage setValue(BankInformation bankInformation) {
        mData.putParcelable(BANK_KEY, bankInformation);
        return this;
    }
}
