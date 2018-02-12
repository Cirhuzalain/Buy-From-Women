package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard;

import android.content.Context;

import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.AbstractWizardModelVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.AccessToInformationPageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.BaseLinePageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.DemographicPageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.FinancePageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.ForecastPageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.GeneralPageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.LandPageVendorVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageListVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.ServicesEquipmentPageVendorVendor;

public class VendorWizardModelVendor extends AbstractWizardModelVendorVendor {

    public VendorWizardModelVendor(Context context) {
        super(context);
    }

    @Override
    protected PageListVendorVendor onNewRootPageList() {
        return new PageListVendorVendor(new GeneralPageVendorVendorVendor(this, "gen_info"),
                new ForecastPageVendorVendorVendor(this, "forecast"),
                new DemographicPageVendorVendorVendor(this, "demographic"),
                new BaseLinePageVendorVendorVendor(this, "base_line"),
                new FinancePageVendorVendorVendor(this, "finance"),
                new ServicesEquipmentPageVendorVendor(this, "access_service"),
                new AccessToInformationPageVendorVendorVendor(this, "access_information"),
                new LandPageVendorVendorVendor(this, "farmer_land"));
    }
}
