package com.nijus.alino.bfwcoopmanagement.cafebar;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;

import java.util.Locale;

class CafeBarUtil {

    static void adjustCustomView(@NonNull CafeBar.Builder builder, @NonNull View view) {
        ViewGroup viewGroup;
        try {
            viewGroup = (ViewGroup) view;
        } catch (ClassCastException e) {
            LogUtil.d(Log.getStackTraceString(e));
            return;
        }

        viewGroup.setClickable(true);

        if (!builder.adjustCustomView()) {
            LogUtil.d("adjustCustomView = false, leave custom view as it is");
            return;
        }

        LogUtil.d("CafeBar has customView adjusting padding, setup content, button etc ignored");

        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();

        boolean tabletMode = builder.context().getResources().getBoolean(R.bool.cafebar_tablet_mode);
        LogUtil.d("CafeBar tabletMode: " +tabletMode);

        if (builder.isFitSystemWindow() && !builder.floating()) {
            Configuration configuration = builder.context().getResources().getConfiguration();
            int navBar = getNavigationBarHeight(builder.context());

            if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                viewGroup.setPadding(left, top, right, (bottom + navBar));
            } else {
                viewGroup.setPadding(left, top, (right + navBar), bottom);
            }
        }

        int index = -1;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof TextView) {
                index = i;
                LogUtil.d("CafeBar with customView found textview at index " +i);
                LogUtil.d("CafeBar always consider fist textview found as content");
                break;
            }
        }

        if (index < 0) return;

        TextView content = (TextView) viewGroup.getChildAt(index);

        if (tabletMode || builder.floating()) {
            ViewGroup.LayoutParams params = content.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            content.setLayoutParams(params);

            content.setMinWidth(builder.context().getResources()
                    .getDimensionPixelSize(R.dimen.cafebar_floating_min_width));
            content.setMaxWidth(builder.context().getResources()
                    .getDimensionPixelSize(R.dimen.cafebar_floating_max_width));
        }
    }

    @NonNull
    static View getBaseCafeBarView(@NonNull CafeBar.Builder builder) {
        int color = builder.theme().getColor();
        int titleColor = builder.theme().getTitleColor();

        CafeBarTheme.Custom customTheme = builder.customTheme();
        if (customTheme != null) {
            color = customTheme.getColor();
            titleColor = customTheme.getTitleColor();
        }

        //Creating LinearLayout as rootView
        LinearLayout root = new LinearLayout(builder.context());
        root.setId(R.id.cafebar_root);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_VERTICAL);
        root.setBackgroundColor(color);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setClickable(true);

        //Creating another LinearLayout for content container
        LinearLayout contentBase = new LinearLayout(builder.context());
        contentBase.setId(R.id.cafebar_content_base);
        contentBase.setOrientation(LinearLayout.HORIZONTAL);
        contentBase.setGravity(Gravity.CENTER_VERTICAL);
        contentBase.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Drawable drawable = null;
        if (builder.icon() != null) {
            LogUtil.d("CafeBar has custom icon");
            drawable = getResizedDrawable(
                    builder.context(),
                    builder.icon(),
                    titleColor,
                    builder.tintIcon());
        }

        //Creating TextView for content as childView
        TextView content = new TextView(builder.context());
        content.setId(R.id.cafebar_content);
        content.setMaxLines(builder.maxLines());
        content.setEllipsize(TextUtils.TruncateAt.END);
        content.setTextColor(titleColor);
        content.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.context().getResources()
                .getDimension(R.dimen.cafebar_content_text));

        if (builder.contentTypeface() != null) {
            LogUtil.d("content has custom typeface");
            content.setTypeface(builder.contentTypeface());
        }

        if (builder.spannableBuilder() != null) {
            content.setText(builder.spannableBuilder(), TextView.BufferType.SPANNABLE);
        } else {
            content.setText(builder.content());
        }

        content.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        content.setGravity(Gravity.CENTER_VERTICAL);

        boolean tabletMode = builder.context().getResources().getBoolean(R.bool.cafebar_tablet_mode);
        if (tabletMode || builder.floating()) {
            content.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            content.setMinWidth(builder.context().getResources()
                    .getDimensionPixelSize(R.dimen.cafebar_floating_min_width));
            content.setMaxWidth(builder.context().getResources()
                    .getDimensionPixelSize(R.dimen.cafebar_floating_max_width));
        }

        int side = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_side);
        int top = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_top);

        if (drawable != null) {
            content.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            content.setCompoundDrawablePadding(top);
        }

        //Try to measure height of content to determine if it has more than 1 line

        Point point = getMeasuredContentSize(builder);
        int defaultHeight = getDefaultContentHeight(builder.context(), builder.contentTypeface());
        LogUtil.d("measured content height for 1 line: " +defaultHeight);

        boolean moreThanOneLine = point.y > defaultHeight;
        boolean containsPositive = builder.positiveText() != null;
        boolean containsNegative = builder.negativeText() != null;
        boolean longNeutralAction = isLongAction(builder.neutralText());

        if (moreThanOneLine || containsPositive || containsNegative || longNeutralAction) {
            LogUtil.d("More than one line: " +moreThanOneLine);
            LogUtil.d("Contains positive button: " +containsPositive);
            LogUtil.d("Contains negative button: " +containsNegative);
            LogUtil.d("Long neutral action: " +longNeutralAction);
            top = side;
            builder.longContent(true);
        }

        root.setPadding(side, top, side, top);

        LogUtil.d("fitSystemWindow: " +builder.isFitSystemWindow());
        if (builder.positiveText() == null && builder.negativeText() == null) {
            LogUtil.d("CafeBar not contains positive and negative button");

            if (builder.isFitSystemWindow() && !builder.floating()) {
                Configuration configuration = builder.context().getResources().getConfiguration();
                int navBar = getNavigationBarHeight(builder.context());

                if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    root.setPadding(side, top, side, (top + navBar));
                } else {
                    root.setPadding(side, top, (side + navBar), top);
                }
            }

            //Adding content to container
            contentBase.addView(content);

            //Adding childView to rootView
            root.addView(contentBase);

            //Returning rootView
            return root;
        }

        LogUtil.d("CafeBar contains positive or negative button");

        //Creating another linear layout for button container
        LinearLayout buttonBase = new LinearLayout(builder.context());
        buttonBase.setId(R.id.cafebar_button_base);
        buttonBase.setOrientation(LinearLayout.HORIZONTAL);
        buttonBase.setGravity(Gravity.END);
        buttonBase.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        //Adding button

        String neutralText = builder.neutralText();
        if (neutralText != null) {
            LogUtil.d("builder has neutral button");
            TextView neutral = getActionView(builder, neutralText, builder.neutralColor());
            neutral.setId(R.id.cafebar_button_neutral);

            if (builder.positiveTypeface() != null) {
                LogUtil.d("neutral has custom typeface");
                neutral.setTypeface(builder.neutralTypeface());
            }

            buttonBase.addView(neutral);
        }

        String negativeText = builder.negativeText();
        if (negativeText != null) {
            LogUtil.d("builder has negative button");
            TextView negative = getActionView(builder, negativeText, builder.negativeColor());
            negative.setId(R.id.cafebar_button_negative);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) negative.getLayoutParams();
            params.setMargins(
                    params.leftMargin + builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_button_margin),
                    params.topMargin,
                    params.rightMargin,
                    params.bottomMargin);

            if (builder.positiveTypeface() != null) {
                LogUtil.d("negative has custom typeface");
                negative.setTypeface(builder.negativeTypeface());
            }

            buttonBase.addView(negative);
        }

        String positiveText = builder.positiveText();
        if (positiveText != null) {
            LogUtil.d("builder has positive button");
            TextView positive = getActionView(builder, positiveText, builder.positiveColor());
            positive.setId(R.id.cafebar_button_positive);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positive.getLayoutParams();
            params.setMargins(
                    params.leftMargin + builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_button_margin),
                    params.topMargin,
                    params.rightMargin,
                    params.bottomMargin);

            if (builder.positiveTypeface() != null) {
                LogUtil.d("positive has custom typeface");
                positive.setTypeface(builder.positiveTypeface());
            }

            buttonBase.addView(positive);
        }

        //Adjust padding
        int buttonPadding = builder.context().getResources().getDimensionPixelSize(
                R.dimen.cafebar_button_padding);
        root.setPadding(side, top, (side - buttonPadding), (top - buttonPadding));

        if (builder.isFitSystemWindow() && !builder.floating()) {
            Configuration configuration = builder.context().getResources().getConfiguration();
            int navBar = getNavigationBarHeight(builder.context());

            if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                root.setPadding(side, top, (side - buttonPadding), (top - buttonPadding + navBar));
            } else {
                root.setPadding(side, top, (side - buttonPadding + navBar), top);
            }
        }

        //Adding content to container
        content.setPadding(0, 0, buttonPadding, 0);
        contentBase.addView(content);

        //Adding childView to rootView
        root.addView(contentBase);

        //Adding button container to root
        root.addView(buttonBase);

        //Returning rootView
        return root;
    }

    @Nullable
    static Snackbar getBaseSnackBar(@NonNull View cafeBarLayout,
                                    @NonNull CafeBar.Builder builder) {
        View view = builder.to();
        if (view == null) {
            view = ((Activity) builder.context()).getWindow().getDecorView().findViewById(android.R.id.content);
        }

        Snackbar snackBar = Snackbar.make(view, "", builder.autoDismiss() ?
                builder.duration() : Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackBar.getView();
        snackBarLayout.setPadding(0, 0, 0, 0);
        snackBarLayout.setBackgroundColor(Color.TRANSPARENT);
        snackBarLayout.setClickable(false);

        try {
            if (snackBarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams snackBarParams = (CoordinatorLayout.LayoutParams)
                        snackBarLayout.getLayoutParams();
                snackBarParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                snackBarLayout.setLayoutParams(snackBarParams);
            } else {
                Snackbar.SnackbarLayout.LayoutParams snackBarParams = (Snackbar.SnackbarLayout.LayoutParams)
                        snackBarLayout.getLayoutParams();
                snackBarParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                snackBarLayout.setLayoutParams(snackBarParams);
            }
        } catch (ClassCastException e) {
            LogUtil.d("target view must be coordinator layout");
            LogUtil.d(Log.getStackTraceString(e));
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackBarLayout.setElevation(0);
        }

        TextView textView = (TextView) snackBarLayout.findViewById(
                android.support.design.R.id.snackbar_text);
        if (textView != null) textView.setVisibility(View.INVISIBLE);

        boolean tabletMode = builder.context().getResources().getBoolean(R.bool.cafebar_tablet_mode);
        LogUtil.d("CafeBar tabletMode: " +tabletMode);
        if (tabletMode || builder.floating()) {
            int shadow = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_shadow_around);
            int padding = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_floating_padding);

            CardView cardView = (CardView) View.inflate(builder.context(), R.layout.cafebar_floating_base, null);
            Snackbar.SnackbarLayout.LayoutParams params = new Snackbar.SnackbarLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = builder.gravity().getGravity();

            int bottom = builder.floating() ? padding : 0;
            snackBarLayout.setClipToPadding(false);
            snackBarLayout.setPadding(padding, shadow, padding, bottom);

            if (builder.isFitSystemWindow() && builder.floating()) {
                Configuration configuration = builder.context().getResources().getConfiguration();
                int navBar = getNavigationBarHeight(builder.context());

                if (tabletMode || configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    snackBarLayout.setPadding(padding, shadow, padding, (bottom + navBar));
                } else {
                    snackBarLayout.setPadding(0, 0, navBar, 0);
                }
            }

            cardView.setLayoutParams(params);
            cardView.setClickable(true);

            if (builder.showShadow()) {
                cardView.setCardElevation(builder.context().getResources().getDimension(R.dimen.cafebar_shadow_around));
            }

            cardView.addView(cafeBarLayout);
            snackBarLayout.addView(cardView, 0);
            return snackBar;
        }

        LinearLayout root = new LinearLayout(builder.context());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (builder.showShadow()) {
            View shadow = new View(builder.context());
            shadow.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    builder.context().getResources().getDimensionPixelSize(
                            R.dimen.cafebar_shadow_top)));
            shadow.setBackgroundResource(R.drawable.cafebar_shadow_top);
            root.addView(shadow);
        }

        root.addView(cafeBarLayout);
        snackBarLayout.addView(root, 0);
        return snackBar;
    }

    @NonNull
    static TextView getActionView(@NonNull CafeBar.Builder builder, @NonNull String action, int color) {
        boolean longAction = isLongAction(action);
        int res = R.layout.cafebar_action_button_dark;
        if (builder.theme() == CafeBarTheme.LIGHT) {
            res = R.layout.cafebar_action_button;
        }

        CafeBarTheme.Custom customTheme = builder.customTheme();
        if (customTheme != null) {
            int titleColor = customTheme.getTitleColor();
            if (titleColor != Color.WHITE) {
                res = R.layout.cafebar_action_button;
            }
        }

        LogUtil.d("getting action view, longAction: " +longAction);

        int padding = builder.context().getResources().getDimensionPixelSize(
                R.dimen.cafebar_button_padding);

        TextView button = (TextView) View.inflate(builder.context(), res, null);
        button.setText(action.toUpperCase(Locale.getDefault()));
        button.setMaxLines(1);
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setTextColor(color);
        button.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int side = builder.context().getResources().getDimensionPixelSize(
                R.dimen.cafebar_content_padding_side);
        int margin = builder.context().getResources().getDimensionPixelSize(
                R.dimen.cafebar_button_margin_start);
        params.setMargins(margin, 0, 0, 0);

        if (longAction) {
            params.setMargins(0, (side - padding), 0, 0);
        }

        if (builder.positiveText() != null || builder.negativeText() != null) {
            longAction = true;
            params.setMargins(0, (side - padding), 0, 0);
        } else {
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        }

        button.setLayoutParams(params);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            boolean dark = builder.theme() == CafeBarTheme.DARK || builder.theme() == CafeBarTheme.CLEAR_BLACK;
            button.setBackgroundResource(dark ? R.drawable.cafebar_action_button_selector_dark :
                    R.drawable.cafebar_action_button_selector);
            return button;
        }

        TypedValue outValue = new TypedValue();
        builder.context().getTheme().resolveAttribute(longAction ?
                        R.attr.selectableItemBackground : R.attr.selectableItemBackgroundBorderless,
                outValue, true);
        button.setBackgroundResource(outValue.resourceId);
        return button;
    }

    static Point getMeasuredContentSize(@NonNull CafeBar.Builder builder) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) builder.context()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boolean tabletMode = builder.context().getResources().getBoolean(R.bool.cafebar_tablet_mode);
        int padding = (builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_side) * 2);

        if (builder.neutralText() != null && builder.negativeText() == null && builder.positiveText() == null &&
                !isLongAction(builder.neutralText())) {
            padding += builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_button_margin_start);

            TextView action = new TextView(builder.context());
            action.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.context().getResources()
                    .getDimension(R.dimen.cafebar_content_text));
            if (builder.neutralTypeface() != null) {
                action.setTypeface(builder.contentTypeface());
            }
            action.setText(builder.neutralText().substring(0,
                    builder.neutralText().length() > 10 ? 10 : builder.neutralText().length()));

            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            action.measure(widthMeasureSpec, heightMeasureSpec);

            LogUtil.d("measured action width: " +action.getMeasuredWidth());
            padding += action.getMeasuredWidth();
        }

        if (builder.icon() != null) {
            int icon = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_icon_size);
            icon += builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_content_padding_top);

            LogUtil.d("measured icon width: " +icon);
            padding += icon;
        }

        int minWidth = 0;
        int maxWidth = 0;
        if (builder.floating() || tabletMode) {
            padding += builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_floating_padding);
            minWidth = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_floating_min_width);
            maxWidth = builder.context().getResources().getDimensionPixelSize(R.dimen.cafebar_floating_max_width);
        }

        int width = metrics.widthPixels - padding;

        //Checking if target width < minWidth
        if (width < minWidth) {
            width = minWidth;
        } else if (width > maxWidth && maxWidth > 0) {
            width = maxWidth;
        }

        TextView textView = new TextView(builder.context());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.context().getResources()
                .getDimension(R.dimen.cafebar_content_text));
        if (builder.contentTypeface() != null) {
            textView.setTypeface(builder.contentTypeface());
        }

        if (builder.spannableBuilder() != null) {
            textView.setText(builder.spannableBuilder(), TextView.BufferType.SPANNABLE);
        } else {
            textView.setText(builder.content());
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        LogUtil.d("minWidth: " +minWidth);
        LogUtil.d("maxWidth: " +maxWidth);
        LogUtil.d("target width: " +width);
        LogUtil.d("measured width: " +textView.getMeasuredWidth());
        LogUtil.d("measured height: " +textView.getMeasuredHeight());
        return new Point(textView.getMeasuredWidth(), textView.getMeasuredHeight());
    }

    static int getDefaultContentHeight(@NonNull Context context, @Nullable Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources()
                .getDimension(R.dimen.cafebar_content_text));
        textView.setTypeface(typeface);
        textView.setText("a");

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    @Nullable
    static Drawable getDrawable(@NonNull Context context, @DrawableRes int res) {
        try {
            Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, res);
            return drawable.mutate();
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Nullable
    static Drawable toDrawable(@NonNull Context context, @Nullable Bitmap bitmap) {
        try {
            if (bitmap == null) return null;
            return new BitmapDrawable(context.getResources(), bitmap);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Nullable
    private static Drawable getResizedDrawable(@NonNull Context context, @Nullable Drawable drawable,
                                               int color, boolean filter) {
        try {
            if (drawable == null) {
                LogUtil.d("drawable: null");
                return null;
            }

            if (filter) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                drawable.mutate();
            }

            int size = context.getResources().getDimensionPixelSize(R.dimen.cafebar_icon_size);

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return new BitmapDrawable(context.getResources(),
                    Bitmap.createScaledBitmap(bitmap, size, size, true));
        } catch (Exception | OutOfMemoryError e) {
            LogUtil.d(Log.getStackTraceString(e));
            return null;
        }
    }

    static int getNavigationBarHeight(@NonNull Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        if (appUsableSize.x < realScreenSize.x) {
            Point point = new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
            LogUtil.d("Navigation Bar height position: Right");
            LogUtil.d("Navigation Bar height: " +point.x);
            return point.x;
        }

        if (appUsableSize.y < realScreenSize.y) {
            Point point = new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
            LogUtil.d("Navigation Bar height position: Bottom");
            LogUtil.d("Navigation Bar height: " +point.y);
            return point.y;
        }

        LogUtil.d("Navigation Bar height: 0");
        return 0;
    }

    private static Point getAppUsableScreenSize(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private static Point getRealScreenSize(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer)     Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                LogUtil.d(Log.getStackTraceString(e));
            }
        }
        return size;
    }

    static boolean isLongAction(@Nullable String action) {
        return action != null && action.length() > 10;
    }

    static int getTitleTextColor(@ColorInt int color) {
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        return (darkness < 0.35) ? getDarkerColor(color) : Color.WHITE;
    }

    static int getSubTitleTextColor(@ColorInt int color) {
        int titleColor = getTitleTextColor(color);
        int alpha2 = Math.round(Color.alpha(titleColor) * 0.6f);
        int red = Color.red(titleColor);
        int green = Color.green(titleColor);
        int blue = Color.blue(titleColor);
        return Color.argb(alpha2, red, green, blue);
    }

    private static int getDarkerColor(@ColorInt int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.25f;
        return Color.HSVToColor(hsv);
    }

    static int getColor(@NonNull Context context, int color) {
        try {
            return ContextCompat.getColor(context, color);
        } catch (Exception e) {
            LogUtil.d(Log.getStackTraceString(e));
            return color;
        }
    }

    @Nullable
    static Typeface getTypeface(@NonNull Context context, String fontName) {
        try {
            return Typeface.createFromAsset(context.getAssets(), "fonts/" +fontName);
        } catch (Exception e) {
            LogUtil.d(Log.getStackTraceString(e));
        }
        return null;
    }
}
