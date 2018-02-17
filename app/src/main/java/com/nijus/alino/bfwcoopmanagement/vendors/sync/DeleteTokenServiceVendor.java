package com.nijus.alino.bfwcoopmanagement.vendors.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.events.DeleteTokenEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteTokenServiceVendor extends IntentService {
    public final String LOG_TAG = DeleteTokenServiceVendor.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    public DeleteTokenServiceVendor() {
        super("");
    }

    public DeleteTokenServiceVendor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appRefreshToken = prefGoog.getString(getResources().getString(R.string.app_refresh_token), "123");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        String API = BuildConfig.DEV_API_URL + "auth/delete_tokens";
        String bodyContent = "{\"refresh_token\": \"" + appRefreshToken + "\"}";

        RequestBody body = RequestBody.create(JSON, bodyContent);

        Request request = new Request.Builder()
                .url(API)
                .header("Content-Type", "text/html")
                .method("POST", body)
                .build();
        try {
            Response userList = client.newCall(request).execute();
            ResponseBody userInfo = userList.body();
            if (userInfo != null) {
                String deleteObject = userInfo.string();
                if (deleteObject.equals("{}")) {
                    EventBus.getDefault().post(new DeleteTokenEvent(true));
                } else {
                    EventBus.getDefault().post(new DeleteTokenEvent(false));
                }
            } else {
                EventBus.getDefault().post(new DeleteTokenEvent(false));
            }
        } catch (IOException exp) {
            EventBus.getDefault().post(new DeleteTokenEvent(false));
        }
    }
}
