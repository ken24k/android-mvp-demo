package com.ken24k.android.mvpdemo.view.activity;

import android.content.Context;

import com.ken24k.android.mvpdemo.common.Constants;
import com.ken24k.android.mvpdemo.common.customview.dialog.UpdateDialogManager;
import com.ken24k.android.mvpdemo.model.bean.AppVersionBean;
import com.ken24k.android.mvpdemo.view.base.BasePresenter;


/**
 * Created by wangming on 2020-05-27
 */

public class MainPresenter extends BasePresenter<IMainActivity> implements IMainPresenter {

    @Override
    public void upgrade() {
        if (!Constants.DEBUG || !Constants.UPGRADE){
            return;
        }
        // TODO: 2020/3/17 更新接口调用
        UpdateDialogManager.getInstance().showUpdateDialog((Context) mViewReference.get(), (MainActivity) mViewReference.get(), new AppVersionBean());
    }

}