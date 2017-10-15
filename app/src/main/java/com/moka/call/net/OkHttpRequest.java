package com.moka.call.net;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.moka.call.BuildConfig;
import com.moka.call.utils.GeneralInfoHelper;
import com.moka.call.utils.ToastBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by su on 17-4-11.
 */

class OkHttpRequest<T> extends NetRequest<T> {

    private static final String TAG = OkHttpRequest.class.getSimpleName();
    private static final HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.BODY;

    static OkHttpClient sClient;
    private Call mCall;
    private FormBody.Builder mFormBodyBuilder = new FormBody.Builder();

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(LOG_LEVEL);
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder()
                .protocols(Util.immutableList(Protocol.HTTP_1_1, Protocol.HTTP_2))//just for http1.1
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "packageName: " + GeneralInfoHelper.getPackageName());
            Log.d(TAG, "processName: " + GeneralInfoHelper.getProcessName());
            builder.addNetworkInterceptor(logging)
                    .addInterceptor(new ChuckInterceptor(GeneralInfoHelper.getContext()));
//            builder.addNetworkInterceptor(logging);
        }
        sClient = builder.build();
    }

    public OkHttpRequest(String url, TypeReference<T> typeReference, Callback<T> sydCallback) {
        this(url, "POST", typeReference, sydCallback);
    }

    public OkHttpRequest(String url, String method, TypeReference<T> typeReference, Callback<T> sydCallback) {
        super(url, method, typeReference, sydCallback);
    }

    private Headers createHeaders() {
        Headers.Builder builder = new Headers.Builder();
        mHeaderMap.putAll(mFixedHeaderMap);
        if (!mNoUserInfo) {
            mHeaderMap.putAll(mUserInfoHeaderMap);
        }
        Set<Map.Entry<String, String>> entrySet = mHeaderMap.entrySet();
        Iterator<Map.Entry<String, String>> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    void addUserPartForm() {
        checkParamsIsNull(mFormBodyMap);
        //后台要求post请求至少要用一个参数
        mFormBodyMap.put("from", "android");
        if (!mNoUserInfo) {
            mFormBodyMap.putAll(mUserInfoFormBodyMap);
        }
    }

    RequestBody createBody() {
        addUserPartForm();
        Set<Map.Entry<String, String>> entrySet = mFormBodyMap.entrySet();
        Iterator<Map.Entry<String, String>> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            mFormBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        return mFormBodyBuilder.build();
    }

    private Request buildRequest() {
         Request.Builder builder = new Request.Builder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(mUrl)
                .tag(mTag)
                .headers(createHeaders());
        if (HttpMethod.requiresRequestBody(mMethod)) {
            builder.method(mMethod, createBody());
        } else {
            builder.method(mMethod, null);
        }
        return builder.build();
    }

    @Override
    public void enqueue() {
        try {
            Request request = buildRequest();
            mCall = sClient.newCall(request);
            mCall.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException ioException) {
                    sHandler.post(new OnFailure(ioException));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    OkHttpRequest.this.onResponse(response);
                }
            });
        } catch (RuntimeException e) {
            showRequestErrorToast();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "url: " + mUrl + " method: " + mMethod, e);
            }
            sHandler.post(new OnFailure(new IOException("build request failed!")));
        }
    }

    final Toast showRequestErrorToast() {
        String content;
        if (BuildConfig.DEBUG) {
            content = "请求错误: " + mUrl + "\n请求方式: " + mMethod;
        } else {
            content = "请求错误: " + mUrl;
        }
        return new ToastBuilder(content)
                .setDuration(Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void execute() {
        try {
            Request request = buildRequest();
            mCall = sClient.newCall(request);
            Response response = mCall.execute();
            onResponse(response);
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "url: " + mUrl, e);
            }
            sHandler.post(new OnFailure(e));
        } catch (RuntimeException e) {
            showRequestErrorToast();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "url: " + mUrl + " method: " + mMethod, e);
            }
            sHandler.post(new OnFailure(new IOException("build request failed!")));
        }
    }

    private void onResponse(Response response) throws IOException {
        try {
            T result = parseNetworkResponse(response.body().string());
            sHandler.post(new OnResponse(new NetResponse(result, response.code(), makeErrorResponseMessage(response))));
        } catch (ParseException e) {
            sHandler.post(new OnResponse(new NetResponse(null, false, response.code(), makeErrorResponseMessage(response))));
        }
    }

    static void cancel(Object tag) {
        for (Call call : sClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                Log.d(TAG, "a queued call canceled: " + call.request().url());
            }
        }
        for (Call call : sClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                Log.d(TAG, "a running call canceled: " + call.request().url());
            }
        }
    }

    @Override
    public boolean isCanceled() {
        if (mCall != null)
            return mCall.isCanceled();
        return false;
    }

    private String makeErrorResponseMessage(Response response) {
        if (GeneralInfoHelper.getContext() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("url: " + mUrl + "\n");

            if (response.code() > 0) {
                sb.append("statusCode: " + response.code() + "\n");
            }

            sb.append("message: " + response.message() + "\n");
            if (response.headers() != null) {
                sb.append("headers: " + response.headers() + "\n");
            }

            return sb.toString();
        }
        return "";
    }
}
