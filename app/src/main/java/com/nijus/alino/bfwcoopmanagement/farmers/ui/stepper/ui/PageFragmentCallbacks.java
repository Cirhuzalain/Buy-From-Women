package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui;


import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard.FarmerWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard.UpdateWizardModel;

public interface PageFragmentCallbacks {
    Page onGetPage(String key);

    FarmerWizardModel onGetFarmerWizard();

    UpdateWizardModel onGetUpdateWizard();
}
