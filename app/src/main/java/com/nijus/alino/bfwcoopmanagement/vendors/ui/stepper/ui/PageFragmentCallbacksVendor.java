package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;


import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard.UpdateWizardModelVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard.VendorWizardModelVendor;

public interface PageFragmentCallbacksVendor {
    PageVendorVendor onGetPage(String key);

    VendorWizardModelVendor onGetFarmerWizard();

    UpdateWizardModelVendorVendor onGetUpdateWizard();
}
