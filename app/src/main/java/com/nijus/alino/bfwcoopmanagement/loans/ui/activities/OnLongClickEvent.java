package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the Event Bus to add message and the "Delete icon" in the appbar.
 * It has a variable named "bus" which references the Bus created by the event bus library.
 * We have to use "getDefault method" to refer to it.
 */

public class OnLongClickEvent extends BroadcastReceiver {
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        SelectedItems selectedItems = null;



        /**
         * Once the OnLongClickEvent has the right information, it is published on the event bus
         * using the post method
         * **/
        bus.post(selectedItems);
    }


}
