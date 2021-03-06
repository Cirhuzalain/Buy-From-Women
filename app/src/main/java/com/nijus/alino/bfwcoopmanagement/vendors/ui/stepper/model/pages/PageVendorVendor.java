package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Represents a single page in the wizard.
 */
public abstract class PageVendorVendor implements PageTreeNodeVendor {
    /**
     * The key into {@link #getData()} used for wizards with simple (single) values.
     */
    public static final String SIMPLE_DATA_KEY = "_";

    protected ModelCallbacksVendor mCallbacks;

    /**
     * Current wizard values/selections.
     */
    protected Bundle mData = new Bundle();
    protected String mTitle;
    protected boolean mRequired = false;
    protected String mParentKey;

    protected PageVendorVendor(ModelCallbacksVendor callbacks, String title) {
        mCallbacks = callbacks;
        mTitle = title;
    }

    public Bundle getData() {
        return mData;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isRequired() {
        return mRequired;
    }

    public PageVendorVendor setRequired(boolean required) {
        mRequired = required;
        return this;
    }

    void setParentKey(String parentKey) {
        mParentKey = parentKey;
    }

    @Override
    public PageVendorVendor findByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<PageVendorVendor> dest) {
        dest.add(this);
    }

    public abstract Fragment createFragment();

    public String getKey() {
        return (mParentKey != null) ? mParentKey + ":" + mTitle : mTitle;
    }

    public boolean isCompleted() {
        return true;
    }

    public void setData(String key, Parcelable info) {
        mData.putParcelable(key, info);
    }

    public void resetData(Bundle data) {
        mData = data;
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        mCallbacks.onPageDataChanged(this);
    }
}
