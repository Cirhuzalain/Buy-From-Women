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
    static final int COOPS_DETAILS = 113;

    static final int USERS = 101;
    static final int FARMER = 102;
    static final int FARMER_DETAILS = 103;
    static final int LAND_PLOT = 104;
    static final int FARMER_BY_LAND_PLOT = 105;
    static final int HARVEST_SEASON = 106;
    static final int BANK_INFO = 107;
    static final int FARMER_BY_BANK_INFO = 108;
    static final int SALE_ORDER = 109;
    static final int PURCHASE_ORDER = 110;
    static final int SALE_ORDER_BY_HARVEST_SEASON = 111;
    static final int PURCHASE_ORDER_HARVEST_SEASON = 112;

    private static final SQLiteQueryBuilder farmerLandPlot;
    private static final SQLiteQueryBuilder farmerBankInfo;
    private static final SQLiteQueryBuilder saleHarvestSeason;
    private static final SQLiteQueryBuilder purchaseHarvestSeason;

    static {
        farmerLandPlot = new SQLiteQueryBuilder();
        farmerBankInfo = new SQLiteQueryBuilder();
        saleHarvestSeason = new SQLiteQueryBuilder();
        purchaseHarvestSeason = new SQLiteQueryBuilder();

        farmerLandPlot.setTables(
                BfwContract.Farmer.TABLE_NAME +
                        " INNER JOIN " + BfwContract.LandPlot.TABLE_NAME +
                        " ON " + BfwContract.LandPlot.TABLE_NAME +
                        "." + BfwContract.LandPlot.COLUMN_FARMER_ID +
                        " = " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID
        );

        farmerBankInfo.setTables(
                BfwContract.BankInfo.TABLE_NAME +
                        " INNER JOIN " + BfwContract.Farmer.TABLE_NAME +
                        " ON " + BfwContract.Farmer.TABLE_NAME +
                        "." + BfwContract.Farmer._ID +
                        " = " + BfwContract.BankInfo.TABLE_NAME +
                        "." + BfwContract.BankInfo.COLUMN_FARMER_ID
        );

        saleHarvestSeason.setTables(
                BfwContract.SaleOrder.TABLE_NAME +
                        " INNER JOIN " + BfwContract.HarvestSeason.TABLE_NAME +
                        " ON " + BfwContract.SaleOrder.TABLE_NAME +
                        "." + BfwContract.SaleOrder.COLUMN_HARVEST_SEASON +
                        " = " + BfwContract.HarvestSeason.TABLE_NAME +
                        "." + BfwContract.HarvestSeason._ID
        );

        purchaseHarvestSeason.setTables(
                BfwContract.PurchaseOrder.TABLE_NAME +
                        " INNER JOIN " + BfwContract.HarvestSeason.TABLE_NAME +
                        " ON " + BfwContract.PurchaseOrder.TABLE_NAME +
                        "." + BfwContract.PurchaseOrder.COLUMN_HARVEST_SEASON +
                        " = " + BfwContract.HarvestSeason.TABLE_NAME +
                        "." + BfwContract.HarvestSeason._ID
        );
    }

    public BfwProvider() {

    }

    private UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = BfwContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, BfwContract.PATH_COOPS, COOPS);
        matcher.addURI(authority, BfwContract.PATH_USER, USERS);
        matcher.addURI(authority, BfwContract.PATH_FARMER, FARMER);

        matcher.addURI(authority, BfwContract.PATH_FARMER + "/#", FARMER_DETAILS);
        matcher.addURI(authority, BfwContract.PATH_LAND_PLOT, LAND_PLOT);
        matcher.addURI(authority, BfwContract.PATH_LAND_PLOT + "/#", FARMER_BY_LAND_PLOT);
        matcher.addURI(authority, BfwContract.PATH_HARVEST_SEASON, HARVEST_SEASON);
        matcher.addURI(authority, BfwContract.PATH_BANK_INFO, BANK_INFO);
        matcher.addURI(authority, BfwContract.PATH_BANK_INFO + "/#", FARMER_BY_BANK_INFO);
        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER, SALE_ORDER);
        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER, PURCHASE_ORDER);
        matcher.addURI(authority, BfwContract.PATH_PURCHASE_ORDER + "/#", PURCHASE_ORDER_HARVEST_SEASON);
        matcher.addURI(authority, BfwContract.PATH_SALE_ORDER + "/#", SALE_ORDER_BY_HARVEST_SEASON);

        matcher.addURI(authority, BfwContract.PATH_COOPS + "/#",    COOPS_DETAILS);


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
            case COOPS_DETAILS :
                return BfwContract.Coops.CONTENT_ITEM_TYPE;
            case USERS:
                return BfwContract.UserEntry.CONTENT_TYPE;
            case FARMER:
                return BfwContract.Farmer.CONTENT_TYPE;
            case FARMER_DETAILS:
                return BfwContract.Farmer.CONTENT_ITEM_TYPE;
            case LAND_PLOT:
                return BfwContract.LandPlot.CONTENT_TYPE;
            case FARMER_BY_LAND_PLOT:
                return BfwContract.LandPlot.CONTENT_TYPE;
            case HARVEST_SEASON:
                return BfwContract.HarvestSeason.CONTENT_TYPE;
            case BANK_INFO:
                return BfwContract.BankInfo.CONTENT_TYPE;
            case FARMER_BY_BANK_INFO:
                return BfwContract.BankInfo.CONTENT_ITEM_TYPE;
            case SALE_ORDER:
                return BfwContract.SaleOrder.CONTENT_TYPE;
            case SALE_ORDER_BY_HARVEST_SEASON:
                return BfwContract.SaleOrder.CONTENT_TYPE;
            case PURCHASE_ORDER:
                return BfwContract.PurchaseOrder.CONTENT_TYPE;
            case PURCHASE_ORDER_HARVEST_SEASON:
                return BfwContract.PurchaseOrder.CONTENT_TYPE;
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
            case USERS:
                id = db.insert(BfwContract.UserEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.UserEntry.buildUserUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table user " + uri);
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
            case LAND_PLOT:
                id = db.insert(BfwContract.LandPlot.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.LandPlot.buildLandPlotUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table land plot " + uri);
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
            case BANK_INFO:
                id = db.insert(BfwContract.BankInfo.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.BankInfo.buildBankInfoUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table bank info " + uri);
                }
                break;
            case SALE_ORDER:
                id = db.insert(BfwContract.SaleOrder.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = BfwContract.SaleOrder.buildSaleOrderUri(id);
                } else {
                    throw new UnsupportedOperationException("Uri not supported table sale order" + uri);
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
            case USERS:
                returnCursor = db.query(BfwContract.UserEntry.TABLE_NAME,
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
            case BANK_INFO:
                returnCursor = db.query(BfwContract.BankInfo.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FARMER_BY_BANK_INFO:
                returnCursor = farmerBankInfo.query(mBfwDbHelper.getReadableDatabase(),
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
            case SALE_ORDER_BY_HARVEST_SEASON:
                returnCursor = saleHarvestSeason.query(mBfwDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PURCHASE_ORDER_HARVEST_SEASON:
                returnCursor = purchaseHarvestSeason.query(mBfwDbHelper.getReadableDatabase(),
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
            case LAND_PLOT:
                updateRow = db.update(BfwContract.LandPlot.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case BANK_INFO:
                updateRow = db.update(BfwContract.BankInfo.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case USERS:
                updateRow = db.update(BfwContract.UserEntry.TABLE_NAME, contentValues,
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
        int updateRow = 0;

        switch (mUriMatcher.match(uri)) {
            case FARMER:
                updateRow = db.delete(BfwContract.Farmer.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case LAND_PLOT:
                updateRow = db.delete(BfwContract.LandPlot.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case BANK_INFO:
                updateRow = db.delete(BfwContract.BankInfo.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri");
        }
        if (updateRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateRow;
    }
}
