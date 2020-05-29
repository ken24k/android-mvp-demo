package com.ken24k.android.mvpdemo.common.idcard;

import android.graphics.Bitmap;

/**
 * Created by wangming on 2020-05-28
 */
public interface ICameraPresenter {

    void takePhoto();

    void confirm(Bitmap mCropBitmap);

}