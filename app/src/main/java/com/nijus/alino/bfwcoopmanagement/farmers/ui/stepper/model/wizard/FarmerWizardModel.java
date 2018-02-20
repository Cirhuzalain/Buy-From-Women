package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard;

import android.content.Context;

import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.AccessToInformationPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.BaseLinePage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.DemographicPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.FinancePage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.ForecastPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.GeneralPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.LandPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.PageList;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.ServicesEquipmentPage;

public class FarmerWizardModel extends AbstractWizardModel {

    public FarmerWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(new GeneralPage(this, "gen_info"),
                new ServicesEquipmentPage(this, "access_service"),
                new DemographicPage(this, "demographic"),
                new ForecastPage(this, "forecast"),
                new BaseLinePage(this, "base_line"),
                new FinancePage(this, "finance"),
                new AccessToInformationPage(this, "access_information"),
                new LandPage(this, "farmer_land"));
    }
}
