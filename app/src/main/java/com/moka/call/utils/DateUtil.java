package com.moka.call.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by su on 15-5-28.
 */
public final class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();

    public static final TimeZone TIMEZONE_CHINA = TimeZone.getTimeZone("GMT+:08:00");
    private static Map<String, ThreadLocal<SimpleDateFormat>> sSdfMap = new HashMap<>();

    public static synchronized SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sSdfMap.get(pattern);
        if (tl == null) {
            tl = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    Log.d(TAG, "thread: " + Thread.currentThread() + " init pattern: " + pattern);
                    return new SimpleDateFormat(pattern, Locale.CHINESE);
                }
            };
            sSdfMap.put(pattern, tl);
        }
        return tl.get();
    }

    private DateUtil() {
    }
}
