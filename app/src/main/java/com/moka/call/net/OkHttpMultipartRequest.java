package com.moka.call.net;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.moka.call.BuildConfig;
import com.moka.call.utils.ToastBuilder;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by su on 17-6-12.
 */

class OkHttpMultipartRequest<T> extends OkHttpRequest<T> {

    private static final String TAG = OkHttpMultipartRequest.class.getSimpleName();

    OkHttpMultipartRequest(String url, TypeReference<T> typeReference, Callback<T> sydCallback) {
        super(url, typeReference, sydCallback);
    }

    @Override
    RequestBody createBody() {
        addUserPartForm();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Set<Map.Entry<String, String>> entrySet = mFormBodyMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "form: " + mFormBodyMap);
        }

        Set<Map.Entry<String, MultipartFile>> fileSet = mMultipartMap.entrySet();
        for (Map.Entry<String, MultipartFile> entry : fileSet) {
            MultipartFile multipartFile = entry.getValue();
            File file = multipartFile.getFile();
            if (file != null && file.exists() && file.isFile()) {
                builder.addFormDataPart(multipartFile.getName(), multipartFile.getFileName(), RequestBody.create(MediaType.parse(multipartFile.getMimeType()), file));
            } else {
                new ToastBuilder("文件选择错误： " + multipartFile).setDuration(Toast.LENGTH_LONG).show();
            }
        }
        return builder.build();
    }
}
