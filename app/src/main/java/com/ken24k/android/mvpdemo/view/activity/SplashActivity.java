package com.ken24k.android.mvpdemo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.SPUtils;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;

/**
 * Created by wangming on 2020-05-27
 */

public class SplashActivity extends BaseActivity<ISplashActivity, SplashPresenter> implements ISplashActivity {

    private static SplashActivity instance;

    public static SplashActivity getInstance() {
        return instance;
    }

    @Override
    protected void setInstance() {
        instance = this;
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void initBeforeSetContent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void gotoMainActivity() {
        Intent intent = new Intent();
        AndroidUtils.gotoAct(intent, getInstance(), MainActivity.class);
        SPUtils.getInstance().saveData(SPUtils.KeyName.FIRST_START, true);
    }

}
