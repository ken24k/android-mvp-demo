package com.ken24k.android.mvpdemo.common.webview;

import com.ken24k.android.mvpdemo.app.MyApplication;
import com.ken24k.android.mvpdemo.common.Constants;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by wangming on 2020-05-28
 */

public class X5InitRunnable implements Runnable {

    @Override
    public void run() {
        init();
    }

    private void init() {
        // x5内核初始化接口
        QbSdk.initX5Environment(MyApplication.getInstance(), new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                if (Constants.DEBUG) {
                    AndroidUtils.showToast("TBS init：" + arg0);
                }
            }

            @Override
            public void onCoreInitFinished() {

            }
        });
    }
}
