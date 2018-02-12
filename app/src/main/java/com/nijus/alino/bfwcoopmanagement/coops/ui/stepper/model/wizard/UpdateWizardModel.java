package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.wizard;

import android.content.Context;

import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.AccessToInformationPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.AvailableRessourcesPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.BaselineFinaceInfoPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.BaselineSalesPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.BaselineYieldPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.ForecastSalesPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.GeneralPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.InternalInformationPage;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.PageList;

public class UpdateWizardModel extends AbstractWizardModel {

    public UpdateWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(new GeneralPage(this, "gen_info"),
                new InternalInformationPage(this, "internal_info"),
                new AvailableRessourcesPage(this, "avalaible_ress"),
                new AccessToInformationPage(this, "access_info"),
                new ForecastSalesPage(this, "forecast_sales"),
                new BaselineYieldPage(this, "baseline"),
                new BaselineSalesPage(this, "baseline_sales"),
                new BaselineFinaceInfoPage(this, "baseline_fin"));
    }

}
