package com.moka.call.net;

import com.alibaba.fastjson.TypeReference;

/**
 * Created by su on 17-4-12.
 * release/t/leakTest版本均使用此Helper，以ln方式链接
 */

public class RequestHelper {

    public static <T> NetRequest<T> getRequest(String url, TypeReference<T> typeReference, SimpleCallback<T> sydCallback) {
        return new OkHttpRequest<T>(url, typeReference, sydCallback);
    }

    public static <T> NetRequest<T> getRequest(String url, String method, TypeReference<T> typeReference, SimpleCallback<T> sydCallback) {
        return new OkHttpRequest<T>(url, method, typeReference, sydCallback);
    }

    public static <T> NetRequest<T> getMultipartRequest(String url, TypeReference<T> typeReference, SimpleCallback<T> sydCallback) {
        return new OkHttpMultipartRequest<>(url, typeReference, sydCallback);
    }

    public static void cancel(Object tag) {
        OkHttpRequest.cancel(tag);
    }
}
