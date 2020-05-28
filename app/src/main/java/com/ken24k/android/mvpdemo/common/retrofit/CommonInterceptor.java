package com.ken24k.android.mvpdemo.common.retrofit;

import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 通用拦截器
 * Created by wangming on 2020-05-28
 */

public class CommonInterceptor implements Interceptor {

    private Map<String, String> mHeaderParamsMap = new HashMap<>();
    private Map<String, String> mUrlParamsMap = new HashMap<>();

    CommonInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        mHeaderParamsMap.clear();
        mUrlParamsMap.clear();

        // 拦截Request对象
        Request oldRequest = chain.request();

        // 获取参数
        String method = oldRequest.method();
        RequestBody body = oldRequest.body();
        String host = oldRequest.url().host();
        String scheme = oldRequest.url().scheme();
        String path = oldRequest.url().url().getPath();
        String url = oldRequest.url().url().toString();

        // 新的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(method, body);

        // 添加参数,添加到header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
                requestBuilder.header(params.getKey(), params.getValue());
            }
        }

        // 生成新请求
        Request newRequest = requestBuilder.build();

        // 获取服务器响应
        Response response = chain.proceed(newRequest);

        // 设置缓存
        if (AndroidUtils.isNetworkAvailable()) {
            int maxAge = 0;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            //  4-weeks stale
            int maxStale = 60 * 60 * 24 * 28;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }

    public static class Builder {

        public enum ParamType {
            HEADER_PARAM,
            URL_PARAM
        }

        public CommonInterceptor mCommonInterceptor;

        public Builder() {
            mCommonInterceptor = new CommonInterceptor();
        }

        public Builder addParams(String key, String value, ParamType paramType) {
            switch (paramType) {
                case HEADER_PARAM:
                    mCommonInterceptor.mHeaderParamsMap.put(key, value);
                    break;
                case URL_PARAM:
                    mCommonInterceptor.mUrlParamsMap.put(key, value);
                    break;
            }
            return this;
        }

        public Builder addParams(String key, int value, ParamType paramType) {
            return addParams(key, String.valueOf(value), paramType);
        }

        public Builder addParams(String key, float value, ParamType paramType) {
            return addParams(key, String.valueOf(value), paramType);
        }

        public Builder addParams(String key, long value, ParamType paramType) {
            return addParams(key, String.valueOf(value), paramType);
        }

        public Builder addParams(String key, double value, ParamType paramType) {
            return addParams(key, String.valueOf(value), paramType);
        }

        public CommonInterceptor build() {
            return mCommonInterceptor;
        }

    }

}