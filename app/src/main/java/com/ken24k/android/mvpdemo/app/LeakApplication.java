package com.ken24k.android.mvpdemo.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * LeakCanaryApplication
 * Created by wangming on 2020-05-28
 */

public class LeakApplication extends MyApplication {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = setupLeakCanary();
    }

    /**
     * 初始化watcher
     */
    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                RefWatcher refWatcher = LeakApplication.getRefWatcher(activity);
                refWatcher.watch(activity);
            }
        });
        return LeakCanary.install(this);
    }

    /**
     * 获得watcher
     */
    public static RefWatcher getRefWatcher(Context context) {
        LeakApplication leakApplication = (LeakApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

}
