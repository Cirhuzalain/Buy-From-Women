package com.nijus.alino.bfwcoopmanagement.cafebar;


import android.graphics.Color;
import android.support.annotation.ColorInt;

@SuppressWarnings("unused")
public enum CafeBarTheme {
    LIGHT(Color.parseColor("#F5F5F5")),
    DARK(Color.parseColor("#323232")),
    CLEAR_BLACK(Color.BLACK);

    private int mColor;

    CafeBarTheme(@ColorInt int color) {
        mColor = color;
    }

    @ColorInt
    int getColor() {
        return mColor;
    }

    @ColorInt
    int getTitleColor() {
        return CafeBarUtil.getTitleTextColor(mColor);
    }

    @ColorInt
    int getSubTitleColor() {
        return CafeBarUtil.getSubTitleTextColor(mColor);
    }

    public static class Custom {

        private int mColor;

        public Custom(@ColorInt int color) {
            mColor = color;
        }

        @ColorInt
        int getColor() {
            return mColor;
        }

        @ColorInt
        int getTitleColor() {
            return CafeBarUtil.getTitleTextColor(mColor);
        }

        @ColorInt
        int getSubTitleColor() {
            return CafeBarUtil.getSubTitleTextColor(mColor);
        }
    }
}
