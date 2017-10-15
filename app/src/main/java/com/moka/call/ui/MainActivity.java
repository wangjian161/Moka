package com.moka.call.ui;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.alibaba.fastjson.TypeReference;
import com.moka.call.R;
import com.moka.call.Url;
import com.moka.call.component.permission.OnPermissionCheckListener;
import com.moka.call.component.permission.OnSetPermissionClickListener;
import com.moka.call.component.permission.PermissionRequest;
import com.moka.call.net.HttpResult;
import com.moka.call.net.RequestHelper;
import com.moka.call.net.SimpleCallback;
import com.moka.call.utils.ToastBuilder;

public class MainActivity extends BaseAppCompatActivity implements OnPermissionCheckListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        putOnPermissionCheckListener(android.Manifest.permission.READ_PHONE_STATE, this);
        permissionRequest(this, PermissionRequest.PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
    }

    private void test() {
        RequestHelper.getRequest(Url.TEST, "GET", new TypeReference<HttpResult>() {}, new SimpleCallback<HttpResult>() {
            @Override
            public void onResponseSuccessful(HttpResult response) {
                new ToastBuilder("response: " + response).show();
            }
        }).noUserInfo()
                .enqueue();
    }

    @Override
    public void onPermissionGranted(Activity activity, String permission) {
        test();
    }

    @Override
    public void onPermissionDenied(Activity activity, String permission) {

    }

    @Override
    public AlertDialog makeHintDialog(Activity activity, String permission) {
        return new AlertDialog.Builder(this)
                .setMessage("测试读取手机信息权限")
                .setNegativeButton("取消", null)
                .setPositiveButton("设置权限", new OnSetPermissionClickListener(this))
                .show();
    }
}
