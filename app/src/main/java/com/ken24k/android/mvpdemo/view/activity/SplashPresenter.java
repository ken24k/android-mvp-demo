package com.ken24k.android.mvpdemo.view.activity;

import android.os.Handler;

import com.ken24k.android.mvpdemo.common.Constants;
import com.ken24k.android.mvpdemo.common.initialize.InitializeUtils;
import com.ken24k.android.mvpdemo.common.permission.PermissionConstants;
import com.ken24k.android.mvpdemo.common.permission.PermissionManager;
import com.ken24k.android.mvpdemo.view.base.BasePresenter;

/**
 * Created by wangming on 2020-05-27
 */

public class SplashPresenter extends BasePresenter<ISplashActivity> implements ISplashPresenter {

    @Override
    public void getPermission() {
        PermissionManager.getInstance().requestPermissions((SplashActivity) mViewReference.get(), new PermissionConstants.PermissionManagerListener() {
                    @Override
                    public void onGranted() {
                        init();
                    }

                    @Override
                    public void onUngranted(String msg) {
//                        AppManager.getInstance().appExitImmediately();
                        init();
                    }

                    @Override
                    public void onError(String msg) {
//                        AppManager.getInstance().appExitImmediately();
                        init();
                    }
                },
                // x5webview
                PermissionConstants.Storage.WRITE_EXTERNAL_STORAGE,
                // x5webview
                PermissionConstants.Phone.READ_PHONE_STATE
        );
    }

    private void init() {
        // 执行初始化操作
        InitializeUtils.init();
        // 延迟跳转首页
        delay();
    }

    private void delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewReference.get().gotoMainActivity();
            }
        }, Constants.SPLASH_DELAY);
    }

}