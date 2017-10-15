package com.moka.call.net;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.moka.call.BuildConfig;
import com.moka.call.utils.GeneralInfoHelper;
import com.moka.call.utils.ToastBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by su on 17-4-10.
 */

public abstract class NetRequest<T> {

    private static final String TAG = NetRequest.class.getSimpleName();

    Object mTag;
    private final TypeReference<T> mType;
    static Handler sHandler = new Handler(GeneralInfoHelper.getContext().getMainLooper());
    String mUrl;
    String mMethod = "POST";
    boolean mNoUserInfo;
    Map<String, String> mFixedHeaderMap = new HashMap<>();
    Map<String, String> mUserInfoHeaderMap = new HashMap<>();
    Map<String, String> mUserInfoFormBodyMap = new HashMap<>();
    Map<String, String> mHeaderMap = new HashMap<>();
    Map<String, String> mFormBodyMap = new HashMap<>();
    Map<String, MultipartFile> mMultipartMap = new HashMap<>();

    private Callback<T> mCallback;

    NetRequest(String url, String method, TypeReference<T> typeReference, Callback<T> callback) {
        if (!"POST".equals(method) && !"GET".equals(method)) {
            throw new IllegalArgumentException("method is wrong: " + method);
        }
        mUrl = url;
        mMethod = method;
        mType = typeReference;
        mCallback = callback;
        init();
    }

    private void init() {
        mFixedHeaderMap.put("user-agent", "Android/" + GeneralInfoHelper.getVersionName()
                + " (" + GeneralInfoHelper.getPackageName() + ";" + Build.VERSION.RELEASE + ";"
                + GeneralInfoHelper.getScreenWidth() + ";" + GeneralInfoHelper.getScreenHeight() + ";" + Build.MANUFACTURER + ";" + Build.MODEL + ";"
                + GeneralInfoHelper.getVersionCode() + ";" + Build.VERSION.SDK_INT + ")");
        mFixedHeaderMap.put("deviceId", GeneralInfoHelper.getDeviceId());
        mFixedHeaderMap.put("version", GeneralInfoHelper.getVersionName());
        mFixedHeaderMap.put("versionCode", String.valueOf(GeneralInfoHelper.getVersionCode()));
        mFixedHeaderMap.put("from", "android");
    }

    public NetRequest<T> addHeader(String key, String value) {
        mHeaderMap.put(key, value);
        return this;
    }

    public NetRequest<T> addHeaders(Map<String, String> headers) {
        mHeaderMap.putAll(headers);
        return this;
    }

    public NetRequest<T> addParameter(String key, String value) {
        mFormBodyMap.put(key, value);
        return this;
    }

    public NetRequest<T> addParameters(Map<String, String> parameters) {
        mFormBodyMap.putAll(parameters);
        return this;
    }

    public NetRequest<T> addMultipart(String key, MultipartFile multipartFile) {
        mMultipartMap.put(key, multipartFile);
        return this;
    }

    public NetRequest<T> addMultiparts(Map<String, MultipartFile> multipartMap) {
        mMultipartMap.putAll(multipartMap);
        return this;
    }

    public NetRequest<T> setTag(Object tag) {
        mTag = tag;
        return this;
    }

    protected T parseNetworkResponse(String json) throws ParseException {
        json = "{\n" +
                "    \"data\": {\n" +
                "        \"test\": \"- -\"\n" +
                "    },\n" +
                "    \"errorCode\": 0,\n" +
                "    \"errorMessage\": \"没有错误\"\n" +
                "}";
        try {
            Type type = mType.getType();
            if (type.equals(String.class)) {
                return (T) json;
            } else {
                return JSON.parseObject(json, mType);
            }
        } catch (RuntimeException e) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "url: " + mUrl + " \tjson: " + json, e);
            }

            ParseException parseException = new ParseException(e.getMessage());
            parseException.initCause(e);
            throw parseException;
        }
    }

    public abstract void enqueue();

    public abstract void execute();

    public abstract boolean isCanceled();

    public NetRequest<T> noUserInfo() {
        mNoUserInfo = true;
        return this;
    }

    static void checkParamsIsNull(Map<String, String> params) {
        for (final Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                if (BuildConfig.DEBUG) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            new ToastBuilder(entry.getKey() + " is null").show();
                        }
                    });
                } else {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            new ToastBuilder("请求参数不合法").show();
                        }
                    });
                }
            }
        }
    }

    class OnFailure implements Runnable {
        private IOException mException;

        OnFailure(IOException e) {
            mException = e;
        }

        @Override
        public void run() {
            if (isCanceled()) {
                mCallback.onCancel();
            } else {
                mCallback.onFailure(mException);
            }
        }
    }

    class OnResponse implements Runnable {
        private NetResponse mResponse;

        OnResponse(NetResponse response) {
            mResponse = response;
        }

        @Override
        public void run() {
            try {
                if (mResponse.isSuccessful()) {
                    mCallback.onResponse(mResponse);
                } else {
                    mCallback.onError(mResponse);
                }
            } catch (IOException e) {
                Log.d(TAG, "url: " + mUrl, e);
            }
        }
    }
}
