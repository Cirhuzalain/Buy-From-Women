package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;


import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.wizard.CoopWizardModel;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.wizard.UpdateWizardModel;

public interface PageFragmentCallbacks {
    Page onGetPage(String key);
    CoopWizardModel onGetFarmerWizard();
    UpdateWizardModel onGetUpdateWizard();
}
