package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBar;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarDuration;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarGravity;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarTheme;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.NavigationActivity;
import com.nijus.alino.bfwcoopmanagement.products.ui.activities.ProductActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.Manifest.permission.READ_CONTACTS;
import static com.nijus.alino.bfwcoopmanagement.R.id.login_form;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        setupActionBar();
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.checkAlreadyLogin(getApplicationContext())) {
            //check who is connected
            Intent intent = new Intent(this, UserProfileActivityAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Map> {

        private final String mEmail;
        private final String mPassword;
        private Map messageData;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Map doInBackground(Void... params) {

            if (Utils.isNetworkAvailable(getApplicationContext())) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(240, TimeUnit.SECONDS)
                        .writeTimeout(240, TimeUnit.SECONDS)
                        .readTimeout(240, TimeUnit.SECONDS)
                        .build();
                Cursor cursor = null;
                String login = "";
                String groupName = "";

                try {
                    String API = BuildConfig.DEV_API_URL + "auth/get_tokens";

                    String bodyContent = "{\"db\": \"" + BuildConfig.DEV_DB_NAME + "\"," +
                            "\"username\":\"" + mEmail + "\"," +
                            "\"password\": \"" + mPassword + "\"}";

                    RequestBody body = RequestBody.create(JSON, bodyContent);

                    Request request = new Request.Builder()
                            .url(API)
                            .header("Content-Type", "text/html")
                            .method("POST", body)
                            .build();

                    Response response = client.newCall(request).execute();
                    ResponseBody info = response.body();
                    if (info != null) {
                        String apiData = info.string();

                        JSONObject objectInfo = new JSONObject(apiData);
                        if (objectInfo.has("uid") && objectInfo.has("access_token") && objectInfo.has("refresh_token")) {

                            //get user information
                            String access_token = objectInfo.getString("access_token");
                            String refresh_token = objectInfo.getString("refresh_token");

                            // set appropriate info (uid, server id if not admin, user details if not admin and group list)
                            JSONObject loginUserInfo = objectInfo.getJSONObject("data");

                            JSONArray groupList = objectInfo.getJSONArray("glists");

                            boolean isAdmin = false;
                            for (int i = 0, n = groupList.length(); i < n; i++) {

                                groupName = groupList.getString(i);
                                if (groupName.equals("Buyer") || groupName.equals("Agent") || groupName.equals("Vendor")) {
                                    break;
                                }

                                if (groupName.equals("Configuration")) {
                                    isAdmin = true;
                                }
                            }

                            if ((!groupName.equals("Buyer") || !groupName.equals("Agent") || !groupName.equals("Vendor")) && isAdmin) {

                                groupName = "Admin";
                            }

                            if (isAdmin) {
                                //Prefetch crm (farmers, vendor,coops, buyer, coop agent, season)
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchSeason(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchCoops(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                cursor = getContentResolver().query(BfwContract.CoopAgent.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchCoopAgent(access_token, client);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchVendor(client, access_token, BuildConfig.DEV_API_URL + "vendor.farmer",false);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchFarmer(client, access_token, BuildConfig.DEV_API_URL + "farmer", loginUserInfo, false);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchBuyer(client, access_token, BuildConfig.DEV_API_URL + "buyer", false);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                //Prefetch sale,purchase,product
                                cursor = getContentResolver().query(BfwContract.ProductTemplate.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchProduct(access_token, client);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }


                                //Prefetch loan
                                //Prefetch sale,purchase,product
                                cursor = getContentResolver().query(BfwContract.Loan.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchLoan(access_token, client);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }
                            }

                            if (groupName.equals("Agent")) {

                                //Prefetch crm (farmers,coops, season)
                                // check if there's no farmer then prefetch all the farmer with filter
                                int coopServerId = loginUserInfo.getInt("coop_id");

                                String coopSelect = BfwContract.Coops.TABLE_NAME + "." +
                                        BfwContract.Coops.COLUMN_COOP_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, coopSelect, new String[]{Long.toString(coopServerId)}, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchCoops(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=farmer&attr=coop_id&value=" + coopServerId + "&token=" + access_token;

                                String farmerSelect = BfwContract.Farmer.TABLE_NAME + "." +
                                        BfwContract.Farmer.COLUMN_COOP_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, farmerSelect, new String[]{Long.toString(coopServerId)}, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchFarmer(client, access_token, proxyUrl, loginUserInfo, true);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }


                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchSeason(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }
                                //Prefetch sale,purchase,product
                                cursor = getContentResolver().query(BfwContract.ProductTemplate.CONTENT_URI, null, null, null, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchProduct(access_token, client);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }
                                //Prefetch loan
                            }

                            if (groupName.equals("Buyer")) {
                                //Prefetch crm (buyer, season)
                                int buyerServerId = loginUserInfo.getInt("id");

                                String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=buyer&attr=id&value=" + buyerServerId + "&token=" + access_token;

                                String buyerSelect = BfwContract.Buyer.TABLE_NAME + "." +
                                        BfwContract.Buyer.COLUMN_BUYER_SERVER_ID + " =  ? ";
                                cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, buyerSelect, new String[]{Long.toString(buyerServerId)}, null);

                                if (cursor != null && !cursor.moveToFirst()) {

                                    boolean isSuccess = prefetchBuyer(client, access_token, proxyUrl, true);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }


                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchSeason(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                //Prefetch product
                            }

                            if (groupName.equals("Vendor")) {
                                //Prefetch crm (vendor, season)
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchSeason(client, access_token);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }

                                int vendorServerId = loginUserInfo.getInt("id");

                                String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=vendor.farmer&attr=id&value=" + vendorServerId + "&token=" + access_token;

                                String vendorSelect = BfwContract.Vendor.TABLE_NAME + "." +
                                        BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID + " =  ? ";
                                cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, vendorSelect, new String[]{Long.toString(vendorServerId)}, null);

                                if (cursor != null && !cursor.moveToFirst()) {
                                    boolean isSuccess = prefetchVendor(client, access_token, proxyUrl, true);
                                    if (!isSuccess) {
                                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                                    }
                                }
                                //Prefetch sale,purchase,product
                                //Prefetch loan
                            }


                            // make activity parent dynamic (Review App Navigation)

                            int userId = objectInfo.getInt("uid");

                            //save token in settings preferences
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(getResources().getString(R.string.app_id), true);
                            editor.putString(getResources().getString(R.string.app_key), access_token);
                            editor.putString(getResources().getString(R.string.app_refresh_token), refresh_token);
                            editor.putString(getResources().getString(R.string.g_name), groupName);

                            if (isAdmin) {
                                login = loginUserInfo.getString("login");
                                editor.putString(getResources().getString(R.string.user_name), login);
                            }

                            if (groupName.equals("Buyer") || groupName.equals("Vendor")) {
                                login = loginUserInfo.getString("name");
                                editor.putString(getResources().getString(R.string.user_name), login);
                                editor.putInt(getResources().getString(R.string.server_id), loginUserInfo.getInt("id"));
                                editor.putInt(getResources().getString(R.string.vendor_buyer_id), loginUserInfo.getInt("id"));
                            }

                            if (groupName.equals("Agent")) {
                                editor.putString(getResources().getString(R.string.user_name), loginUserInfo.getString("cname"));
                                editor.putInt(getResources().getString(R.string.coop_id), loginUserInfo.getInt("coop_id"));
                                editor.putInt(getResources().getString(R.string.server_id), loginUserInfo.getInt("id"));
                            }
                            editor.putInt(getResources().getString(R.string.user_server_id), userId);
                            editor.apply();
                            return getLoginMessage("", groupName, true);
                        } else {
                            getLoginMessage(getResources().getString(R.string.auth_error), "", false);
                        }
                    } else {
                        return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                    }
                } catch (IOException exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                } catch (JSONException exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                } catch (Exception exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), "", false);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return getLoginMessage(getResources().getString(R.string.auth_error), groupName, false);
            } else {
                return getLoginMessage((getResources().getString(R.string.connectivity_error)), "", false);

            }
        }

        private boolean prefetchSeason(OkHttpClient client, String token) {
            getContentResolver().delete(BfwContract.HarvestSeason.CONTENT_URI, null, null);
            try {
                Response seasonData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "harvest.season");
                if (seasonData != null) {
                    ResponseBody seasonInfo = seasonData.body();
                    if (seasonInfo != null) {
                        String seasonsList = seasonInfo.string();

                        JSONObject harvestSeasonData = new JSONObject(seasonsList);
                        JSONArray harvestSeasonArray = harvestSeasonData.getJSONArray("results");

                        JSONObject harvestSeasonObjectInfo;

                        for (int i = 0; i < harvestSeasonArray.length(); i++) {
                            harvestSeasonObjectInfo = harvestSeasonArray.getJSONObject(i);
                            int serverId = harvestSeasonObjectInfo.getInt("id");
                            String name = harvestSeasonObjectInfo.getString("name");
                            String start = harvestSeasonObjectInfo.getString("date_start");
                            String end = harvestSeasonObjectInfo.getString("date_end");
                            boolean isActive = harvestSeasonObjectInfo.getBoolean("active");

                            // convert data
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date startDate = df.parse(start);
                            Date endDate = df.parse(end);

                            // Get New Harvest Season and save them
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_NAME, name);
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_START_DATE, startDate.getTime());
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_END_DATE, endDate.getTime());
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_SERVER_ID, serverId);
                            if (isActive) {
                                contentValues.put(BfwContract.HarvestSeason.COLUMN_ACTIVE, 1);
                            } else {
                                contentValues.put(BfwContract.HarvestSeason.COLUMN_ACTIVE, 0);
                            }

                            getContentResolver().insert(BfwContract.HarvestSeason.CONTENT_URI, contentValues);

                        }

                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (IOException | ParseException | JSONException exp) {
                return false;
            }
            return true;
        }

        private boolean prefetchCoops(OkHttpClient client, String token) {
            getContentResolver().delete(BfwContract.Coops.CONTENT_URI, null, null);
            Cursor cursor = null;
            try {
                Response coopData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "coop");
                if (coopData != null) {

                    ResponseBody coopsInfo = coopData.body();
                    if (coopsInfo != null) {
                        String coopsList = coopsInfo.string();
                        // get coops and save them

                        JSONObject resCoopsInfos = new JSONObject(coopsList);
                        JSONArray resCoopsArray = resCoopsInfos.getJSONArray("results");

                        JSONObject coopObjectInfo;
                        JSONArray coopInfos;
                        JSONArray forecastSalesIds;
                        JSONArray baseSalesLinesIds;
                        JSONArray baseLinesYieldIds;
                        JSONArray baseLinesFinanceInfos;
                        JSONArray expectedYieldIds;

                        JSONObject objectCoopInfos;
                        JSONObject objectForecastSalesIds;
                        JSONObject objectBaseLinseSalesIds;
                        JSONObject objectBaseLineYieldIds;
                        JSONObject objectBaseLinesFinancesInfos;
                        JSONObject objectExpectedYieldsIds;

                        String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                        String name = null;
                        String phone = null;
                        String email = null;
                        String address = null;

                        String chairName = null;
                        String chairGender = null;
                        String chairCell = null;

                        String viceChairName = null;
                        String viceChairGender = null;
                        String viceChairCell = null;

                        String secretaryName = null;
                        String secretaryGender = null;
                        String secretaryCell = null;

                        String rcaRegistration = null;
                        Double landSizeUnderCip = null;
                        Double landSizeUnderCip2 = null;

                        Boolean officespace = null;
                        Integer officeSpace = null;
                        Integer moistureMeter = null;
                        Integer weightningScales = null;
                        Integer qualityInput = null;
                        Integer tractors = null;
                        Integer harvester = null;
                        Integer dryer = null;
                        Integer thresher = null;
                        Integer safeStorage = null;
                        Integer other = null;
                        String otherDetails = null;
                        String storageDetails = null;
                        Integer maleInCoop = null;
                        Integer femaleInCoop = null;
                        Integer totalMembers = null;

                        for (int i = 0; i < resCoopsArray.length(); i++) {
                            coopObjectInfo = resCoopsArray.getJSONObject(i);

                            int serverId = coopObjectInfo.getInt("id");

                            if (coopObjectInfo.has("name")) {
                                name = coopObjectInfo.getString("name");
                            }
                            if (coopObjectInfo.has("phone")) {
                                phone = coopObjectInfo.getString("phone");
                            }
                            if (coopObjectInfo.has("email")) {
                                email = coopObjectInfo.getString("email");
                            }
                            if (coopObjectInfo.has("address")) {
                                address = coopObjectInfo.getString("address");
                            }
                            if (coopObjectInfo.has("chair_name")) {
                                chairName = coopObjectInfo.getString("chair_name");
                            }
                            if (coopObjectInfo.has("chair_gender")) {
                                chairGender = coopObjectInfo.getString("chair_gender");
                            }
                            if (coopObjectInfo.has("chair_cell")) {
                                chairCell = coopObjectInfo.getString("chair_cell");
                            }
                            if (coopObjectInfo.has("vicechair_name")) {
                                viceChairName = coopObjectInfo.getString("vicechair_name");
                            }
                            if (coopObjectInfo.has("vicechair_gender")) {
                                viceChairGender = coopObjectInfo.getString("vicechair_gender");
                            }
                            if (coopObjectInfo.has("vicechair_cell")) {
                                viceChairCell = coopObjectInfo.getString("vicechair_cell");
                            }
                            if (coopObjectInfo.has("secretary_name")) {
                                secretaryName = coopObjectInfo.getString("secretary_name");
                            }
                            if (coopObjectInfo.has("secretary_gender")) {
                                secretaryGender = coopObjectInfo.getString("secretary_gender");
                            }
                            if (coopObjectInfo.has("secretary_cell")) {
                                secretaryCell = coopObjectInfo.getString("secretary_cell");
                            }
                            if (coopObjectInfo.has("rca_year_registration")) {
                                rcaRegistration = coopObjectInfo.getString("rca_year_registration");
                            }
                            if (coopObjectInfo.has("land_size_under_cip")) {
                                landSizeUnderCip = coopObjectInfo.getDouble("land_size_under_cip");
                            }
                            if (coopObjectInfo.has("land_size_under_cip2")) {
                                landSizeUnderCip2 = coopObjectInfo.getDouble("land_size_under_cip2");
                            }


                            if (!coopObjectInfo.getString("cr_office_space").equals("null")) {
                                officespace = coopObjectInfo.getBoolean("cr_office_space");
                                if (officespace != null) {
                                    officeSpace = officespace ? 1 : 0;
                                }
                            }

                            if (!coopObjectInfo.getString("cr_moisture_meter").equals("null")) {
                                Boolean moisturemeter = coopObjectInfo.getBoolean("cr_moisture_meter");
                                if (moisturemeter != null) {
                                    moistureMeter = moisturemeter ? 1 : 0;
                                }
                            }

                            if (!coopObjectInfo.getString("cr_weighting_scales").equals("null")) {
                                Boolean weightningscales = coopObjectInfo.getBoolean("cr_weighting_scales");
                                if (weightningscales != null) {
                                    weightningScales = weightningscales ? 1 : 0;
                                }
                            }

                            if (!coopObjectInfo.getString("cr_quality_inputs").equals("null")) {
                                Boolean qualityinput = coopObjectInfo.getBoolean("cr_quality_inputs");
                                if (qualityinput != null) {
                                    qualityInput = qualityinput ? 1 : 0;
                                }
                            }
                            if (!coopObjectInfo.getString("cr_tractors").equals("null")) {
                                Boolean tractor = coopObjectInfo.getBoolean("cr_tractors");
                                if (tractor != null) {
                                    tractors = tractor ? 1 : 0;
                                }
                            }
                            if (!coopObjectInfo.getString("cr_harvesters").equals("null")) {
                                Boolean harvestr = coopObjectInfo.getBoolean("cr_harvesters");
                                harvester = null;
                                if (harvestr != null) {
                                    harvester = harvestr ? 1 : 0;
                                }
                            }
                            if (!coopObjectInfo.getString("cr_dryer").equals("null")) {
                                Boolean dry = coopObjectInfo.getBoolean("cr_dryer");
                                if (dry != null) {
                                    dryer = dry ? 1 : 0;
                                }
                            }

                            if (!coopObjectInfo.getString("cr_thresher").equals("null")) {
                                Boolean thresh = coopObjectInfo.getBoolean("cr_thresher");
                                if (thresh != null) {
                                    thresher = thresh ? 1 : 0;
                                }
                            }
                            if (!coopObjectInfo.getString("cr_safe_storage").equals("null")) {
                                Boolean safestorage = coopObjectInfo.getBoolean("cr_safe_storage");
                                if (safestorage != null) {
                                    safeStorage = safestorage ? 1 : 0;
                                }
                            }
                            if (!coopObjectInfo.getString("cr_other").equals("null")) {
                                Boolean othe = coopObjectInfo.getBoolean("cr_other");
                                if (othe != null) {
                                    other = othe ? 1 : 0;
                                }
                            }
                            if (coopObjectInfo.has("other_details")) {
                                otherDetails = coopObjectInfo.getString("other_details");
                            }
                            if (coopObjectInfo.has("storage_details")) {
                                storageDetails = coopObjectInfo.getString("storage_details");
                            }
                            if (coopObjectInfo.has("registered_males_coop")) {
                                maleInCoop = coopObjectInfo.getInt("registered_males_coop");
                            }
                            if (coopObjectInfo.has("registered_females_coop")) {
                                femaleInCoop = coopObjectInfo.getInt("registered_females_coop");
                            }
                            if (coopObjectInfo.has("total_members")) {
                                totalMembers = coopObjectInfo.getInt("total_members");
                            }

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(BfwContract.Coops.COLUMN_COOP_NAME, name);
                            contentValues.put(BfwContract.Coops.COLUMN_EMAIL, email);
                            contentValues.put(BfwContract.Coops.COLUMN_PHONE, phone);
                            contentValues.put(BfwContract.Coops.COLUMN_ADDRESS, address);

                            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_NAME, chairName);
                            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_GENDER, chairGender);
                            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_CELL, chairCell);

                            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_NAME, viceChairName);
                            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_CELL, viceChairCell);
                            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_GENDER, viceChairGender);

                            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_NAME, secretaryName);
                            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_GENDER, secretaryGender);
                            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_CELL, secretaryCell);

                            contentValues.put(BfwContract.Coops.COLUMN_RCA_REGISTRATION, rcaRegistration);
                            contentValues.put(BfwContract.Coops.COLUMN_LAND_SIZE_CIP, landSizeUnderCip);
                            contentValues.put(BfwContract.Coops.COLUMN_LAND_SIZE_CIP2, landSizeUnderCip2);

                            contentValues.put(BfwContract.Coops.COLUMN_OFFICE_SPACE, officeSpace);
                            contentValues.put(BfwContract.Coops.COLUMN_MOISTURE_METER, moistureMeter);
                            contentValues.put(BfwContract.Coops.COLUMN_WEIGHTNING_SCALES, weightningScales);
                            contentValues.put(BfwContract.Coops.COLUMN_QUALITY_INPUT, qualityInput);
                            contentValues.put(BfwContract.Coops.COLUMN_TRACTORS, tractors);
                            contentValues.put(BfwContract.Coops.COLUMN_HARVESTER, harvester);
                            contentValues.put(BfwContract.Coops.COLUMN_DRYER, dryer);
                            contentValues.put(BfwContract.Coops.COLUMN_THRESHER, thresher);
                            contentValues.put(BfwContract.Coops.COLUMN_SAFE_STORAGE, safeStorage);
                            contentValues.put(BfwContract.Coops.COLUMN_OTHER, other);
                            contentValues.put(BfwContract.Coops.COLUMN_MALE_COOP, maleInCoop);
                            contentValues.put(BfwContract.Coops.COLUMN_FEMALE_COOP, femaleInCoop);
                            contentValues.put(BfwContract.Coops.COLUMN_MEMBER, totalMembers);
                            contentValues.put(BfwContract.Coops.COLUMN_OTHER_DETAILS, otherDetails);
                            contentValues.put(BfwContract.Coops.COLUMN_STORAGE_DETAILS, storageDetails);
                            contentValues.put(BfwContract.Coops.COLUMN_IS_SYNC, 1);
                            contentValues.put(BfwContract.Coops.COLUMN_IS_UPDATE, 1);

                            contentValues.put(BfwContract.Coops.COLUMN_COOP_SERVER_ID, serverId);

                            Uri uri = getContentResolver().insert(BfwContract.Coops.CONTENT_URI, contentValues);
                            long coopId = ContentUris.parseId(uri);

                            // get coop access info (access_infos_ids) objects
                            coopInfos = coopObjectInfo.getJSONArray("access_infos_ids");

                            for (int j = 0; j < coopInfos.length(); j++) {
                                objectCoopInfos = coopInfos.getJSONObject(j);

                                Integer agriExt = null;
                                Integer climateInfo = null;
                                Integer seeds = null;
                                Integer orgFert = null;
                                Integer inorgFert = null;
                                Integer labour = null;
                                Integer waterPumps = null;
                                Integer spreaders = null;
                                Long harvestId = null;

                                int infoServerId = objectCoopInfos.getInt("id");
                                if (!objectCoopInfos.getString("cr_agricultural_extension").equals("null")) {
                                    Boolean agriext = objectCoopInfos.getBoolean("cr_agricultural_extension");
                                    if (agriext != null) {
                                        agriExt = agriext ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("cr_climate_related_info").equals("null")) {
                                    Boolean climateinfo = objectCoopInfos.getBoolean("cr_climate_related_info");
                                    if (climateinfo != null) {
                                        climateInfo = climateinfo ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_seeds").equals("null")) {
                                    Boolean seed = objectCoopInfos.getBoolean("car_seeds");
                                    if (seed != null) {
                                        seeds = seed ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_of").equals("null")) {
                                    Boolean orgFer = objectCoopInfos.getBoolean("car_of");
                                    if (orgFer != null) {
                                        orgFert = orgFer ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_if").equals("null")) {
                                    Boolean inorgfert = objectCoopInfos.getBoolean("car_if");
                                    inorgFert = null;
                                    if (inorgfert != null) {
                                        inorgFert = inorgfert ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_labour").equals("null")) {
                                    Boolean lab = objectCoopInfos.getBoolean("car_labour");
                                    if (lab != null) {
                                        labour = lab ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_iwp").equals("null")) {
                                    Boolean waterPump = objectCoopInfos.getBoolean("car_iwp");
                                    if (waterPump != null) {
                                        waterPumps = waterPump ? 1 : 0;
                                    }
                                }
                                if (!objectCoopInfos.getString("car_ss").equals("null")) {
                                    Boolean spreader = objectCoopInfos.getBoolean("car_ss");
                                    if (spreader != null) {
                                        spreaders = spreader ? 1 : 0;
                                    }
                                }
                                if (objectCoopInfos.has("harvest_id")) {
                                    harvestId = objectCoopInfos.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues infoValues = new ContentValues();
                                infoValues.put(BfwContract.CoopInfo.COLUMN_AGRI_EXTENSION, agriExt);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_CLIMATE_INFO, climateInfo);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_SEEDS, seeds);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_ORGANIC_FERT, orgFert);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_INORGANIC_FERT, inorgFert);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_LABOUR, labour);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_WATER_PUMPS, waterPumps);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_SPREADER, spreaders);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_SERVER_ID, infoServerId);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_SEASON_ID, seasonId);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_COOP_ID, coopId);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_IS_SYNC, 1);
                                infoValues.put(BfwContract.CoopInfo.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.CoopInfo.CONTENT_URI, infoValues);

                            }

                            // get forecast sales id (forecast_sales_ids)
                            forecastSalesIds = coopObjectInfo.getJSONArray("forecast_sales_ids");
                            for (int k = 0; k < forecastSalesIds.length(); k++) {

                                objectForecastSalesIds = forecastSalesIds.getJSONObject(k);
                                int forecastSaleId = objectForecastSalesIds.getInt("id");

                                Integer ebcRGCC = null;
                                Integer ebcProdev = null;
                                Integer ebcSakura = null;
                                Integer ebcAif = null;
                                Integer ebcEax = null;
                                Integer ebcNone = null;
                                Integer ebcOther = null;
                                Integer contractVolume = null;
                                String coopGrade = null;
                                String coopMinimumFloorGrade = null;
                                Long harvestId = null;

                                if (!objectForecastSalesIds.getString("ebc_rgcc").equals("null")) {
                                    Boolean ebcRGC = objectForecastSalesIds.getBoolean("ebc_rgcc");
                                    if (ebcRGC != null) {
                                        ebcRGCC = ebcRGC ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_prodev").equals("null")) {
                                    Boolean ebcProdv = objectForecastSalesIds.getBoolean("ebc_prodev");
                                    if (ebcProdv != null) {
                                        ebcProdev = ebcProdv ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_sakura").equals("null")) {
                                    Boolean ebcSakur = objectForecastSalesIds.getBoolean("ebc_sakura");
                                    if (ebcSakur != null) {
                                        ebcSakura = ebcSakur ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_aif").equals("null")) {
                                    Boolean ebcaif = objectForecastSalesIds.getBoolean("ebc_aif");
                                    if (ebcaif != null) {
                                        ebcAif = ebcaif ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_eax").equals("null")) {
                                    Boolean ebceax = objectForecastSalesIds.getBoolean("ebc_eax");
                                    if (ebceax != null) {
                                        ebcEax = ebceax ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_none").equals("null")) {
                                    Boolean ebcnone = objectForecastSalesIds.getBoolean("ebc_none");
                                    if (ebcnone != null) {
                                        ebcNone = ebcnone ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("ebc_other").equals("null")) {
                                    Boolean ebcother = objectForecastSalesIds.getBoolean("ebc_other");
                                    if (ebcother != null) {
                                        ebcOther = ebcother ? 1 : 0;
                                    }
                                }
                                if (!objectForecastSalesIds.getString("contract_volume").equals("null")) {
                                    contractVolume = objectForecastSalesIds.getInt("contract_volume");
                                }
                                if (objectForecastSalesIds.has("coop_grade")) {
                                    coopGrade = objectForecastSalesIds.getString("coop_grade");
                                }
                                if (objectForecastSalesIds.has("coop_minimum_floor_grade")) {
                                    coopMinimumFloorGrade = objectForecastSalesIds.getString("coop_minimum_floor_grade");
                                }
                                if (objectForecastSalesIds.has("harvest_id")) {
                                    harvestId = objectForecastSalesIds.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues forecastValues = new ContentValues();
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_RGCC, ebcRGCC);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_PRODEV, ebcProdev);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_SAKURA, ebcSakura);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_AIF, ebcAif);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_EAX, ebcEax);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_NONE, ebcNone);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_OTHER, ebcOther);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_CONTRACT_VOLUME, contractVolume);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_COOP_GRADE, coopGrade);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_FLOOR_GRADE, coopMinimumFloorGrade);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_SEASON_ID, seasonId);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_SERVER_ID, forecastSaleId);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_COOP_ID, coopId);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_IS_SYNC, 1);
                                forecastValues.put(BfwContract.SalesCoop.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.SalesCoop.CONTENT_URI, forecastValues);

                            }

                            // get base line yield ids (baseline_yield_ids)
                            baseLinesYieldIds = coopObjectInfo.getJSONArray("baseline_yield_ids");
                            for (int l = 0; l < baseLinesYieldIds.length(); l++) {
                                objectBaseLineYieldIds = baseLinesYieldIds.getJSONObject(l);

                                Integer mpMaize = null;
                                Integer mpBean = null;
                                Integer mpSoy = null;
                                Integer mpOther = null;
                                Long harvestId = null;

                                int baseLineYiedId = objectBaseLineYieldIds.getInt("id");

                                if (!objectBaseLineYieldIds.getString("mp_maize").equals("null")) {
                                    Boolean mpmaize = objectBaseLineYieldIds.getBoolean("mp_maize");
                                    if (mpmaize != null) {
                                        mpMaize = mpmaize ? 1 : 0;
                                    }
                                }

                                if (!objectBaseLineYieldIds.getString("mp_bean").equals("null")) {
                                    Boolean mpbean = objectBaseLineYieldIds.getBoolean("mp_bean");
                                    if (mpbean != null) {
                                        mpBean = mpbean ? 1 : 0;
                                    }
                                }

                                if (!objectBaseLineYieldIds.getString("mp_soy").equals("null")) {
                                    Boolean mpsoy = objectBaseLineYieldIds.getBoolean("mp_soy");
                                    if (mpsoy != null) {
                                        mpSoy = mpsoy ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLineYieldIds.getString("mp_other").equals("null")) {
                                    Boolean mpother = objectBaseLineYieldIds.getBoolean("mp_other");
                                    if (mpother != null) {
                                        mpOther = mpother ? 1 : 0;
                                    }
                                }
                                if (objectBaseLineYieldIds.has("harvest_id")) {
                                    harvestId = objectBaseLineYieldIds.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues baseLineYiedValues = new ContentValues();
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_BEAN, mpBean);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_MAIZE, mpMaize);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_SOY, mpSoy);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_OTHER, mpOther);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_SERVER_ID, baseLineYiedId);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_SEASON_ID, seasonId);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_COOP_ID, coopId);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_IS_SYNC, 1);
                                baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.YieldCoop.CONTENT_URI, baseLineYiedValues);
                            }

                            // get base line sales ids (baseline_sales_ids)
                            baseSalesLinesIds = coopObjectInfo.getJSONArray("baseline_sales_ids");
                            for (int m = 0; m < baseSalesLinesIds.length(); m++) {

                                objectBaseLinseSalesIds = baseSalesLinesIds.getJSONObject(i);

                                int lineSaleId = objectBaseLinseSalesIds.getInt("id");

                                Integer prevCycleHarvest = null;
                                Integer prevCycleHarvestPrice = null;
                                Integer prevNonMembersPurchase = null;
                                Integer prevNonMembersPurchaseCost = null;
                                String rgccBuyerContract = null;
                                Double rgccContractVolume = null;
                                Double quantitySoldRgcc = null;
                                Double priceSoldRgcc = null;
                                Double quantitySoldOutsideRgcc = null;
                                Double priceSoldOutsideRgcc = null;
                                Integer rgccBuyerFormal = null;
                                Integer rgccBuyerInformal = null;
                                Integer rgccBuyerOther = null;
                                Long harvestId = null;

                                if (!objectBaseLinseSalesIds.getString("prev_cycle_harvest").equals("null")) {
                                    prevCycleHarvest = objectBaseLinseSalesIds.getInt("prev_cycle_harvest");
                                }

                                if (!objectBaseLinseSalesIds.getString("prev_cycle_harvest_price").equals("null")) {
                                    prevCycleHarvestPrice = objectBaseLinseSalesIds.getInt("prev_cycle_harvest_price");
                                }

                                if (!objectBaseLinseSalesIds.getString("prev_non_members_purchase").equals("null")) {
                                    prevNonMembersPurchase = objectBaseLinseSalesIds.getInt("prev_non_members_purchase");
                                }
                                if (!objectBaseLinseSalesIds.getString("prev_non_members_purchase_cost").equals("null")) {
                                    prevNonMembersPurchaseCost = objectBaseLinseSalesIds.getInt("prev_non_members_purchase_cost");
                                }
                                if (objectBaseLinseSalesIds.has("rgcc_buyer_contract")) {
                                    rgccBuyerContract = objectBaseLinseSalesIds.getString("rgcc_buyer_contract");
                                }

                                if (!objectBaseLinseSalesIds.getString("rgcc_contract_volume").equals("null")) {
                                    rgccContractVolume = objectBaseLinseSalesIds.getDouble("rgcc_contract_volume");
                                }

                                if (!objectBaseLinseSalesIds.getString("quantity_sold_rgcc").equals("null")) {
                                    quantitySoldRgcc = objectBaseLinseSalesIds.getDouble("quantity_sold_rgcc");
                                }

                                if (!objectBaseLinseSalesIds.getString("price_sold_rgcc").equals("null")) {
                                    priceSoldRgcc = objectBaseLinseSalesIds.getDouble("price_sold_rgcc");
                                }
                                if (!objectBaseLinseSalesIds.getString("quantity_sold_outside_rgcc").equals("null")) {
                                    quantitySoldOutsideRgcc = objectBaseLinseSalesIds.getDouble("quantity_sold_outside_rgcc");
                                }
                                if (!objectBaseLinseSalesIds.getString("price_sold_outside_rgcc").equals("null")) {
                                    priceSoldOutsideRgcc = objectBaseLinseSalesIds.getDouble("price_sold_outside_rgcc");
                                }
                                if (!objectBaseLinseSalesIds.getString("rgcc_buyer_formal").equals("null")) {
                                    Boolean rgccbuyerFormal = objectBaseLinseSalesIds.getBoolean("rgcc_buyer_formal");
                                    if (rgccbuyerFormal != null) {
                                        rgccBuyerFormal = rgccbuyerFormal ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinseSalesIds.getString("rgcc_buyer_informal").equals("null")) {
                                    Boolean rgccbuyerInformal = objectBaseLinseSalesIds.getBoolean("rgcc_buyer_informal");
                                    if (rgccbuyerInformal != null) {
                                        rgccBuyerInformal = rgccbuyerInformal ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinseSalesIds.getString("rgcc_buyer_other").equals("null")) {
                                    Boolean rgccbuyerother = objectBaseLinseSalesIds.getBoolean("rgcc_buyer_other");
                                    if (rgccbuyerother != null) {
                                        rgccBuyerOther = rgccbuyerother ? 1 : 0;
                                    }
                                }
                                if (objectBaseLinseSalesIds.has("")) {
                                    harvestId = objectBaseLinseSalesIds.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues saleLinesValues = new ContentValues();
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST, prevCycleHarvest);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST_PRICE, prevCycleHarvestPrice);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE, prevNonMembersPurchase);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE_COST, prevNonMembersPurchaseCost);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_BUYER_CONTRACT, rgccBuyerContract);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CONTRACT_VOLUME, rgccContractVolume);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_RGCC, quantitySoldRgcc);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_RGCC, priceSoldRgcc);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC, quantitySoldOutsideRgcc);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_OUTSIDE_RGCC, priceSoldOutsideRgcc);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_RGCC_INFORMAL, rgccBuyerInformal);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_RGCC_BUYER_FORMAL, rgccBuyerFormal);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_BUYER_OTHER, rgccBuyerOther);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_COOP_ID, coopId);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID, seasonId);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_SERVER_ID, lineSaleId);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC, 1);
                                saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.BaselineSalesCoop.CONTENT_URI, saleLinesValues);

                            }

                            // get base line finance infos (baseline_financeinfos_ids)
                            baseLinesFinanceInfos = coopObjectInfo.getJSONArray("baseline_financeinfos_ids");

                            for (int n = 0; n < baseLinesFinanceInfos.length(); n++) {
                                objectBaseLinesFinancesInfos = baseLinesFinanceInfos.getJSONObject(n);

                                int financeId = objectBaseLinesFinancesInfos.getInt("id");

                                String previousCycleLoan = null;
                                Integer pclpBank = null;
                                Integer pclpCooperative = null;
                                Integer pclpSacco = null;
                                Integer pclpOther = null;
                                Double prevCycleLoanAmount = null;
                                Double prevCycleInterestRate = null;
                                Integer prevCycleLoanDuration = null;
                                Integer pcLoanPurposeLabour = null;
                                Integer pcLoanPurposeSeeds = null;
                                Integer pcLoanPurposeInput = null;
                                Integer pcLoanPurposeMachinery = null;
                                Integer pcLoanPurposeOther = null;
                                Integer aphlpBank = null;
                                Integer aphlpCooperative = null;
                                Integer aphlpSacco = null;
                                Integer aphlpOther = null;
                                Double aphlAmount = null;
                                Double aphlInterestRate = null;
                                Integer aphLoanPurposeLabour = null;
                                Integer aphLoanPurposeInput = null;
                                Integer aphLoanPurposeMachinery = null;
                                Integer aphLoanPurposeOther = null;
                                String aphLoanDisbMethod = null;
                                Long harvestId = null;
                                String agregationPostHarvestLoan = null;
                                String prevCycleLoanDisbMethod = null;
                                Integer aphlDuration = null;

                                if (objectBaseLinesFinancesInfos.has("previous_cycle_loan")) {
                                    previousCycleLoan = objectBaseLinesFinancesInfos.getString("previous_cycle_loan");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pclp_bank").equals("null")) {
                                    Boolean pclpbank = objectBaseLinesFinancesInfos.getBoolean("pclp_bank");
                                    if (pclpbank != null) {
                                        pclpBank = pclpbank ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pclp_cooperative").equals("null")) {
                                    Boolean pclpcooperative = objectBaseLinesFinancesInfos.getBoolean("pclp_cooperative");
                                    if (pclpcooperative != null) {
                                        pclpCooperative = pclpcooperative ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pclp_sacco").equals("null")) {
                                    Boolean pclpsacco = objectBaseLinesFinancesInfos.getBoolean("pclp_sacco");
                                    if (pclpsacco != null) {
                                        pclpSacco = pclpsacco ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pclp_other").equals("null")) {
                                    Boolean pclpother = objectBaseLinesFinancesInfos.getBoolean("pclp_other");
                                    if (pclpother != null) {
                                        pclpOther = pclpother ? 1 : 0;
                                    }
                                }

                                if (!objectBaseLinesFinancesInfos.getString("prev_cycle_loan_amount").equals("null")) {
                                    prevCycleLoanAmount = objectBaseLinesFinancesInfos.getDouble("prev_cycle_loan_amount");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("prev_cycle_interest_rate").equals("null")) {
                                    prevCycleInterestRate = objectBaseLinesFinancesInfos.getDouble("prev_cycle_interest_rate");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("prev_cycle_loan_duration").equals("null")) {
                                    prevCycleLoanDuration = objectBaseLinesFinancesInfos.getInt("prev_cycle_loan_duration");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pc_loan_purpose_labour").equals("null")) {
                                    Boolean pcloanpurposelabour = objectBaseLinesFinancesInfos.getBoolean("pc_loan_purpose_labour");
                                    if (pcloanpurposelabour != null) {
                                        pcLoanPurposeLabour = pcloanpurposelabour ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pc_loan_purpose_seeds").equals("null")) {
                                    Boolean pcLoanPurposeSeed = objectBaseLinesFinancesInfos.getBoolean("pc_loan_purpose_seeds");
                                    if (pcLoanPurposeSeed != null) {
                                        pcLoanPurposeSeeds = pcLoanPurposeSeed ? 1 : 0;
                                    }
                                }

                                if (!objectBaseLinesFinancesInfos.getString("pc_loan_purpose_input").equals("null")) {
                                    Boolean pcloanpurposeinput = objectBaseLinesFinancesInfos.getBoolean("pc_loan_purpose_input");
                                    if (pcloanpurposeinput != null) {
                                        pcLoanPurposeInput = pcloanpurposeinput ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pc_loan_purpose_machinery").equals("null")) {
                                    Boolean pcloanpurposemachinery = objectBaseLinesFinancesInfos.getBoolean("pc_loan_purpose_machinery");
                                    if (pcloanpurposemachinery != null) {
                                        pcLoanPurposeMachinery = pcloanpurposemachinery ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("pc_loan_purpose_other").equals("null")) {
                                    Boolean pcloanpurposeother = objectBaseLinesFinancesInfos.getBoolean("pc_loan_purpose_other");
                                    if (pcloanpurposeother != null) {
                                        pcLoanPurposeMachinery = pcloanpurposeother ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("prev_cycle_loan_disb_method").equals("null")) {
                                    prevCycleLoanDisbMethod = objectBaseLinesFinancesInfos.getString("prev_cycle_loan_disb_method");

                                }
                                if (!objectBaseLinesFinancesInfos.getString("agregation_post_harvest_loan").equals("null")) {
                                    agregationPostHarvestLoan = objectBaseLinesFinancesInfos.getString("agregation_post_harvest_loan");
                                }

                                if (!objectBaseLinesFinancesInfos.getString("aphlp_bank").equals("null")) {
                                    Boolean aphlpbank = objectBaseLinesFinancesInfos.getBoolean("aphlp_bank");
                                    if (aphlpbank != null) {
                                        aphlpBank = aphlpbank ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aphlp_cooperative").equals("null")) {
                                    Boolean aphlpcooperative = objectBaseLinesFinancesInfos.getBoolean("aphlp_cooperative");
                                    if (aphlpcooperative != null) {
                                        aphlpCooperative = aphlpcooperative ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aphlp_sacco").equals("null")) {
                                    Boolean aphlpsacco = objectBaseLinesFinancesInfos.getBoolean("aphlp_sacco");
                                    if (aphlpsacco != null) {
                                        aphlpSacco = aphlpsacco ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aphlp_other").equals("null")) {
                                    Boolean aphlpother = objectBaseLinesFinancesInfos.getBoolean("aphlp_other");
                                    if (aphlpother != null) {
                                        aphlpOther = aphlpother ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aphl_amount").equals("null")) {
                                    aphlAmount = objectBaseLinesFinancesInfos.getDouble("aphl_amount");
                                }

                                if (!objectBaseLinesFinancesInfos.getString("aphl_interest_rate").equals("null")) {
                                    aphlInterestRate = objectBaseLinesFinancesInfos.getDouble("aphl_interest_rate");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aphl_duration").equals("null")) {
                                    aphlDuration = objectBaseLinesFinancesInfos.getInt("aphl_duration");
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aph_loan_purpose_labour").equals("null")) {
                                    Boolean aphloanpurposelabour = objectBaseLinesFinancesInfos.getBoolean("aph_loan_purpose_labour");
                                    aphLoanPurposeLabour = null;
                                    if (aphloanpurposelabour != null) {
                                        aphLoanPurposeLabour = aphloanpurposelabour ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aph_loan_purpose_input").equals("null")) {
                                    Boolean aphloanpurposeinput = objectBaseLinesFinancesInfos.getBoolean("aph_loan_purpose_input");
                                    aphLoanPurposeInput = null;
                                    if (aphloanpurposeinput != null) {
                                        aphLoanPurposeInput = aphloanpurposeinput ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aph_loan_purpose_machinery").equals("null")) {
                                    Boolean aphloanpurposemachinery = objectBaseLinesFinancesInfos.getBoolean("aph_loan_purpose_machinery");
                                    aphLoanPurposeMachinery = null;
                                    if (aphloanpurposemachinery != null) {
                                        aphLoanPurposeMachinery = aphloanpurposemachinery ? 1 : 0;
                                    }
                                }
                                if (!objectBaseLinesFinancesInfos.getString("aph_loan_purpose_other").equals("null")) {
                                    Boolean aphloanpurposeother = objectBaseLinesFinancesInfos.getBoolean("aph_loan_purpose_other");
                                    aphLoanPurposeOther = null;
                                    if (aphloanpurposeother != null) {
                                        aphLoanPurposeOther = aphloanpurposeother ? 1 : 0;
                                    }
                                }
                                if (objectBaseLinesFinancesInfos.has("aph_loan_disb_method")) {
                                    aphLoanDisbMethod = objectBaseLinesFinancesInfos.getString("aph_loan_disb_method");
                                }
                                if (objectBaseLinesFinancesInfos.has("harvest_id")) {
                                    harvestId = objectBaseLinesFinancesInfos.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues financesValues = new ContentValues();
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN, previousCycleLoan);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_BANK, pclpBank);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_SACCO, pclpSacco);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE, pclpCooperative);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_OTHER, pclpOther);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_AMOUNT, prevCycleLoanAmount);

                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_INTEREST_RATE, prevCycleInterestRate);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DURATION, prevCycleLoanDuration);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_INPUT, pcLoanPurposeInput);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_LABOUR, pcLoanPurposeLabour);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_SEEDS, pcLoanPurposeSeeds);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_MACHINERY, pcLoanPurposeMachinery);

                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_OTHER, pcLoanPurposeOther);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DISB_METHOD, prevCycleLoanDisbMethod);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN, agregationPostHarvestLoan);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK, aphlpBank);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO, aphlpCooperative);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE, aphlpSacco);

                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER, aphlpOther);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_AMOUNT, aphlAmount);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_INTEREST_RATE, aphlInterestRate);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DURATION, aphlDuration);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT, aphLoanPurposeInput);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR, aphLoanPurposeLabour);

                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY, aphLoanPurposeMachinery);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER, aphLoanPurposeOther);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD, aphLoanDisbMethod);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID, seasonId);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_COOP_ID, coopId);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_SERVER_ID, financeId);

                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC, 1);
                                financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.FinanceInfoCoop.CONTENT_URI, financesValues);

                            }

                            // get expected yield (expected_yield_ids)
                            expectedYieldIds = coopObjectInfo.getJSONArray("expected_yield_ids");

                            for (int o = 0; o < expectedYieldIds.length(); o++) {

                                objectExpectedYieldsIds = expectedYieldIds.getJSONObject(o);

                                int expectYieldId = objectExpectedYieldsIds.getInt("id");

                                Double totalExpectedCoopProduction = null;
                                Double totalCoopLandSize = null;
                                Double expectedproductionInMt = null;
                                Long harvestId = null;

                                if (!objectExpectedYieldsIds.getString("total_expected_coop_production").equals("null")) {
                                    totalExpectedCoopProduction = objectExpectedYieldsIds.getDouble("total_expected_coop_production");
                                }
                                if (!objectExpectedYieldsIds.getString("total_coop_land_size").equals("null")) {
                                    totalCoopLandSize = objectExpectedYieldsIds.getDouble("total_coop_land_size");
                                }
                                if (!objectExpectedYieldsIds.getString("expected_production_in_mt").equals("null")) {
                                    expectedproductionInMt = objectExpectedYieldsIds.getDouble("expected_production_in_mt");
                                }
                                if (objectExpectedYieldsIds.has("harvest_id")) {
                                    harvestId = objectExpectedYieldsIds.getLong("harvest_id");
                                }

                                int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(harvestId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues expectedValues = new ContentValues();
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_COOP_LAND_SIZE, totalCoopLandSize);
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_PRODUCTION_MT, expectedproductionInMt);
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_COOP_PRODUCTION, totalExpectedCoopProduction);
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_SERVER_ID, expectYieldId);
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_SEASON_ID, seasonId);
                                expectedValues.put(BfwContract.ExpectedYieldCoop.COLUMN_COOP_ID, coopId);

                                getContentResolver().insert(BfwContract.ExpectedYieldCoop.CONTENT_URI, expectedValues);

                            }
                        }

                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (IOException | JSONException exp) {
                return false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return true;
        }

        private boolean prefetchLoan(String token, OkHttpClient client) {
            getContentResolver().delete(BfwContract.Loan.CONTENT_URI, null, null);
            Cursor cursor = null;
            try {
                Response loanData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "res.partner.loan");
                if (loanData != null) {
                    ResponseBody loanInfo = loanData.body();
                    if (loanInfo != null) {
                        String loanList = loanInfo.string();

                        JSONObject loanInfos = new JSONObject(loanList);
                        JSONArray loanArray = loanInfos.getJSONArray("results");
                        JSONObject loanObject;

                        for (int s = 0; s < loanArray.length(); s++) {
                            loanObject = loanArray.getJSONObject(s);

                            String name = null;
                            Integer farmer_id = null;//json array
                            Integer coop_id = null;
                            Integer vendor_id = null; // json array
                            Long start_date = null;
                            Double amount = null;
                            Double interest_rate = null;
                            Double duration = null;
                            String purpose = null;
                            String financial_institution = null;
                            Integer amount_due = null;
                            Integer amount_total = null;
                            String state = null;
                            Date startDate = null;

                            int loanId = loanObject.getInt("id");

                            name = loanObject.getString("name");
                            purpose = loanObject.getString("purpose");
                            financial_institution = loanObject.getString("financial_institution");

                            if (!loanObject.getString("coop_id").equals("null")) {
                                coop_id = loanObject.getInt("coop_id");
                            }
                            if (!loanObject.getString("start_date").equals("null")) {
                                // convert data
                                String getDate = loanObject.getString("start_date");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                startDate = df.parse(getDate);
                            }
                            if (!loanObject.getString("amount").equals("null")) {
                                amount = loanObject.getDouble("amount");
                            }
                            if (!loanObject.getString("interest_rate").equals("null")) {
                                interest_rate = loanObject.getDouble("interest_rate");
                            }
                            if (!loanObject.getString("duration").equals("null")) {
                                duration = loanObject.getDouble("duration");
                            }
                            if (!loanObject.getString("amount_due").equals("null")) {
                                amount_due = loanObject.getInt("amount_due");
                            }
                            if (!loanObject.getString("amount_total").equals("null")) {
                                amount_total = loanObject.getInt("amount_total");
                            }
                            if (!loanObject.getString("state").equals("null")) {
                                state = loanObject.getString("state");
                            }

                            JSONArray partenerArray;
                            JSONArray vendorArray;

                            JSONObject partenerObject;
                            JSONObject vendorObject;

                            partenerArray = loanObject.getJSONArray("partner_id");
                            vendorArray = loanObject.getJSONArray("vendor_id");

                            for (int a = 0; a < partenerArray.length(); a++) {
                                partenerObject = partenerArray.getJSONObject(a);
                                farmer_id = partenerObject.getInt("id");

                                String partenerSelect = BfwContract.Farmer.TABLE_NAME + "." +
                                        BfwContract.Farmer.COLUMN_FARMER_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null,
                                        partenerSelect,
                                        new String[]{Long.toString(farmer_id)},
                                        null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        farmer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer._ID));
                                    }
                                }
                            }

                            for (int a = 0; a < vendorArray.length(); a++) {
                                vendorObject = vendorArray.getJSONObject(a);
                                vendor_id = vendorObject.getInt("id");

                                String vendorSelect = BfwContract.Vendor.TABLE_NAME + "." +
                                        BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null,
                                        vendorSelect,
                                        new String[]{Long.toString(vendor_id)},
                                        null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        vendor_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor._ID));
                                    }
                                }
                            }

                            ContentValues loanValues = new ContentValues();

                            loanValues.put(BfwContract.Loan.COLUMN_SERVER_ID, loanId);
                            loanValues.put(BfwContract.Loan.COLUMN_NAME, name);
                            loanValues.put(BfwContract.Loan.COLUMN_PURPOSE, purpose);
                            loanValues.put(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION, financial_institution);
                            loanValues.put(BfwContract.Loan.COLUMN_COOP_ID, coop_id);
                            loanValues.put(BfwContract.Loan.COLUMN_START_DATE, startDate.getTime());
                            loanValues.put(BfwContract.Loan.COLUMN_AMOUNT, amount);
                            loanValues.put(BfwContract.Loan.COLUMN_INTEREST_RATE, interest_rate);
                            loanValues.put(BfwContract.Loan.COLUMN_DURATION, duration);
                            loanValues.put(BfwContract.Loan.COLUMN_AMOUNT_DUE, amount_due);
                            loanValues.put(BfwContract.Loan.COLUMN_AMOUNT_TOTAL, amount_total);
                            loanValues.put(BfwContract.Loan.COLUMN_STATE, state);

                            loanValues.put(BfwContract.Loan.COLUMN_FARMER_ID, farmer_id);
                            loanValues.put(BfwContract.Loan.COLUMN_VENDOR_ID, vendor_id);

                            loanValues.put(BfwContract.Loan.COLUMN_IS_SYNC, 1);
                            loanValues.put(BfwContract.Loan.COLUMN_IS_UPDATE, 1);

                            Uri loanUri = getContentResolver().insert(BfwContract.Loan.CONTENT_URI, loanValues);

                            long localLoanId = ContentUris.parseId(loanUri);

                            //declaration des array et leurs objects
                            JSONArray lineArray;
                            JSONArray paymentArray;

                            JSONObject lineObject;
                            JSONObject paymentObject;


                            lineArray = loanObject.getJSONArray("line_ids");

                            for (int a = 0; a < lineArray.length(); a++) {
                                lineObject = lineArray.getJSONObject(a);

                                Date payment_date = null;
                                Double principal = null;
                                Double interest = null;
                                Double remaining_amount = null;
                                Double next_payment_amount = null;
                                int loan_line_id = 0;

                                if (!lineObject.getString("payment_date").equals("null")) {
                                    //payment_date = lineObject.getLong("payment_date");
                                    String getDate = lineObject.getString("payment_date");
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                    payment_date = df.parse(getDate);
                                }
                                if (!lineObject.getString("principal").equals("null")) {
                                    principal = lineObject.getDouble("principal");
                                }
                                if (!lineObject.getString("interest").equals("null")) {
                                    interest = lineObject.getDouble("interest");
                                }
                                if (!lineObject.getString("remaining_amount").equals("null")) {
                                    remaining_amount = lineObject.getDouble("remaining_amount");
                                }
                                if (!lineObject.getString("next_payment_amount").equals("null")) {
                                    next_payment_amount = lineObject.getDouble("next_payment_amount");
                                }
                                if (!lineObject.getString("id").equals("null")) {
                                    loan_line_id = lineObject.getInt("id");
                                }

                                ContentValues lineValues = new ContentValues();

                                lineValues.put(BfwContract.LoanLine.COLUMN_LOAN_ID, localLoanId);
                                lineValues.put(BfwContract.LoanLine.COLUMN_PAYMENT_DATE, payment_date.getTime());
                                lineValues.put(BfwContract.LoanLine.COLUMN_PRINCIPAL, principal);
                                lineValues.put(BfwContract.LoanLine.COLUMN_INTEREST, interest);
                                lineValues.put(BfwContract.LoanLine.COLUMN_REMAINING_AMOUNT, remaining_amount);
                                lineValues.put(BfwContract.LoanLine.COLUMN_NEXT_PAYMENT_AMOUNT, next_payment_amount);
                                lineValues.put(BfwContract.LoanLine.COLUMN_SERVER_ID, loan_line_id);

                                getContentResolver().insert(BfwContract.LoanLine.CONTENT_URI, lineValues);
                            }

                            paymentArray = loanObject.getJSONArray("payment_ids");

                            for (int a = 0; a < paymentArray.length(); a++) {
                                paymentObject = paymentArray.getJSONObject(a);

                                Date payment_date = null;
                                Double amount_payment = null;
                                int loan_payment_id = 0;

                                if (!paymentObject.getString("payment_date").equals("null")) {
                                    //payment_date = paymentObject.getLong("payment_date");
                                    String getDate = paymentObject.getString("payment_date");
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                    payment_date = df.parse(getDate);
                                }

                                if (!paymentObject.getString("amount").equals("null")) {
                                    amount_payment = paymentObject.getDouble("amount");
                                }
                                if (!paymentObject.getString("id").equals("null")) {
                                    loan_payment_id = paymentObject.getInt("id");
                                }

                                ContentValues paymentValues = new ContentValues();

                                paymentValues.put(BfwContract.LoanPayment.COLUMN_LOAN_ID, localLoanId);
                                paymentValues.put(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE, payment_date.getTime());
                                paymentValues.put(BfwContract.LoanPayment.COLUMN_AMOUNT, amount_payment);
                                paymentValues.put(BfwContract.LoanPayment.COLUMN_SERVER_ID, loan_payment_id);

                                paymentValues.put(BfwContract.LoanPayment.COLUMN_IS_SYNC, 1);
                                paymentValues.put(BfwContract.LoanPayment.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.LoanPayment.CONTENT_URI, paymentValues);
                            }
                        }

                    }
                } else {
                    return false;
                }
            } catch (IOException | JSONException exp) {
                return false;
            } catch (ParseException e) {
                return false;
            }
            return true;
        }

        private boolean prefetchProduct(String token, OkHttpClient client) {
            getContentResolver().delete(BfwContract.ProductTemplate.CONTENT_URI, null, null);
            Cursor cursor = null;
            try {
                Response productData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "product.template");
                if (productData != null) {
                    ResponseBody productInfo = productData.body();
                    if (productInfo != null) {
                        String productList = productInfo.string();

                        JSONObject productInfos = new JSONObject(productList);
                        JSONArray productArray = productInfos.getJSONArray("results");
                        JSONObject productObject;

                        for (int s = 0; s < productArray.length(); s++) {
                            productObject = productArray.getJSONObject(s);

                            //declaration des array et leurs objects
                            JSONArray farmerArray;
                            JSONArray buyerArray;
                            JSONArray vendorArray;

                            JSONObject farmerObject;
                            JSONObject buyerObject;
                            JSONObject vendorObject;

                            Integer product_harvest_season = null;
                            String product_name = null;
                            String product_harvest_grade = null;
                            String product_state = null;
                            Integer product_vendor_qty = null;
                            Double product_price = null;

                            Integer product_farmer_id = null;
                            Integer product_buyer_id = null;
                            Integer product_vendor_id = null;

                            int productId = productObject.getInt("id");
                            product_name = productObject.getString("name");

                            if (!productObject.getString("harvest_season").equals("null")) {
                                int serverID_harvest_season = productObject.getInt("harvest_season");

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";
                                //int seasonId = 0;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(serverID_harvest_season)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        product_harvest_season = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }
                            }

                            product_harvest_grade = productObject.getString("harvest_grade");
                            product_state = productObject.getString("state");

                            if (!productObject.getString("vendor_qty").equals("null")) {
                                product_vendor_qty = productObject.getInt("vendor_qty");
                            }

                            if (!productObject.getString("standard_price").equals("null")) {
                                product_price = productObject.getDouble("standard_price");
                            }

                            farmerArray = productObject.getJSONArray("farmer_id");
                            vendorArray = productObject.getJSONArray("vendor_farmer_id");
                            buyerArray = productObject.getJSONArray("buyer_id");

                            for (int a = 0; a < farmerArray.length(); a++) {
                                farmerObject = farmerArray.getJSONObject(a);
                                product_farmer_id = farmerObject.getInt("id");
                                int farmerIdLocal = 0;

                                String farmerSelect = BfwContract.Farmer.TABLE_NAME + "." +
                                        BfwContract.Farmer.COLUMN_FARMER_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null,
                                        farmerSelect,
                                        new String[]{Long.toString(product_farmer_id)},
                                        null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        product_farmer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                            }

                            for (int a = 0; a < vendorArray.length(); a++) {
                                vendorObject = vendorArray.getJSONObject(a);
                                product_vendor_id = vendorObject.getInt("id");
                                int vendorIdLocal = 0;

                                String vendorSelect = BfwContract.Vendor.TABLE_NAME + "." +
                                        BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null,
                                        vendorSelect,
                                        new String[]{Long.toString(product_vendor_id)},
                                        null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        product_vendor_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor._ID));
                                    }
                                }
                            }

                            for (int a = 0; a < buyerArray.length(); a++) {
                                buyerObject = buyerArray.getJSONObject(a);
                                product_buyer_id = buyerObject.getInt("id");
                                int buyerIdLocal = 0;

                                String buyerSelect = BfwContract.Buyer.TABLE_NAME + "." +
                                        BfwContract.Buyer.COLUMN_BUYER_SERVER_ID + " =  ? ";

                                cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null,
                                        buyerSelect,
                                        new String[]{Long.toString(product_buyer_id)},
                                        null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        product_buyer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Buyer._ID));
                                    }
                                }
                            }


                            ContentValues productValues = new ContentValues();

                            productValues.put(BfwContract.ProductTemplate.COLUMN_SERVER_ID, productId);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON, product_harvest_season);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME, product_name);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_HARVEST_GRADE, product_harvest_grade);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_STATE, product_state);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_VENDOR_QTY, product_vendor_qty);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_FARMER_ID, product_farmer_id);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_PRICE, product_price);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_BUYER_ID, product_buyer_id);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_VENDOR_ID, product_vendor_id);

                            productValues.put(BfwContract.ProductTemplate.COLUMN_IS_SYNC, 1);
                            productValues.put(BfwContract.ProductTemplate.COLUMN_IS_UPDATE, 1);

                            getContentResolver().insert(BfwContract.ProductTemplate.CONTENT_URI, productValues);

                        }

                    }
                } else {
                    return false;
                }
            } catch (IOException | JSONException exp) {
                return false;
            }
            return true;
        }


        private boolean prefetchCoopAgent(String token, OkHttpClient client) {
            getContentResolver().delete(BfwContract.CoopAgent.CONTENT_URI, null, null);
            try {
                Response buyerData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "coop.agent");
                if (buyerData != null) {
                    ResponseBody buyerInfo = buyerData.body();
                    if (buyerInfo != null) {
                        String agentList = buyerInfo.string();

                        JSONObject agentCoopsInfos = new JSONObject(agentList);
                        JSONArray agentCoopsArray = agentCoopsInfos.getJSONArray("results");
                        JSONObject agentObject;

                        for (int s = 0; s < agentCoopsArray.length(); s++) {
                            agentObject = agentCoopsArray.getJSONObject(s);

                            int agentId = agentObject.getInt("id");
                            String name = agentObject.getString("name");
                            String phoneNumber = agentObject.getString("phone");
                            String agentEmail = agentObject.getString("email");
                            int agentUserId = agentObject.getInt("user_id");
                            int coopId = agentObject.getInt("coop_id");

                            ContentValues agentValues = new ContentValues();

                            agentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_NAME, name);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_EMAIL, agentEmail);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_PHONE, phoneNumber);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_USER_ID, agentUserId);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_SERVER_ID, agentId);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_COOP_ID, coopId);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_IS_SYNC, 1);
                            agentValues.put(BfwContract.CoopAgent.COLUMN_IS_UPDATE, 1);

                            getContentResolver().insert(BfwContract.CoopAgent.CONTENT_URI, agentValues);

                        }

                    }
                } else {
                    return false;
                }
            } catch (IOException | JSONException exp) {
                return false;
            }
            return true;
        }

        private boolean prefetchBuyer(OkHttpClient client, String token, String url, boolean isBuyer) {
            getContentResolver().delete(BfwContract.Buyer.CONTENT_URI, null, null);
            try {
                Response buyerData = Utils.getServerData(client, token, url);
                if (buyerData != null) {
                    ResponseBody buyerInfo = buyerData.body();
                    if (buyerInfo != null) {
                        String buyersList = buyerInfo.string();

                        JSONObject buyersCoopsInfos = new JSONObject(buyersList);
                        JSONArray buyersCoopsArray = null;

                        if (!isBuyer) {
                            buyersCoopsArray = buyersCoopsInfos.getJSONArray("results");
                        } else {
                            String filterServeResponse = buyersCoopsInfos.getString("response");
                            JSONObject filterServerObject = new JSONObject(filterServeResponse);
                            buyersCoopsArray = filterServerObject.getJSONArray("results");
                        }

                        JSONObject buyerObject;

                        for (int s = 0; s < buyersCoopsArray.length(); s++) {
                            buyerObject = buyersCoopsArray.getJSONObject(s);

                            int buyerId = buyerObject.getInt("id");
                            String name = buyerObject.getString("name");
                            String phoneNumber = buyerObject.getString("buyer_phone");
                            String buyerEmail = buyerObject.getString("buyer_email");
                            int buyerUserId = buyerObject.getInt("user_id");

                            ContentValues buyerValues = new ContentValues();

                            buyerValues.put(BfwContract.Buyer.COLUMN_BUYER_NAME, name);
                            buyerValues.put(BfwContract.Buyer.COLUMN_BUYER_EMAIL, buyerEmail);
                            buyerValues.put(BfwContract.Buyer.COLUMN_BUYER_PHONE, phoneNumber);
                            buyerValues.put(BfwContract.Buyer.COLUMN_USER_ID, buyerUserId);
                            buyerValues.put(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID, buyerId);
                            buyerValues.put(BfwContract.Buyer.COLUMN_IS_SYNC, 1);
                            buyerValues.put(BfwContract.Buyer.COLUMN_IS_UPDATE, 1);

                            getContentResolver().insert(BfwContract.Buyer.CONTENT_URI, buyerValues);

                        }

                    }
                } else {
                    return false;
                }
            } catch (IOException | JSONException exp) {
                return false;
            }
            return true;
        }

        private boolean prefetchVendor(OkHttpClient client, String token, String url, boolean isAgent) {
            getContentResolver().delete(BfwContract.Vendor.CONTENT_URI, null, null);
            Cursor cursor = null;
            try {
                Response farmerData = Utils.getServerData(client, token, url);
                if (farmerData != null) {
                    ResponseBody farmerInfo = farmerData.body();
                    if (farmerInfo != null) {
                        String farmersList = farmerInfo.string();

                        JSONObject farmersJsonObject = new JSONObject(farmersList);

                        JSONArray farmerArrayLists;
                        if (!isAgent) {
                            farmerArrayLists = farmersJsonObject.getJSONArray("results");
                        } else {
                            String filterResponse = farmersJsonObject.getString("response");
                            JSONObject filterServerObject = new JSONObject(filterResponse);
                            farmerArrayLists = filterServerObject.getJSONArray("results");
                        }
                        JSONObject farmerObjectInfo;

                        JSONArray accessInfosArray;
                        JSONArray farmerLandIdsArray;
                        JSONArray forecastFarmerArray;
                        JSONArray financeDataArray;
                        JSONArray baseLinesArray;

                        JSONObject accessInfoObject;
                        JSONObject farmerLandObject;
                        JSONObject forecastObject;
                        JSONObject financeDataObject;
                        JSONObject baseLinesObject;

                        for (int t = 0; t < farmerArrayLists.length(); t++) {

                            farmerObjectInfo = farmerArrayLists.getJSONObject(t);

                            int farmerId = farmerObjectInfo.getInt("id");

                            String name = null;
                            String cellPhone = null;
                            String address = null;
                            String gender = null;
                            Integer headOfHousehold = null;
                            Integer numhouseholdmembers = null;
                            String firstName = null;
                            String lastName = null;
                            String cellCarrier = null;
                            String cellPhoneAlt = null;
                            String membershipId = null;
                            String storageDetails = null;
                            String otherDetails = null;
                            Integer safestorage = null;
                            Integer tractors = null;
                            Integer harvester = null;
                            Integer dryer = null;
                            Integer thresher = null;
                            Integer other = null;
                            String otherWaterSource = null;

                            Integer mwsDam = null;
                            Integer well = null;
                            Integer boreFhole = null;
                            Integer rs = null;
                            Integer mwspb = null;
                            Integer mwsIrrigation = null;
                            Integer none = null;
                            Integer mwsOther = null;
                            Double totalLandPlotSize = null;

                            if (farmerObjectInfo.has("name")) {
                                name = farmerObjectInfo.getString("name");
                            }
                            if (farmerObjectInfo.has("gender")) {
                                gender = farmerObjectInfo.getString("gender");
                            }
                            if (farmerObjectInfo.has("address")) {
                                address = farmerObjectInfo.getString("address");
                            }
                            if (farmerObjectInfo.has("phone")) {
                                cellPhone = farmerObjectInfo.getString("phone");
                            }
                            if (!farmerObjectInfo.getString("num_household_members").equals("null")) {
                                numhouseholdmembers = farmerObjectInfo.getInt("num_household_members");
                            }
                            if (farmerObjectInfo.has("cellphone_alt")) {
                                cellPhoneAlt = farmerObjectInfo.getString("cellphone_alt");
                            }

                            if (farmerObjectInfo.has("cell_carrier")) {
                                cellCarrier = farmerObjectInfo.getString("cell_carrier");
                            }
                            if (farmerObjectInfo.has("spouse_lastname")) {
                                lastName = farmerObjectInfo.getString("spouse_lastname");
                            }
                            if (farmerObjectInfo.has("spouse_firstname")) {
                                firstName = farmerObjectInfo.getString("spouse_firstname");
                            }
                            if (!farmerObjectInfo.getString("head_of_household").equals("null")) {
                                Boolean headHousehold = farmerObjectInfo.getBoolean("head_of_household");
                                if (headHousehold != null) {
                                    headOfHousehold = headHousehold ? 1 : 0;
                                }
                            }
                            if (farmerObjectInfo.has("membership_id")) {
                                membershipId = farmerObjectInfo.getString("membership_id");
                            }
                            if (farmerObjectInfo.has("storage_details")) {
                                storageDetails = farmerObjectInfo.getString("storage_details");
                            }

                            if (farmerObjectInfo.has("other_details")) {
                                otherDetails = farmerObjectInfo.getString("other_details");
                            }
                            if (!farmerObjectInfo.getString("ar_safestorage").equals("null")) {
                                Boolean arSafestorage = farmerObjectInfo.getBoolean("ar_safestorage");
                                if (arSafestorage != null) {
                                    safestorage = arSafestorage ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_tractors").equals("null")) {
                                Boolean arTractors = farmerObjectInfo.getBoolean("ar_tractors");
                                if (arTractors != null) {
                                    tractors = arTractors ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_harverster").equals("null")) {
                                Boolean arHarverster = farmerObjectInfo.getBoolean("ar_harverster");
                                if (arHarverster != null) {
                                    harvester = arHarverster ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_dryer").equals("null")) {
                                Boolean arDryer = farmerObjectInfo.getBoolean("ar_dryer");
                                if (arDryer != null) {
                                    dryer = arDryer ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_thresher").equals("null")) {
                                thresher = null;
                                Boolean arThresher = farmerObjectInfo.getBoolean("ar_thresher");
                                if (arThresher != null) {
                                    thresher = arThresher ? 1 : 0;
                                }
                            }

                            if (!farmerObjectInfo.getString("ar_other").equals("null")) {
                                Boolean arOther = farmerObjectInfo.getBoolean("ar_other");
                                if (arOther != null) {
                                    other = arOther ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_dam").equals("null")) {
                                Boolean dam = farmerObjectInfo.getBoolean("mws_dam");
                                if (dam != null) {
                                    mwsDam = dam ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_well").equals("null")) {
                                Boolean msWell = farmerObjectInfo.getBoolean("mws_well");
                                if (msWell != null) {
                                    well = msWell ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_borehole").equals("null")) {
                                Boolean mwsBorehole = farmerObjectInfo.getBoolean("mws_borehole");
                                if (boreFhole != null) {
                                    boreFhole = mwsBorehole ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_rs").equals("null")) {
                                rs = null;
                                Boolean mwsrs = farmerObjectInfo.getBoolean("mws_rs");
                                if (mwsrs != null) {
                                    rs = mwsrs ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_pb").equals("null")) {
                                Boolean pb = farmerObjectInfo.getBoolean("mws_pb");
                                if (pb != null) {
                                    mwspb = pb ? 1 : 0;
                                }
                            }

                            if (!farmerObjectInfo.getString("mws_irrigation").equals("null")) {
                                Boolean irrigation = farmerObjectInfo.getBoolean("mws_irrigation");
                                if (irrigation != null) {
                                    mwsIrrigation = irrigation ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_none").equals("null")) {
                                Boolean mwsnone = farmerObjectInfo.getBoolean("mws_none");
                                if (mwsnone != null) {
                                    none = mwsnone ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_other").equals("null")) {
                                Boolean mwother = farmerObjectInfo.getBoolean("mws_other");
                                if (mwother != null) {
                                    mwsOther = mwother ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("total_land_plot_size").equals("null")) {
                                totalLandPlotSize = farmerObjectInfo.getDouble("total_land_plot_size");
                            }
                            // TODO Check well this field
                            if (farmerObjectInfo.has("other_water_source")) {
                                otherWaterSource = farmerObjectInfo.getString("other_water_source");
                            }

                            ContentValues farmerValues = new ContentValues();

                            farmerValues.put(BfwContract.Vendor.COLUMN_NAME, name);
                            farmerValues.put(BfwContract.Vendor.COLUMN_PHONE, cellPhone);
                            farmerValues.put(BfwContract.Vendor.COLUMN_GENDER, gender);
                            farmerValues.put(BfwContract.Vendor.COLUMN_ADDRESS, address);
                            farmerValues.put(BfwContract.Vendor.COLUMN_HOUSEHOLD_HEAD, headOfHousehold);
                            farmerValues.put(BfwContract.Vendor.COLUMN_HOUSE_MEMBER, numhouseholdmembers);
                            farmerValues.put(BfwContract.Vendor.COLUMN_FIRST_NAME, firstName);

                            farmerValues.put(BfwContract.Vendor.COLUMN_LAST_NAME, lastName);
                            farmerValues.put(BfwContract.Vendor.COLUMN_CELL_PHONE, cellPhoneAlt);
                            farmerValues.put(BfwContract.Vendor.COLUMN_CELL_CARRIER, cellCarrier);
                            farmerValues.put(BfwContract.Vendor.COLUMN_MEMBER_SHIP, membershipId);
                            farmerValues.put(BfwContract.Vendor.COLUMN_STORAGE_DETAIL, storageDetails);
                            farmerValues.put(BfwContract.Vendor.COLUMN_NEW_SOURCE_DETAIL, otherDetails);
                            farmerValues.put(BfwContract.Vendor.COLUMN_SAFE_STORAGE, safestorage);

                            farmerValues.put(BfwContract.Vendor.COLUMN_TRACTORS, tractors);
                            farmerValues.put(BfwContract.Vendor.COLUMN_HARVESTER, harvester);
                            farmerValues.put(BfwContract.Vendor.COLUMN_DRYER, dryer);
                            farmerValues.put(BfwContract.Vendor.COLUMN_TRESHER, thresher);
                            farmerValues.put(BfwContract.Vendor.COLUMN_OTHER_INFO , other);
                            farmerValues.put(BfwContract.Vendor.COLUMN_WATER_SOURCE_DETAILS, otherWaterSource);
                            farmerValues.put(BfwContract.Vendor.COLUMN_DAM, mwsDam);

                            farmerValues.put(BfwContract.Vendor.COLUMN_WELL, well);
                            farmerValues.put(BfwContract.Vendor.COLUMN_BOREHOLE, boreFhole);
                            farmerValues.put(BfwContract.Vendor.COLUMN_RIVER_STREAM, rs);
                            farmerValues.put(BfwContract.Vendor.COLUMN_PIPE_BORNE, mwspb);
                            farmerValues.put(BfwContract.Vendor.COLUMN_IRRIGATION, mwsIrrigation);
                            farmerValues.put(BfwContract.Vendor.COLUMN_NONE, none);
                            farmerValues.put(BfwContract.Vendor.COLUMN_OTHER, mwsOther);

                            farmerValues.put(BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID, farmerId);
                            farmerValues.put(BfwContract.Vendor.COLUMN_IS_SYNC, 1);
                            farmerValues.put(BfwContract.Vendor.COLUMN_IS_UPDATE, 1);

                            farmerValues.put(BfwContract.Vendor.COLUMN_TOTAL_PLOT_SIZE, totalLandPlotSize);

                            Uri farmerUri = getContentResolver().insert(BfwContract.Vendor.CONTENT_URI, farmerValues);
                            long localFarmerId = ContentUris.parseId(farmerUri);

                            accessInfosArray = farmerObjectInfo.getJSONArray("access_info_ids");

                            for (int a = 0; a < accessInfosArray.length(); a++) {

                                accessInfoObject = accessInfosArray.getJSONObject(a);

                                int farmerInfoId = accessInfoObject.getInt("id");

                                Integer aes = null;
                                Integer cri = null;
                                Integer seeds = null;
                                Integer orgFert = null;
                                Integer inorgFert = null;
                                Integer labour = null;
                                Integer iwp = null;
                                Integer ss = null;
                                long infoSeasonId = 1;

                                if (accessInfoObject.has("ar_aes")) {
                                    if (!accessInfoObject.getString("ar_aes").equals("null")) {
                                        Boolean arAes = accessInfoObject.getBoolean("ar_aes");
                                        if (arAes != null) {
                                            aes = arAes ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_cri")) {
                                    if (!accessInfoObject.getString("ar_cri").equals("null")) {
                                        Boolean arCri = accessInfoObject.getBoolean("ar_cri");
                                        if (arCri != null) {
                                            cri = arCri ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_seeds")) {
                                    if (!accessInfoObject.getString("ar_seeds").equals("null")) {
                                        Boolean arSeeds = accessInfoObject.getBoolean("ar_seeds");
                                        if (arSeeds != null) {
                                            seeds = arSeeds ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_of")) {
                                    if (!accessInfoObject.getString("ar_of").equals("null")) {
                                        Boolean arof = accessInfoObject.getBoolean("ar_of");
                                        if (arof != null) {
                                            orgFert = arof ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_if")) {
                                    if (!accessInfoObject.getString("ar_if").equals("null")) {
                                        Boolean arif = accessInfoObject.getBoolean("ar_if");
                                        if (arif != null) {
                                            inorgFert = arif ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_labour")) {
                                    if (!accessInfoObject.getString("ar_labour").equals("null")) {
                                        Boolean arlabour = accessInfoObject.getBoolean("ar_labour");
                                        if (arlabour != null) {
                                            labour = arlabour ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_iwp")) {
                                    if (!accessInfoObject.getString("ar_iwp").equals("null")) {
                                        Boolean ariwp = accessInfoObject.getBoolean("ar_iwp");
                                        if (ariwp != null) {
                                            iwp = ariwp ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_ss")) {
                                    if (!accessInfoObject.getString("ar_ss").equals("null")) {
                                        Boolean arss = accessInfoObject.getBoolean("ar_ss");
                                        if (arss != null) {
                                            ss = arss ? 1 : 0;
                                        }
                                    }
                                }

                                if (accessInfoObject.has("harvest_id")) {
                                    if (!accessInfoObject.getString("harvest_id").equals("null")) {
                                        infoSeasonId = accessInfoObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(infoSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues farmerInfoValues = new ContentValues();
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV, aes);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO, cri);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEEDS, seeds);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER, orgFert);

                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorgFert);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_LABOUR, labour);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS, iwp);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SPRAYERS, ss);

                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, 1);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 1);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID, seasonId);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID, localFarmerId);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SERVER_ID, farmerInfoId);

                                getContentResolver().insert(BfwContract.VendorAccessInfo.CONTENT_URI, farmerInfoValues);
                            }

                            farmerLandIdsArray = farmerObjectInfo.getJSONArray("vendor_land_ids");

                            for (int b = 0; b < farmerLandIdsArray.length(); b++) {

                                farmerLandObject = farmerLandIdsArray.getJSONObject(b);
                                int landServerId = farmerLandObject.getInt("id");

                                Double plot_size = null;
                                Double lat = null;
                                Double lng = null;
                                long landSeasonId = 1;

                                if (farmerLandObject.has("plot_size")) {
                                    if (!farmerLandObject.getString("plot_size").equals("null")) {
                                        plot_size = farmerLandObject.getDouble("plot_size");
                                    }
                                }
                                if (farmerLandObject.has("lat")) {
                                    if (!farmerLandObject.getString("lat").equals("null")) {
                                        lat = farmerLandObject.getDouble("lat");
                                    }
                                }
                                if (farmerLandObject.has("lng")) {
                                    if (!farmerLandObject.getString("lng").equals("null")) {
                                        lng = farmerLandObject.getDouble("lng");
                                    }
                                }

                                if (farmerLandObject.has("harvest_id")) {
                                    if (!farmerLandObject.getString("harvest_id").equals("null")) {
                                        landSeasonId = farmerLandObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(landSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues farmerLandValues = new ContentValues();
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_PLOT_SIZE, plot_size);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_LAT_INFO, lat);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_LNG_INFO, lng);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_SERVER_ID, landServerId);

                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_SEASON_ID, seasonId);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 1);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 1);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_VENDOR_ID, localFarmerId);

                                getContentResolver().insert(BfwContract.VendorLand.CONTENT_URI, farmerLandValues);

                            }

                            forecastFarmerArray = farmerObjectInfo.getJSONArray("forecast_vendor_ids");
                            for (int c = 0; c < forecastFarmerArray.length(); c++) {

                                forecastObject = forecastFarmerArray.getJSONObject(c);

                                int forecastServerId = forecastObject.getInt("id");

                                Double totalArableLandPlots = null;
                                Double expectedProductionInMt = null;
                                Double forecastedyieldmt = null;
                                Double forecastedharvestsalevalue = null;
                                Double totalcooplandsize = null;
                                Double farmerpercentageland = null;
                                Double currentpppcommitment = null;
                                Double farmercontributionppp = null;
                                Double farmerexpectedminppp = null;
                                Double minimumflowprice = null;
                                long forecastSeasonId = 1;

                                if (forecastObject.has("total_arable_land_plots")) {
                                    if (!forecastObject.getString("total_arable_land_plots").equals("null")) {
                                        totalArableLandPlots = forecastObject.getDouble("total_arable_land_plots");
                                    }
                                }
                                if (forecastObject.has("expected_production_in_mt")) {
                                    if (!forecastObject.getString("expected_production_in_mt").equals("null")) {
                                        expectedProductionInMt = forecastObject.getDouble("expected_production_in_mt");
                                    }
                                }

                                if (forecastObject.has("forecasted_yield_mt")) {
                                    if (!forecastObject.getString("forecasted_yield_mt").equals("null")) {
                                        forecastedyieldmt = forecastObject.getDouble("forecasted_yield_mt");
                                    }
                                }
                                if (forecastObject.has("forecasted_harvest_sale_value")) {
                                    if (!forecastObject.getString("forecasted_harvest_sale_value").equals("null")) {
                                        forecastedharvestsalevalue = forecastObject.getDouble("forecasted_harvest_sale_value");
                                    }
                                }
                                if (forecastObject.has("total_coop_land_size")) {
                                    if (!forecastObject.getString("total_coop_land_size").equals("null")) {
                                        totalcooplandsize = forecastObject.getDouble("total_coop_land_size");
                                    }
                                }
                                if (forecastObject.has("vendor_percentage_land")) {
                                    if (!forecastObject.getString("vendor_percentage_land").equals("null")) {
                                        farmerpercentageland = forecastObject.getDouble("vendor_percentage_land");
                                    }
                                }
                                if (forecastObject.has("current_ppp_commitment")) {
                                    if (!forecastObject.getString("current_ppp_commitment").equals("null")) {
                                        currentpppcommitment = forecastObject.getDouble("current_ppp_commitment");
                                    }
                                }

                                if (forecastObject.has("vendor_contribution_ppp")) {
                                    if (!forecastObject.getString("vendor_contribution_ppp").equals("null")) {
                                        farmercontributionppp = forecastObject.getDouble("vendor_contribution_ppp");
                                    }
                                }
                                if (forecastObject.has("vendor_expected_min_ppp")) {
                                    if (!forecastObject.getString("vendor_expected_min_ppp").equals("null")) {
                                        farmerexpectedminppp = forecastObject.getDouble("vendor_expected_min_ppp");
                                    }
                                }
                                if (forecastObject.has("minimum_flow_price")) {
                                    if (!forecastObject.getString("minimum_flow_price").equals("null")) {
                                        minimumflowprice = forecastObject.getDouble("minimum_flow_price");
                                    }
                                }

                                if (forecastObject.has("harvest_id")) {
                                    if (!forecastObject.getString("harvest_id").equals("null")) {
                                        forecastSeasonId = forecastObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(forecastSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues forecastFarmerValues = new ContentValues();
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT, totalArableLandPlots);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);

                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE, minimumflowprice);

                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, forecastServerId);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_SEASON_ID, seasonId);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_VENDOR_ID, localFarmerId);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.ForecastVendor.CONTENT_URI, forecastFarmerValues);

                            }

                            financeDataArray = farmerObjectInfo.getJSONArray("finance_data_ids");
                            for (int d = 0; d < financeDataArray.length(); d++) {

                                financeDataObject = financeDataArray.getJSONObject(d);
                                int financeServerId = financeDataObject.getInt("id");

                                Integer outstandingloan = null;
                                Integer loanPurposeI = null;
                                Integer loanPurposeA = null;
                                Integer loanPurposeO = null;
                                Integer mobileMoneyAccount = null;
                                Double totalLoanAmount = null;
                                Double totaloutstanding = null;
                                Double interestrate = null;
                                Integer duration = null;
                                String loanProvider = null;
                                long financeSeasonId = 1;

                                if (financeDataObject.has("outstanding_loan")) {
                                    if (!financeDataObject.getString("outstanding_loan").equals("null")) {
                                        Boolean outstandingLoan = financeDataObject.getBoolean("outstanding_loan");
                                        if (outstandingLoan != null) {
                                            outstandingloan = outstandingLoan ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_i")) {
                                    if (!financeDataObject.getString("loan_purpose_i").equals("null")) {
                                        Boolean loanpurposei = financeDataObject.getBoolean("loan_purpose_i");
                                        if (loanpurposei != null) {
                                            loanPurposeI = loanpurposei ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_a")) {
                                    if (!financeDataObject.getString("loan_purpose_a").equals("null")) {
                                        Boolean loanpurposea = financeDataObject.getBoolean("loan_purpose_a");
                                        if (loanpurposea != null) {
                                            loanPurposeA = loanpurposea ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_o")) {
                                    if (!financeDataObject.getString("loan_purpose_o").equals("null")) {
                                        Boolean loanpurposeo = financeDataObject.getBoolean("loan_purpose_o");
                                        if (loanpurposeo != null) {
                                            loanPurposeO = loanpurposeo ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("mobile_money_account")) {
                                    if (!financeDataObject.getString("mobile_money_account").equals("null")) {
                                        Boolean mobilemoneyaccount = financeDataObject.getBoolean("mobile_money_account");
                                        if (mobilemoneyaccount != null) {
                                            mobileMoneyAccount = mobilemoneyaccount ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("total_loan_amount")) {
                                    if (!financeDataObject.getString("total_loan_amount").equals("null")) {
                                        totalLoanAmount = financeDataObject.getDouble("total_loan_amount");
                                    }
                                }
                                if (financeDataObject.has("total_outstanding")) {
                                    if (!financeDataObject.getString("total_outstanding").equals("null")) {
                                        totaloutstanding = financeDataObject.getDouble("total_outstanding");
                                    }
                                }
                                if (financeDataObject.has("interest_rate")) {
                                    if (!financeDataObject.getString("interest_rate").equals("null")) {
                                        interestrate = financeDataObject.getDouble("interest_rate");
                                    }
                                }
                                if (financeDataObject.has("duration")) {
                                    if (!financeDataObject.getString("duration").equals("null")) {
                                        duration = financeDataObject.getInt("duration");
                                    }
                                }
                                if (financeDataObject.has("loan_provider")) {
                                    if (financeDataObject.has("loan_provider")) {
                                        loanProvider = financeDataObject.getString("loan_provider");
                                    }
                                }

                                if (financeDataObject.has("harvest_id")) {
                                    if (!financeDataObject.getString("harvest_id").equals("null")) {
                                        financeSeasonId = financeDataObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(financeSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues financeValues = new ContentValues();
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN, outstandingloan);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT, totalLoanAmount);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING, totaloutstanding);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE, interestrate);

                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_DURATION, duration);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER, loanProvider);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG, loanPurposeA);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT, loanPurposeI);

                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER, loanPurposeO);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT, mobileMoneyAccount);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID, seasonId);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID, localFarmerId);

                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_SERVER_ID, financeServerId);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 1);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.FinanceDataVendor.CONTENT_URI, financeValues);

                            }

                            baseLinesArray = farmerObjectInfo.getJSONArray("baseline_ids");
                            for (int e = 0; e < baseLinesArray.length(); e++) {

                                baseLinesObject = baseLinesArray.getJSONObject(e);

                                int baselineServerId = baseLinesObject.getInt("id");

                                Double seasonharvest = null;
                                Double lostharvesttotal = null;
                                Double soldharvesttotal = null;
                                Double totalqtycoops = null;
                                Double pricesoldcoops = null;
                                Double totalqtymiddlemen = null;
                                Double pricesoldmiddlemen = null;
                                long baselineSeasonId = 1;

                                if (baseLinesObject.has("seasona_harvest")) {
                                    if (!baseLinesObject.getString("seasona_harvest").equals("null")) {
                                        seasonharvest = baseLinesObject.getDouble("seasona_harvest");
                                    }
                                }
                                if (baseLinesObject.has("price_sold_middlemen")) {
                                    if (!baseLinesObject.getString("price_sold_middlemen").equals("null")) {
                                        pricesoldmiddlemen = baseLinesObject.getDouble("price_sold_middlemen");
                                    }
                                }
                                if (baseLinesObject.has("total_qty_middlemen")) {
                                    if (!baseLinesObject.getString("total_qty_middlemen").equals("null")) {
                                        totalqtymiddlemen = baseLinesObject.getDouble("total_qty_middlemen");
                                    }
                                }
                                if (baseLinesObject.has("price_sold_coops")) {
                                    if (!baseLinesObject.getString("price_sold_coops").equals("null")) {
                                        pricesoldcoops = baseLinesObject.getDouble("price_sold_coops");
                                    }
                                }
                                if (baseLinesObject.has("total_qty_coops")) {
                                    if (!baseLinesObject.getString("total_qty_coops").equals("null")) {
                                        totalqtycoops = baseLinesObject.getDouble("total_qty_coops");
                                    }
                                }
                                if (baseLinesObject.has("sold_harvest_total")) {
                                    if (!baseLinesObject.getString("sold_harvest_total").equals("null")) {
                                        soldharvesttotal = baseLinesObject.getDouble("sold_harvest_total");
                                    }
                                }
                                if (baseLinesObject.has("lost_harvest_total")) {
                                    if (!baseLinesObject.getString("lost_harvest_total").equals("null")) {
                                        lostharvesttotal = baseLinesObject.getDouble("lost_harvest_total");
                                    }
                                }

                                if (baseLinesObject.has("harvest_id")) {
                                    if (!baseLinesObject.getString("harvest_id").equals("null")) {
                                        baselineSeasonId = baseLinesObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(baselineSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues baselineValues = new ContentValues();
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG, seasonharvest);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG, lostharvesttotal);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG, soldharvesttotal);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP, totalqtycoops);

                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG, pricesoldcoops);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG, totalqtymiddlemen);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG, pricesoldmiddlemen);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SEASON_ID, seasonId);

                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SERVER_ID, baselineServerId);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 1);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 1);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_VENDOR_ID, localFarmerId);

                                getContentResolver().insert(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues);
                            }
                        }
                    }
                } else {
                    return false;
                }
            } catch (JSONException exp) {
                return false;
            } catch (IOException exp) {
                return false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return true;
        }


        private boolean prefetchFarmer(OkHttpClient client, String token, String url, JSONObject userInfo, boolean isAgent) {
            getContentResolver().delete(BfwContract.Farmer.CONTENT_URI, null, null);
            Cursor cursor = null;
            try {
                Response farmerData = Utils.getServerData(client, token, url);
                if (farmerData != null) {
                    ResponseBody farmerInfo = farmerData.body();
                    if (farmerInfo != null) {
                        String farmersList = farmerInfo.string();

                        JSONObject farmersJsonObject = new JSONObject(farmersList);

                        JSONArray farmerArrayLists;
                        if (!isAgent) {
                            farmerArrayLists = farmersJsonObject.getJSONArray("results");
                        } else {
                            String filterResponse = farmersJsonObject.getString("response");
                            JSONObject filterServerObject = new JSONObject(filterResponse);
                            farmerArrayLists = filterServerObject.getJSONArray("results");
                        }
                        JSONObject farmerObjectInfo;

                        JSONArray accessInfosArray;
                        JSONArray farmerLandIdsArray;
                        JSONArray forecastFarmerArray;
                        JSONArray financeDataArray;
                        JSONArray baseLinesArray;

                        JSONObject accessInfoObject;
                        JSONObject farmerLandObject;
                        JSONObject forecastObject;
                        JSONObject financeDataObject;
                        JSONObject baseLinesObject;

                        int coopServerId = 0;

                        if (isAgent) {
                            coopServerId = userInfo.getInt("coop_id");
                        }

                        for (int t = 0; t < farmerArrayLists.length(); t++) {

                            farmerObjectInfo = farmerArrayLists.getJSONObject(t);

                            int farmerId = farmerObjectInfo.getInt("id");
                            int coopFarmerId = farmerObjectInfo.getInt("coop_id");

                            String coopSelect = BfwContract.Coops.TABLE_NAME + "." +
                                    BfwContract.Coops.COLUMN_COOP_SERVER_ID + " =  ? ";

                            int localCoopId = 0;
                            cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, coopSelect, new String[]{Long.toString(coopFarmerId)}, null);
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    localCoopId = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops._ID));
                                }
                            }

                            String name = null;
                            String cellPhone = null;
                            String address = null;
                            String gender = null;
                            Integer headOfHousehold = null;
                            Integer numhouseholdmembers = null;
                            String firstName = null;
                            String lastName = null;
                            String cellCarrier = null;
                            String cellPhoneAlt = null;
                            String membershipId = null;
                            String storageDetails = null;
                            String otherDetails = null;
                            Integer safestorage = null;
                            Integer tractors = null;
                            Integer harvester = null;
                            Integer dryer = null;
                            Integer thresher = null;
                            Integer other = null;
                            String otherWaterSource = null;

                            Integer mwsDam = null;
                            Integer well = null;
                            Integer boreFhole = null;
                            Integer rs = null;
                            Integer mwspb = null;
                            Integer mwsIrrigation = null;
                            Integer none = null;
                            Integer mwsOther = null;
                            Double totalLandPlotSize = null;

                            if (farmerObjectInfo.has("name")) {
                                name = farmerObjectInfo.getString("name");
                            }
                            if (farmerObjectInfo.has("gender")) {
                                gender = farmerObjectInfo.getString("gender");
                            }
                            if (farmerObjectInfo.has("address")) {
                                address = farmerObjectInfo.getString("address");
                            }
                            if (farmerObjectInfo.has("cell_phone")) {
                                cellPhone = farmerObjectInfo.getString("cell_phone");
                            }
                            if (!farmerObjectInfo.getString("num_household_members").equals("null")) {
                                numhouseholdmembers = farmerObjectInfo.getInt("num_household_members");
                            }
                            if (farmerObjectInfo.has("cellphone_alt")) {
                                cellPhoneAlt = farmerObjectInfo.getString("cellphone_alt");
                            }

                            if (farmerObjectInfo.has("cell_carrier")) {
                                cellCarrier = farmerObjectInfo.getString("cell_carrier");
                            }
                            if (farmerObjectInfo.has("spouse_lastname")) {
                                lastName = farmerObjectInfo.getString("spouse_lastname");
                            }
                            if (farmerObjectInfo.has("spouse_firstname")) {
                                firstName = farmerObjectInfo.getString("spouse_firstname");
                            }
                            if (!farmerObjectInfo.getString("head_of_household").equals("null")) {
                                Boolean headHousehold = farmerObjectInfo.getBoolean("head_of_household");
                                if (headHousehold != null) {
                                    headOfHousehold = headHousehold ? 1 : 0;
                                }
                            }
                            if (farmerObjectInfo.has("membership_id")) {
                                membershipId = farmerObjectInfo.getString("membership_id");
                            }
                            if (farmerObjectInfo.has("storage_details")) {
                                storageDetails = farmerObjectInfo.getString("storage_details");
                            }

                            if (farmerObjectInfo.has("other_details")) {
                                otherDetails = farmerObjectInfo.getString("other_details");
                            }
                            if (!farmerObjectInfo.getString("ar_safestorage").equals("null")) {
                                Boolean arSafestorage = farmerObjectInfo.getBoolean("ar_safestorage");
                                if (arSafestorage != null) {
                                    safestorage = arSafestorage ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_tractors").equals("null")) {
                                Boolean arTractors = farmerObjectInfo.getBoolean("ar_tractors");
                                if (arTractors != null) {
                                    tractors = arTractors ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_harverster").equals("null")) {
                                Boolean arHarverster = farmerObjectInfo.getBoolean("ar_harverster");
                                if (arHarverster != null) {
                                    harvester = arHarverster ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_dryer").equals("null")) {
                                Boolean arDryer = farmerObjectInfo.getBoolean("ar_dryer");
                                if (arDryer != null) {
                                    dryer = arDryer ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("ar_thresher").equals("null")) {
                                thresher = null;
                                Boolean arThresher = farmerObjectInfo.getBoolean("ar_thresher");
                                if (arThresher != null) {
                                    thresher = arThresher ? 1 : 0;
                                }
                            }

                            if (!farmerObjectInfo.getString("ar_other").equals("null")) {
                                Boolean arOther = farmerObjectInfo.getBoolean("ar_other");
                                if (arOther != null) {
                                    other = arOther ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_dam").equals("null")) {
                                Boolean dam = farmerObjectInfo.getBoolean("mws_dam");
                                if (dam != null) {
                                    mwsDam = dam ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_well").equals("null")) {
                                Boolean msWell = farmerObjectInfo.getBoolean("mws_well");
                                if (msWell != null) {
                                    well = msWell ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_borehole").equals("null")) {
                                Boolean mwsBorehole = farmerObjectInfo.getBoolean("mws_borehole");
                                if (boreFhole != null) {
                                    boreFhole = mwsBorehole ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_rs").equals("null")) {
                                rs = null;
                                Boolean mwsrs = farmerObjectInfo.getBoolean("mws_rs");
                                if (mwsrs != null) {
                                    rs = mwsrs ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_pb").equals("null")) {
                                Boolean pb = farmerObjectInfo.getBoolean("mws_pb");
                                if (pb != null) {
                                    mwspb = pb ? 1 : 0;
                                }
                            }

                            if (!farmerObjectInfo.getString("mws_irrigation").equals("null")) {
                                Boolean irrigation = farmerObjectInfo.getBoolean("mws_irrigation");
                                if (irrigation != null) {
                                    mwsIrrigation = irrigation ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_none").equals("null")) {
                                Boolean mwsnone = farmerObjectInfo.getBoolean("mws_none");
                                if (mwsnone != null) {
                                    none = mwsnone ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("mws_other").equals("null")) {
                                Boolean mwother = farmerObjectInfo.getBoolean("mws_other");
                                if (mwother != null) {
                                    mwsOther = mwother ? 1 : 0;
                                }
                            }
                            if (!farmerObjectInfo.getString("total_land_plot_size").equals("null")) {
                                totalLandPlotSize = farmerObjectInfo.getDouble("total_land_plot_size");
                            }
                            if (farmerObjectInfo.has("other_water_source")) {
                                otherWaterSource = farmerObjectInfo.getString("other_water_source");
                            }

                            ContentValues farmerValues = new ContentValues();

                            farmerValues.put(BfwContract.Farmer.COLUMN_NAME, name);
                            farmerValues.put(BfwContract.Farmer.COLUMN_PHONE, cellPhone);
                            farmerValues.put(BfwContract.Farmer.COLUMN_GENDER, gender);
                            farmerValues.put(BfwContract.Farmer.COLUMN_ADDRESS, address);
                            farmerValues.put(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD, headOfHousehold);
                            farmerValues.put(BfwContract.Farmer.COLUMN_HOUSE_MEMBER, numhouseholdmembers);
                            farmerValues.put(BfwContract.Farmer.COLUMN_FIRST_NAME, firstName);

                            farmerValues.put(BfwContract.Farmer.COLUMN_LAST_NAME, lastName);
                            farmerValues.put(BfwContract.Farmer.COLUMN_CELL_PHONE, cellPhoneAlt);
                            farmerValues.put(BfwContract.Farmer.COLUMN_CELL_CARRIER, cellCarrier);
                            farmerValues.put(BfwContract.Farmer.COLUMN_MEMBER_SHIP, membershipId);
                            farmerValues.put(BfwContract.Farmer.COLUMN_STORAGE_DETAIL, storageDetails);
                            farmerValues.put(BfwContract.Farmer.COLUMN_NEW_SOURCE_DETAIL, otherDetails);
                            farmerValues.put(BfwContract.Farmer.COLUMN_SAFE_STORAGE, safestorage);

                            farmerValues.put(BfwContract.Farmer.COLUMN_TRACTORS, tractors);
                            farmerValues.put(BfwContract.Farmer.COLUMN_HARVESTER, harvester);
                            farmerValues.put(BfwContract.Farmer.COLUMN_DRYER, dryer);
                            farmerValues.put(BfwContract.Farmer.COLUMN_TRESHER, thresher);
                            farmerValues.put(BfwContract.Farmer.COLUMN_OTHER_INFO , other);
                            farmerValues.put(BfwContract.Farmer.COLUMN_WATER_SOURCE_DETAILS, otherWaterSource);
                            farmerValues.put(BfwContract.Farmer.COLUMN_DAM, mwsDam);

                            farmerValues.put(BfwContract.Farmer.COLUMN_WELL, well);
                            farmerValues.put(BfwContract.Farmer.COLUMN_BOREHOLE, boreFhole);
                            farmerValues.put(BfwContract.Farmer.COLUMN_RIVER_STREAM, rs);
                            farmerValues.put(BfwContract.Farmer.COLUMN_PIPE_BORNE, mwspb);
                            farmerValues.put(BfwContract.Farmer.COLUMN_IRRIGATION, mwsIrrigation);
                            farmerValues.put(BfwContract.Farmer.COLUMN_NONE, none);
                            farmerValues.put(BfwContract.Farmer.COLUMN_OTHER, mwsOther);

                            farmerValues.put(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID, farmerId);
                            farmerValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 1);
                            farmerValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 1);
                            farmerValues.put(BfwContract.Farmer.COLUMN_COOP_USER_ID, localCoopId);
                            farmerValues.put(BfwContract.Farmer.COLUMN_COOP_SERVER_ID, coopServerId);
                            farmerValues.put(BfwContract.Farmer.COLUMN_TOTAL_PLOT_SIZE, totalLandPlotSize);

                            Uri farmerUri = getContentResolver().insert(BfwContract.Farmer.CONTENT_URI, farmerValues);
                            long localFarmerId = ContentUris.parseId(farmerUri);

                            accessInfosArray = farmerObjectInfo.getJSONArray("access_info_ids");

                            for (int a = 0; a < accessInfosArray.length(); a++) {

                                accessInfoObject = accessInfosArray.getJSONObject(a);

                                int farmerInfoId = accessInfoObject.getInt("id");

                                Integer aes = null;
                                Integer cri = null;
                                Integer seeds = null;
                                Integer orgFert = null;
                                Integer inorgFert = null;
                                Integer labour = null;
                                Integer iwp = null;
                                Integer ss = null;
                                long infoSeasonId = 1;

                                if (accessInfoObject.has("ar_aes")) {
                                    if (!accessInfoObject.getString("ar_aes").equals("null")) {
                                        Boolean arAes = accessInfoObject.getBoolean("ar_aes");
                                        if (arAes != null) {
                                            aes = arAes ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_cri")) {
                                    if (!accessInfoObject.getString("ar_cri").equals("null")) {
                                        Boolean arCri = accessInfoObject.getBoolean("ar_cri");
                                        if (arCri != null) {
                                            cri = arCri ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_seeds")) {
                                    if (!accessInfoObject.getString("ar_seeds").equals("null")) {
                                        Boolean arSeeds = accessInfoObject.getBoolean("ar_seeds");
                                        if (arSeeds != null) {
                                            seeds = arSeeds ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_of")) {
                                    if (!accessInfoObject.getString("ar_of").equals("null")) {
                                        Boolean arof = accessInfoObject.getBoolean("ar_of");
                                        if (arof != null) {
                                            orgFert = arof ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_if")) {
                                    if (!accessInfoObject.getString("ar_if").equals("null")) {
                                        Boolean arif = accessInfoObject.getBoolean("ar_if");
                                        if (arif != null) {
                                            inorgFert = arif ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_labour")) {
                                    if (!accessInfoObject.getString("ar_labour").equals("null")) {
                                        Boolean arlabour = accessInfoObject.getBoolean("ar_labour");
                                        if (arlabour != null) {
                                            labour = arlabour ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_iwp")) {
                                    if (!accessInfoObject.getString("ar_iwp").equals("null")) {
                                        Boolean ariwp = accessInfoObject.getBoolean("ar_iwp");
                                        if (ariwp != null) {
                                            iwp = ariwp ? 1 : 0;
                                        }
                                    }
                                }
                                if (accessInfoObject.has("ar_ss")) {
                                    if (!accessInfoObject.getString("ar_ss").equals("null")) {
                                        Boolean arss = accessInfoObject.getBoolean("ar_ss");
                                        if (arss != null) {
                                            ss = arss ? 1 : 0;
                                        }
                                    }
                                }

                                if (accessInfoObject.has("harvest_id")) {
                                    if (!accessInfoObject.getString("harvest_id").equals("null")) {
                                        infoSeasonId = accessInfoObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(infoSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues farmerInfoValues = new ContentValues();
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV, aes);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO, cri);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEEDS, seeds);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER, orgFert);

                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorgFert);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_LABOUR, labour);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS, iwp);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS, ss);

                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC, 1);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_UPDATE, 1);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID, seasonId);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID, localFarmerId);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SERVER_ID, farmerInfoId);

                                getContentResolver().insert(BfwContract.FarmerAccessInfo.CONTENT_URI, farmerInfoValues);
                            }

                            farmerLandIdsArray = farmerObjectInfo.getJSONArray("farmer_land_ids");

                            for (int b = 0; b < farmerLandIdsArray.length(); b++) {

                                farmerLandObject = farmerLandIdsArray.getJSONObject(b);
                                int landServerId = farmerLandObject.getInt("id");

                                Double plot_size = null;
                                Double lat = null;
                                Double lng = null;
                                long landSeasonId = 1;

                                if (farmerLandObject.has("plot_size")) {
                                    if (!farmerLandObject.getString("plot_size").equals("null")) {
                                        plot_size = farmerLandObject.getDouble("plot_size");
                                    }
                                }
                                if (farmerLandObject.has("lat")) {
                                    if (!farmerLandObject.getString("lat").equals("null")) {
                                        lat = farmerLandObject.getDouble("lat");
                                    }
                                }
                                if (farmerLandObject.has("lng")) {
                                    if (!farmerLandObject.getString("lng").equals("null")) {
                                        lng = farmerLandObject.getDouble("lng");
                                    }
                                }

                                if (farmerLandObject.has("harvest_id")) {
                                    if (!farmerLandObject.getString("harvest_id").equals("null")) {
                                        landSeasonId = farmerLandObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(landSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues farmerLandValues = new ContentValues();
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_PLOT_SIZE, plot_size);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_LAT_INFO, lat);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_LNG_INFO, lng);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_SERVER_ID, landServerId);

                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_SEASON_ID, seasonId);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 1);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 1);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_FARMER_ID, localFarmerId);

                                getContentResolver().insert(BfwContract.LandPlot.CONTENT_URI, farmerLandValues);

                            }

                            forecastFarmerArray = farmerObjectInfo.getJSONArray("forecast_farmer_ids");
                            for (int c = 0; c < forecastFarmerArray.length(); c++) {

                                forecastObject = forecastFarmerArray.getJSONObject(c);

                                int forecastServerId = forecastObject.getInt("id");

                                Double totalArableLandPlots = null;
                                Double expectedProductionInMt = null;
                                Double forecastedyieldmt = null;
                                Double forecastedharvestsalevalue = null;
                                Double totalcooplandsize = null;
                                Double farmerpercentageland = null;
                                Double currentpppcommitment = null;
                                Double farmercontributionppp = null;
                                Double farmerexpectedminppp = null;
                                Double minimumflowprice = null;
                                long forecastSeasonId = 1;

                                if (forecastObject.has("total_arable_land_plots")) {
                                    if (!forecastObject.getString("total_arable_land_plots").equals("null")) {
                                        totalArableLandPlots = forecastObject.getDouble("total_arable_land_plots");
                                    }
                                }
                                if (forecastObject.has("expected_production_in_mt")) {
                                    if (!forecastObject.getString("expected_production_in_mt").equals("null")) {
                                        expectedProductionInMt = forecastObject.getDouble("expected_production_in_mt");
                                    }
                                }

                                if (forecastObject.has("forecasted_yield_mt")) {
                                    if (!forecastObject.getString("forecasted_yield_mt").equals("null")) {
                                        forecastedyieldmt = forecastObject.getDouble("forecasted_yield_mt");
                                    }
                                }
                                if (forecastObject.has("forecasted_harvest_sale_value")) {
                                    if (!forecastObject.getString("forecasted_harvest_sale_value").equals("null")) {
                                        forecastedharvestsalevalue = forecastObject.getDouble("forecasted_harvest_sale_value");
                                    }
                                }
                                if (forecastObject.has("total_coop_land_size")) {
                                    if (!forecastObject.getString("total_coop_land_size").equals("null")) {
                                        totalcooplandsize = forecastObject.getDouble("total_coop_land_size");
                                    }
                                }
                                if (forecastObject.has("farmer_percentage_land")) {
                                    if (!forecastObject.getString("farmer_percentage_land").equals("null")) {
                                        farmerpercentageland = forecastObject.getDouble("farmer_percentage_land");
                                    }
                                }
                                if (forecastObject.has("current_ppp_commitment")) {
                                    if (!forecastObject.getString("current_ppp_commitment").equals("null")) {
                                        currentpppcommitment = forecastObject.getDouble("current_ppp_commitment");
                                    }
                                }

                                if (forecastObject.has("farmer_contribution_ppp")) {
                                    if (!forecastObject.getString("farmer_contribution_ppp").equals("null")) {
                                        farmercontributionppp = forecastObject.getDouble("farmer_contribution_ppp");
                                    }
                                }
                                if (forecastObject.has("farmer_expected_min_ppp")) {
                                    if (!forecastObject.getString("farmer_expected_min_ppp").equals("null")) {
                                        farmerexpectedminppp = forecastObject.getDouble("farmer_expected_min_ppp");
                                    }
                                }
                                if (forecastObject.has("minimum_flow_price")) {
                                    if (!forecastObject.getString("minimum_flow_price").equals("null")) {
                                        minimumflowprice = forecastObject.getDouble("minimum_flow_price");
                                    }
                                }

                                if (forecastObject.has("harvest_id")) {
                                    if (!forecastObject.getString("harvest_id").equals("null")) {
                                        forecastSeasonId = forecastObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(forecastSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues forecastFarmerValues = new ContentValues();
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT, totalArableLandPlots);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_YIELD_MT, forecastedyieldmt);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE, totalcooplandsize);

                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE, minimumflowprice);

                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_SERVER_ID, forecastServerId);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_SEASON_ID, seasonId);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FARMER_ID, localFarmerId);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 1);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.ForecastFarmer.CONTENT_URI, forecastFarmerValues);

                            }

                            financeDataArray = farmerObjectInfo.getJSONArray("finance_data_ids");
                            for (int d = 0; d < financeDataArray.length(); d++) {

                                financeDataObject = financeDataArray.getJSONObject(d);
                                int financeServerId = financeDataObject.getInt("id");

                                Integer outstandingloan = null;
                                Integer loanPurposeI = null;
                                Integer loanPurposeA = null;
                                Integer loanPurposeO = null;
                                Integer mobileMoneyAccount = null;
                                Double totalLoanAmount = null;
                                Double totaloutstanding = null;
                                Double interestrate = null;
                                Integer duration = null;
                                String loanProvider = null;
                                long financeSeasonId = 1;

                                if (financeDataObject.has("outstanding_loan")) {
                                    if (!financeDataObject.getString("outstanding_loan").equals("null")) {
                                        Boolean outstandingLoan = financeDataObject.getBoolean("outstanding_loan");
                                        if (outstandingLoan != null) {
                                            outstandingloan = outstandingLoan ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_i")) {
                                    if (!financeDataObject.getString("loan_purpose_i").equals("null")) {
                                        Boolean loanpurposei = financeDataObject.getBoolean("loan_purpose_i");
                                        if (loanpurposei != null) {
                                            loanPurposeI = loanpurposei ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_a")) {
                                    if (!financeDataObject.getString("loan_purpose_a").equals("null")) {
                                        Boolean loanpurposea = financeDataObject.getBoolean("loan_purpose_a");
                                        if (loanpurposea != null) {
                                            loanPurposeA = loanpurposea ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("loan_purpose_o")) {
                                    if (!financeDataObject.getString("loan_purpose_o").equals("null")) {
                                        Boolean loanpurposeo = financeDataObject.getBoolean("loan_purpose_o");
                                        if (loanpurposeo != null) {
                                            loanPurposeO = loanpurposeo ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("mobile_money_account")) {
                                    if (!financeDataObject.getString("mobile_money_account").equals("null")) {
                                        Boolean mobilemoneyaccount = financeDataObject.getBoolean("mobile_money_account");
                                        if (mobilemoneyaccount != null) {
                                            mobileMoneyAccount = mobilemoneyaccount ? 1 : 0;
                                        }
                                    }
                                }
                                if (financeDataObject.has("total_loan_amount")) {
                                    if (!financeDataObject.getString("total_loan_amount").equals("null")) {
                                        totalLoanAmount = financeDataObject.getDouble("total_loan_amount");
                                    }
                                }
                                if (financeDataObject.has("total_outstanding")) {
                                    if (!financeDataObject.getString("total_outstanding").equals("null")) {
                                        totaloutstanding = financeDataObject.getDouble("total_outstanding");
                                    }
                                }
                                if (financeDataObject.has("interest_rate")) {
                                    if (!financeDataObject.getString("interest_rate").equals("null")) {
                                        interestrate = financeDataObject.getDouble("interest_rate");
                                    }
                                }
                                if (financeDataObject.has("duration")) {
                                    if (!financeDataObject.getString("duration").equals("null")) {
                                        duration = financeDataObject.getInt("duration");
                                    }
                                }
                                if (financeDataObject.has("loan_provider")) {
                                    if (financeDataObject.has("loan_provider")) {
                                        loanProvider = financeDataObject.getString("loan_provider");
                                    }
                                }

                                if (financeDataObject.has("harvest_id")) {
                                    if (!financeDataObject.getString("harvest_id").equals("null")) {
                                        financeSeasonId = financeDataObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(financeSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues financeValues = new ContentValues();
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN, outstandingloan);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT, totalLoanAmount);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING, totaloutstanding);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE, interestrate);

                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_DURATION, duration);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER, loanProvider);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG, loanPurposeA);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT, loanPurposeI);

                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER, loanPurposeO);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT, mobileMoneyAccount);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID, seasonId);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID, localFarmerId);

                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_SERVER_ID, financeServerId);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC, 1);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE, 1);

                                getContentResolver().insert(BfwContract.FinanceDataFarmer.CONTENT_URI, financeValues);

                            }

                            baseLinesArray = farmerObjectInfo.getJSONArray("baseline_ids");
                            for (int e = 0; e < baseLinesArray.length(); e++) {

                                baseLinesObject = baseLinesArray.getJSONObject(e);

                                int baselineServerId = baseLinesObject.getInt("id");

                                Double seasonharvest = null;
                                Double lostharvesttotal = null;
                                Double soldharvesttotal = null;
                                Double totalqtycoops = null;
                                Double pricesoldcoops = null;
                                Double totalqtymiddlemen = null;
                                Double pricesoldmiddlemen = null;
                                long baselineSeasonId = 1;

                                if (baseLinesObject.has("seasona_harvest")) {
                                    if (!baseLinesObject.getString("seasona_harvest").equals("null")) {
                                        seasonharvest = baseLinesObject.getDouble("seasona_harvest");
                                    }
                                }
                                if (baseLinesObject.has("price_sold_middlemen")) {
                                    if (!baseLinesObject.getString("price_sold_middlemen").equals("null")) {
                                        pricesoldmiddlemen = baseLinesObject.getDouble("price_sold_middlemen");
                                    }
                                }
                                if (baseLinesObject.has("total_qty_middlemen")) {
                                    if (!baseLinesObject.getString("total_qty_middlemen").equals("null")) {
                                        totalqtymiddlemen = baseLinesObject.getDouble("total_qty_middlemen");
                                    }
                                }
                                if (baseLinesObject.has("price_sold_coops")) {
                                    if (!baseLinesObject.getString("price_sold_coops").equals("null")) {
                                        pricesoldcoops = baseLinesObject.getDouble("price_sold_coops");
                                    }
                                }
                                if (baseLinesObject.has("total_qty_coops")) {
                                    if (!baseLinesObject.getString("total_qty_coops").equals("null")) {
                                        totalqtycoops = baseLinesObject.getDouble("total_qty_coops");
                                    }
                                }
                                if (baseLinesObject.has("sold_harvest_total")) {
                                    if (!baseLinesObject.getString("sold_harvest_total").equals("null")) {
                                        soldharvesttotal = baseLinesObject.getDouble("sold_harvest_total");
                                    }
                                }
                                if (baseLinesObject.has("lost_harvest_total")) {
                                    if (!baseLinesObject.getString("lost_harvest_total").equals("null")) {
                                        lostharvesttotal = baseLinesObject.getDouble("lost_harvest_total");
                                    }
                                }

                                if (baseLinesObject.has("harvest_id")) {
                                    if (!baseLinesObject.getString("harvest_id").equals("null")) {
                                        baselineSeasonId = baseLinesObject.getLong("harvest_id");
                                    }
                                }

                                String harvSelect = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                        BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";

                                int seasonId = 1;
                                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, harvSelect, new String[]{Long.toString(baselineSeasonId)}, null);
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                                    }
                                }

                                ContentValues baselineValues = new ContentValues();
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG, seasonharvest);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG, lostharvesttotal);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG, soldharvesttotal);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP, totalqtycoops);

                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG, pricesoldcoops);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG, totalqtymiddlemen);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG, pricesoldmiddlemen);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_SEASON_ID, seasonId);

                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_SERVER_ID, baselineServerId);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_SYNC, 1);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_UPDATE, 1);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_FARMER_ID, localFarmerId);

                                getContentResolver().insert(BfwContract.BaselineFarmer.CONTENT_URI, baselineValues);
                            }
                        }
                    }
                } else {
                    return false;
                }
            } catch (JSONException exp) {
                return false;
            } catch (IOException exp) {
                return false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return true;
        }


        private Map getLoginMessage(String message, String groupName, boolean isSuccess) {

            messageData = new HashMap();
            messageData.put("message", message);
            messageData.put("success", isSuccess);
            messageData.put("group", groupName);
            return messageData;
        }


        @Override
        protected void onPostExecute(final Map messageData) {
            mAuthTask = null;
            String message = (String) messageData.get("message");
            String groupName = (String) messageData.get("group");
            boolean isSuccess = (boolean) messageData.get("success");
            showProgress(false);

            if (isSuccess) {

                //Start appropriate activity base on which user is login
                if (groupName.equals("Admin")) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivityAdmin.class));
                } else if (groupName.equals("Vendor")) {
                    // pass id to open as intent
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                } else if (groupName.equals("Agent")) {
                    startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                } else if (groupName.equals("Buyer")) {
                    // pass id of buyer to edit
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                }

            } else {
                CafeBar.builder(LoginActivity.this)
                        .content(message)
                        .theme(CafeBarTheme.CLEAR_BLACK)
                        .floating(true)
                        .gravity(CafeBarGravity.CENTER)
                        .duration(CafeBarDuration.LONG.getDuration())
                        .neutralText("Close")
                        .neutralColor(Color.parseColor("#FF4081"))
                        .show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }

}

