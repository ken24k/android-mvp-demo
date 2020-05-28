package com.ken24k.android.mvpdemo.common.retrofit;


import com.ken24k.android.mvpdemo.app.MyApplication;
import com.ken24k.android.mvpdemo.common.utils.SettingsUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit管理器
 * Created by wangming on 2020-05-28
 */

public class RetrofitServiceManager {

    private RetrofitServiceManager() {
        init();
    }

    private static class SingleTonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    public static RetrofitServiceManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * 连接超时时间
     */
    private final int DEFAULT_TIME_OUT = 5;
    /**
     * 读写操作超时时间
     */
    private final int DEFAULT_READ_TIME_OUT = 10;
    /**
     * Retrofit实例
     */
    private Retrofit mRetrofit;

    /**
     * 初始化
     */
    private RetrofitServiceManager init() {

        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 连接超时时间
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        // 写操作超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        // 读操作超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);

        // 设置缓存 10M
        Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), 1024 * 1024 * 10);
        builder.cache(cache);

        // 添加公共参数拦截器
        CommonInterceptor commonInterceptor = new CommonInterceptor.Builder().build();
        builder.addInterceptor(commonInterceptor);

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SettingsUtils.getApiUrl())
                .build();

        return this;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}