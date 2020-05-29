package com.ken24k.android.mvpdemo.model.bean.device;

/**
 * Created by wangming on 2020-05-28
 */

public class AppInfoBean {

    public enum AppType {
        APP_ALL,
        APP_SYSTEM,
        APP_NO_SYSTEM,
    }

    /**
     * 名称
     */
    private String appName;
    /**
     * 包名
     */
    private String packageName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
