package com.ken24k.android.mvpdemo.common.retrofit;

import com.google.gson.JsonSyntaxException;
import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 通用observer对象
 * Created by wangming on 2020-05-28
 */

public abstract class CommonObserver<T> implements Observer<T> {

    private String msg = null;

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            if (e instanceof TimeoutException || e instanceof SocketTimeoutException || e instanceof ConnectException) {
                msg = AndroidUtils.getResString(R.string.connect_exception);
            } else if (e instanceof JsonSyntaxException) {
                msg = AndroidUtils.getResString(R.string.data_exception);
            } else if (e instanceof ApiException) {
                msg = e.getMessage();
            }
        }
        if (msg == null || msg.trim().length() == 0) {
            msg = AndroidUtils.getResString(R.string.else_exception);
        }
    }

    public String getErrorMsg() {
        return msg;
    }

}
