package com.nijus.alino.bfwcoopmanagement.cafebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.BoolRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.nijus.alino.bfwcoopmanagement.R;

@SuppressWarnings("unused")
public class CafeBar {

    private Builder mBuilder;
    private Snackbar mSnackBar;

    private CafeBar(@NonNull Builder builder) {
        mBuilder = builder;

        View cafeBarLayout = mBuilder.mCustomView;
        if (cafeBarLayout == null) {
            LogUtil.d("CafeBar doesn't have customView, preparing it...");
            cafeBarLayout = CafeBarUtil.getBaseCafeBarView(mBuilder);
        } else {
            CafeBarUtil.adjustCustomView(mBuilder, cafeBarLayout);
        }

        mSnackBar = CafeBarUtil.getBaseSnackBar(cafeBarLayout, mBuilder);
        if (mSnackBar == null) {
            LogUtil.d("Error, base snackbar == null");
            mBuilder = null;
            return;
        }

        if (mBuilder.mCustomView != null) {
            LogUtil.d("CafeBar has custom view, set buttons ignored");
            return;
        }

        if (mBuilder.mPositiveText == null && mBuilder.mNegativeText == null) {
            LogUtil.d("CafeBar only contains neutral button");
            if (mBuilder.mNeutralText != null) {
                setAction(mBuilder.mNeutralText, mBuilder.mNeutralColor, mBuilder.mNeutralCallback);
            }
        } else {
            LogUtil.d("CafeBar contains positive or negative button");
            LinearLayout root = (LinearLayout) getCafeBarView();
            LinearLayout buttonBase = (LinearLayout) root.findViewById(R.id.cafebar_button_base);

            if (mBuilder.mNeutralText != null) {
                TextView neutral = (TextView) buttonBase.findViewById(R.id.cafebar_button_neutral);
                neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mBuilder.mNeutralCallback != null) {
                            mBuilder.mNeutralCallback.OnClick(getCafeBar());
                            return;
                        }

                        dismiss();
                    }
                });
            }

            if (mBuilder.mNegativeText != null) {
                TextView negative = (TextView) buttonBase.findViewById(R.id.cafebar_button_negative);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mBuilder.mNegativeCallback != null) {
                            mBuilder.mNegativeCallback.OnClick(getCafeBar());
                            return;
                        }

                        dismiss();
                    }
                });
            }

            if (mBuilder.mPositiveText != null) {
                TextView positive = (TextView) buttonBase.findViewById(R.id.cafebar_button_positive);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mBuilder.mPositiveCallback != null) {
                            mBuilder.mPositiveCallback.OnClick(getCafeBar());
                            return;
                        }

                        dismiss();
                    }
                });
            }
        }
    }

    public static void enableLogging(boolean enableLogging) {
        com.nijus.alino.bfwcoopmanagement.cafebar.LogUtil.sEnableLogging = enableLogging;
    }

    @NonNull
    public static CafeBar make(@NonNull Context context, @NonNull String content, @NonNull CafeBarDuration duration) {
        return prepareCafeBar(context, content, duration);
    }

    @NonNull
    public static CafeBar make(@NonNull Context context, @StringRes int res, @NonNull CafeBarDuration duration) {
        String string = context.getResources().getString(res);
        return prepareCafeBar(context, string, duration);
    }

    @NonNull
    public static CafeBar make(@NonNull View to, @NonNull String content, @NonNull CafeBarDuration duration) {
        return prepareCafeBar(to, content, duration);
    }

    @NonNull
    public static CafeBar make(@NonNull View to, @StringRes int res, @NonNull CafeBarDuration duration) {
        String string = to.getContext().getResources().getString(res);
        return prepareCafeBar(to, string, duration);
    }

    @NonNull
    private static CafeBar prepareCafeBar(@NonNull Context context, @NonNull String content,
                                          @NonNull CafeBarDuration duration) {
        CafeBar.Builder builder = new Builder(context);
        builder.content(content);
        builder.duration(duration.getDuration());
        if (duration == CafeBarDuration.INDEFINITE) {
            builder.autoDismiss(false);
        }
        return new CafeBar(builder);
    }

    @NonNull
    private static CafeBar prepareCafeBar(@NonNull View to, @NonNull String content,
                                          @NonNull CafeBarDuration duration) {
        Context context = to.getContext();
        if (context instanceof ContextThemeWrapper) {
            context = ((ContextThemeWrapper) context).getBaseContext();
        }

        CafeBar.Builder builder = new Builder(context);
        builder.to(to);
        builder.content(content);
        builder.duration(duration.getDuration());
        if (duration == CafeBarDuration.INDEFINITE) {
            builder.autoDismiss(false);
        }
        return new CafeBar(builder);
    }

    @NonNull
    public static Builder builder(@NonNull Context context) {
        return new Builder(context);
    }

    public void setAction(@NonNull String action, @Nullable CafeBarCallback callback) {
        setButtonAction(action, mBuilder.mTheme.getTitleColor(), callback);
    }

    public void setAction(@NonNull String action, int color, @Nullable CafeBarCallback callback) {
        setButtonAction(action, color, callback);
    }

    public void setAction(@StringRes int res, @Nullable CafeBarCallback callback) {
        String string = mBuilder.mContext.getResources().getString(res);
        setButtonAction(string, mBuilder.mTheme.getTitleColor(), callback);
    }

    public void setAction(@StringRes int res, int color, @Nullable CafeBarCallback callback) {
        String string = mBuilder.mContext.getResources().getString(res);
        setButtonAction(string, color, callback);
    }

    private void setButtonAction(@NonNull String action, int color, @Nullable final CafeBarCallback callback) {
        if (mBuilder.mCustomView != null) {
            LogUtil.d("CafeBar has customView, setAction ignored.");
            return;
        }

        LogUtil.d("preparing action view");
        mBuilder.mNeutralText = action;
        mBuilder.mNeutralColor = color;

        LinearLayout root = (LinearLayout) getCafeBarView();
        boolean longAction = CafeBarUtil.isLongAction(action);
        LinearLayout contentBase = (LinearLayout) root.findViewById(R.id.cafebar_content_base);

        if (contentBase.getChildCount() > 1) {
            LogUtil.d("content container childView count > 1, setAction already set from builder via neutralText");
            return;
        }

        TextView content = (TextView) contentBase.findViewById(R.id.cafebar_content);

        int side = mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_side);
        int top = mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_top);
        int buttonPadding = mBuilder.mContext.getResources().getDimensionPixelSize(
                R.dimen.cafebar_button_padding);
        int bottom = 0;

        if (longAction) {
            bottom = buttonPadding;
            contentBase.setOrientation(LinearLayout.VERTICAL);
            content.setPadding(0, 0, buttonPadding, 0);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) content.getLayoutParams();
            params.width = 0;
            params.weight = 1f;
            content.setLayoutParams(params);
        }

        int navBar = 0;
        LogUtil.d("fitSystemWindow: " + mBuilder.mFitSystemWindow);
        if (mBuilder.mFitSystemWindow && !mBuilder.mFloating) {
            navBar = CafeBarUtil.getNavigationBarHeight(mBuilder.mContext);
        }

        Configuration configuration = mBuilder.mContext.getResources().getConfiguration();
        boolean tabletMode = mBuilder.mContext.getResources().getBoolean(R.bool.cafebar_tablet_mode);

        if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mBuilder.longContent()) {
                LogUtil.d("content has multi lines");
                root.setPadding(side, side, (side - buttonPadding), (side - bottom + navBar));
            } else if (longAction) {
                LogUtil.d("content only 1 line with longAction");
                root.setPadding(side, top, (side - buttonPadding), (top - buttonPadding + navBar));
            } else {
                LogUtil.d("content only 1 line");
                root.setPadding(side, (top - buttonPadding), (side - buttonPadding), (top - buttonPadding + navBar));
            }
        } else {
            if (mBuilder.longContent()) {
                LogUtil.d("content has multi lines");
                root.setPadding(side, side, (side - buttonPadding + navBar), (side - bottom));
            } else if (longAction) {
                LogUtil.d("content only 1 line with longAction");
                root.setPadding(side, top, (side - buttonPadding + navBar), (top - buttonPadding));
            } else {
                LogUtil.d("content only 1 line");
                root.setPadding(side, (top - buttonPadding), (side - buttonPadding + navBar), (top - buttonPadding));
            }
        }

        TextView button = CafeBarUtil.getActionView(mBuilder, action, color);
        if (mBuilder.mNeutralTypeface != null) {
            LogUtil.d("action has custom typeface");
            button.setTypeface(mBuilder.mNeutralTypeface);
        }

        if (!longAction) {
            LogUtil.d("checking if content root padding need to be re-adjusted");
            Point point = CafeBarUtil.getMeasuredContentSize(mBuilder);
            int defaultHeight = CafeBarUtil.getDefaultContentHeight(mBuilder.mContext, mBuilder.mNeutralTypeface);
            if (point.y > defaultHeight) {
                LogUtil.d("re-adjusting content root padding");

                if (mBuilder.mFitSystemWindow && !mBuilder.mFloating) {
                    if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        root.setPadding(side, side, (side - buttonPadding), (side + navBar));
                    } else {
                        root.setPadding(side, side, (side - buttonPadding + navBar), side);
                    }
                } else {
                    root.setPadding(side, side, (side - buttonPadding), side);
                }
            } else {
                LogUtil.d("no need to re-adjust content root padding");
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.OnClick(getCafeBar());
                    return;
                }

                LogUtil.d("callback = null, CafeBar dismissed");
                dismiss();
            }
        });

        contentBase.addView(button);
    }

    @NonNull
    private CafeBar getCafeBar() {
        return this;
    }

    @NonNull
    public View getCafeBarView() {
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) mSnackBar.getView();

        boolean tabletMode = mBuilder.mContext.getResources().getBoolean(R.bool.cafebar_tablet_mode);
        LogUtil.d("Tablet mode: " + tabletMode);

        if (tabletMode || mBuilder.mFloating) {
            CardView cardView = (CardView) snackBarLayout.getChildAt(0);
            return cardView.getChildAt(0);
        }

        LinearLayout linearLayout = (LinearLayout) snackBarLayout.getChildAt(0);
        if (mBuilder.mShowShadow) return linearLayout.getChildAt(1);
        return linearLayout.getChildAt(0);
    }

    public void dismiss() {
        if (mSnackBar == null) return;

        mSnackBar.dismiss();
    }

    public void show() {
        if (mSnackBar == null) return;

        mSnackBar.show();

        if (mBuilder.mSwipeToDismiss) return;

        if (mSnackBar.getView().getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            mSnackBar.getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mSnackBar.getView().getViewTreeObserver().removeOnPreDrawListener(this);
                    ((CoordinatorLayout.LayoutParams) mSnackBar.getView().getLayoutParams()).setBehavior(null);
                    return true;
                }
            });
        }
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private Context mContext;

        private View mView;
        private View mCustomView;

        private CafeBarTheme mTheme = CafeBarTheme.DARK;
        private CafeBarTheme.Custom mCustomTheme = null;
        private CafeBarGravity mGravity = CafeBarGravity.CENTER;

        private int mDuration = CafeBarDuration.SHORT.getDuration();
        private int mMaxLines = 2;
        private int mPositiveColor = CafeBarTheme.DARK.getTitleColor();
        private int mNegativeColor = CafeBarTheme.DARK.getTitleColor();
        private int mNeutralColor = CafeBarTheme.DARK.getTitleColor();

        private boolean mLongContent = false;
        private boolean mAutoDismiss = true;
        private boolean mShowShadow = true;
        private boolean mFitSystemWindow = false;
        private boolean mFloating = false;
        private boolean mAdjustCustomView = false;
        private boolean mTintIcon = true;
        private boolean mSwipeToDismiss = true;

        private Typeface mContentTypeface;
        private Typeface mPositiveTypeface;
        private Typeface mNegativeTypeface;
        private Typeface mNeutralTypeface;
        private Drawable mIcon;

        private String mContent = "";
        private String mPositiveText = null;
        private String mNegativeText = null;
        private String mNeutralText = null;

        private SpannableStringBuilder mSpannableBuilder = null;

        private CafeBarCallback mPositiveCallback;
        private CafeBarCallback mNegativeCallback;
        private CafeBarCallback mNeutralCallback;

        public Builder(@NonNull Context context) {
            mContext = context;
        }

        public Builder to(@Nullable View view) {
            mView = view;
            return this;
        }

        public Builder customView(@LayoutRes int res) {
            mCustomView = View.inflate(mContext, res, null);
            return this;
        }

        public Builder customView(@Nullable View customView) {
            mCustomView = customView;
            return this;
        }

        public Builder customView(@LayoutRes int res, boolean adjustCustomView) {
            mCustomView = View.inflate(mContext, res, null);
            mAdjustCustomView = adjustCustomView;
            return this;
        }

        public Builder customView(@Nullable View customView, boolean adjustCustomView) {
            mCustomView = customView;
            mAdjustCustomView = adjustCustomView;
            return this;
        }

        public Builder content(@StringRes int res) {
            mContent = mContext.getResources().getString(res);
            return this;
        }

        public Builder content(@NonNull String content) {
            mContent = content;
            return this;
        }

        public Builder content(@NonNull SpannableStringBuilder spannableBuilder) {
            mSpannableBuilder = spannableBuilder;
            return this;
        }

        public Builder maxLines(@IntRange(from = 1, to = 6) int maxLines) {
            mMaxLines = maxLines;
            return this;
        }

        public Builder duration(@IntRange(from = 1, to = 10000) int duration) {
            mDuration = duration;
            return this;
        }

        public Builder theme(@NonNull CafeBarTheme theme) {
            mTheme = theme;
            mPositiveColor = mTheme.getTitleColor();
            mNegativeColor = mNeutralColor = mTheme.getSubTitleColor();
            return this;
        }

        public Builder theme(@NonNull CafeBarTheme.Custom customTheme) {
            mCustomTheme = customTheme;
            mPositiveColor = mCustomTheme.getTitleColor();
            mNegativeColor = mNeutralColor = mCustomTheme.getSubTitleColor();
            return this;
        }

        public Builder icon(@Nullable Drawable icon) {
            mIcon = icon;
            return this;
        }

        public Builder icon(@Nullable Bitmap icon) {
            mIcon = CafeBarUtil.toDrawable(mContext, icon);
            return this;
        }

        public Builder icon(@DrawableRes int res) {
            mIcon = CafeBarUtil.getDrawable(mContext, res);
            return this;
        }

        public Builder icon(@Nullable Drawable icon, boolean tintIcon) {
            mIcon = icon;
            mTintIcon = tintIcon;
            return this;
        }

        public Builder icon(@Nullable Bitmap icon, boolean tintIcon) {
            mIcon = CafeBarUtil.toDrawable(mContext, icon);
            mTintIcon = tintIcon;
            return this;
        }

        public Builder icon(@DrawableRes int res, boolean tintIcon) {
            mIcon = CafeBarUtil.getDrawable(mContext, res);
            mTintIcon = tintIcon;
            return this;
        }

        public Builder showShadow(@BoolRes int res) {
            mShowShadow = mContext.getResources().getBoolean(res);
            return this;
        }

        public Builder showShadow(boolean showShadow) {
            mShowShadow = showShadow;
            return this;
        }

        public Builder autoDismiss(@BoolRes int res) {
            mAutoDismiss = mContext.getResources().getBoolean(res);
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            mAutoDismiss = autoDismiss;
            return this;
        }

        public Builder swipeToDismiss(@BoolRes int res) {
            mSwipeToDismiss = mContext.getResources().getBoolean(res);
            return this;
        }

        public Builder swipeToDismiss(boolean swipeToDismiss) {
            mSwipeToDismiss = swipeToDismiss;
            return this;
        }

        public Builder floating(@BoolRes int res) {
            mFloating = mContext.getResources().getBoolean(res);
            return this;
        }

        public Builder floating(boolean floating) {
            mFloating = floating;
            return this;
        }

        public Builder gravity(@NonNull CafeBarGravity gravity) {
            mGravity = gravity;
            return this;
        }

        /**
         * @deprecated use {{@link #fitSystemWindow()}} instead
         */
        @Deprecated
        public Builder fitSystemWindow(@BoolRes int res) {
            return fitSystemWindow();
        }

        /**
         * @deprecated use {{@link #fitSystemWindow()}} instead
         */
        @Deprecated
        public Builder fitSystemWindow(boolean fitSystemWindow) {
            return fitSystemWindow();
        }

        public Builder fitSystemWindow() {
            Activity activity = (Activity) mContext;
            Window window = activity.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            int navigationBarHeight = CafeBarUtil.getNavigationBarHeight(mContext);

            boolean isInMultiWindowMode = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode = activity.isInMultiWindowMode();
            }

            if ((params.flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) ==
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
                mFitSystemWindow = navigationBarHeight > 0 && !isInMultiWindowMode;
            }
            return this;
        }

        public Builder typeface(@Nullable Typeface content, @Nullable Typeface button) {
            mContentTypeface = content;
            mNeutralTypeface = mPositiveTypeface = mNegativeTypeface = button;
            return this;
        }

        public Builder typeface(String contentFontName, String buttonFontName) {
            mContentTypeface = CafeBarUtil.getTypeface(mContext, contentFontName);
            mNeutralTypeface = mPositiveTypeface = mNegativeTypeface = CafeBarUtil.getTypeface(mContext, buttonFontName);
            return this;
        }

        public Builder contentTypeface(@Nullable Typeface typeface) {
            mContentTypeface = typeface;
            return this;
        }

        public Builder contentTypeface(String fontName) {
            mContentTypeface = CafeBarUtil.getTypeface(mContext, fontName);
            return this;
        }

        public Builder positiveTypeface(@Nullable Typeface typeface) {
            mPositiveTypeface = typeface;
            return this;
        }

        public Builder positiveTypeface(String fontName) {
            mPositiveTypeface = CafeBarUtil.getTypeface(mContext, fontName);
            return this;
        }

        public Builder negativeTypeface(@Nullable Typeface typeface) {
            mNegativeTypeface = typeface;
            return this;
        }

        public Builder negativeTypeface(String fontName) {
            mNegativeTypeface = CafeBarUtil.getTypeface(mContext, fontName);
            return this;
        }

        public Builder neutralTypeface(@Nullable Typeface typeface) {
            mNeutralTypeface = typeface;
            return this;
        }

        public Builder neutralTypeface(String fontName) {
            mNeutralTypeface = CafeBarUtil.getTypeface(mContext, fontName);
            return this;
        }

        public Builder buttonTypeface(@Nullable Typeface typeface) {
            mNeutralTypeface = mPositiveTypeface = mNegativeTypeface = typeface;
            return this;
        }

        public Builder buttonTypeface(String fontName) {
            mNeutralTypeface = mPositiveTypeface = mNegativeTypeface = CafeBarUtil.getTypeface(mContext, fontName);
            return this;
        }

        public Builder positiveColor(int positiveColor) {
            mPositiveColor = CafeBarUtil.getColor(mContext, positiveColor);
            return this;
        }

        public Builder negativeColor(int negativeColor) {
            mNegativeColor = CafeBarUtil.getColor(mContext, negativeColor);
            return this;
        }

        public Builder neutralColor(int neutralColor) {
            mNeutralColor = CafeBarUtil.getColor(mContext, neutralColor);
            return this;
        }

        public Builder buttonColor(int buttonColor) {
            int color = CafeBarUtil.getColor(mContext, buttonColor);
            mNeutralColor = mPositiveColor = mNegativeColor = color;
            return this;
        }

        public Builder positiveText(@StringRes int res) {
            mPositiveText = mContext.getResources().getString(res);
            return this;
        }

        public Builder positiveText(@NonNull String positiveText) {
            mPositiveText = positiveText;
            return this;
        }

        public Builder negativeText(@StringRes int res) {
            mNegativeText = mContext.getResources().getString(res);
            return this;
        }

        public Builder negativeText(@NonNull String negativeText) {
            mNegativeText = negativeText;
            return this;
        }

        public Builder neutralText(@StringRes int res) {
            mNeutralText = mContext.getResources().getString(res);
            return this;
        }

        public Builder neutralText(@NonNull String neutralText) {
            mNeutralText = neutralText;
            return this;
        }

        public Builder onPositive(@Nullable CafeBarCallback positiveCallback) {
            mPositiveCallback = positiveCallback;
            return this;
        }

        public Builder onNegative(@Nullable CafeBarCallback negativeCallback) {
            mNegativeCallback = negativeCallback;
            return this;
        }

        public Builder onNeutral(@Nullable CafeBarCallback neutralCallback) {
            mNeutralCallback = neutralCallback;
            return this;
        }

        public void show() {
            build().show();
        }

        public CafeBar build() {
            return new CafeBar(this);
        }

        void longContent(boolean longContent) {
            mLongContent = longContent;
        }

        boolean longContent() {
            return mLongContent;
        }

        @NonNull
        Context context() {
            return mContext;
        }

        @Nullable
        View to() {
            return mView;
        }

        @Nullable
        View customView() {
            return mCustomView;
        }

        boolean adjustCustomView() {
            return mAdjustCustomView;
        }

        @Nullable
        Typeface contentTypeface() {
            return mContentTypeface;
        }

        @NonNull
        CafeBarTheme theme() {
            return mTheme;
        }

        @Nullable
        CafeBarTheme.Custom customTheme() {
            return mCustomTheme;
        }

        int duration() {
            return mDuration;
        }

        boolean autoDismiss() {
            return mAutoDismiss;
        }

        boolean showShadow() {
            return mShowShadow;
        }

        boolean isFitSystemWindow() {
            return mFitSystemWindow;
        }

        boolean floating() {
            return mFloating;
        }

        @NonNull
        CafeBarGravity gravity() {
            return mGravity;
        }

        @Nullable
        Drawable icon() {
            return mIcon;
        }

        boolean tintIcon() {
            return mTintIcon;
        }

        @NonNull
        String content() {
            return mContent;
        }

        @Nullable
        SpannableStringBuilder spannableBuilder() {
            return mSpannableBuilder;
        }

        int maxLines() {
            return mMaxLines;
        }

        int positiveColor() {
            return mPositiveColor;
        }

        int negativeColor() {
            return mNegativeColor;
        }

        int neutralColor() {
            return mNeutralColor;
        }

        @Nullable
        Typeface positiveTypeface() {
            return mPositiveTypeface;
        }

        @Nullable
        Typeface negativeTypeface() {
            return mNegativeTypeface;
        }

        @Nullable
        Typeface neutralTypeface() {
            return mNeutralTypeface;
        }

        @Nullable
        String positiveText() {
            return mPositiveText;
        }

        @Nullable
        String negativeText() {
            return mNegativeText;
        }

        @Nullable
        String neutralText() {
            return mNeutralText;
        }
    }
}
