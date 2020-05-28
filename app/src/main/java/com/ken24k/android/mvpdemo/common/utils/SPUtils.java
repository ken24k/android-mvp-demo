package com.ken24k.android.mvpdemo.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ken24k.android.mvpdemo.app.MyApplication;
import com.ken24k.android.mvpdemo.common.Constants;

import java.util.List;

/**
 * SharedPreferences工具
 * Created by wangming on 2020-05-28
 */

public class SPUtils {

    private SPUtils() {
        sp = MyApplication.getInstance().getSharedPreferences(Constants.ROOT_NAME, Context.MODE_PRIVATE);
    }

    private static class SingleTonHolder {
        private static final SPUtils INSTANCE = new SPUtils();
    }

    public static SPUtils getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public class KeyName {
        public static final String FIRST_START = "FIRST_START";
        public static final String WEB_URL = "WEB_URL";
        public static final String BACK_URL = "BACK_URL";
    }

    /**
     * SharedPreferences实例
     */
    private static SharedPreferences sp;

    /**
     * 保存数据
     */
    public void saveData(String keyName, Object value) {
        if (sp != null) {
            String prefStr = JSON.toJSONString(value);
            sp.edit().putString(keyName, prefStr).apply();
        }
    }

    /**
     * 获取数据
     */
    public <T extends Object> T getData(String keyName, Class<T> t) {
        T object = null;
        if (sp != null) {
            String prefStr = sp.getString(keyName, null);
            if (prefStr != null && prefStr.trim().length() > 0) {
                object = JSON.parseObject(prefStr, t);
            }
        }
        return object;
    }

    /**
     * 保存数据
     */
    public <T extends Object> void saveArrayData(String keyName, List<T> value) {
        if (sp != null) {
            String prefStr = JSONArray.toJSONString(value);
            sp.edit().putString(keyName, prefStr).apply();
        }
    }

    /**
     * 获取数据
     */
    public <T extends Object> List<T> getArrayData(String keyName, Class<T> t) {
        List<T> array = null;
        if (sp != null) {
            String prefStr = sp.getString(keyName, null);
            if (prefStr != null && prefStr.trim().length() > 0) {
                try {
                    array = JSONArray.parseArray(prefStr, t);
                } catch (Exception e) {

                }
            }
        }
        return array;
    }

    /**
     * 删除数据
     */
    public void clearData(String keyName) {
        if (sp != null) {
            sp.edit().remove(keyName).apply();
        }
    }

    /**
     * 清空所有数据
     */
    public void clearAll() {
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }
    }

}