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
                new UpdateServicePageVendorVendor(this, "access_service"),
                new UpdateDemographicPageVendorVendor(this, "demographic"),
                new UpdateForecastPageVendorVendor(this, "forecast"),
                new UpdateBaseLinePageVendorVendor(this, "base_line"),
                new UpdateFinancePageVendorVendor(this, "finance"),
                new UpdateAccessInfoPageVendorVendor(this, "access_information"),
                new UpdateLandPageVendorVendor(this, "vendor_land"));
    }

}
