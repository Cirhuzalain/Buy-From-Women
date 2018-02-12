package com.nijus.alino.bfwcoopmanagement.cafebar;


@SuppressWarnings("unused")
public enum CafeBarDuration {
    SHORT(2000),
    MEDIUM(3500),
    LONG(5000),
    INDEFINITE(-1);

    private int mDuration;

    CafeBarDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }
}
