package com.ken24k.android.mvpdemo.common.idcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;

import com.ken24k.android.mvpdemo.common.utils.DateUtils;
import com.ken24k.android.mvpdemo.common.utils.FileUtils;
import com.ken24k.android.mvpdemo.view.base.BasePresenter;

import java.io.File;

/**
 * Created by wangming on 2020-05-28
 */

public class CameraPresenter extends BasePresenter<ICameraActivity> implements ICameraPresenter {

    @Override
    public void takePhoto() {
        CameraUtils.getCamera().setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(final byte[] bytes, Camera camera) {
                final Camera.Size size = camera.getParameters().getPreviewSize(); // 获取预览大小
                camera.stopPreview();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int w = size.width;
                        final int h = size.height;
                        Bitmap bitmap = FileUtils.getBitmapFromByte(bytes, w, h);
                        getView().cropImage(bitmap);
                    }
                }).start();
            }
        });
    }

    @Override
    public void confirm(Bitmap mCropBitmap) {
        String imageId = DateUtils.getCurrentDate(DateUtils.DateFormat.yyyyMMddHHmmssSSS);
        String imagePath = FileUtils.FilePath.JPG_PATH + imageId + FileUtils.FileType.JPG;
        File file = new File(imagePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        if (FileUtils.saveBitmap(mCropBitmap, file, Bitmap.CompressFormat.JPEG)) {
            Intent intent = new Intent();
            intent.putExtra(IDCardCamera.IMAGE_PATH, imagePath);
            getView().goSetResult(IDCardCamera.RESULT_CODE, intent);
        }
    }

}