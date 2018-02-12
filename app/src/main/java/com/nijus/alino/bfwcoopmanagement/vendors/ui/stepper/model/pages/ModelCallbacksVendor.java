package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.CreateVendorFragment;

/**
 * Callback interface connecting {@link PageVendorVendor}, {@link AbstractWizardModelVendorVendor}, and model container
 * objects (e.g. {@link CreateVendorFragment}.
 */
public interface ModelCallbacksVendor {
    void onPageDataChanged(PageVendorVendor pageVendor);

    void onPageTreeChanged();
}