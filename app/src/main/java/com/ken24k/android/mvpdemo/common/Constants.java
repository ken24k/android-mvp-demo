package com.ken24k.android.mvpdemo.common;

/**
 * 常量
 * Created by wangming on 2020-05-28
 */

public class Constants {

    public static final String ROOT_NAME = "com.ken24k.android.mvpdemo";

    public static class ENV {
        public static final int DEV = 0;
        public static final int STG = 1;
        public static final int UAT = 2;
        public static final int PRD = 9;
    }

    /**************************************** CONFIG ******************************************/

    /**
     * DEBUG模式开关
     */
    public static final boolean DEBUG = true;
    /**
     * SDK DEBUG模式开关
     */
    public static final boolean SDK_DEBUG = true;
    /**
     * 更新开关
     */
    public static final boolean UPGRADE = true;
    /**
     * 环境配置
     */
    public static final int ENVI = ENV.DEV;// 环境选择
    /**
     * 启动页延迟
     */
    public static final int SPLASH_DELAY = 1500;// 单位毫秒

    /**************************************** URL ******************************************/

    /**
     * API地址
     */
    public static final String API_URL = "https://ken24k.com/";// prd
    /**
     * H5页面地址
     */
    public static final String WEB_URL = "https://ken24k.com";// prd

    /**
     * API地址
     */
    public static final String API_URL_DEV = "https://ken24k.com/";// dev
    /**
     * H5页面地址
     */
    public static final String WEB_URL_DEV = "https://ken24k.com";// dev

    /**
     * API地址
     */
    public static final String API_URL_STG = "https://ken24k.com/";// stg
    /**
     * H5页面地址
     */
    public static final String WEB_URL_STG = "https://ken24k.com";// stg

    /**
     * API地址
     */
    public static final String API_URL_UAT = "https://ken24k.com/";// uat
    /**
     * H5页面地址
     */
    public static final String WEB_URL_UAT = "https://ken24k.com";// uat

}
