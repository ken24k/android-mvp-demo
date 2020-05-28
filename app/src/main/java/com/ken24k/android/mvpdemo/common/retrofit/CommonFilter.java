package com.ken24k.android.mvpdemo.common.retrofit;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.functions.Function;

/**
 * 通用过滤器
 * Created by wangming on 2020-05-28
 */

public class CommonFilter<T> implements Function<BaseResponse<T>, T> {

    private final static Integer CODE_SUCCESS = 200;

    @Override
    public T apply(BaseResponse<T> obj) {
        if (obj.getStatus() != CODE_SUCCESS) {
            JSONObject data = (JSONObject) obj.getData();
            throw new ApiException(obj.getMessage(), data);
        }
        return obj.getData();
    }

}
