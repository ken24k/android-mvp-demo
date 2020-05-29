package com.ken24k.android.mvpdemo.common.idcard;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by wangming on 2020-05-28
 */
public interface ICameraActivity {

    void cropImage(Bitmap bitmap);

    void goSetResult(int code, Intent intent);

}
