package com.moka.call.component.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by su on 17-2-16.
 */

public class PermissionProcessor {
    private static PermissionProcessor sChecker = new PermissionProcessor();
    private PermissionProcessor() {}

    public static PermissionProcessor getInstance() {
        return sChecker;
    }

    private Map<String, OnPermissionCheckListener> mOnPermissionCheckListeners = new HashMap<>();

    public void putOnPermissionCheckListener(String permission, OnPermissionCheckListener l) {
        mOnPermissionCheckListeners.put(permission, l);
    }

    public void removeOnPermissionCheckListener(String permission) {
        mOnPermissionCheckListeners.remove(permission);
    }

    public Map<String, OnPermissionCheckListener> getPermissionCheckListeners() {
        return mOnPermissionCheckListeners;
    }

    public void permissionRequest(Activity activity, int requestCode, String permission) {
        OnPermissionCheckListener listener = mOnPermissionCheckListeners.get(permission);
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            listener.onPermissionGranted(activity, permission);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                listener.makeHintDialog(activity, permission).show();
            }
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        String permission = permissions[0];
        OnPermissionCheckListener l = mOnPermissionCheckListeners.get(permission);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            l.onPermissionGranted(activity, permission);
        } else {
            l.onPermissionDenied(activity, permission);
        }
    }
}
