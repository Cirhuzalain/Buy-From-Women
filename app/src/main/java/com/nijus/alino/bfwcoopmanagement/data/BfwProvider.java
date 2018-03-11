package com.nijus.alino.bfwcoopmanagement.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BfwProvider extends ContentProvider {

    private BfwDbHelper mBfwDbHelper;
    public UriMatcher mUriMatcher = buildUriMatcher();

    static final int COOPS = 100;
    static final int COOPS_DETAILS = 158;

    static final int COOP_INFO = 120;
    static final int COOP_INFO_BY_COOP = 127;

    static final int SALE_COOP = 121;
    static final int SALE_COOP_BY_COOP = 129;

    static final int YIELD_COOP = 122;
    static final int YIELD_BY_COOP = 152;

    static final int BASELINE_SALE_COOP = 123;
    static final int BASELINE_SALE_BY_COOP = 153;

    static final int FINANCE_INFO_COOP = 124;
    static final int FINANCE_INFO_BY_COOP = 155;

    static final int EXPECTED_YIELD = 125;
    static final int EXPECTED_YIELD_BY_COOP = 157;


    static final int BUYER = 101;
    static final int BUYER_DETAILS = 150;
    static final int COOP_AGENT = 102;
    static final int COOP_AGENT_DETAILS = 160;


    static final int FARMER = 103;
    static final int FARMER_DETAILS = 104;

    static final int FARMER_INFO = 130;
    static final int INFO_BY_FARMER = 170;

    static final int FARMER_BASELINE = 131;
    static final int BASELINE_BY_FARMER = 171;

    static final int FARMER_FORECAST = 132;
    static final int FORECAST_BY_FARMER = 172;

    static final int FARMER_FINANCE_DATA = 133;
    static final int FINANCE_DATA_BY_FARMER = 173;

    static final int LAND_PLOT = 105;
    static final int FARMER_BY_LAND_PLOT = 106;

    static final int VENDOR = 114;
    static final int VENDOR_DETAILS = 175;

    static final int VENDOR_INFO = 140;
    static final int INFO_BY_VENDOR = 176;

    static final int VENDOR_LAND = 141;
    static final int LAND_BY_VENDOR = 177;

    static final int BASELINE_VENDOR = 142;
    static final int BASELINE_BY_VENDOR = 178;

    static final int FINANCE_DATA_VENDOR = 143;
    static final int FINANCE_DATA_BY_VENDOR = 179;

    static final int FORECAST_VENDOR = 144;
    static final int FORECAST_BY_VENDOR = 180;

    static final int HARVEST_SEASON = 107;

    static final int PRODUCT_PRODUCT = 200;
    static final int PRODUCT_PRODUCT_DETAILS = 210;
    static final int PRODUCT_TEMPLATE = 201;
    static final int PRODUCT_TEMPLATE_DETAILS = 211;

    static final int SALE_ORDER = 203;
    static final int SALE_ORDER_LINE = 204;
    static final int SALE_ORDER_DETAILS = 212;

    static final int PURCHASE_ORDER = 205;
    static final int PURCHASE_ORDER_LINE = 206;
    static final int PURCHASE_ORDER_DETAILS = 213;

    static final int LOAN_LINE = 207;
    static final int LOAN_PAYMENT = 208;
    static final int LOAN = 209;
    static final int LOAN_DETAILS = 214;

    static final int SALE_ORDER_LINE_PRODUCT = 222;
    static final int PURCHASE_ORDER_LINE_PRODUCT = 223;
    static final int LOAN_LINE_PAYMENT = 224;

    static final int PAYMENT_TERM = 250;

    private static final SQLiteQueryBuilder infoByCoop;
    private static final SQLiteQueryBuilder saleByCoop;
    private static final SQLiteQueryBuilder yieldByCoop;
    private static final SQLiteQueryBuilder baselineByCoop;
    private static final SQLiteQueryBuilder financeInfoByCoop;
    private static final SQLiteQueryBuilder expectedYieldByCoop;

    private static final SQLiteQueryBuilder farmerLandPlot;
    private static final SQLiteQueryBuilder infoByFarmer;
    private static final SQLiteQueryBuilder baselineByFarmer;
    private static final SQLiteQueryBuilder forecastByFarmer;
    private static final SQLiteQueryBuilder financeDataByFarmer;

    private static final SQLiteQueryBuilder infoByVendor;
    private static final SQLiteQueryBuilder landByVendor;
    private static final SQLiteQueryBuilder baselineByVendor;
    private static final SQLiteQueryBuilder financeByVendor;
    private static final SQLiteQueryBuilder forecastByVendor;

    private static final SQLiteQueryBuilder saleOrderLineProduct;
    private static final SQLiteQueryBuilder purchaseOderLineProduct;
    private static final SQLiteQueryBuilder loanLinePayment;

    static {
        farmerLandPlot = new SQLiteQueryBuilder();
        infoByFarmer = new SQLiteQueryBuilder();
        baselineByFarmer = new SQLiteQueryBuilder();
        forecastByFarmer = new SQLiteQueryBuilder();
        financeDataByFarmer = new SQLiteQueryBuilder();

        infoByCoop = new SQLiteQueryBuilder();
        saleByCoop = new SQLiteQueryBuilder();
        yieldByCoop = new SQLiteQueryBuilder();
        baselineByCoop = new SQLiteQueryBuilder();
        financeInfoByCoop = new SQLiteQueryBuilder();
        expectedYieldByCoop = new SQLiteQueryBuilder();

        infoByVendor = new SQLiteQueryBuilder();
        landByVendor = new SQLiteQueryBuilder();
        baselineByVendor = new SQLiteQueryBuilder();
        financeByVendor = new SQLiteQueryBuilder();
        forecastByVendor = new SQLiteQueryBuilder();

        saleOrderLineProduct = new SQLiteQueryBuilder();
        purchaseOderLineProduct = new SQLiteQueryBuilder();
        loanLinePayment = new SQLiteQueryBuilder();

        saleOrderLineProduct.setTables(
                BfwContract.SaleOrder.TABLE_NAME +
                        " INNER JOIN " + BfwContract.SaleOrderLine.TABLE_NAME +
                        " INNER JOIN " + BfwContract.ProductProduct.TABLE_NAME +
                        " ON " + BfwContract.SaleOrderLine.TABLE_NAME +
                        "." + BfwContract.SaleOrderLine.COLUMN_ORDER_ID +
                        " = " + BfwContract.SaleOrder.TABLE_NAME +
                        "." + BfwContract.SaleOrder._ID +
                        " AND " + BfwContract.ProductProduct.TABLE_NAME +
                        "." + BfwContract.ProductProduct._ID +
                        " = " + BfwContract.SaleOrderLine.TABLE_NAME +
                        "." + BfwContract.SaleOrderLine.COLUMN_PRODUCT_ID

        );

        purchaseOderLineProduct.setTables(
                BfwContract.PurchaseOrder.TABLE_NAME +
                        " INNER JOIN " + BfwContract.PurchaseOrderLine.TABLE_NAME +
                        " INNER JOIN " + BfwContract.ProductProduct.TABLE_NAME +
                        " ON " + BfwContract.PurchaseOrderLine.TABLE_NAME +
                        "." + BfwContract.PurchaseOrderLine.COLUMN_ORDER_ID +
                        " = " + BfwContract.PurchaseOrder.TABLE_NAME +
                        "." + BfwContract.PurchaseOrder._ID +
                        " AND " + BfwContract.ProductProduct.TABLE_NAME +
                        "." + BfwContract.ProductProduct._ID +
                        " = " + BfwContract.PurchaseOrderLine.TABLE_NAME +
                        "." + BfwContract.PurchaseOrderLine.COLUMN_PRODUCT_ID
        );

        loanLinePayment.setTables(
                BfwContract.Loan.TABLE_NAME +
                        " INNER JOIN " + BfwContract.LoanLine.TABLE_NAME +
                        " INNER JOIN " + BfwContract.LoanPayment.TABLE_NAME +
                        " ON " + BfwContract.Loan.TABLE_NAME +
                        "." + BfwContract.Loan._ID +
                        " = " + BfwContract.LoanLine.TABLE_NAME +
                        "." + BfwContract.LoanLine.COLUMN_LOAN_ID +
                        " AND " + BfwContract.Loan.TABLE_NAME +
                        "." + BfwContract.Loan._ID +
                        " = " + BfwContract.LoanPayment.TABLE_NAME +
                        "." + BfwContract.LoanPayment.COLUMN_LOAN_ID
        );

        infoByVendor.setTables(
                BfwContract.Vendor.TABLE_NAME +
                        " INNER JOIN " + BfwContract.VendorAccessInfo.TABLE_NAME +
                        " ON " + BfwContract.VendorAccessInfo.TABLE_NAME +
                        "." + BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID +
                        " = " + BfwContract.Vendor.TABLE_NAME +
                        "." + BfwContract.Vendor._ID
        );

        landByVendor.setTables(
                BfwContract.Vendor.TABLE_NAME +
                        " INNER JOIN " + BfwContract.VendorLand.TABLE_NAME +
                        " ON " + BfwContract.VendorLand.TABLE_NAME +
                        "." + BfwContract.VendorLand.COLUMN_VENDOR_ID +
                        " = " + BfwContract.Vendor.TABLE_NAME +
                        "." + BfwContract.Vendor._ID
        );

        baselineByVendor.setTables(
                BfwContract.Vendor.TABLE_NAME +
                        " INNER JOIN " + BfwContract.BaseLineVendor.TABLE_NAME +
                        " ON " + BfwContract.BaseLineVendor.TABLE_NAME +
                        "." + BfwContract.BaseLineVendor.COLUMN_VENDOR_ID +
                        " = " + BfwContract.Vendor.TABLE_NAME +
                        "." + BfwContract.Vendor._ID
        );

        financeByVendor.setTables(
                BfwContract.Vendor.TABLE_NAME +
                        " INNER JOIN " + BfwContract.FinanceDataVendor.TABLE_NAME +
                        " ON " + BfwContract.FinanceDataVendor.TABLE_NAME +
                        "." + BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID +
                        " = " + BfwContract.Vendor.TABLE_NAME +
                        "." + BfwContract.Vendor._ID
        );

        forecastByVendor.setTables(
                BfwContract.Vendor.TABLE_NAME +
                        " INNER JOIN " + BfwContract.ForecastVendor.TABLE_NAME +
                        " ON " + BfwContract.ForecastVendor.TABLE_NAME +
                        "." + BfwContract.ForecastVendor.COLUMN_VENDOR_ID +
                        " = " + BfwContract.Vendor.TABLE_NAME +
                        "." + BfwContract.Vendor._ID
        );

        infoByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.CoopInfo.TABLE_NAME +
                        " ON " + BfwContract.CoopInfo.TABLE_NAME +
                        "." + BfwContract.CoopInfo.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        saleByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.SalesCoop.TABLE_NAME +
                        " ON " + BfwContract.SalesCoop.TABLE_NAME +
                        "." + BfwContract.SalesCoop.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        yieldByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.YieldCoop.TABLE_NAME +
                        " ON " + BfwContract.YieldCoop.TABLE_NAME +
                        "." + BfwContract.YieldCoop.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        baselineByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.BaselineSalesCoop.TABLE_NAME +
                        " ON " + BfwContract.BaselineSalesCoop.TABLE_NAME +
                        "." + BfwContract.BaselineSalesCoop.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        financeInfoByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.FinanceInfoCoop.TABLE_NAME +
                        " ON " + BfwContract.FinanceInfoCoop.TABLE_NAME +
                        "." + BfwContract.FinanceInfoCoop.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        expectedYieldByCoop.setTables(
                BfwContract.Coops.TABLE_NAME +
                        " INNER JOIN " + BfwContract.ExpectedYieldCoop.TABLE_NAME +
                        " ON " + BfwContract.ExpectedYieldCoop.TABLE_NAME +
                        "." + BfwContract.ExpectedYieldCoop.COLUMN_COOP_ID +
                        " = " + BfwContract.Coops.TABLE_NAME +
                        "." + BfwContract.Coops._ID
        );

        farmerLandPlot.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.LandPlot.TABLE_NAME +
                        " ON " + BfwContract.LandPlot.TABLE_NAME +
                        "." + BfwContract.LandPlot.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );

        infoByFarmer.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.FarmerAccessInfo.TABLE_NAME +
                        " ON " + BfwContract.FarmerAccessInfo.TABLE_NAME +
                        "." + BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );

        baselineByFarmer.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.BaselineFarmer.TABLE_NAME +
                        " ON " + BfwContract.BaselineFarmer.TABLE_NAME +
                        "." + BfwContract.BaselineFarmer.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );

        forecastByFarmer.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.ForecastFarmer.TABLE_NAME +
                        " ON " + BfwContract.ForecastFarmer.TABLE_NAME +
                        "." + BfwContract.ForecastFarmer.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );

        financeDataByFarmer.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.FinanceDataFarmer.TABLE_NAME +
                        " ON " + BfwContract.FinanceDataFarmer.TABLE_NAME +
                        "." + BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );
    }

    public BfwProvider() {

    }

    private UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = BfwContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BfwContract.PATH_COOPS, COOPS);
        matcher.addURI(authority, BfwContract.PATH_COOPS + "/#", COOPS_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_COOP_INFO, COOP_INFO);
        matcher.addURI(authority, BfwContract.PATH_COOP_INFO + "/#", COOP_INFO_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_SALES_COOP, SALE_COOP);
        matcher.addURI(authority, BfwContract.PATH_SALES_COOP + "/#", SALE_COOP_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_YIELD_COOP, YIELD_COOP);
        matcher.addURI(authority, BfwContract.PATH_YIELD_COOP + "/#", YIELD_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_BASELINE_SALES_COOP, BASELINE_SALE_COOP);
        matcher.addURI(authority, BfwContract.PATH_BASELINE_SALES_COOP + "/#", BASELINE_SALE_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_FINANCEINFO_COOP, FINANCE_INFO_COOP);
        matcher.addURI(authority, BfwContract.PATH_FINANCEINFO_COOP + "/#", FINANCE_INFO_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_EXPECTED_YIELD_COOP, EXPECTED_YIELD);
        matcher.addURI(authority, BfwContract.PATH_EXPECTED_YIELD_COOP + "/#", EXPECTED_YIELD_BY_COOP);

        matcher.addURI(authority, BfwContract.PATH_BUYER, BUYER);
        matcher.addURI(authority, BfwContract.PATH_BUYER + "/#", BUYER_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_COOP_AGENT, COOP_AGENT);
        matcher.addURI(authority, BfwContract.PATH_COOP_AGENT + "/#", COOP_AGENT_DETAILS);


        matcher.addURI(authority, BfwContract.PATH_FARMER, FARMER);
        matcher.addURI(authority, BfwContract.PATH_FARMER + "/#", FARMER_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_FARMER_ACCESS_INFO, FARMER_INFO);
        matcher.addURI(authority, BfwContract.PATH_FARMER_ACCESS_INFO + "/#", INFO_BY_FARMER);

        matcher.addURI(authority, BfwContract.PATH_BASELINE_FARMER, FARMER_BASELINE);
        matcher.addURI(authority, BfwContract.PATH_BASELINE_FARMER + "/#", BASELINE_BY_FARMER);

        matcher.addURI(authority, BfwContract.PATH_FORECAST_FARMER, FARMER_FORECAST);
        matcher.addURI(authority, BfwContract.PATH_FORECAST_FARMER + "/#", FORECAST_BY_FARMER);

        matcher.addURI(authority, BfwContract.PATH_FINANCEDATA_FARMER, FARMER_FINANCE_DATA);
        matcher.addURI(authority, BfwContract.PATH_FINANCEDATA_FARMER + "/#", FINANCE_DATA_BY_FARMER);

        matcher.addURI(authority, BfwContract.PATH_LAND_PLOT, LAND_PLOT);
        matcher.addURI(authority, BfwContract.PATH_LAND_PLOT + "/#", FARMER_BY_LAND_PLOT);

        matcher.addURI(authority, BfwContract.PATH_VENDOR, VENDOR);
        matcher.addURI(authority, BfwContract.PATH_VENDOR + "/#", VENDOR_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_VENDOR_ACCESS_INFO, VENDOR_INFO);
        matcher.addURI(authority, BfwContract.PATH_VENDOR_ACCESS_INFO + "/#", INFO_BY_VENDOR);

        matcher.addURI(authority, BfwContract.PATH_VENDOR_LAND, VENDOR_LAND);
        matcher.addURI(authority, BfwContract.PATH_VENDOR_LAND + "/#", LAND_BY_VENDOR);

        matcher.addURI(authority, BfwContract.PATH_BASELINE_VENDOR, BASELINE_VENDOR);
        matcher.addURI(authority, BfwContract.PATH_BASELINE_VENDOR + "/#", BASELINE_BY_VENDOR);

        matcher.addURI(authority, BfwContract.PATH_FINANCEDATA_VENDOR, FINANCE_DATA_VENDOR);
        matcher.addURI(authority, BfwContract.PATH_FINANCEDATA_VENDOR + "/#", FINANCE_DATA_BY_VENDOR);

        matcher.addURI(authority, BfwContract.PATH_FORECAST_VENDOR, FORECAST_VENDOR);
        matcher.addURI(authority, BfwContract.PATH_FORECAST_VENDOR + "/#", FORECAST_BY_VENDOR);

        matcher.addURI(authority, BfwContract.PATH_HARVEST_SEASON, HARVEST_SEASON);

        matcher.addURI(authority, BfwContract.PATH_PRODUCT, PRODUCT_PRODUCT);
        matcher.addURI(authority, BfwContract.PATH_PRODUCT + "/#", PRODUCT_PRODUCT_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_PRODUCT_TEMPLATE, PRODUCT_TEMPLATE);
        matcher.addURI(authority, BfwContract.PATH_PRODUCT_TEMPLATE + "/#", PRODUCT_TEMPLATE_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER, SALE_ORDER);
        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER + "/#", SALE_ORDER_DETAILS);
        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER_LINE, SALE_ORDER_LINE);

        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER, PURCHASE_ORDER);
        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER + "/#", PURCHASE_ORDER_DETAILS);
        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER_LINE, PURCHASE_ORDER_LINE);

        matcher.addURI(authority, BfwContract.PATH_LOAN_LINE, LOAN_LINE);
        matcher.addURI(authority, BfwContract.PATH_LOAN_PAYMENT, LOAN_PAYMENT);
        matcher.addURI(authority, BfwContract.PATH_LOAN, LOAN);
        matcher.addURI(authority, BfwContract.PATH_LOAN + "/#", LOAN_DETAILS);

        matcher.addURI(authority, BfwContract.PATH_LOAN + "/#/#", LOAN_LINE_PAYMENT);
        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER + "/#/#", SALE_ORDER_LINE_PRODUCT);

        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER + "/#/#", PURCHASE_ORDER_LINE_PRODUCT);

        matcher.addURI(authority, BfwContract.PATH_PAYMENT_TERM, PAYMENT_TERM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mBfwDbHelper = new BfwDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case COOPS:
                return BfwContract.Coops.CONTENT_TYPE;
            case COOPS_DETAILS:
                return BfwContract.Coops.CONTENT_ITEM_TYPE;
            case COOP_INFO:
                return BfwContract.CoopInfo.CONTENT_TYPE;
            case COOP_INFO_BY_COOP:
                return BfwContract.CoopInfo.CONTENT_TYPE;
            case SALE_COOP:
                return BfwContract.SalesCoop.CONTENT_TYPE;
            case SALE_COOP_BY_COOP:
                return BfwContract.SalesCoop.CONTENT_TYPE;
            case YIELD_COOP:
                return BfwContract.YieldCoop.CONTENT_TYPE;
            case YIELD_BY_COOP:
                return BfwContract.YieldCoop.CONTENT_TYPE;
            case BASELINE_SALE_COOP:
                return BfwContract.BaselineSalesCoop.CONTENT_TYPE;
            case BASELINE_SALE_BY_COOP:
                return BfwContract.BaselineSalesCoop.CONTENT_TYPE;
            case FINANCE_INFO_COOP:
                return BfwContract.FinanceInfoCoop.CONTENT_TYPE;
            case FINANCE_INFO_BY_COOP:
                return BfwContract.FinanceInfoCoop.CONTENT_TYPE;
            case EXPECTED_YIELD:
                return BfwContract.ExpectedYieldCoop.CONTENT_TYPE;
            case EXPECTED_YIELD_BY_COOP:
                return BfwContract.ExpectedYieldCoop.CONTENT_TYPE;
            case BUYER:
                return BfwContract.Buyer.CONTENT_TYPE;
            case BUYER_DETAILS:
                return BfwContract.Buyer.CONTENT_ITEM_TYPE;
            case COOP_AGENT:
                return BfwContract.CoopAgent.CONTENT_TYPE;
            case COOP_AGENT_DETAILS:
                return BfwContract.CoopAgent.CONTENT_ITEM_TYPE;
            case FARMER:
                return BfwContract.Farmer.CONTENT_TYPE;
            case FARMER_DETAILS:
                return BfwContract.Farmer.CONTENT_ITEM_TYPE;
            case LAND_PLOT:
                return BfwContract.LandPlot.CONTENT_TYPE;
            case FARMER_BY_LAND_PLOT:
                return BfwContract.LandPlot.CONTENT_TYPE;
            case FARMER_INFO:
                return BfwContract.FarmerAccessInfo.CONTENT_TYPE;
            case INFO_BY_FARMER:
                return BfwContract.FarmerAccessInfo.CONTENT_TYPE;
            case FARMER_BASELINE:
                return BfwContract.BaselineFarmer.CONTENT_TYPE;
            case BASELINE_BY_FARMER:
                return BfwContract.BaselineFarmer.CONTENT_TYPE;
            case FARMER_FORECAST:
                return BfwContract.ForecastFarmer.CONTENT_TYPE;
            case FORECAST_BY_FARMER:
                return BfwContract.ForecastFarmer.CONTENT_TYPE;
            case FARMER_FINANCE_DATA:
                return BfwContract.FinanceDataFarmer.CONTENT_TYPE;
            case FINANCE_DATA_BY_FARMER:
                return BfwContract.FinanceDataFarmer.CONTENT_TYPE;
            case VENDOR:
                return BfwContract.Vendor.CONTENT_TYPE;
            case VENDOR_DETAILS:
                return BfwContract.Vendor.CONTENT_ITEM_TYPE;
            case VENDOR_INFO:
                return BfwContract.VendorAccessInfo.CONTENT_TYPE;
            case INFO_BY_VENDOR:
                return BfwContract.VendorAccessInfo.CONTENT_TYPE;
            case VENDOR_LAND:
                return BfwContract.VendorLand.CONTENT_TYPE;
            case LAND_BY_VENDOR:
                return BfwContract.VendorLand.CONTENT_TYPE;
            case BASELINE_BY_VENDOR:
                return BfwContract.BaseLineVendor.CONTENT_TYPE;
            case BASELINE_VENDOR:
                return BfwContract.BaseLineVendor.CONTENT_TYPE;
            case FINANCE_DATA_VENDOR:
                return BfwContract.FinanceDataVendor.CONTENT_TYPE;
            case FINANCE_DATA_BY_VENDOR:
                return BfwContract.FinanceDataVendor.CONTENT_TYPE;
            case FORECAST_VENDOR:
                return BfwContract.ForecastVendor.CONTENT_TYPE;
            case FORECAST_BY_VENDOR:
                return BfwContract.ForecastVendor.CONTENT_TYPE;
            case HARVEST_SEASON:
                return BfwContract.HarvestSeason.CONTENT_TYPE;
            case PRODUCT_PRODUCT:
                return BfwContract.ProductProduct.CONTENT_TYPE;
            case PRODUCT_PRODUCT_DETAILS:
                return BfwContract.ProductProduct.CONTENT_ITEM_TYPE;
            case PRODUCT_TEMPLATE:
                return BfwContract.ProductTemplate.CONTENT_TYPE;
            case PRODUCT_TEMPLATE_DETAILS:
                return BfwContract.ProductTemplate.CONTENT_ITEM_TYPE;
            case SALE_ORDER:
                return BfwContract.SaleOrder.CONTENT_TYPE;
            case SALE_ORDER_DETAILS:
                return BfwContract.SaleOrder.CONTENT_ITEM_TYPE;
            case SALE_ORDER_LINE:
                return BfwContract.SaleOrder.CONTENT_TYPE;
            case PURCHASE_ORDER:
                return BfwContract.PurchaseOrder.CONTENT_TYPE;
            case PURCHASE_ORDER_DETAILS:
                return BfwContract.PurchaseOrder.CONTENT_ITEM_TYPE;
            case PURCHASE_ORDER_LINE:
                return BfwContract.PurchaseOrder.CONTENT_TYPE;
            case LOAN:
                return BfwContract.Loan.CONTENT_TYPE;
            case LOAN_LINE:
                return BfwContract.LoanLine.CONTENT_TYPE;
            case LOAN_PAYMENT:
                return BfwContract.LoanPayment.CONTENT_TYPE;
            case LOAN_DETAILS:
                return BfwContract.Loan.CONTENT_ITEM_TYPE;
            case SALE_ORDER_LINE_PRODUCT:
                return BfwContract.SaleOrder.CONTENT_TYPE;
            case PURCHASE_ORDER_LINE_PRODUCT:
                return BfwContract.PurchaseOrder.CONTENT_TYPE;
            case LOAN_LINE_PAYMENT:
                return BfwContract.Loan.CONTENT_TYPE;
            case PAYMENT_TERM:
                return BfwContract.PaymentTerm.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mBfwDbHelper.getWritableDatabase();
        Uri returnUri;
        long id;

        switch (mUriMatcher.match(uri)) {
            case COOPS:
                id = db.insert(BfwContract.Coops.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.Coops.buildCoopUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table coops " + uri);
                }
                break;
            case COOP_INFO:
                id = db.insert(BfwContract.CoopInfo.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.CoopInfo.buildCoopInfoUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table coop info " + uri);
                }
                break;
            case SALE_COOP:
                id = db.insert(BfwContract.SalesCoop.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.SalesCoop.buildSalesCoopInfoUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table sale coop " + uri);
                }
                break;
            case YIELD_COOP:
                id = db.insert(BfwContract.YieldCoop.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.YieldCoop.buildYieldCoopUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table yield coop " + uri);
                }
                break;
            case FINANCE_INFO_COOP:
                id = db.insert(BfwContract.FinanceInfoCoop.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.FinanceInfoCoop.buildBaselineFinanceInfoCoopUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table finance coop " + uri);
                }
                break;
            case BASELINE_SALE_COOP:
                id = db.insert(BfwContract.BaselineSalesCoop.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.BaselineSalesCoop.buildBaselineSalesCoppUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table baseline coop " + uri);
                }
                break;
            case EXPECTED_YIELD:
                id = db.insert(BfwContract.ExpectedYieldCoop.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.ExpectedYieldCoop.buildExpectedYieldCoopUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not support table baseline coop " + uri);
                }
                break;
            case FARMER:
                id = db.insert(BfwContract.Farmer.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.Farmer.buildFarmerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table farmer " + uri);
                }
                break;
            case FARMER_BASELINE:
                id = db.insert(BfwContract.BaselineFarmer.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.BaselineFarmer.buildBaselineFarmerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table baseline farmer " + uri);
                }
                break;
            case FARMER_FORECAST:
                id = db.insert(BfwContract.ForecastFarmer.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.ForecastFarmer.buildForecastFarmerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table forecast farmer " + uri);
                }
                break;
            case FARMER_FINANCE_DATA:
                id = db.insert(BfwContract.FinanceDataFarmer.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.FinanceDataFarmer.buildFinanceDataFarmerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table finance data farmer " + uri);
                }
                break;
            case LAND_PLOT:
                id = db.insert(BfwContract.LandPlot.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.LandPlot.buildLandPlotUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table land plot " + uri);
                }
                break;
            case FARMER_INFO:
                id = db.insert(BfwContract.FarmerAccessInfo.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.FarmerAccessInfo.buildFarmerAccessInfoUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table farmer access info " + uri);
                }
                break;
            case HARVEST_SEASON:
                id = db.insert(BfwContract.HarvestSeason.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.HarvestSeason.buildHarvestSeasonUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table harvest season " + uri);
                }
                break;
            case VENDOR:
                id = db.insert(BfwContract.Vendor.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.Vendor.buildVendorUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table vendor " + uri);
                }
                break;
            case VENDOR_INFO:
                id = db.insert(BfwContract.VendorAccessInfo.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.VendorAccessInfo.buildVendorAccessInfoUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table vendor access info " + uri);
                }
                break;
            case FORECAST_VENDOR:
                id = db.insert(BfwContract.ForecastVendor.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.ForecastVendor.buildForecastVendorUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table forecast vendor info " + uri);
                }
                break;
            case BASELINE_VENDOR:
                id = db.insert(BfwContract.BaseLineVendor.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.BaseLineVendor.buildBaselineVendorUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table baseline vendor info " + uri);
                }
                break;
            case VENDOR_LAND:
                id = db.insert(BfwContract.VendorLand.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.VendorLand.buildVendorLandUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table vendor land info " + uri);
                }
                break;
            case FINANCE_DATA_VENDOR:
                id = db.insert(BfwContract.FinanceDataVendor.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.FinanceDataVendor.buildFinanceDataVendorUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table vendor land info " + uri);
                }
                break;
            case BUYER:
                id = db.insert(BfwContract.Buyer.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.Buyer.buildBuyerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table buyer " + uri);
                }
                break;
            case COOP_AGENT:
                id = db.insert(BfwContract.CoopAgent.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.CoopAgent.buildAgentUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table coop agent " + uri);
                }
                break;
            case SALE_ORDER:
                id = db.insert(BfwContract.SaleOrder.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.SaleOrder.buildSaleOrderUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table sale order " + uri);
                }
                break;
            case SALE_ORDER_LINE:
                id = db.insert(BfwContract.SaleOrderLine.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.SaleOrderLine.buildSaleOrderUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table sale order line " + uri);
                }
                break;
            case PURCHASE_ORDER:
                id = db.insert(BfwContract.PurchaseOrder.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.PurchaseOrder.buildPurchaseOrderUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table purchase order " + uri);
                }
                break;
            case PURCHASE_ORDER_LINE:
                id = db.insert(BfwContract.PurchaseOrderLine.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.PurchaseOrderLine.buildPurchaseOrdeLinerUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table purchase order " + uri);
                }
                break;
            case PRODUCT_PRODUCT:
                id = db.insert(BfwContract.ProductProduct.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.ProductProduct.buildProductUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table product product" + uri);
                }
                break;
            case PRODUCT_TEMPLATE:
                id = db.insert(BfwContract.ProductTemplate.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.ProductTemplate.buildProductTemplateUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table product template" + uri);
                }
                break;
            case LOAN:
                id = db.insert(BfwContract.Loan.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.Loan.buildLoanUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table loan  " + uri);
                }
                break;
            case LOAN_LINE:
                id = db.insert(BfwContract.LoanLine.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.LoanLine.buildProductLoanUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table loan line " + uri);
                }
                break;
            case LOAN_PAYMENT:
                id = db.insert(BfwContract.LoanPayment.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.LoanPayment.buildProductLoanPaymentUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table loan payment  " + uri);
                }
                break;
            case PAYMENT_TERM:
                id = db.insert(BfwContract.PaymentTerm.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.PaymentTerm.buildPaymentTermUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table loan payment  " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not Implemented yet");

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor returnCursor;
        final SQLiteDatabase db = mBfwDbHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case BUYER:
                returnCursor = db.query(BfwContract.Buyer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COOP_AGENT:
                returnCursor = db.query(BfwContract.CoopAgent.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PAYMENT_TERM:
                returnCursor = db.query(BfwContract.PaymentTerm.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COOPS:
                returnCursor = db.query(BfwContract.Coops.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COOPS_DETAILS:
                returnCursor = db.query(BfwContract.Coops.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COOP_INFO:
                returnCursor = db.query(BfwContract.CoopInfo.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COOP_INFO_BY_COOP:
                returnCursor = infoByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_COOP:
                returnCursor = db.query(BfwContract.SalesCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_ORDER:
                returnCursor = db.query(BfwContract.SaleOrder.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_ORDER_DETAILS:
                returnCursor = db.query(BfwContract.SalesCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_ORDER_LINE:
                returnCursor = db.query(BfwContract.SaleOrderLine.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PURCHASE_ORDER:
                returnCursor = db.query(BfwContract.PurchaseOrder.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PURCHASE_ORDER_DETAILS:
                returnCursor = db.query(BfwContract.SalesCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PURCHASE_ORDER_LINE:
                returnCursor = db.query(BfwContract.PurchaseOrderLine.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_PRODUCT:
                returnCursor = db.query(BfwContract.ProductProduct.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_PRODUCT_DETAILS:
                returnCursor = db.query(BfwContract.ProductProduct.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_TEMPLATE:
                returnCursor = db.query(BfwContract.ProductTemplate.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_TEMPLATE_DETAILS:
                returnCursor = db.query(BfwContract.ProductTemplate.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOAN:
                returnCursor = db.query(BfwContract.Loan.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOAN_DETAILS:
                returnCursor = db.query(BfwContract.Loan.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOAN_LINE:
                returnCursor = db.query(BfwContract.LoanLine.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOAN_PAYMENT:
                returnCursor = db.query(BfwContract.LoanPayment.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_COOP_BY_COOP:
                returnCursor = saleByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BASELINE_SALE_COOP:
                returnCursor = db.query(BfwContract.BaselineSalesCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BASELINE_SALE_BY_COOP:
                returnCursor = baselineByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case YIELD_COOP:
                returnCursor = db.query(BfwContract.YieldCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case YIELD_BY_COOP:
                returnCursor = yieldByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EXPECTED_YIELD:
                returnCursor = db.query(BfwContract.ExpectedYieldCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EXPECTED_YIELD_BY_COOP:
                returnCursor = expectedYieldByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FINANCE_INFO_COOP:
                returnCursor = db.query(BfwContract.FinanceInfoCoop.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FINANCE_INFO_BY_COOP:
                returnCursor = financeInfoByCoop.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER:
                returnCursor = db.query(BfwContract.Farmer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_DETAILS:
                returnCursor = db.query(BfwContract.Farmer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LAND_PLOT:
                returnCursor = db.query(BfwContract.LandPlot.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_BY_LAND_PLOT:
                returnCursor = farmerLandPlot.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PURCHASE_ORDER_LINE_PRODUCT:
                returnCursor = purchaseOderLineProduct.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SALE_ORDER_LINE_PRODUCT:
                returnCursor = saleOrderLineProduct.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOAN_LINE_PAYMENT:
                returnCursor = loanLinePayment.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_BASELINE:
                returnCursor = db.query(BfwContract.BaselineFarmer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BASELINE_BY_FARMER:
                returnCursor = baselineByFarmer.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_FORECAST:
                returnCursor = db.query(BfwContract.ForecastFarmer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORECAST_BY_FARMER:
                returnCursor = forecastByFarmer.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_FINANCE_DATA:
                returnCursor = db.query(BfwContract.FinanceDataFarmer.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FINANCE_DATA_BY_FARMER:
                returnCursor = financeDataByFarmer.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_INFO:
                returnCursor = db.query(BfwContract.FarmerAccessInfo.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INFO_BY_FARMER:
                returnCursor = infoByFarmer.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENDOR:
                returnCursor = db.query(BfwContract.Vendor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENDOR_DETAILS:
                returnCursor = db.query(BfwContract.Vendor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENDOR_LAND:
                returnCursor = db.query(BfwContract.VendorLand.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LAND_BY_VENDOR:
                returnCursor = landByVendor.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORECAST_VENDOR:
                returnCursor = db.query(BfwContract.ForecastVendor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORECAST_BY_VENDOR:
                returnCursor = forecastByVendor.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENDOR_INFO:
                returnCursor = db.query(BfwContract.VendorAccessInfo.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INFO_BY_VENDOR:
                returnCursor = infoByVendor.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BASELINE_VENDOR:
                returnCursor = db.query(BfwContract.BaseLineVendor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BASELINE_BY_VENDOR:
                returnCursor = baselineByVendor.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FINANCE_DATA_VENDOR:
                returnCursor = db.query(BfwContract.FinanceDataVendor.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FINANCE_DATA_BY_VENDOR:
                returnCursor = financeByVendor.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case HARVEST_SEASON:
                returnCursor = db.query(BfwContract.HarvestSeason.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        return returnCursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mBfwDbHelper.getWritableDatabase();
        int updateRow = 0;

        switch (mUriMatcher.match(uri)) {
            case FARMER:
                updateRow = db.update(BfwContract.Farmer.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FARMER_BASELINE:
                updateRow = db.update(BfwContract.BaselineFarmer.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case PAYMENT_TERM:
                updateRow = db.update(BfwContract.PaymentTerm.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FARMER_FORECAST:
                updateRow = db.update(BfwContract.ForecastFarmer.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FARMER_INFO:
                updateRow = db.update(BfwContract.FarmerAccessInfo.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case LAND_PLOT:
                updateRow = db.update(BfwContract.LandPlot.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FARMER_FINANCE_DATA:
                updateRow = db.update(BfwContract.FinanceDataFarmer.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case COOPS:
                updateRow = db.update(BfwContract.Coops.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case COOP_INFO:
                updateRow = db.update(BfwContract.CoopInfo.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case SALE_COOP:
                updateRow = db.update(BfwContract.SalesCoop.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case BASELINE_SALE_COOP:
                updateRow = db.update(BfwContract.BaselineSalesCoop.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FINANCE_INFO_COOP:
                updateRow = db.update(BfwContract.FinanceInfoCoop.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case YIELD_COOP:
                updateRow = db.update(BfwContract.YieldCoop.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case EXPECTED_YIELD:
                updateRow = db.update(BfwContract.ExpectedYieldCoop.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case VENDOR:
                updateRow = db.update(BfwContract.Vendor.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case VENDOR_INFO:
                updateRow = db.update(BfwContract.VendorAccessInfo.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case VENDOR_LAND:
                updateRow = db.update(BfwContract.VendorLand.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FORECAST_VENDOR:
                updateRow = db.update(BfwContract.ForecastVendor.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case BASELINE_VENDOR:
                updateRow = db.update(BfwContract.BaseLineVendor.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case FINANCE_DATA_VENDOR:
                updateRow = db.update(BfwContract.FinanceDataVendor.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case BUYER:
                updateRow = db.update(BfwContract.Buyer.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case COOP_AGENT:
                updateRow = db.update(BfwContract.CoopAgent.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case HARVEST_SEASON:
                updateRow = db.update(BfwContract.HarvestSeason.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case SALE_ORDER:
                updateRow = db.update(BfwContract.SaleOrder.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case SALE_ORDER_LINE:
                updateRow = db.update(BfwContract.SaleOrderLine.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case PURCHASE_ORDER:
                updateRow = db.update(BfwContract.PurchaseOrder.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case PURCHASE_ORDER_LINE:
                updateRow = db.update(BfwContract.PurchaseOrderLine.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case PRODUCT_PRODUCT:
                updateRow = db.update(BfwContract.ProductProduct.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case PRODUCT_TEMPLATE:
                updateRow = db.update(BfwContract.ProductTemplate.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case LOAN:
                updateRow = db.update(BfwContract.Loan.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case LOAN_LINE:
                updateRow = db.update(BfwContract.LoanLine.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case LOAN_PAYMENT:
                updateRow = db.update(BfwContract.LoanPayment.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri");
        }
        if (updateRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateRow;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mBfwDbHelper.getWritableDatabase();
        int deleteRow = 0;

        switch (mUriMatcher.match(uri)) {
            case FARMER:
                deleteRow = db.delete(BfwContract.Farmer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FARMER_BASELINE:
                deleteRow = db.delete(BfwContract.BaselineFarmer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PAYMENT_TERM:
                deleteRow = db.delete(BfwContract.PaymentTerm.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FARMER_FORECAST:
                deleteRow = db.delete(BfwContract.ForecastFarmer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FARMER_INFO:
                deleteRow = db.delete(BfwContract.FarmerAccessInfo.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case LAND_PLOT:
                deleteRow = db.delete(BfwContract.LandPlot.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FARMER_FINANCE_DATA:
                deleteRow = db.delete(BfwContract.FinanceDataFarmer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case COOPS:
                deleteRow = db.delete(BfwContract.Coops.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case COOP_INFO:
                deleteRow = db.delete(BfwContract.CoopInfo.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SALE_COOP:
                deleteRow = db.delete(BfwContract.SalesCoop.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case BASELINE_SALE_COOP:
                deleteRow = db.delete(BfwContract.BaselineSalesCoop.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FINANCE_INFO_COOP:
                deleteRow = db.delete(BfwContract.FinanceInfoCoop.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case YIELD_COOP:
                deleteRow = db.delete(BfwContract.YieldCoop.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case EXPECTED_YIELD:
                deleteRow = db.delete(BfwContract.ExpectedYieldCoop.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case VENDOR:
                deleteRow = db.delete(BfwContract.Vendor.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case VENDOR_INFO:
                deleteRow = db.delete(BfwContract.VendorAccessInfo.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case VENDOR_LAND:
                deleteRow = db.delete(BfwContract.VendorLand.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FORECAST_VENDOR:
                deleteRow = db.delete(BfwContract.ForecastVendor.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case BASELINE_VENDOR:
                deleteRow = db.delete(BfwContract.BaseLineVendor.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FINANCE_DATA_VENDOR:
                deleteRow = db.delete(BfwContract.FinanceDataVendor.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case BUYER:
                deleteRow = db.delete(BfwContract.Buyer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case COOP_AGENT:
                deleteRow = db.delete(BfwContract.CoopAgent.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case HARVEST_SEASON:
                deleteRow = db.delete(BfwContract.HarvestSeason.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SALE_ORDER:
                deleteRow = db.delete(BfwContract.SaleOrder.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SALE_ORDER_LINE:
                deleteRow = db.delete(BfwContract.SaleOrderLine.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PURCHASE_ORDER:
                deleteRow = db.delete(BfwContract.PurchaseOrder.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PURCHASE_ORDER_LINE:
                deleteRow = db.delete(BfwContract.PurchaseOrderLine.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PRODUCT_PRODUCT:
                deleteRow = db.delete(BfwContract.ProductProduct.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PRODUCT_TEMPLATE:
                deleteRow = db.delete(BfwContract.ProductTemplate.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case LOAN:
                deleteRow = db.delete(BfwContract.Loan.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case LOAN_LINE:
                deleteRow = db.delete(BfwContract.LoanLine.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case LOAN_PAYMENT:
                deleteRow = db.delete(BfwContract.LoanPayment.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri");
        }
        if (deleteRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleteRow;
    }
}
