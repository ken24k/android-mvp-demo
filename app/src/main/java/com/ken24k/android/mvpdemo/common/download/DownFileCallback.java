package com.ken24k.android.mvpdemo.common.download;

/**
 * Created by wangming on 2020-05-28
 */

public interface DownFileCallback {

    void onSuccess(String path);

    void onFail(String msg);

    void onProgress(long totalSize, long downSize);

}
