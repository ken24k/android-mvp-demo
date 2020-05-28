package com.ken24k.android.mvpdemo.common.retrofit;

import java.io.Serializable;

/**
 * base返回对象
 * Created by wangming on 2020-05-28
 */

public class BaseResponse<T> implements Serializable {

    private String message;
    private T data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T payload) {
        this.data = payload;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
