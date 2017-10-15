package com.moka.call.component.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by su on 16-3-14.
 */
public class OnSetPermissionClickListener implements DialogInterface.OnClickListener {

    private Activity mActivity;

    public OnSetPermissionClickListener(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mActivity.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", mActivity.getPackageName(), null)));
    }
}
