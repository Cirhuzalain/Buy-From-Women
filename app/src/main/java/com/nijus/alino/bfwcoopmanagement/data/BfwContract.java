package com.nijus.alino.bfwcoopmanagement.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class BfwContract {

    public static final String CONTENT_AUTHORITY = "com.nijus.alino.bfwcoopmanagement";
    public static final Uri BASE_CONTENT = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_FARMER = "farmers";
    public static final String PATH_COOPS = "coops";
    public static final String PATH_BUYER = "buyer";
    public static final String PATH_COOP_AGENT = "coop_agent";
    public static final String PATH_VENDOR = "vendor_farmer";

    public static final String PATH_COOP_INFO = "coop_access_info";
    public static final String PATH_SALES_COOP = "coop_forecast_sales";
    public static final String PATH_YIELD_COOP = "coop_baseline_yield";
    public static final String PATH_BASELINE_SALES_COOP = "coop_baseline_sales";
    public static final String PATH_FINANCEINFO_COOP = "coop_baseline_finance_info";
    public static final String PATH_EXPECTED_YIELD_COOP = "coop_expected_yield";

    public static final String PATH_FARMER_ACCESS_INFO = "access_info_farmer";
    public static final String PATH_BASELINE_FARMER = "baseline_farmer";
    public static final String PATH_FORECAST_FARMER = "forecast_farmer";
    public static final String PATH_FINANCEDATA_FARMER = "finance_data_farmer";
    public static final String PATH_LAND_PLOT = "land_plot";

    public static final String PATH_VENDOR_ACCESS_INFO = "access_info_vendor";
    public static final String PATH_VENDOR_LAND = "vendor_land";
    public static final String PATH_BASELINE_VENDOR = "baseline_vendor";
    public static final String PATH_FORECAST_VENDOR = "forecast_vendor";
    public static final String PATH_FINANCEDATA_VENDOR = "finance_data_vendor";

    public static final String PATH_HARVEST_SEASON = "harvest_season";
    public static final String PATH_PURCHASE_ORDER = "purchase_order";
    public static final String PATH_SALE_ORDER = "sale_order";
    public static final String PATH_SALE_ORDER_LINE = "sale_order_line";
    public static final String PATH_PURCHASE_ORDER_LINE = "purchase_order_line";

    public static final String PATH_PRODUCT = "product";
    public static final String PATH_PRODUCT_TEMPLATE = "product_template";
    public static final String PATH_LOAN = "path_loan";
    public static final String PATH_LOAN_LINE = "path_loan_line";
    public static final String PATH_LOAN_PAYMENT = "path_loan_payment";


    public static final class CoopAgent implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COOP_AGENT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOP_AGENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOP_AGENT;

        public static final String TABLE_NAME = "coop_agent";

        public static final String COLUMN_AGENT_NAME = "agent_name";
        public static final String COLUMN_AGENT_PHONE = "agent_phone";
        public static final String COLUMN_AGENT_EMAIL = "agent_email";
        public static final String COLUMN_USER_ID = "agent_user_id";
        public static final String COLUMN_COOP_ID = "agent_coop_id";
        public static final String COLUMN_AGENT_SERVER_ID = "agent_server_id";

        public static Uri buildAgentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class Buyer implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_BUYER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BUYER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BUYER;

        public static final String TABLE_NAME = "buyer";

        public static final String COLUMN_BUYER_NAME = "buyer_name";
        public static final String COLUMN_BUYER_PHONE = "buyer_phone";
        public static final String COLUMN_BUYER_EMAIL = "buyer_email";
        public static final String COLUMN_USER_ID = "buyer_user_id";
        public static final String COLUMN_BUYER_SERVER_ID = "buyer_server_id";

        public static Uri buildBuyerUri(long id) {
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
        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_HOUSEHOLD_HEAD = "household_head";
        public static final String COLUMN_HOUSE_MEMBER = "num_household_member";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_CELL_PHONE = "cell_phone";
        public static final String COLUMN_CELL_CARRIER = "cell_carrier";
        public static final String COLUMN_MEMBER_SHIP = "membership_id";

        public static final String COLUMN_TRACTORS = "tractors";
        public static final String COLUMN_HARVESTER = "harvester";
        public static final String COLUMN_DRYER = "dryer";
        public static final String COLUMN_TRESHER = "thresher";
        public static final String COLUMN_SAFE_STORAGE = "safe_storage";
        public static final String COLUMN_STORAGE_DETAIL = "storage_detail";
        public static final String COLUMN_OTHER_INFO = "other_info";
        public static final String COLUMN_NEW_SOURCE_DETAIL = "source_detail";

        public static final String COLUMN_DAM = "dam";
        public static final String COLUMN_WELL = "well";
        public static final String COLUMN_BOREHOLE = "bore_hole";
        public static final String COLUMN_RIVER_STREAM = "river_stream";
        public static final String COLUMN_PIPE_BORNE = "pipe_borne";
        public static final String COLUMN_IRRIGATION = "irrigation";
        public static final String COLUMN_NONE = "none";
        public static final String COLUMN_OTHER = "others";
        public static final String COLUMN_WATER_SOURCE_DETAILS = "water_source_details";
        public static final String COLUMN_TOTAL_PLOT_SIZE = "total_plot_size";

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
        public static final String COLUMN_LAT_INFO = "land_lat";
        public static final String COLUMN_LNG_INFO = "land_lng";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_LAND_ID = "land_uniq_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildLandPlotUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static final class FarmerAccessInfo implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FARMER_ACCESS_INFO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMER_ACCESS_INFO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMER_ACCESS_INFO;

        public static final String TABLE_NAME = "access_info_farmer";


        public static final String COLUMN_AGRI_EXTENSION_SERV = "extension_services";
        public static final String COLUMN_CLIMATE_RELATED_INFO = "climate_info";
        public static final String COLUMN_SEEDS = "seeds";
        public static final String COLUMN_ORGANIC_FERTILIZER = "organic_fertilizer";
        public static final String COLUMN_INORGANIC_FERTILIZER = "inorganic_fertilizer";
        public static final String COLUMN_LABOUR = "labour";
        public static final String COLUMN_WATER_PUMPS = "water_pumps";
        public static final String COLUMN_SPRAYERS = "sprayers";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildFarmerAccessInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class BaselineFarmer implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_BASELINE_FARMER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_FARMER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_FARMER;

        public static final String TABLE_NAME = "baseline_farmer";

        public static final String COLUMN_TOT_PROD_B_KG = "tot_prod_b";
        public static final String COLUMN_TOT_LOST_KG = "tot_lost_kg";
        public static final String COLUMN_TOT_SOLD_KG = "tot_sold_kg";
        public static final String COLUMN_TOT_VOL_SOLD_COOP = "tot_vol_sold_coop";
        public static final String COLUMN_PRICE_SOLD_COOP_PER_KG = "price_coop_kg";
        public static final String COLUMN_TOT_VOL_SOLD_IN_KG = "tot_vol_sold_kg";
        public static final String COLUMN_PRICE_SOLD_KG = "price_sold_kg";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildBaselineFarmerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ForecastFarmer implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FORECAST_FARMER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_FARMER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_FARMER;

        public static final String TABLE_NAME = "forecast_farmer";

        public static final String COLUMN_ARABLE_LAND_PLOT = "arable_land_plot";
        public static final String COLUMN_PRODUCTION_MT = "production_in_mt";
        public static final String COLUMN_YIELD_MT = "yield_in_mt";
        public static final String COLUMN_HARVEST_SALE_VALUE = "harvest_sale_value";
        public static final String COLUMN_COOP_LAND_SIZE = "tot_coop_land_size";
        public static final String COLUMN_PPP_COMMITMENT = "ppp_commitment";
        public static final String COLUMN_CONTRIBUTION_PPP = "contribution_ppp";
        public static final String COLUMN_EXPECTED_MIN_PPP = "expected_min_ppp";
        public static final String COLUMN_FLOW_PRICE = "flow_price";
        public static final String COLUMN_PERCENT_FARMER_LAND_SIZE = "percent_farmer_land_size";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";


        public static Uri buildForecastFarmerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class FinanceDataFarmer implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FINANCEDATA_FARMER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEDATA_FARMER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEDATA_FARMER;

        public static final String TABLE_NAME = "finance_data_farmer";

        public static final String COLUMN_OUTSANDING_LOAN = "outstanding_loan";
        public static final String COLUMN_TOT_LOAN_AMOUNT = "tot_loan_amount";
        public static final String COLUMN_TOT_OUTSTANDING = "tot_outstanding";
        public static final String COLUMN_INTEREST_RATE = "interest_rate";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_LOAN_PROVIDER = "loan_provider";
        public static final String COLUMN_MOBILE_MONEY_ACCOUNT = "mobile_money_account";
        public static final String COLUMN_LOANPROVIDER_INPUT = "lp_input";
        public static final String COLUMN_LOANPROVIDER_AGGREG = "lp_aggregation";
        public static final String COLUMN_LOANPROVIDER_OTHER = "lp_other";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildFinanceDataFarmerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class Vendor implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_VENDOR).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR;

        public static final String TABLE_NAME = "vendor_farmers";

        public static final String COLUMN_NAME = "farmer_name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_HOUSEHOLD_HEAD = "household_head";
        public static final String COLUMN_HOUSE_MEMBER = "num_household_member";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_CELL_PHONE = "cell_phone";
        public static final String COLUMN_CELL_CARRIER = "cell_carrier";
        public static final String COLUMN_MEMBER_SHIP = "membership_id";

        public static final String COLUMN_TRACTORS = "tractors";
        public static final String COLUMN_HARVESTER = "harvester";
        public static final String COLUMN_DRYER = "dryer";
        public static final String COLUMN_TRESHER = "thresher";
        public static final String COLUMN_SAFE_STORAGE = "safe_storage";
        public static final String COLUMN_STORAGE_DETAIL = "storage_detail";
        public static final String COLUMN_OTHER_INFO = "other_info";
        public static final String COLUMN_NEW_SOURCE_DETAIL = "source_detail";

        public static final String COLUMN_DAM = "dam";
        public static final String COLUMN_WELL = "well";
        public static final String COLUMN_BORHOLE = "bore_hole";
        public static final String COLUMN_RIVER_STREAM = "river_stream";
        public static final String COLUMN_PIPE_BORNE = "pipe_borne";
        public static final String COLUMN_IRRIGATION = "irrigation";
        public static final String COLUMN_NONE = "none";
        public static final String COLUMN_OTHER = "others";
        public static final String COLUMN_WATER_SOURCE_DETAILS = "water_source_details";
        public static final String COLUMN_TOTAL_PLOT_SIZE = "total_plot_size";

        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";
        public static final String COLUMN_VENDOR_SERVER_ID = "vendor_server_id";

        public static Uri buildVendorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class VendorAccessInfo implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_VENDOR_ACCESS_INFO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR_ACCESS_INFO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR_ACCESS_INFO;

        public static final String TABLE_NAME = "access_info_vendor";


        public static final String COLUMN_AGRI_EXTENSION_SERV = "extension_services";
        public static final String COLUMN_CLIMATE_RELATED_INFO = "climate_info";
        public static final String COLUMN_SEEDS = "seeds";
        public static final String COLUMN_ORGANIC_FERTILIZER = "organic_fertilizer";
        public static final String COLUMN_INORGANIC_FERTILIZER = "inorganic_fertilizer";
        public static final String COLUMN_LABOUR = "labour";
        public static final String COLUMN_WATER_PUMPS = "water_pumps";
        public static final String COLUMN_SPRAYERS = "sprayers";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildVendorAccessInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class VendorLand implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_VENDOR_LAND).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR_LAND;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENDOR_LAND;

        public static final String TABLE_NAME = "vendor_land";

        public static final String COLUMN_PLOT_SIZE = "plot_size";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_LAT_INFO = "land_lat";
        public static final String COLUMN_LNG_INFO = "land_lng";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_LAND_ID = "land_uniq_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildVendorLandUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class BaseLineVendor implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_BASELINE_VENDOR).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_VENDOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_VENDOR;

        public static final String TABLE_NAME = "baseline_vendor";

        public static final String COLUMN_TOT_PROD_B_KG = "tot_prod_b";
        public static final String COLUMN_TOT_LOST_KG = "tot_lost_kg";
        public static final String COLUMN_TOT_SOLD_KG = "tot_sold_kg";
        public static final String COLUMN_TOT_VOL_SOLD_COOP = "tot_vol_sold_coop";
        public static final String COLUMN_PRICE_SOLD_COOP_PER_KG = "price_coop_kg";
        public static final String COLUMN_TOT_VOL_SOLD_IN_KG = "tot_vol_sold_kg";
        public static final String COLUMN_PRICE_SOLD_KG = "price_sold_kg";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildBaselineVendorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ForecastVendor implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FORECAST_VENDOR).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_VENDOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_VENDOR;

        public static final String TABLE_NAME = "forecast_vendor";

        public static final String COLUMN_ARABLE_LAND_PLOT = "arable_land_plot";
        public static final String COLUMN_PRODUCTION_MT = "production_in_mt";
        public static final String COLUMN_YIELD_MT = "yield_in_mt";
        public static final String COLUMN_HARVEST_SALE_VALUE = "harvest_sale_value";
        public static final String COLUMN_COOP_LAND_SIZE = "tot_coop_land_size";
        public static final String COLUMN_PPP_COMMITMENT = "ppp_commitment";
        public static final String COLUMN_CONTRIBUTION_PPP = "contribution_ppp";
        public static final String COLUMN_EXPECTED_MIN_PPP = "expected_min_ppp";
        public static final String COLUMN_FLOW_PRICE = "flow_price";
        public static final String COLUMN_PERCENT_FARMER_LAND_SIZE = "percent_farmer_land_size";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";


        public static Uri buildForecastVendorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class FinanceDataVendor implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FINANCEDATA_VENDOR).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEDATA_VENDOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEDATA_VENDOR;

        public static final String TABLE_NAME = "finance_data_vendor";

        public static final String COLUMN_OUTSANDING_LOAN = "outstanding_loan";
        public static final String COLUMN_TOT_LOAN_AMOUNT = "tot_loan_amount";
        public static final String COLUMN_TOT_OUTSTANDING = "tot_outstanding";
        public static final String COLUMN_INTEREST_RATE = "interest_rate";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_LOAN_PROVIDER = "loan_provider";
        public static final String COLUMN_MOBILE_MONEY_ACCOUNT = "mobile_money_account";
        public static final String COLUMN_LOANPROVIDER_INPUT = "lp_input";
        public static final String COLUMN_LOANPROVIDER_AGGREG = "lp_aggregation";
        public static final String COLUMN_LOANPROVIDER_OTHER = "lp_other";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildFinanceDataFarmerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static final class Coops implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COOPS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOPS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOPS;

        public static final String TABLE_NAME = "coops";

        public static final String COLUMN_COOP_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_CHAIR_NAME = "chair_name";
        public static final String COLUMN_CHAIR_GENDER = "chair_gender";
        public static final String COLUMN_CHAIR_CELL = "chair_cell";

        public static final String COLUMN_VICECHAIR_NAME = "vice_chair_name";
        public static final String COLUMN_VICECHAIR_GENDER = "vice_chair_gender";
        public static final String COLUMN_VICECHAIR_CELL = "vice_chair_cell";

        public static final String COLUMN_SECRETARY_NAME = "secretary_name";
        public static final String COLUMN_SECRETARY_GENDER = "secretary_gender";
        public static final String COLUMN_SECRETARY_CELL = "secretary_cell";

        public static final String COLUMN_RCA_REGISTRATION = "rca_registration";
        public static final String COLUMN_LAND_SIZE_CIP = "total_land_size";
        public static final String COLUMN_LAND_SIZE_CIP2 = "land_size_cip";
        public static final String COLUMN_OFFICE_SPACE = "office_space";
        public static final String COLUMN_MOISTURE_METER = "moisture_meter";
        public static final String COLUMN_WEIGHTNING_SCALES = "weightning_scales";
        public static final String COLUMN_QUALITY_INPUT = "quality_inputs";
        public static final String COLUMN_TRACTORS = "tractors";
        public static final String COLUMN_HARVESTER = "harvester";
        public static final String COLUMN_DRYER = "dryer";
        public static final String COLUMN_THRESHER = "thresher";
        public static final String COLUMN_SAFE_STORAGE = "safe_storage";
        public static final String COLUMN_OTHER = "other";

        public static final String COLUMN_MALE_COOP = "male_coop";
        public static final String COLUMN_FEMALE_COOP = "female_coop";
        public static final String COLUMN_MEMBER = "tot_member";

        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static final String COLUMN_COOP_SERVER_ID = "coop_server_id";

        public static Uri buildCoopUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class CoopInfo implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COOP_INFO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOP_INFO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COOP_INFO;

        public static final String TABLE_NAME = "coop_info";

        public static final String COLUMN_AGRI_EXTENSION = "agri_extension";
        public static final String COLUMN_CLIMATE_INFO = "climate_info";
        public static final String COLUMN_SEEDS = "seeds";
        public static final String COLUMN_ORGANIC_FERT = "organic_fert";
        public static final String COLUMN_INORGANIC_FERT = "inorganic_fert";
        public static final String COLUMN_LABOUR = "labour";
        public static final String COLUMN_WATER_PUMPS = "water_pumps";
        public static final String COLUMN_SPREADER = "spreader";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildCoopInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class SalesCoop implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SALES_COOP).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALES_COOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALES_COOP;

        public static final String TABLE_NAME = "sales_coop";

        public static final String COLUMN_RGCC = "rgcc";
        public static final String COLUMN_PRODEV = "prodev";
        public static final String COLUMN_SAKURA = "sakura";
        public static final String COLUMN_AIF = "aif";
        public static final String COLUMN_EAX = "eax";
        public static final String COLUMN_NONE = "none";
        public static final String COLUMN_OTHER = "other";
        public static final String COLUMN_CONTRACT_VOLUME = "contract_volume";
        public static final String COLUMN_COOP_GRADE = "coop_grade";
        public static final String COLUMN_FLOOR_GRADE = "floor_grade";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildSalesCoopInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class YieldCoop implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_YIELD_COOP).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_YIELD_COOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_YIELD_COOP;

        public static final String TABLE_NAME = "yield_coop";

        public static final String COLUMN_MAIZE = "maize";
        public static final String COLUMN_BEAN = "bean";
        public static final String COLUMN_SOY = "soy";
        public static final String COLUMN_OTHER = "other";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildYieldCoopUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class BaselineSalesCoop implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_BASELINE_SALES_COOP).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_SALES_COOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BASELINE_SALES_COOP;

        public static final String TABLE_NAME = "baselines_coop";

        public static final String COLUMN_CYCLE_HARVEST = "cycle_harvest";
        public static final String COLUMN_CYCLE_HARVEST_PRICE = "cycle_harvest_price";
        public static final String COLUMN_NON_MEMBER_PURCHASE = "non_member_purchase";
        public static final String COLUMN_NON_MEMBER_PURCHASE_COST = "non_member_purchase_cost";
        public static final String COLUMN_BUYER_CONTRACT = "buyer_contract";
        public static final String COLUMN_CONTRACT_VOLUME = "contract_volume";
        public static final String COLUMN_QUANTITY_SOLD_RGCC = "quantity_sold_rgcc";
        public static final String COLUMN_PRICE_SOLD_RGCC = "price_sold_rgcc";
        public static final String COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC = "quantity_sold_outside_rgcc";
        public static final String COLUMN_RGCC_BUYER_FORMAL = "rgcc_buyer_formal";
        public static final String COLUMN_RGCC_INFORMAL = "rgcc_buyer_informal";
        public static final String COLUMN_BUYER_OTHER = "buyer_other";
        public static final String COLUMN_PRICE_SOLD_OUTSIDE_RGCC = "price_sold_outside_rgcc";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildBaselineSalesCoppUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class FinanceInfoCoop implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FINANCEINFO_COOP).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEINFO_COOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FINANCEINFO_COOP;

        public static final String TABLE_NAME = "baseline_finance_info_coop";

        public static final String COLUMN_CYCLE_LOAN = "cycle_loan";
        public static final String COLUMN_INPUT_LOAN_PROVIDER_BANK = "input_loan_provider_bank";
        public static final String COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE = "input_loan_provider_coop";
        public static final String COLUMN_INPUT_LOAN_PROVIDER_SACCO = "input_loan_provider_sacco";
        public static final String COLUMN_INPUT_LOAN_PROVIDER_OTHER = "input_loan_provider_other";
        public static final String COLUMN_CYLE_LOAN_AMOUNT = "cycle_loan_amount";
        public static final String COLUMN_CYCLE_INTEREST_RATE = "cycle_interest_rate";
        public static final String COLUMN_CYCLE_LOAN_DURATION = "cycle_loan_duration";
        public static final String COLUMN_INPUT_LOAN_PURPOSE_LABOUR = "loan_purpose_labour";
        public static final String COLUMN_INPUT_LOAN_PURPOSE_SEEDS = "loan_purpose_seeds";
        public static final String COLUMN_INPUT_LOAN_PURPOSE_INPUT = "loan_purpose_input";
        public static final String COLUMN_INPUT_LOAN_PURPOSE_MACHINERY = "loan_purpose_machinery";
        public static final String COLUMN_INPUT_LOAN_PURPOSE_OTHER = "loan_purpose_other";
        public static final String COLUMN_CYCLE_LOAN_DISB_METHOD = "cycle_loan_disb_method";
        public static final String COLUMN_POST_HARVEST_LOAN = "post_harvest_loan";
        public static final String COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK = "post_harvest_loan_provider_bank";
        public static final String COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO = "post_harvest_loan_provider_sacco";
        public static final String COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER = "post_harvest_loan_provider_other";
        public static final String COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE = "post_harvest_loan_provider_cooperative";
        public static final String COLUMN_POST_HARVEST_LOAN_AMOUNT = "post_harvest_loan_amount";
        public static final String COLUMN_POST_HARVEST_LOAN_INTEREST_RATE = "post_harvest_loan_interest_rate";
        public static final String COLUMN_POST_HARVEST_LOAN_DURATION = "post_harvest_loan_duration";
        public static final String COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR = "post_harvest_loan_purpose_labour";
        public static final String COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT = "post_harvest_loan_purpose_input";
        public static final String COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY = "post_harvest_loan_purpose_machinery";
        public static final String COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER = "post_harvest_loan_purpose_other";
        public static final String COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD = "post_harvest_loan_disbursement_method";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildBaselineFinanceInfoCoopUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ExpectedYieldCoop implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_EXPECTED_YIELD_COOP).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPECTED_YIELD_COOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPECTED_YIELD_COOP;

        public static final String TABLE_NAME = "expected_yield_coop";

        public static final String COLUMN_COOP_PRODUCTION = "coop_production";
        public static final String COLUMN_COOP_LAND_SIZE = "coop_land_size";
        public static final String COLUMN_PRODUCTION_MT = "production_mt";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_SERVER_ID = "server_id";

        public static Uri buildExpectedYieldCoopUri(long id) {
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

    public static final class PurchaseOrder implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PURCHASE_ORDER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER;

        public static final String TABLE_NAME = "purchase_order";

        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_AMOUNT_DUE = "amount_due";
        public static final String COLUMN_BALANCE_TO_RECEIVE = "balance_to_receive";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_AGENT_ID = "agent_id";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_HARVEST_SEASON = "harvest_season";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildPurchaseOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PurchaseOrderLine implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PURCHASE_ORDER_LINE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER_LINE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ORDER_LINE;

        public static final String TABLE_NAME = "purchase_order_line";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRICE_UNIT = "price_unit";
        public static final String COLUMN_SUB_TOTAL = "sub_total";
        public static final String COLUMN_QTY = "qty";
        public static final String COLUMN_PRICE_TOTAL = "price_total";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildPurchaseOrdeLinerUri(long id) {
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
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_COOP_AGENT_ID = "coop_agent_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_BUYER_ID = "buyer_id";
        public static final String COLUMN_AVG_PRICE = "avg_price";
        public static final String COLUMN_ORDERED_QTY = "ordered_qty";
        public static final String COLUMN_DELIVERED_QTY = "delivered_qty";
        public static final String COLUMN_BALANCE_TO_SHIP = "balance_to_ship";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildSaleOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class SaleOrderLine implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SALE_ORDER_LINE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALE_ORDER_LINE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SALE_ORDER_LINE;

        public static final String TABLE_NAME = "sale_order_line";

        public static final String COLUMN_DELIVER_QTY = "delivered_qty";
        public static final String COLUMN_TO_INVOICE = "to_invoice";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildSaleOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ProductTemplate implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PRODUCT_TEMPLATE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_TEMPLATE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_TEMPLATE;

        public static final String TABLE_NAME = "product_template";

        public static final String COLUMN_HARVEST_SEASON = "harvest_season";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_HARVEST_GRADE = "harvest_grade";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_VENDOR_QTY = "vendor_qty";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_BUYER_ID = "buyer_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_TYPE = "product_type";
        public static final String COLUMN_FARMER_REG_ID = "farmer_reg_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";


        public static Uri buildProductTemplateUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class ProductProduct implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PRODUCT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String TABLE_NAME = "product";

        public static final String COLUMN_HARVEST_SEASON = "harvest_season";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_HARVEST_GRADE = "harvest_grade";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_VENDOR_QTY = "vendor_qty";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_BUYER_ID = "buyer_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_FARMER_REG_ID = "farmer_reg_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";


        public static Uri buildProductUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class Loan implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_LOAN).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN;

        public static final String TABLE_NAME = "loans";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FARMER_ID = "farmer_id";
        public static final String COLUMN_COOP_ID = "coop_id";
        public static final String COLUMN_VENDOR_ID = "vendor_id";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_INTEREST_RATE = "interest_rate";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_PURPOSE = "purpose";
        public static final String COLUMN_FINANCIAL_INSTITUTION = "financial_institution";
        public static final String COLUMN_AMOUNT_DUE = "amount_due";
        public static final String COLUMN_AMOUNT_TOTAL = "amount_total";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildLoanUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class LoanLine implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_LOAN_LINE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN_LINE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN_LINE;

        public static final String TABLE_NAME = "loan_line";

        public static final String COLUMN_PAYMENT_DATE = "payment_date";
        public static final String COLUMN_PRINCIPAL = "principal";
        public static final String COLUMN_INTEREST = "interest";
        public static final String COLUMN_REMAINING_AMOUNT = "remaining_amount";
        public static final String COLUMN_NEXT_PAYMENT_AMOUNT = "next_payment_amount";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_LOAN_ID = "loan_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";


        public static Uri buildProductLoanUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class LoanPayment implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_LOAN_PAYMENT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN_PAYMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOAN_PAYMENT;

        public static final String TABLE_NAME = "loan_payment";

        public static final String COLUMN_PAYMENT_DATE = "payment_date";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_LOAN_ID = "loan_id";
        public static final String COLUMN_IS_SYNC = "is_sync";
        public static final String COLUMN_IS_UPDATE = "is_update";

        public static Uri buildProductLoanPaymentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
