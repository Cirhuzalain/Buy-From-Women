package com.nijus.alino.bfwcoopmanagement.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class BfwContract {

    public static final String CONTENT_AUTHORITY = "com.nijus.alino.bfwcoopmanagement";
    public static final Uri BASE_CONTENT = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_USER = "users";
    public static final String PATH_FARMER = "farmers";
    public static final String PATH_COOPS = "coops";
    public static final String PATH_LAND_PLOT = "land_plot";
    public static final String PATH_HARVEST_SEASON = "harvest_season";
    public static final String PATH_PURCHASE_ORDER = "purchase_order";
    public static final String PATH_SALE_ORDER = "sale_order";
    public static final String PATH_BANK_INFO = "bank_info";


    public static final class Coops implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COOPS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOPS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOPS;

        public static final String TABLE_NAME = "coops";

        public static final String COLUMN_COOP_NAME = "coop_name";
        public static final String COLUMN_COOP_SERVER_ID = "coop_server_id";

        public static Uri buildCoopUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_USER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_USERNAME = "login";
        public static final String COLUMN_USER_SERVER_ID = "user_server_id";
        public static final String COLUMN_IS_LOGIN_USER = "is_login_user";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class Farmer implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FARMER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMER;

        public static final String TABLE_NAME = "farmers";

        public static final String COLUMN_NAME = "farmer_name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_GENDER = "gender";

        public static final String COLUMN_ARABLE_LAND_PLOT = "arable_land_plot";
        public static final String COLUMN_LAND_SIZE = "land_size";
        public static final String COLUMN_TOT_PROD_F_KG = "tot_prod_f";
        public static final String COLUMN_SALE_OUTSIDE_PPP = "sale_outside_ppp";
        public static final String COLUMN_POST_HARVEST_KG = "expected_post_harvest_kg";
        public static final String COLUMN_COOP_LAND_SIZE = "tot_coop_land_size";
        public static final String COLUMN_PERCENT_FARMER_LAND_SIZE = "percent_farmer_land_size";

        public static final String COLUMN_HOUSEHOLD_HEAD = "household_head";
        public static final String COLUMN_HOUSE_MEMBER = "num_household_member";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_CELL_PHONE = "cell_phone";
        public static final String COLUMN_CELL_CARRIER = "cell_carrier";
        public static final String COLUMN_MEMBER_SHIP = "membership_id";

        public static final String COLUMN_TOT_PROD_B_KG = "tot_prod_b";
        public static final String COLUMN_TOT_LOST_KG = "tot_lost_kg";
        public static final String COLUMN_TOT_SOLD_KG = "tot_sold_kg";
        public static final String COLUMN_TOT_VOL_SOLD_COOP = "tot_vol_sold_coop";
        public static final String COLUMN_PRICE_SOLD_COOP_PER_KG = "price_coop_kg";
        public static final String COLUMN_TOT_VOL_SOLD_IN_KG = "tot_vol_sold_kg";
        public static final String COLUMN_PRICE_SOLD_KG = "price_sold_kg";

        public static final String COLUMN_OUTSANDING_LOAN = "outstanding_loan";
        public static final String COLUMN_TOT_LOAN_AMOUNT = "tot_loan_amount";
        public static final String COLUMN_TOT_OUTSTANDING = "tot_outstanding";
        public static final String COLUMN_INTEREST_RATE = "interest_rate";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_LOAN_PROVIDER = "loan_provider";
        public static final String COLUMN_MOBILE_MONEY_ACCOUNT = "mobile_money_account";
        public static final String COLUMN_LP_INPUT = "lp_input";
        public static final String COLUMN_LP_AGGREG = "lp_aggregation";
        public static final String COLUMN_LP_OTHER = "lp_other";

        public static final String COLUMN_AGRI_EXTENSION_SERV = "extension_services";
        public static final String COLUMN_CLIMATE_RELATED_INFO = "climate_info";
        public static final String COLUMN_SEEDS = "seeds";
        public static final String COLUMN_ORGANIC_FERTILIZER = "organic_fertilizer";
        public static final String COLUMN_INORGANIC_FERTILIZER = "inorganic_fertilizer";
        public static final String COLUMN_LABOUR = "labour";
        public static final String COLUMN_WATER_PUMPS = "water_pumps";
        public static final String COLUMN_TRACTORS = "tractors";
        public static final String COLUMN_HARVESTER = "harvester";
        public static final String COLUMN_SPRAYERS = "sprayers";
        public static final String COLUMN_DRYER = "dryer";
        public static final String COLUMN_TRESHER = "thresher";
        public static final String COLUMN_SAFE_STORAGE = "safe_storage";
        public static final String COLUMN_OTHER_INFO = "other_info";
        public static final String COLUMN_DAM = "dam";
        public static final String COLUMN_WELL = "well";
        public static final String COLUMN_BORHOLE = "bore_hole";
        public static final String COLUMN_PIPE_BORNE = "pipe_borne";
        public static final String COLUMN_RIVER_STREAM = "river_stream";
        public static final String COLUMN_IRRIGATION = "irrigation";
        public static final String COLUMN_NONE = "none";
        public static final String COLUMN_OTHER = "others";

        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";
        public static final String COLUMN_FARMER_SERVER_ID = "farmer_server_id";
        public static final String COLUMN_COOP_USER_ID = "coop_user_id";

        public static Uri buildFarmerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class LandPlot implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_LAND_PLOT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LAND_PLOT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LAND_PLOT;

        public static final String TABLE_NAME = "land_plot";

        public static final String COLUMN_PLOT_SIZE = "plot_size";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_LAND_ID = "land_uniq_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildLandPlotUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class HarvestSeason implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_HARVEST_SEASON).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HARVEST_SEASON;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HARVEST_SEASON;

        public static final String TABLE_NAME = "harvest_season";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_ACTIVE = "active";

        public static Uri buildHarvestSeasonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class BankInfo implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_BANK_INFO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BANK_INFO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BANK_INFO;

        public static final String TABLE_NAME = "bank_info";

        public static final String COLUMN_ACCOUNT_NUMBER = "account_number";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_BANK_NAME = "bank_name";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildBankInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PurchaseOrder implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PURCHASE_ORDER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER;

        public static final String TABLE_NAME = "purchase_order";

        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_AMOUNT_DUE = "amount_due";
        public static final String COLUMN_BALANCE_TO_RECEIVE = "balance_to_receive";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_HARVEST_SEASON = "harvest_season";
        public static final String COLUMN_STATE = "state";

        public static Uri buildPurchaseOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SaleOrder implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SALE_ORDER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALE_ORDER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALE_ORDER;

        public static final String TABLE_NAME = "sale_order";

        public static final String COLUMN_HARVEST_SEASON = "harvest_season";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_AVG_PRICE = "avg_price";
        public static final String COLUMN_ORDERED_QTY = "ordered_qty";
        public static final String COLUMN_DELIVERED_QTY = "delivered_qty";
        public static final String COLUMN_BALANCE_TO_SHIP = "balance_to_ship";

        public static Uri buildSaleOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
