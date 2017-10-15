package com.moka.call.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by su on 2016-01-04.
 */
public class ToastBuilder {

    private Context mContext = GeneralInfoHelper.getContext();
    private CharSequence mText;
    private int mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private int mDuration = Toast.LENGTH_SHORT;

    public ToastBuilder(int resId) {
        mText = mContext.getString(resId);
    }

    public ToastBuilder(CharSequence text) {
        mText = text;
    }

    public ToastBuilder setText(CharSequence text) {
        mText = text;
        return this;
    }

    public ToastBuilder setText(int resId) {
        setText(mContext.getString(resId));
        return this;
    }

    public ToastBuilder setGravity(int gravity) {
        if (gravity == Gravity.TOP
                || gravity == Gravity.CENTER_VERTICAL
                || gravity == Gravity.BOTTOM) {
            mGravity = gravity | Gravity.CENTER_HORIZONTAL;
        } else if (gravity == Gravity.CENTER) {
            mGravity = Gravity.CENTER;
        } else {
            mGravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            new ToastBuilder("gravity can only be TOP, CENTER or BOTTOM!").setDuration(Toast.LENGTH_LONG).show();
        }
        return this;
    }

    public ToastBuilder setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public Toast create() {
        Toast toast = Toast.makeText(mContext, mText, mDuration);
        int yOffset = dp2px(64, mContext.getResources().getDisplayMetrics());
        toast.setGravity(mGravity, 0, yOffset);
        return toast;
    }

    public Toast show() {
        Toast toast = create();
        toast.show();
        return toast;
    }

    public static int dp2px(int dpValue, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, displayMetrics);
    }

    public static int sp2px(int spValue, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, displayMetrics);
    }

}
