package com.ken24k.android.mvpdemo.common.glide;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ken24k.android.mvpdemo.R;

import java.lang.ref.WeakReference;

/**
 * Glide管理工具
 * Created by wangming on 2020-05-28
 */

public class GlideManager {

    private GlideManager() {
    }

    private static class SingleTonHolder {
        private static final GlideManager INSTANCE = new GlideManager();
    }

    public static GlideManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * 图片缩放类型
     */
    public enum ScaleType {
        CENTER_CROP,
        FIT_CENTER,
        NONE
    }

    /**
     * 默认图
     */
    private int defaultImage = R.mipmap.default_image;
    /**
     * 配置器
     */
    private RequestOptions options = new RequestOptions();
    /**
     * Activity弱引用
     */
    private WeakReference<Activity> weakReference;

    /**
     * 加载图片
     */
    public void load(Activity activity, ImageView imageView, Object url) {
        load(activity, imageView, url, defaultImage, ScaleType.CENTER_CROP);
    }

    /**
     * 加载图片
     */
    public void load(Activity activity, ImageView imageView, Object url, ScaleType scaleType) {
        load(activity, imageView, url, defaultImage, scaleType);
    }

    /**
     * 加载图片
     */
    public void load(Activity activity, ImageView imageView, Object url, int defaultResourceId) {
        load(activity, imageView, url, defaultResourceId, ScaleType.CENTER_CROP);
    }

    /**
     * 加载图片
     *
     * @param activity
     * @param imageView
     * @param url
     * @param defaultResourceId
     * @param scaleType
     */
    public void load(Activity activity, ImageView imageView, Object url, int defaultResourceId, ScaleType scaleType) {
        weakReference = new WeakReference<>(activity);

        if (defaultResourceId == -1) {
            defaultResourceId = defaultImage;
        }

        options.error(defaultResourceId).diskCacheStrategy(DiskCacheStrategy.ALL);
        switch (scaleType) {
            case CENTER_CROP:
                options.centerCrop();
                break;
            case FIT_CENTER:
                options.fitCenter();
                break;
        }

        Glide.with(weakReference.get()).load(url).apply(options).into(imageView);
    }

}