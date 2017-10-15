package com.moka.call.net;

import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.moka.call.BuildConfig;
import com.moka.call.utils.NetworkUtil;
import com.moka.call.utils.ToastBuilder;

import java.io.IOException;

/**
 * Created by su on 17-4-11.
 */

public class SimpleCallback<T> implements Callback<T> {

    private static Toast sFailureToast = new ToastBuilder("请求失败,请检查网络设置").create();
    private static Toast sErrorToast = new ToastBuilder("请求失败,请重试").create();
    private static Toast sCanceledToast = new ToastBuilder("请求已取消").create();
    private static Toast sParseErrorToast = new ToastBuilder("数据解析失败").create();

    @Override
    public final void onFailure(IOException exception) {
        if (NetworkUtil.isNetworkAvailable()) {
            sErrorToast.show();
        } else {
            sFailureToast.show();
        }
        onServerError();
    }

    @Override
    public final void onCancel() {
        if (BuildConfig.DEBUG) {
            sCanceledToast.show();
        }
        onRequestCancel();
    }

    @Override
    public final void onError(NetResponse<T> response) {
        if (BuildConfig.DEBUG) {
            new ToastBuilder(response.getMessage()).setGravity(Gravity.CENTER).show();
        } else {
            sErrorToast.show();
        }
        onServerError();
    }

    @Override
    public final void onResponse(NetResponse<T> response) {
        if (response.isParseSuccessful()) {
            int errorCode = getErrorCode(response.getResult());
            processErrorCode(errorCode);
            onResponseSuccessful(response.getResult());
        } else {
            sParseErrorToast.show();
            onServerError();
        }
    }

    public void onResponseSuccessful(T response) {}

    public void onRequestCancel() {}

    public void onServerError() {}

    public int getErrorCode(T response) {
        int errorCode = 0;
        if (response instanceof HttpResult) {
            HttpResult httpResult = (HttpResult) response;
            errorCode = httpResult.getErrorCode();
        } else if (response instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) response;
            errorCode = jsonObject.getIntValue("errorCode");
        }
        return errorCode;
    }

    private void processErrorCode(int errorCode) {
//        if (errorCode == 302) {//302 重新登录
//            GeneralInfoHelper.getContext().sendBroadcast(new Intent(String.valueOf(BaseAppCompatActivity.APP_NEED_LOGIN)));
//        } else if (errorCode == 699) {//699 系统维护
//            GeneralInfoHelper.getContext().sendBroadcast(new Intent(String.valueOf(BaseAppCompatActivity.APP_SYS_DIE)));
//        }
    }
}
