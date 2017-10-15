package com.moka.call;

import android.app.Application;
import android.util.Log;

import com.moka.call.utils.GeneralInfoHelper;

import java.io.File;

/**
 * Created by su on 2017/6/10.
 */

public class MokaApplication extends Application {

    private static final String TAG = MokaApplication.class.getSimpleName();
    private static File sTempFolder;

    @Override
    public void onCreate() {
        super.onCreate();
        GeneralInfoHelper.init(this);
        Log.d(TAG, "GeneralInfoHelper: " + GeneralInfoHelper.infoToString());
        init();
//        CrashManager.init();
    }

    private static void init() {

    }

}
