package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages;

import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.CreateFarmerFragment;

/**
 * Callback interface connecting {@link Page}, {@link AbstractWizardModel}, and model container
 * objects (e.g. {@link CreateFarmerFragment}.
 */
public interface ModelCallbacks {
    void onPageDataChanged(Page page);

    void onPageTreeChanged();
}