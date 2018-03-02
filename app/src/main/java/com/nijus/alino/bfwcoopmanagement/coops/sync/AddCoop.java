package com.nijus.alino.bfwcoopmanagement.coops.sync;


import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;

import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.UUID;

public class AddCoop extends IntentService {

    public final String LOG_TAG = AddCoop.class.getSimpleName();

    public AddCoop() {
        super("");
    }

    public AddCoop(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // grab data

        // prepare and set variable

        // handle coops table

        // handle baseline finance info table

        // handle baseline sales table

        // handle baseline yield table

        // handle forecast sales table

        // handle access  to information table

    }
}
