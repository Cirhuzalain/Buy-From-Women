package com.nijus.alino.bfwcoopmanagement.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BfwDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "buyfromwomen.db";

    public BfwDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE_COOP_AGENT = "CREATE TABLE " + BfwContract.CoopAgent.TABLE_NAME + " ( " +
                BfwContract.CoopAgent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.CoopAgent.COLUMN_AGENT_NAME + " TEXT NOT NULL," +
                BfwContract.CoopAgent.COLUMN_AGENT_EMAIL + " TEXT NOT NULL," +
                BfwContract.CoopAgent.COLUMN_AGENT_PHONE + " TEXT NOT NULL," +
                BfwContract.CoopAgent.COLUMN_COOP_ID + " INTEGER NOT NULL," +
                BfwContract.CoopAgent.COLUMN_USER_ID + " INTEGER," +
                BfwContract.CoopAgent.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.CoopAgent.COLUMN_IS_UPDATE + " INTEGER," +
                BfwContract.CoopAgent.COLUMN_AGENT_SERVER_ID + " INTEGER" +
                " ); ";

        final String CREATE_TABLE_BUYER = "CREATE TABLE " + BfwContract.Buyer.TABLE_NAME + " ( " +
                BfwContract.Buyer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.Buyer.COLUMN_BUYER_NAME + " TEXT NOT NULL," +
                BfwContract.Buyer.COLUMN_BUYER_EMAIL + " TEXT NOT NULL," +
                BfwContract.Buyer.COLUMN_BUYER_PHONE + " TEXT NOT NULL," +
                BfwContract.Buyer.COLUMN_USER_ID + " INTEGER," +
                BfwContract.Buyer.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.Buyer.COLUMN_IS_UPDATE + " INTEGER," +
                BfwContract.Buyer.COLUMN_BUYER_SERVER_ID + " INTEGER" +
                " );";

        final String CREATE_TABLE_HARVEST_SEASON = "CREATE TABLE " + BfwContract.HarvestSeason.TABLE_NAME + " ( " +
                BfwContract.HarvestSeason._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.HarvestSeason.COLUMN_NAME + " TEXT, " +
                BfwContract.HarvestSeason.COLUMN_ACTIVE + " INTEGER NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_START_DATE + " TEXT NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_END_DATE + " TEXT NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_SERVER_ID + " INTEGER NOT NULL " +
                ");";

        final String CREATE_TABLE_FARMER = "CREATE TABLE " + BfwContract.Farmer.TABLE_NAME + " ( " +
                BfwContract.Farmer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.Farmer.COLUMN_NAME + " TEXT NOT NULL, " +
                BfwContract.Farmer.COLUMN_GENDER + " TEXT NOT NULL, " +
                BfwContract.Farmer.COLUMN_PHONE + " TEXT NOT NULL, " +
                BfwContract.Farmer.COLUMN_ADDRESS + " TEXT," +

                BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD + " INTEGER, " +
                BfwContract.Farmer.COLUMN_HOUSE_MEMBER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_FIRST_NAME + " TEXT, " +
                BfwContract.Farmer.COLUMN_LAST_NAME + " TEXT, " +
                BfwContract.Farmer.COLUMN_CELL_PHONE + " TEXT, " +
                BfwContract.Farmer.COLUMN_CELL_CARRIER + " TEXT, " +
                BfwContract.Farmer.COLUMN_MEMBER_SHIP + " TEXT, " +

                BfwContract.Farmer.COLUMN_TRACTORS + " INTEGER, " +
                BfwContract.Farmer.COLUMN_HARVESTER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_DRYER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TRESHER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_SAFE_STORAGE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_OTHER_INFO + " INTEGER, " +
                BfwContract.Farmer.COLUMN_DAM + " INTEGER, " +
                BfwContract.Farmer.COLUMN_WELL + " INTEGER, " +
                BfwContract.Farmer.COLUMN_BOREHOLE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_PIPE_BORNE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_RIVER_STREAM + " INTEGER, " +
                BfwContract.Farmer.COLUMN_IRRIGATION + " INTEGER, " +
                BfwContract.Farmer.COLUMN_NONE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_OTHER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_STORAGE_DETAIL + " TEXT," +
                BfwContract.Farmer.COLUMN_NEW_SOURCE_DETAIL + " TEXT," +
                BfwContract.Farmer.COLUMN_TOTAL_PLOT_SIZE + " REAL," +
                BfwContract.Farmer.COLUMN_WATER_SOURCE_DETAILS + " TEXT," +

                BfwContract.Farmer.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.Farmer.COLUMN_IS_UPDATE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_FARMER_SERVER_ID + " INTEGER, " +
                BfwContract.Farmer.COLUMN_COOP_SERVER_ID + " INTEGER, " +
                BfwContract.Farmer.COLUMN_COOP_USER_ID + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.Farmer.COLUMN_COOP_USER_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")" +
                " ); ";

        final String CREATE_TABLE_LAND_PLOT = "CREATE TABLE " + BfwContract.LandPlot.TABLE_NAME + " ( " +
                BfwContract.LandPlot._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.LandPlot.COLUMN_PLOT_SIZE + " REAL NOT NULL, " +
                BfwContract.LandPlot.COLUMN_SERVER_ID + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_FARMER_ID + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_LAND_ID + " TEXT, " +
                BfwContract.LandPlot.COLUMN_LAT_INFO + " REAL," +
                BfwContract.LandPlot.COLUMN_LNG_INFO + " REAL," +
                BfwContract.LandPlot.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.LandPlot.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_IS_UPDATE + " INTEGER, " +
                "FOREIGN KEY (" + BfwContract.LandPlot.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.LandPlot.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FARMER_ACCESS_INFO = "CREATE TABLE " + BfwContract.FarmerAccessInfo.TABLE_NAME + " ( " +
                BfwContract.FarmerAccessInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_SEEDS + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_LABOUR + "  INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.FarmerAccessInfo.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                " );";

        final String CREATE_TABLE_BASELINE_FARMER = "CREATE TABLE " + BfwContract.BaselineFarmer.TABLE_NAME + " ( " +
                BfwContract.BaselineFarmer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG + " REAL," +
                BfwContract.BaselineFarmer.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.BaselineFarmer.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.BaselineFarmer.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.BaselineFarmer.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.BaselineFarmer.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.BaselineFarmer.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.BaselineFarmer.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FORECAST_FARMER = "CREATE TABLE " + BfwContract.ForecastFarmer.TABLE_NAME + " ( " +
                BfwContract.ForecastFarmer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_YIELD_MT + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE + " REAL," +
                BfwContract.ForecastFarmer.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.ForecastFarmer.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.ForecastFarmer.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.ForecastFarmer.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.ForecastFarmer.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.ForecastFarmer.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ForecastFarmer.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FINANCEDATA_FARMER = "CREATE TABLE " + BfwContract.FinanceDataFarmer.TABLE_NAME + " ( " +
                BfwContract.FinanceDataFarmer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT + " REAL," +
                BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING + " REAL," +
                BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE + " REAL," +
                BfwContract.FinanceDataFarmer.COLUMN_DURATION + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER + " TEXT," +
                BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_VENDOR = "CREATE TABLE " + BfwContract.Vendor.TABLE_NAME + " ( " +
                BfwContract.Vendor._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.Vendor.COLUMN_NAME + " TEXT NOT NULL, " +
                BfwContract.Vendor.COLUMN_GENDER + " TEXT NOT NULL, " +
                BfwContract.Vendor.COLUMN_PHONE + " TEXT NOT NULL, " +
                BfwContract.Vendor.COLUMN_EMAIL + " TEXT," +
                BfwContract.Vendor.COLUMN_ADDRESS + " TEXT," +

                BfwContract.Vendor.COLUMN_HOUSEHOLD_HEAD + " INTEGER, " +
                BfwContract.Vendor.COLUMN_HOUSE_MEMBER + " INTEGER, " +
                BfwContract.Vendor.COLUMN_FIRST_NAME + " TEXT, " +
                BfwContract.Vendor.COLUMN_LAST_NAME + " TEXT, " +
                BfwContract.Vendor.COLUMN_CELL_PHONE + " TEXT, " +
                BfwContract.Vendor.COLUMN_CELL_CARRIER + " TEXT, " +
                BfwContract.Vendor.COLUMN_MEMBER_SHIP + " TEXT, " +

                BfwContract.Vendor.COLUMN_TRACTORS + " INTEGER, " +
                BfwContract.Vendor.COLUMN_HARVESTER + " INTEGER, " +
                BfwContract.Vendor.COLUMN_DRYER + " INTEGER, " +
                BfwContract.Vendor.COLUMN_TRESHER + " INTEGER, " +
                BfwContract.Vendor.COLUMN_SAFE_STORAGE + " INTEGER, " +
                BfwContract.Vendor.COLUMN_OTHER_INFO + " INTEGER, " +
                BfwContract.Vendor.COLUMN_NEW_SOURCE_DETAIL + " TEXT," +

                BfwContract.Vendor.COLUMN_DAM + " INTEGER, " +
                BfwContract.Vendor.COLUMN_WELL + " INTEGER, " +
                BfwContract.Vendor.COLUMN_BOREHOLE + " INTEGER, " +
                BfwContract.Vendor.COLUMN_PIPE_BORNE + " INTEGER, " +
                BfwContract.Vendor.COLUMN_RIVER_STREAM + " INTEGER, " +
                BfwContract.Vendor.COLUMN_IRRIGATION + " INTEGER, " +
                BfwContract.Vendor.COLUMN_NONE + " INTEGER, " +
                BfwContract.Vendor.COLUMN_OTHER + " INTEGER, " +
                BfwContract.Vendor.COLUMN_STORAGE_DETAIL + " TEXT," +
                BfwContract.Vendor.COLUMN_TOTAL_PLOT_SIZE + " REAL," +
                BfwContract.Vendor.COLUMN_WATER_SOURCE_DETAILS + " TEXT," +

                BfwContract.Vendor.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.Vendor.COLUMN_IS_UPDATE + " INTEGER, " +
                BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID + " INTEGER " +
                ");";

        final String CREATE_TABLE_VENDOR_ACCESS_INFO = "CREATE TABLE " + BfwContract.VendorAccessInfo.TABLE_NAME + " ( " +
                BfwContract.VendorAccessInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_SEEDS + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_LABOUR + "  INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_SPRAYERS + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.VendorAccessInfo.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_VENDOR_LAND = "CREATE TABLE " + BfwContract.VendorLand.TABLE_NAME + " ( " +
                BfwContract.VendorLand._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.VendorLand.COLUMN_PLOT_SIZE + " REAL NOT NULL, " +
                BfwContract.VendorLand.COLUMN_SERVER_ID + " INTEGER, " +
                BfwContract.VendorLand.COLUMN_VENDOR_ID + " INTEGER, " +
                BfwContract.VendorLand.COLUMN_LAND_ID + " TEXT, " +
                BfwContract.VendorLand.COLUMN_LAT_INFO + " REAL," +
                BfwContract.VendorLand.COLUMN_LNG_INFO + " REAL," +
                BfwContract.VendorLand.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.VendorLand.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.VendorLand.COLUMN_IS_UPDATE + " INTEGER, " +
                "FOREIGN KEY (" + BfwContract.VendorLand.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.VendorLand.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_BASELINE_VENDOR = "CREATE TABLE " + BfwContract.BaseLineVendor.TABLE_NAME + " ( " +
                BfwContract.BaseLineVendor._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG + " REAL," +
                BfwContract.BaseLineVendor.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.BaseLineVendor.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.BaseLineVendor.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.BaseLineVendor.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.BaseLineVendor.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.BaseLineVendor.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.BaseLineVendor.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FORECAST_VENDOR = "CREATE TABLE " + BfwContract.ForecastVendor.TABLE_NAME + " ( " +
                BfwContract.ForecastVendor._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT + " REAL," +
                BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT + " REAL," +
                BfwContract.ForecastVendor.COLUMN_YIELD_MT + " REAL," +
                BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE + " REAL," +
                BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE + " REAL," +
                BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT + " REAL," +
                BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP + " REAL," +
                BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP + " REAL," +
                BfwContract.ForecastVendor.COLUMN_FLOW_PRICE + " REAL," +
                BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE + " REAL," +
                BfwContract.ForecastVendor.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.ForecastVendor.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.ForecastVendor.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.ForecastVendor.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.ForecastVendor.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.ForecastVendor.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ForecastVendor.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FINANCEDATA_VENDOR = "CREATE TABLE " + BfwContract.FinanceDataVendor.TABLE_NAME + " ( " +
                BfwContract.FinanceDataVendor._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT + " REAL," +
                BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING + " REAL," +
                BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE + " REAL," +
                BfwContract.FinanceDataVendor.COLUMN_DURATION + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER + " TEXT," +
                BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.FinanceDataVendor.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_COOPS = "CREATE TABLE " + BfwContract.Coops.TABLE_NAME + " ( " +
                BfwContract.Coops._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.Coops.COLUMN_COOP_NAME + " TEXT NOT NULL, " +
                BfwContract.Coops.COLUMN_ADDRESS + " TEXT," +
                BfwContract.Coops.COLUMN_PHONE + " TEXT," +
                BfwContract.Coops.COLUMN_EMAIL + " TEXT," +
                BfwContract.Coops.COLUMN_CHAIR_NAME + " TEXT," +
                BfwContract.Coops.COLUMN_CHAIR_GENDER + " TEXT," +
                BfwContract.Coops.COLUMN_CHAIR_CELL + " TEXT," +
                BfwContract.Coops.COLUMN_VICECHAIR_NAME + " TEXT," +
                BfwContract.Coops.COLUMN_VICECHAIR_GENDER + " TEXT," +
                BfwContract.Coops.COLUMN_VICECHAIR_CELL + " TEXT," +
                BfwContract.Coops.COLUMN_SECRETARY_NAME + " TEXT," +
                BfwContract.Coops.COLUMN_SECRETARY_GENDER + " TEXT," +
                BfwContract.Coops.COLUMN_SECRETARY_CELL + " TEXT," +

                BfwContract.Coops.COLUMN_RCA_REGISTRATION + " TEXT," +
                BfwContract.Coops.COLUMN_LAND_SIZE_CIP + " REAL," +
                BfwContract.Coops.COLUMN_LAND_SIZE_CIP2 + " REAL," +

                BfwContract.Coops.COLUMN_OFFICE_SPACE + " INTEGER," +
                BfwContract.Coops.COLUMN_MOISTURE_METER + " INTEGER," +
                BfwContract.Coops.COLUMN_WEIGHTNING_SCALES + " INTEGER," +
                BfwContract.Coops.COLUMN_QUALITY_INPUT + " INTEGER," +
                BfwContract.Coops.COLUMN_TRACTORS + " INTEGER," +
                BfwContract.Coops.COLUMN_HARVESTER + " INTEGER," +
                BfwContract.Coops.COLUMN_DRYER + " INTEGER," +
                BfwContract.Coops.COLUMN_THRESHER + " INTEGER," +
                BfwContract.Coops.COLUMN_SAFE_STORAGE + " INTEGER," +
                BfwContract.Coops.COLUMN_OTHER + " INTEGER," +
                BfwContract.Coops.COLUMN_MALE_COOP + " INTEGER," +
                BfwContract.Coops.COLUMN_FEMALE_COOP + " INTEGER," +
                BfwContract.Coops.COLUMN_MEMBER + " INTEGER," +
                BfwContract.Coops.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.Coops.COLUMN_IS_UPDATE + " INTEGER," +
                BfwContract.Coops.COLUMN_STORAGE_DETAILS + " TEXT," +
                BfwContract.Coops.COLUMN_OTHER_DETAILS + " TEXT," +
                BfwContract.Coops.COLUMN_COOP_SERVER_ID + " INTEGER" +
                " ); ";

        final String CREATE_TABLE_COOP_INFO = "CREATE TABLE " + BfwContract.CoopInfo.TABLE_NAME + " ( " +
                BfwContract.CoopInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.CoopInfo.COLUMN_AGRI_EXTENSION + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_CLIMATE_INFO + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_SEEDS + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_ORGANIC_FERT + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_INORGANIC_FERT + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_LABOUR + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_WATER_PUMPS + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_SPREADER + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.CoopInfo.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.CoopInfo.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.CoopInfo.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                " ); ";

        final String CREATE_TABLE_SALE_COOP = "CREATE TABLE " + BfwContract.SalesCoop.TABLE_NAME + " ( " +
                BfwContract.SalesCoop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.SalesCoop.COLUMN_RGCC + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_PRODEV + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_EAX + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_SAKURA + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_AIF + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_NONE + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_OTHER + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_CONTRACT_VOLUME + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_COOP_GRADE + " TEXT," +
                BfwContract.SalesCoop.COLUMN_FLOOR_GRADE + " TEXT," +
                BfwContract.SalesCoop.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.SalesCoop.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.SalesCoop.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SalesCoop.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                " );";

        final String CREATE_TABLE_YIELD_COOP = "CREATE TABLE " + BfwContract.YieldCoop.TABLE_NAME + " ( " +
                BfwContract.YieldCoop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.YieldCoop.COLUMN_BEAN + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_MAIZE + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_SOY + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_OTHER + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.YieldCoop.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.YieldCoop.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.YieldCoop.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_BASELINE_SALE_COOP = "CREATE TABLE " + BfwContract.BaselineSalesCoop.TABLE_NAME + " ( " +
                BfwContract.BaselineSalesCoop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST_PRICE + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE_COST + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_BUYER_CONTRACT + " TEXT," +
                BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_RGCC + " REAL," +
                BfwContract.BaselineSalesCoop.COLUMN_CONTRACT_VOLUME + " REAL," +
                BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_OUTSIDE_RGCC + " REAL," +
                BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC + " REAL," +
                BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_RGCC + " REAL," +
                BfwContract.BaselineSalesCoop.COLUMN_RGCC_BUYER_FORMAL + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_RGCC_INFORMAL + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_BUYER_OTHER + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.BaselineSalesCoop.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.BaselineSalesCoop.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_FINANCE_INFO_COOP = "CREATE TABLE " + BfwContract.FinanceInfoCoop.TABLE_NAME + " ( " +
                BfwContract.FinanceInfoCoop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN + " TEXT," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_BANK + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_SACCO + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_OTHER + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_AMOUNT + " REAL," +
                BfwContract.FinanceInfoCoop.COLUMN_CYCLE_INTEREST_RATE + " REAL," +
                BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DURATION + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_LABOUR + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_MACHINERY + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_SEEDS + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_INPUT + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_OTHER + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DISB_METHOD + " TEXT," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN + " TEXT," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_AMOUNT + " REAL," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_INTEREST_RATE + " REAL," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DURATION + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.FinanceInfoCoop.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.FinanceInfoCoop.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_EXPECTED_YIELD = "CREATE TABLE " + BfwContract.ExpectedYieldCoop.TABLE_NAME + " ( " +
                BfwContract.ExpectedYieldCoop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.ExpectedYieldCoop.COLUMN_COOP_LAND_SIZE + " REAL," +
                BfwContract.ExpectedYieldCoop.COLUMN_COOP_PRODUCTION + " REAL," +
                BfwContract.ExpectedYieldCoop.COLUMN_PRODUCTION_MT + " REAL," +
                BfwContract.ExpectedYieldCoop.COLUMN_SEASON_ID + " INTEGER," +
                BfwContract.ExpectedYieldCoop.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.ExpectedYieldCoop.COLUMN_SERVER_ID + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.ExpectedYieldCoop.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ExpectedYieldCoop.COLUMN_SEASON_ID + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_PRODUCT_TEMPLATE = "CREATE TABLE " + BfwContract.ProductTemplate.TABLE_NAME + " ( " +
                BfwContract.ProductTemplate._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME + " TEXT," +
                BfwContract.ProductTemplate.COLUMN_HARVEST_GRADE + " TEXT," +
                BfwContract.ProductTemplate.COLUMN_STATE + " TEXT," +
                BfwContract.ProductTemplate.COLUMN_VENDOR_QTY + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_PRICE + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_BUYER_ID + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_TYPE + " TEXT," +
                BfwContract.ProductTemplate.COLUMN_FARMER_REG_ID + " TEXT," +
                BfwContract.ProductTemplate.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.ProductTemplate.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductTemplate.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductTemplate.COLUMN_BUYER_ID + ") REFERENCES " +
                BfwContract.Buyer.TABLE_NAME + " (" + BfwContract.Buyer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductTemplate.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")" +
                ");";

        final String CREATE_TABLE_PRODUCT_PRODUCT = "CREATE TABLE " + BfwContract.ProductProduct.TABLE_NAME + " ( " +
                BfwContract.ProductProduct._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.ProductProduct.COLUMN_HARVEST_SEASON + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_PRODUCT_NAME + " TEXT," +
                BfwContract.ProductProduct.COLUMN_HARVEST_GRADE + "TEXT," +
                BfwContract.ProductProduct.COLUMN_STATE + " TEXT," +
                BfwContract.ProductProduct.COLUMN_PRICE + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_BUYER_ID + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_VENDOR_QTY + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_FARMER_REG_ID + " TEXT," +
                BfwContract.ProductProduct.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.ProductProduct.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.ProductProduct.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductProduct.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductProduct.COLUMN_BUYER_ID + ") REFERENCES " +
                BfwContract.Buyer.TABLE_NAME + " (" + BfwContract.Buyer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.ProductProduct.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")" +
                ");";

        final String CREATE_TABLE_SALE_ORDER = "CREATE TABLE " + BfwContract.SaleOrder.TABLE_NAME + " ( " +
                BfwContract.SaleOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.SaleOrder.COLUMN_HARVEST_SEASON + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_STATE + " TEXT, " +
                BfwContract.SaleOrder.COLUMN_NAME + " TEXT," +
                BfwContract.SaleOrder.COLUMN_USER_ID + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_AVG_PRICE + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_ORDERED_QTY + " REAL, " +
                BfwContract.SaleOrder.COLUMN_DELIVERED_QTY + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_COOP_AGENT_ID + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_PAYMENT_TERM + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_IS_UPDATE + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_BUYER_ID + " INTEGER," +
                BfwContract.SaleOrder.COLUMN_BALANCE_TO_SHIP + " REAL, " +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_BUYER_ID + ") REFERENCES " +
                BfwContract.Buyer.TABLE_NAME + " (" + BfwContract.Buyer._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_COOP_AGENT_ID + ") REFERENCES " +
                BfwContract.CoopAgent.TABLE_NAME + " (" + BfwContract.CoopAgent._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_PAYMENT_TERM + ") REFERENCES " +
                BfwContract.PaymentTerm.TABLE_NAME + " (" + BfwContract.PaymentTerm._ID + ")" +
                ");";

        final String CREATE_TABLE_SALE_ORDER_LINE = "CREATE TABLE " + BfwContract.SaleOrderLine.TABLE_NAME + " ( " +
                BfwContract.SaleOrderLine._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.SaleOrderLine.COLUMN_DELIVER_QTY + " REAL," +
                BfwContract.SaleOrderLine.COLUMN_TO_INVOICE + " INTEGER," +
                BfwContract.SaleOrderLine.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.SaleOrderLine.COLUMN_NAME + " TEXT," +
                BfwContract.SaleOrderLine.COLUMN_ORDER_ID + " INTEGER," +
                BfwContract.SaleOrderLine.COLUMN_PRODUCT_ID + " INTEGER," +
                BfwContract.SaleOrderLine.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.SaleOrderLine.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.SaleOrderLine.COLUMN_ORDER_ID + ") REFERENCES " +
                BfwContract.SaleOrder.TABLE_NAME + " (" + BfwContract.SaleOrder._ID + ")," +
                "FOREIGN KEY (" + BfwContract.SaleOrderLine.COLUMN_PRODUCT_ID + ") REFERENCES " +
                BfwContract.ProductProduct.TABLE_NAME + " (" + BfwContract.ProductProduct._ID + ")" +
                ");";

        final String CREATE_TABLE_PURCHASE_ORDER = "CREATE TABLE " + BfwContract.PurchaseOrder.TABLE_NAME + " ( " +
                BfwContract.PurchaseOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.PurchaseOrder.COLUMN_PRICE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_NAME + " TEXT," +
                BfwContract.PurchaseOrder.COLUMN_QUANTITY + " REAL, " +
                BfwContract.PurchaseOrder.COLUMN_AMOUNT_DUE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_BALANCE_TO_RECEIVE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_USER_ID + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_HARVEST_SEASON + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_STATE + " TEXT, " +
                BfwContract.PurchaseOrder.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_AGENT_ID + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.PurchaseOrder.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrder.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrder.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrder.COLUMN_AGENT_ID + ") REFERENCES " +
                BfwContract.CoopAgent.TABLE_NAME + " (" + BfwContract.CoopAgent._ID + ")," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrder.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")" +
                ");";

        final String CREATE_TABLE_PURCHASE_ORDER_LINE = "CREATE TABLE " + BfwContract.PurchaseOrderLine.TABLE_NAME + " ( " +
                BfwContract.PurchaseOrderLine._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.PurchaseOrderLine.COLUMN_NAME + " TEXT," +
                BfwContract.PurchaseOrderLine.COLUMN_ORDER_ID + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_SUB_TOTAL + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_QTY + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_PRICE_UNIT + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_PRICE_TOTAL + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_PRODUCT_ID + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.PurchaseOrderLine.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrderLine.COLUMN_ORDER_ID + ") REFERENCES " +
                BfwContract.PurchaseOrder.TABLE_NAME + " (" + BfwContract.PurchaseOrder._ID + ")," +
                "FOREIGN KEY (" + BfwContract.PurchaseOrderLine.COLUMN_PRODUCT_ID + ") REFERENCES " +
                BfwContract.ProductProduct.TABLE_NAME + " (" + BfwContract.ProductProduct._ID + ")" +
                ");";

        final String CREATE_TABLE_LOAN = "CREATE TABLE " + BfwContract.Loan.TABLE_NAME + " ( " +
                BfwContract.Loan._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.Loan.COLUMN_NAME + " TEXT," +
                BfwContract.Loan.COLUMN_COOP_ID + " INTEGER," +
                BfwContract.Loan.COLUMN_FARMER_ID + " INTEGER," +
                BfwContract.Loan.COLUMN_VENDOR_ID + " INTEGER," +
                BfwContract.Loan.COLUMN_START_DATE + " INTEGER," +
                BfwContract.Loan.COLUMN_AMOUNT + " REAL," +
                BfwContract.Loan.COLUMN_INTEREST_RATE + " REAL," +
                BfwContract.Loan.COLUMN_DURATION + " REAL," +
                BfwContract.Loan.COLUMN_PURPOSE + " TEXT," +
                BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION + " TEXT," +
                BfwContract.Loan.COLUMN_AMOUNT_DUE + " INTEGER," +
                BfwContract.Loan.COLUMN_AMOUNT_TOTAL + " INTEGER," +
                BfwContract.Loan.COLUMN_STATE + " INTEGER," +
                BfwContract.Loan.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.Loan.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.Loan.COLUMN_IS_UPDATE + " INTEGER," +
                "FOREIGN KEY (" + BfwContract.Loan.COLUMN_VENDOR_ID + ") REFERENCES " +
                BfwContract.Vendor.TABLE_NAME + " (" + BfwContract.Vendor._ID + ")," +
                "FOREIGN KEY (" + BfwContract.Loan.COLUMN_COOP_ID + ") REFERENCES " +
                BfwContract.Coops.TABLE_NAME + " (" + BfwContract.Coops._ID + ")," +
                "FOREIGN KEY (" + BfwContract.Loan.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")" +
                ");";

        final String CREATE_TABLE_LOAN_LINE = "CREATE TABLE " + BfwContract.LoanLine.TABLE_NAME + " ( " +
                BfwContract.LoanLine._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.LoanLine.COLUMN_PAYMENT_DATE + " INTEGER," +
                BfwContract.LoanLine.COLUMN_INTEREST + " REAL," +
                BfwContract.LoanLine.COLUMN_PRINCIPAL + " REAL," +
                BfwContract.LoanLine.COLUMN_NEXT_PAYMENT_AMOUNT + " REAL," +
                BfwContract.LoanLine.COLUMN_REMAINING_AMOUNT + " REAL," +
                BfwContract.LoanLine.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.LoanLine.COLUMN_LOAN_ID + " INTEGER," +
                BfwContract.LoanLine.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.LoanLine.COLUMN_IS_UPDATE + " INTEGER" +
                ");";

        final String CREATE_TABLE_LOAN_PAYMENT = "CREATE TABLE " + BfwContract.LoanPayment.TABLE_NAME + " ( " +
                BfwContract.LoanPayment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.LoanPayment.COLUMN_PAYMENT_DATE + " INTEGER," +
                BfwContract.LoanPayment.COLUMN_AMOUNT + " INTEGER," +
                BfwContract.LoanPayment.COLUMN_SERVER_ID + " INTEGER," +
                BfwContract.LoanPayment.COLUMN_LOAN_ID + " INTEGER," +
                BfwContract.LoanPayment.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.LoanPayment.COLUMN_IS_UPDATE + " INTEGER" +
                ");";

        final String CREATE_TABLE_PAYMENT_TERM = "CREATE TABLE " + BfwContract.PaymentTerm.TABLE_NAME + " ( " +
                BfwContract.PaymentTerm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BfwContract.PaymentTerm.COLUMN_NAME + " TEXT," +
                BfwContract.PaymentTerm.COLUMN_NOTE + " TEXT," +
                BfwContract.PaymentTerm.COLUMN_ACTIVE + " INTEGER," +
                BfwContract.PaymentTerm.COLUMN_IS_SYNC + " INTEGER," +
                BfwContract.PaymentTerm.COLUMN_IS_UPDATE + " INTEGER," +
                BfwContract.PaymentTerm.COLUMN_SERVER_ID + " INTEGER" +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_BUYER);
        sqLiteDatabase.execSQL(CREATE_TABLE_COOP_AGENT);
        sqLiteDatabase.execSQL(CREATE_TABLE_HARVEST_SEASON);

        sqLiteDatabase.execSQL(CREATE_TABLE_COOPS);
        sqLiteDatabase.execSQL(CREATE_TABLE_COOP_INFO);
        sqLiteDatabase.execSQL(CREATE_TABLE_SALE_COOP);
        sqLiteDatabase.execSQL(CREATE_TABLE_YIELD_COOP);
        sqLiteDatabase.execSQL(CREATE_TABLE_BASELINE_SALE_COOP);
        sqLiteDatabase.execSQL(CREATE_TABLE_FINANCE_INFO_COOP);
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPECTED_YIELD);

        sqLiteDatabase.execSQL(CREATE_TABLE_FARMER);
        sqLiteDatabase.execSQL(CREATE_TABLE_LAND_PLOT);
        sqLiteDatabase.execSQL(CREATE_TABLE_FARMER_ACCESS_INFO);
        sqLiteDatabase.execSQL(CREATE_TABLE_BASELINE_FARMER);
        sqLiteDatabase.execSQL(CREATE_TABLE_FORECAST_FARMER);
        sqLiteDatabase.execSQL(CREATE_TABLE_FINANCEDATA_FARMER);

        sqLiteDatabase.execSQL(CREATE_TABLE_VENDOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_VENDOR_ACCESS_INFO);
        sqLiteDatabase.execSQL(CREATE_TABLE_VENDOR_LAND);
        sqLiteDatabase.execSQL(CREATE_TABLE_BASELINE_VENDOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_FINANCEDATA_VENDOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_FORECAST_VENDOR);

        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT_PRODUCT);
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT_TEMPLATE);
        sqLiteDatabase.execSQL(CREATE_TABLE_PAYMENT_TERM);
        sqLiteDatabase.execSQL(CREATE_TABLE_SALE_ORDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_SALE_ORDER_LINE);
        sqLiteDatabase.execSQL(CREATE_TABLE_PURCHASE_ORDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_PURCHASE_ORDER_LINE);

        sqLiteDatabase.execSQL(CREATE_TABLE_LOAN);
        sqLiteDatabase.execSQL(CREATE_TABLE_LOAN_LINE);
        sqLiteDatabase.execSQL(CREATE_TABLE_LOAN_PAYMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
