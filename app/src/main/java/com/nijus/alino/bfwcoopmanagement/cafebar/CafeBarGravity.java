package com.nijus.alino.bfwcoopmanagement.cafebar;


import android.view.Gravity;

@SuppressWarnings("unused")
public enum CafeBarGravity {
    START(Gravity.START),
    CENTER(Gravity.CENTER),
    END(Gravity.END);

    private int mGravity;

    CafeBarGravity(int gravity) {
        mGravity = gravity;
    }

    int getGravity() {
        return mGravity;
    }
}
