package com.ken24k.android.mvpdemo.common.utils;

import com.ken24k.android.mvpdemo.common.Constants;

/**
 * 系统设置工具
 * Created by wangming on 2020-05-28
 */

public class SettingsUtils {

    /**
     * 根据环境配置获取对应WEB地址
     */
    public static String getWebUrl() {
        String url = null;
        switch (Constants.ENVI) {
            case Constants.ENV.DEV:
                url = Constants.WEB_URL_DEV;
                break;
            case Constants.ENV.STG:
                url = Constants.WEB_URL_STG;
                break;
            case Constants.ENV.UAT:
                url = Constants.WEB_URL_UAT;
                break;
            case Constants.ENV.PRD:
                url = Constants.WEB_URL;
                break;
        }
        return url;
    }

    /**
     * 根据环境配置获取对应API地址
     */
    public static String getApiUrl() {
        String url = null;
        switch (Constants.ENVI) {
            case Constants.ENV.DEV:
                url = Constants.API_URL_DEV;
                break;
            case Constants.ENV.STG:
                url = Constants.API_URL_STG;
                break;
            case Constants.ENV.UAT:
                url = Constants.API_URL_UAT;
                break;
            case Constants.ENV.PRD:
                url = Constants.API_URL;
                break;
        }
        return url;
    }

}
