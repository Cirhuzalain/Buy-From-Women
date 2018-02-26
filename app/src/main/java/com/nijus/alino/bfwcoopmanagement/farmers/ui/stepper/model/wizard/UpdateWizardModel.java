package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard;

import android.content.Context;

import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.PageList;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateAccessInfoPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateBaseLinePage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateDemographicPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateFinancePage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateForecastPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateGeneralPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateLandPage;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.UpdateServicePage;

public class UpdateWizardModel extends AbstractWizardModel {

    public UpdateWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(new UpdateGeneralPage(this, "gen_info"),
                new UpdateServicePage(this, "access_service"),
                new UpdateDemographicPage(this, "demographic"),
                new UpdateForecastPage(this, "forecast"),
                new UpdateBaseLinePage(this, "base_line"),
                new UpdateFinancePage(this, "finance"),
                new UpdateAccessInfoPage(this, "access_information"),
                new UpdateLandPage(this, "farmer_land"));
    }

}
