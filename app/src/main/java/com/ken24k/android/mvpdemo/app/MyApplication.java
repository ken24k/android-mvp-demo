package com.ken24k.android.mvpdemo.app;

import android.app.Application;

/**
 * Created by wangming on 2020-05-27
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
