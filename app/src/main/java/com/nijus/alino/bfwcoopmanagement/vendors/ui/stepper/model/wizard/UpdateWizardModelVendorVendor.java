package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard;

import android.content.Context;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.AbstractWizardModelVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageListVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateAccessInfoPageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateBaseLinePageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateDemographicPageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateFinancePageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateForecastPageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateGeneralPageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateLandPageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.UpdateServicePageVendorVendor;

public class UpdateWizardModelVendorVendor extends AbstractWizardModelVendorVendor {

    public UpdateWizardModelVendorVendor(Context context) {
        super(context);
    }

    @Override
    protected PageListVendorVendor onNewRootPageList() {
        return new PageListVendorVendor(new UpdateGeneralPageVendorVendor(this, "gen_info"),
                new UpdateForecastPageVendorVendor(this, "forecast"),
                new UpdateDemographicPageVendorVendor(this, "demographic"),
                new UpdateBaseLinePageVendorVendor(this, "base_line"),
                new UpdateFinancePageVendorVendor(this, "finance"),
                new UpdateServicePageVendorVendor(this, "access_service"),
                new UpdateAccessInfoPageVendorVendor(this, "access_information"),
                new UpdateLandPageVendorVendor(this, "farmer_land"));
    }

}
