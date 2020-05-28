package com.ken24k.android.mvpdemo.common.retrofit;

import com.alibaba.fastjson.JSONObject;

/**
 * 自定义exception
 * Created by wangming on 2020-05-28
 */

public class ApiException extends RuntimeException {

    private final JSONObject data;

    public ApiException(String message, JSONObject object) {
        super(message);
        this.data = object;
    }

    public JSONObject getData() {
        return data;
    }

}
