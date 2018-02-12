package com.nijus.alino.bfwcoopmanagement.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BfwDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "buyfrowomen.db";

    public BfwDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String CREATE_TABLE_COOPS = "CREATE TABLE " + BfwContract.Coops.TABLE_NAME + " ( " +
                BfwContract.Coops._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.Coops.COLUMN_COOP_NAME + " TEXT NOT NULL, " +
                BfwContract.Coops.COLUMN_COOP_SERVER_ID + " INTEGER NOT NULL " +
                " ); ";

        final String CREATE_TABLE_USER_ENTRY = "CREATE TABLE " + BfwContract.UserEntry.TABLE_NAME + " ( " +
                BfwContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                BfwContract.UserEntry.COLUMN_USER_SERVER_ID + " INTEGER NOT NULL, " +
                BfwContract.UserEntry.COLUMN_IS_LOGIN_USER + " INTEGER NOT NULL " +
                " ); ";

        final String CREATE_TABLE_FARMER = "CREATE TABLE " + BfwContract.Farmer.TABLE_NAME + " ( " +
                BfwContract.Farmer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.Farmer.COLUMN_NAME + " TEXT NOT NULL, " +
                BfwContract.Farmer.COLUMN_GENDER + " TEXT NOT NULL, " +
                BfwContract.Farmer.COLUMN_PHONE + " TEXT NOT NULL, " +

                BfwContract.Farmer.COLUMN_ARABLE_LAND_PLOT + " REAL, " +
                BfwContract.Farmer.COLUMN_LAND_SIZE + " REAL, " +
                BfwContract.Farmer.COLUMN_TOT_PROD_F_KG + " REAL, " +
                BfwContract.Farmer.COLUMN_SALE_OUTSIDE_PPP + " INTEGER, " +
                BfwContract.Farmer.COLUMN_POST_HARVEST_KG + " REAL, " +
                BfwContract.Farmer.COLUMN_COOP_LAND_SIZE + " REAL, " +
                BfwContract.Farmer.COLUMN_PERCENT_FARMER_LAND_SIZE + " REAL, " +

                BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD + " INTEGER, " +
                BfwContract.Farmer.COLUMN_HOUSE_MEMBER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_FIRST_NAME + " TEXT, " +
                BfwContract.Farmer.COLUMN_LAST_NAME + " TEXT, " +
                BfwContract.Farmer.COLUMN_CELL_PHONE + " TEXT, " +
                BfwContract.Farmer.COLUMN_CELL_CARRIER + " TEXT, " +
                BfwContract.Farmer.COLUMN_MEMBER_SHIP + " TEXT, " +

                BfwContract.Farmer.COLUMN_TOT_PROD_B_KG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_LOST_KG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_SOLD_KG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_COOP + " INTEGER, " +
                BfwContract.Farmer.COLUMN_PRICE_SOLD_COOP_PER_KG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_IN_KG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_PRICE_SOLD_KG + " INTEGER, " +

                BfwContract.Farmer.COLUMN_OUTSANDING_LOAN + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_LOAN_AMOUNT + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TOT_OUTSTANDING + " INTEGER, " +
                BfwContract.Farmer.COLUMN_INTEREST_RATE + " REAL,  " +
                BfwContract.Farmer.COLUMN_DURATION + " INTEGER, " +
                BfwContract.Farmer.COLUMN_LOAN_PROVIDER + " TEXT, " +
                BfwContract.Farmer.COLUMN_MOBILE_MONEY_ACCOUNT + " INTEGER, " +
                BfwContract.Farmer.COLUMN_LP_AGGREG + " INTEGER, " +
                BfwContract.Farmer.COLUMN_LP_INPUT + " INTEGER, " +
                BfwContract.Farmer.COLUMN_LP_OTHER + " INTEGER, " +

                BfwContract.Farmer.COLUMN_AGRI_EXTENSION_SERV + " INTEGER,  " +
                BfwContract.Farmer.COLUMN_CLIMATE_RELATED_INFO + " INTEGER, " +
                BfwContract.Farmer.COLUMN_SEEDS + " INTEGER, " +
                BfwContract.Farmer.COLUMN_ORGANIC_FERTILIZER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_INORGANIC_FERTILIZER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_LABOUR + " INTEGER, " +
                BfwContract.Farmer.COLUMN_WATER_PUMPS + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TRACTORS + " INTEGER, " +
                BfwContract.Farmer.COLUMN_HARVESTER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_SPRAYERS + " INTEGER, " +
                BfwContract.Farmer.COLUMN_DRYER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_TRESHER + " INTEGER, " +
                BfwContract.Farmer.COLUMN_SAFE_STORAGE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_OTHER_INFO + " INTEGER, " +
                BfwContract.Farmer.COLUMN_DAM + " INTEGER, " +
                BfwContract.Farmer.COLUMN_WELL + " INTEGER, " +
                BfwContract.Farmer.COLUMN_BORHOLE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_PIPE_BORNE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_RIVER_STREAM + " INTEGER, " +
                BfwContract.Farmer.COLUMN_IRRIGATION + " INTEGER, " +
                BfwContract.Farmer.COLUMN_NONE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_OTHER + " INTEGER, " +

                BfwContract.Farmer.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.Farmer.COLUMN_IS_UPDATE + " INTEGER, " +
                BfwContract.Farmer.COLUMN_FARMER_SERVER_ID + " INTEGER, " +
                BfwContract.Farmer.COLUMN_COOP_USER_ID + " INTEGER " +
                " ); ";

        final String CREATE_TABLE_LAND_PLOT = "CREATE TABLE " + BfwContract.LandPlot.TABLE_NAME + " ( " +
                BfwContract.LandPlot._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.LandPlot.COLUMN_PLOT_SIZE + " REAL NOT NULL, " +
                BfwContract.LandPlot.COLUMN_SERVER_ID + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_FARMER_ID + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_LAND_ID + " TEXT, " +
                BfwContract.LandPlot.COLUMN_IS_SYNC + " INTEGER, " +
                BfwContract.LandPlot.COLUMN_IS_UPDATE + " INTEGER, " +
                "FOREIGN KEY (" + BfwContract.LandPlot.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")" +
                ");";

        final String CREATE_TABLE_HARVEST_SEASON = "CREATE TABLE " + BfwContract.HarvestSeason.TABLE_NAME + " ( " +
                BfwContract.HarvestSeason._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.HarvestSeason.COLUMN_NAME + " TEXT, " +
                BfwContract.HarvestSeason.COLUMN_ACTIVE + " INTEGER NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_START_DATE + " TEXT NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_END_DATE + " TEXT NOT NULL, " +
                BfwContract.HarvestSeason.COLUMN_SERVER_ID + " INTEGER NOT NULL " +
                ");";

        final String CREATE_TABLE_BANK_INFO = "CREATE TABLE " + BfwContract.BankInfo.TABLE_NAME + " ( " +
                BfwContract.BankInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.BankInfo.COLUMN_ACCOUNT_NUMBER + " TEXT, " +
                BfwContract.BankInfo.COLUMN_SERVER_ID + " INTEGER, " +
                BfwContract.BankInfo.COLUMN_BANK_NAME + " TEXT, " +
                BfwContract.BankInfo.COLUMN_FARMER_ID + " INTEGER, " +
                BfwContract.BankInfo.COLUMN_IS_SYNC + " INTEGER NOT NULL, " +
                BfwContract.BankInfo.COLUMN_IS_UPDATE + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + BfwContract.BankInfo.COLUMN_FARMER_ID + ") REFERENCES " +
                BfwContract.Farmer.TABLE_NAME + " (" + BfwContract.Farmer._ID + ")" +
                ");";

        final String CREATE_TABLE_SALE_ORDER = "CREATE TABLE " + BfwContract.SaleOrder.TABLE_NAME + " ( " +
                BfwContract.SaleOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.SaleOrder.COLUMN_HARVEST_SEASON + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_STATE + " TEXT, " +
                BfwContract.SaleOrder.COLUMN_USER_ID + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_AVG_PRICE + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_ORDERED_QTY + " REAL, " +
                BfwContract.SaleOrder.COLUMN_DELIVERED_QTY + " INTEGER, " +
                BfwContract.SaleOrder.COLUMN_BALANCE_TO_SHIP + " REAL, " +
                "FOREIGN KEY (" + BfwContract.SaleOrder.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        final String CREATE_TABLE_PURCHASE_ORDER = "CREATE TABLE " + BfwContract.PurchaseOrder.TABLE_NAME + " ( " +
                BfwContract.PurchaseOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BfwContract.PurchaseOrder.COLUMN_PRICE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_QUANTITY + " REAL, " +
                BfwContract.PurchaseOrder.COLUMN_AMOUNT_DUE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_BALANCE_TO_RECEIVE + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_USER_ID + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_HARVEST_SEASON + " INTEGER, " +
                BfwContract.PurchaseOrder.COLUMN_STATE + " TEXT, " +
                "FOREIGN KEY (" + BfwContract.PurchaseOrder.COLUMN_HARVEST_SEASON + ") REFERENCES " +
                BfwContract.HarvestSeason.TABLE_NAME + " (" + BfwContract.HarvestSeason._ID + ")" +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_COOPS);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_ENTRY);
        sqLiteDatabase.execSQL(CREATE_TABLE_FARMER);
        sqLiteDatabase.execSQL(CREATE_TABLE_LAND_PLOT);
        sqLiteDatabase.execSQL(CREATE_TABLE_HARVEST_SEASON);
        sqLiteDatabase.execSQL(CREATE_TABLE_BANK_INFO);
        sqLiteDatabase.execSQL(CREATE_TABLE_SALE_ORDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_PURCHASE_ORDER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
