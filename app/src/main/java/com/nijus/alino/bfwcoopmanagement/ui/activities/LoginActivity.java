package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
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
import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.BuyerActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBar;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarDuration;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarGravity;
import com.nijus.alino.bfwcoopmanagement.cafebar.CafeBarTheme;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final Pattern emailRegex = Pattern.compile("^[A-Z0-9.-_]+@[A-Z0-9.-_]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

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
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
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

    private boolean isEmailValid(String email) {
        Matcher match = emailRegex.matcher(email);
        return match.find();
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
        int IS_PRIMARY = 1;
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
                String API = BuildConfig.API_URL + "auth/get_tokens";

                String bodyContent = "{\"db\": \"buyfromwomen\"," +
                        "\"username\":\"" + mEmail + "\"," +
                        "\"password\": \"" + mPassword + "\"}";

                RequestBody body = RequestBody.create(JSON, bodyContent);

                Request request = new Request.Builder()
                        .url(API)
                        .header("Content-Type", "text/html")
                        .method("POST", body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody info = response.body();
                    if (info != null) {
                        String apiData = info.string();

                        JSONObject objectInfo = new JSONObject(apiData);
                        if (objectInfo.has("uid") && objectInfo.has("access_token") && objectInfo.has("refresh_token")) {

                            //get user information
                            String access_token = objectInfo.getString("access_token");
                            String refresh_token = objectInfo.getString("refresh_token");
                            int userId = objectInfo.getInt("uid");
                            String login = null;

                            Request requestUser = new Request.Builder()
                                    .url(BuildConfig.API_URL + "res.users")
                                    .header("Access-Token", access_token)
                                    .get()
                                    .build();

                            Response userList = client.newCall(requestUser).execute();
                            ResponseBody userInfo = userList.body();
                            if (userInfo != null) {
                                String userApiData = userInfo.string();

                                JSONObject userData = new JSONObject(userApiData);

                                JSONArray dataInfo = userData.getJSONArray("results");

                                JSONObject userDesc;
                                for (int i = 0; i < dataInfo.length(); i++) {
                                    userDesc = dataInfo.getJSONObject(i);

                                    if (userDesc.getInt("id") == userId) {
                                        login = userDesc.getString("login");
                                    }
                                }
                                if (login != null) {
                                    //check if no coop is available and save coop data
                                    cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, null, null, null);

                                    if (cursor != null && !cursor.moveToFirst()) {
                                        Request requestUsers = new Request.Builder()
                                                .url(BuildConfig.API_URL + "res.users")
                                                .header("Access-Token", access_token)
                                                .get()
                                                .build();

                                        Response usersResponse = client.newCall(requestUsers).execute();
                                        ResponseBody usersInfo = usersResponse.body();

                                        if (usersInfo != null) {
                                            String usersInfoObject = usersInfo.string();

                                            JSONObject resUsersInfo = new JSONObject(usersInfoObject);
                                            JSONArray resUsersArray = resUsersInfo.getJSONArray("results");

                                            JSONObject resUsersObjects;

                                            for (int i = 0; i < resUsersArray.length(); i++) {
                                                resUsersObjects = resUsersArray.getJSONObject(i);

                                                if (resUsersObjects.getInt("partner_id") >= 10697 && resUsersObjects.getInt("partner_id") <= 10708) {
                                                    String name = resUsersObjects.getString("login").split("@")[0];
                                                    ContentValues contentValues = new ContentValues();
                                                    contentValues.put(BfwContract.Coops.COLUMN_COOP_NAME, name);
                                                    contentValues.put(BfwContract.Coops.COLUMN_COOP_SERVER_ID, resUsersObjects.getInt("id"));

                                                    getContentResolver().insert(BfwContract.Coops.CONTENT_URI, contentValues);
                                                }
                                            }
                                        }
                                    }

                                    //save token in settings preferences
                                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(getResources().getString(R.string.app_id), true);
                                    editor.putString(getResources().getString(R.string.app_key), access_token);
                                    editor.putString(getResources().getString(R.string.app_refresh_token), refresh_token);
                                    editor.putString(getResources().getString(R.string.user_name), login);
                                    editor.putInt(getResources().getString(R.string.user_server_id), userId);
                                    editor.apply();

                                } else {
                                    return getLoginMessage(getResources().getString(R.string.unknown_msg), false);
                                }
                            } else {
                                return getLoginMessage(getResources().getString(R.string.json_error), false);
                            }
                        } else {
                             getLoginMessage(getResources().getString(R.string.json_error), false);
                            //return getLoginMessage("essai de reponse2", false);
                        }
                    } else {
                        return getLoginMessage(getResources().getString(R.string.json_error), false);
                    }
                } catch (IOException exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), false);
                } catch (JSONException exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), false);
                } catch (Exception exp) {
                    return getLoginMessage(getResources().getString(R.string.json_error), false);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            else
            {
                return getLoginMessage2((getResources().getString(R.string.connectivity_error)), false);

            }
            return getLoginMessage("", true);
        }
        private Map getLoginMessage2(String message, boolean isSuccess) {
            messageData = new HashMap();
            messageData.put("message", message);
            messageData.put("success", isSuccess);
            return messageData;
        }

        private Map getLoginMessage(String message, boolean isSuccess) {

            messageData = new HashMap();
            messageData.put("message", message);
            messageData.put("success", isSuccess);
            return messageData;
        }


        @Override
        protected void onPostExecute(final Map messageData) {
            mAuthTask = null;
            String message = (String) messageData.get("message");
            boolean isSuccess = (boolean) messageData.get("success");
            showProgress(false);

            if (isSuccess) {
                //check who is connected
                startActivity(new Intent(getApplicationContext(), UserProfileActivityAdmin.class));
                //startActivity(new Intent(getApplicationContext(), BuyerActivity.class));
            } else {
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                CafeBar.builder(LoginActivity.this)
                        .content(message)
                        .theme(CafeBarTheme.CLEAR_BLACK)
                        .floating(true)
                        .gravity(CafeBarGravity.START)
                        //.autoDismiss(false)
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

