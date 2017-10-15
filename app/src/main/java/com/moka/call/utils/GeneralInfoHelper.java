package com.moka.call.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.moka.call.BuildConfig;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by su on 17-2-8.
 */

public class GeneralInfoHelper {

    private static final String TAG = GeneralInfoHelper.class.getSimpleName();

    //application context
    private static Context sContext;
    private static String sAndroidId = "";
    private static String sDeviceId = "";
    private static int sScreenWidth;
    private static int sScreenHeight;
    private static int sAvailableWidth;
    private static int sAvailableHeight;

    private static String sVersionName = "";
    private static int sVersionCode = 0;
    private static String sPackageName = "";
    private static String sAppName = "";
    private static String sApplicationLabel = "";
    private static boolean sDebuggable;
    private static String sProcessName = "";
    private static boolean sInit;

    private GeneralInfoHelper() {}
    
    public static void init(Context context) {
        sContext = context.getApplicationContext();
//        sInit = SpHelper.isInit();
        initPackageInfo();
        initUUID();
        initScreenSize();
        sProcessName = getCurrentProcessName();
    }

    public static String getProcessName() {
        return sProcessName;
    }

    private static void initPackageInfo() {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                PackageManager pm = sContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(sContext.getPackageName(), 0);
                sVersionName = pi.versionName;
                sVersionCode = pi.versionCode;
                sPackageName = pi.packageName;
                sAppName = pi.applicationInfo.loadLabel(pm).toString();
                ApplicationInfo applicationInfo = pm.getApplicationInfo(getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                sApplicationLabel = pm.getApplicationLabel(applicationInfo).toString();
                sDebuggable = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
            } catch (PackageManager.NameNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onCreate: " + e.getMessage());
                }
            }
        }
    }

    @Nullable
    private static String getCurrentProcessName() {
        try {
            return IOUtil.streamToString(new FileInputStream("/proc/self/cmdline")).trim();
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "can't get current process name!", e);
            }
            return "";
        }
    }

    public static void initUUID() {
        sAndroidId = Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//        TelephonyManager tm = (TelephonyManager) sContext.getSystemService(TELEPHONY_SERVICE);
        sDeviceId = sAndroidId;//TextUtils.isEmpty(tm.getDeviceId()) ? "" : tm.getDeviceId();
    }

    private static void initScreenSize() {
        WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point realSize = new Point();
        display.getRealSize(realSize);
        sScreenWidth = Math.min(realSize.x, realSize.y);
        sScreenHeight = Math.max(realSize.x, realSize.y);
        Point availableSize = new Point();
        display.getSize(availableSize);
        sAvailableWidth = Math.min(availableSize.x, availableSize.y);
        sAvailableHeight = Math.max(availableSize.x, availableSize.y);
    }

    public static String getDeviceId() {
        return sDeviceId;
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getVersionName() {
        return sVersionName;
    }

    public static int getVersionCode() {
        return sVersionCode;
    }

    public static String getPackageName() {
        return sPackageName;
    }

    public static String getAppName() {
        return sAppName;
    }

    public static String getAndroidId() {
        return sAndroidId;
    }

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getAvailableWidth() {
        return sAvailableWidth;
    }

    public static int getAvailableHeight() {
        return sAvailableHeight;
    }

    public static String getApplicationLabel() {
        return sApplicationLabel;
    }

    public static boolean isDebuggable() {
        return sDebuggable;
    }

    public static boolean isInit() {
        return sInit;
    }

    public static String infoToString() {
        return "GeneralInfoHelper{" +
                "context=" + sContext +
                ", debuggable=" + sDebuggable +
                ", versionName=" + sVersionName +
                ", versionCode=" + sVersionCode +
                ", packageName=" + sPackageName +
                ", processName=" + sProcessName +
                ", appName=" + sAppName +
                ", deviceId=" + sAndroidId +
                ", screenWidth=" + sScreenWidth +
                ", screenHeight=" + sScreenHeight +
                ", availableWidth=" + sAvailableWidth +
                ", availableHeight=" + sAvailableHeight +
                ", applicationLabel=" + sApplicationLabel +
                ", init=" + sInit +
                '}';
    }
}
