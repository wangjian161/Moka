package com.moka.call.component.permission;

import android.app.Activity;

/**
 * Created by su on 15-12-10.
 */
public interface PermissionRequest {
    /**
     * permission requestCode
     * */
    int PERMISSIONS_REQUEST_CODE_CALL_PHONE = 1;
    int PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE = 2;
    int PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 3;

    public void permissionRequest(Activity activity, int requestCode, String permission);
}
