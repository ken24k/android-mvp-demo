package com.ken24k.android.mvpdemo.view.base;

import java.lang.ref.WeakReference;

/**
 * Created by wangming on 2020-05-27
 */

public abstract class BasePresenter<V> {

    protected WeakReference<V> mViewReference;

    /**
     * 进行关联
     */
    public void attachView(V view) {
        mViewReference = new WeakReference<>(view);
    }

    /**
     * 解除关联
     */
    public void detachView() {
        if (mViewReference != null) {
            mViewReference.clear();
            mViewReference = null;
        }
    }

    /**
     * 取得view接口
     */
    protected V getView() {
        return mViewReference.get();
    }

}