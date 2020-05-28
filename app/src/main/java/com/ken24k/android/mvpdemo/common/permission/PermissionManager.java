package com.ken24k.android.mvpdemo.common.permission;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.Constants;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 权限管理
 * Created by wangming on 2020-05-28
 */

public class PermissionManager {

    private PermissionManager() {
    }

    private static class SingleTonHolder {
        private static final PermissionManager INSTANCE = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * Activity弱引用
     */
    private WeakReference<Activity> weakReference;
    /**
     * 监听器
     */
    private PermissionConstants.PermissionManagerListener mListener;

    /**
     * 申请权限
     */
    public void requestPermissions(Activity activity, String... permission) {
        requestPermissions(activity, null, permission);
    }

    /**
     * 申请权限
     */
    public void requestPermissions(Activity activity, PermissionConstants.PermissionManagerListener listener, final String... permissions) {
        weakReference = new WeakReference<>(activity);
        mListener = listener;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) weakReference.get());
                rxPermissions.requestEachCombined(getPermissions(permissions))
                        .subscribe(new Observer<com.tbruyelle.rxpermissions2.Permission>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(com.tbruyelle.rxpermissions2.Permission permission) {
                                if (permission.granted) {
                                    if (mListener != null) {
                                        mListener.onGranted();
                                    }
                                } else {
                                    String msg;
                                    if (Constants.DEBUG) {
                                        msg = String.format(AndroidUtils.getResString(R.string.permisssion_failed), permission.name);
                                    } else {
                                        msg = String.format(AndroidUtils.getResString(R.string.permisssion_failed));
                                    }
                                    AndroidUtils.showToast(msg);
                                    if (mListener != null) {
                                        mListener.onUngranted(msg);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mListener != null) {
                                    mListener.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    /**
     * 类型转换
     */
    private String[] getPermissions(String[] permissions) {
        List<String> temp = new ArrayList<>();
        for (String per : permissions) {
            temp.add(per);
        }
        return temp.toArray(new String[temp.size()]);
    }

}