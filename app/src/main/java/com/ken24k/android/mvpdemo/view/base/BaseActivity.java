package com.ken24k.android.mvpdemo.view.base;

import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ken24k.android.mvpdemo.common.manager.AppManager;

/**
 * Created by wangming on 2020-05-27
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;

    protected abstract void setInstance();

    protected abstract T createPresenter();

    protected abstract void initBeforeSetContent();

    protected abstract int getContentViewId();

    protected abstract void bindView();

    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建presenter
        mPresenter = createPresenter();
        // 关联View
        mPresenter.attachView((V) this);
        // 设置activity instance
        setInstance();
        // 初始化界面
        initBeforeSetContent();
        // 加载资源文件
        setContentView(getContentViewId());
        // view绑定
        bindView();
        // 初始化页面
        initView();
        // 界面管理器
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            // 解绑view
            mPresenter.detachView();
        }
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 使窗口支持透明度设置
        // x5webview播放视频为了避免闪屏和透明问题设置
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

}