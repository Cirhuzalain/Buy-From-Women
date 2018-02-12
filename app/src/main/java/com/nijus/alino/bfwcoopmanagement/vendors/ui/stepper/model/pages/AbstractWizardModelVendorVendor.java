package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wizard model, including the pages/steps in the wizard, their dependencies, and their
 * currently populated choices/values/selections.
 * <p>
 * To create an actual wizard model, extend this class and implement {@link #onNewRootPageList()}.
 */
public abstract class AbstractWizardModelVendorVendor implements ModelCallbacksVendor {
    protected Context mContext;

    private List<ModelCallbacksVendor> mListeners = new ArrayList<ModelCallbacksVendor>();
    private PageListVendorVendor mRootPageListVendor;

    public AbstractWizardModelVendorVendor(Context context) {
        mContext = context;
        mRootPageListVendor = onNewRootPageList();
    }

    /**
     * Override this to define a new wizard model.
     */
    protected abstract PageListVendorVendor onNewRootPageList();

    @Override
    public void onPageDataChanged(PageVendorVendor pageVendor) {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageDataChanged(pageVendor);
        }
    }

    @Override
    public void onPageTreeChanged() {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageTreeChanged();
        }
    }

    public PageVendorVendor findByKey(String key) {
        return mRootPageListVendor.findByKey(key);
    }

    public void load(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
            mRootPageListVendor.findByKey(key).resetData(savedValues.getBundle(key));
        }
    }

    public void registerListener(ModelCallbacksVendor listener) {
        mListeners.add(listener);
    }

    public Bundle save() {
        Bundle bundle = new Bundle();
        for (PageVendorVendor pageVendor : getCurrentPageSequence()) {
            bundle.putBundle(pageVendor.getKey(), pageVendor.getData());
        }
        return bundle;
    }

    public Context getPageContext(){
        return mContext;
    }

    /**
     * Gets the current list of wizard steps, flattening nested (dependent) pages based on the
     * user's choices.
     */
    public List<PageVendorVendor> getCurrentPageSequence() {
        ArrayList<PageVendorVendor> flattened = new ArrayList<>();
        mRootPageListVendor.flattenCurrentPageSequence(flattened);
        return flattened;
    }

    public void unregisterListener(ModelCallbacksVendor listener) {
        mListeners.remove(listener);
    }
}
