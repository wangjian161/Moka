package com.moka.call.component.permission;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

/**
 * Created by su on 15-12-9.
 */
public interface OnPermissionCheckListener {
    void onPermissionGranted(Activity activity, String permission);

    void onPermissionDenied(Activity activity, String permission);

    AlertDialog makeHintDialog(Activity activity, String permission);
}
