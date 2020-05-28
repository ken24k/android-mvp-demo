package com.ken24k.android.mvpdemo.common.intentaction;

import android.app.Activity;
import android.content.Context;

import com.ken24k.android.mvpdemo.common.permission.PermissionConstants;
import com.ken24k.android.mvpdemo.common.permission.PermissionManager;

import java.lang.ref.WeakReference;

/**
 * intent事件工具
 * Created by wangming on 2020-05-28
 */

public class IntentActionManager {

    private IntentActionManager() {
    }

    private static class SingleTonHolder {
        private static final IntentActionManager INSTANCE = new IntentActionManager();
    }

    public static IntentActionManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public class Action {
        /**
         * 拍照
         */
        public static final int TAKE_PHOTO = 0x000003E0;
        /**
         * 打电话
         */
        public static final int PHONE_CALL = 0x000003E1;
        /**
         * 打开电话簿
         */
        public static final int CONTACTS_BOOK = 0x000003E2;
    }

    /**
     * Context弱引用
     */
    private WeakReference<Context> weakReference;
    /**
     * 电话号码
     */
    private String mPhoneCallNumber;

    /**
     * 开始intent事件
     */
    public void startAction(Context context, int action) {
        startAction(context, action, null, true);
    }

    /**
     * 开始intent事件
     */
    public void startAction(Context context, int action, boolean permission) {
        startAction(context, action, null, permission);
    }

    /**
     * 开始intent事件
     */
    public void startAction(Context context, final int action, final String phoneNumber, boolean permission) {
        weakReference = new WeakReference<>(context);

        if (permission) {
            String[] permissions = {};
            switch (action) {
                case Action.PHONE_CALL:
                    mPhoneCallNumber = phoneNumber;
                    permissions = new String[]{PermissionConstants.Phone.CALL_PHONE};
                    break;
                case Action.TAKE_PHOTO:
                    permissions = new String[]{PermissionConstants.Storage.WRITE_EXTERNAL_STORAGE, PermissionConstants.Camera.CAMERA};
                    break;
                case Action.CONTACTS_BOOK:
                    permissions = new String[]{PermissionConstants.Contacts.READ_CONTACTS};
                    break;
                default:
                    break;
            }

            PermissionManager.getInstance().requestPermissions((Activity) context, new PermissionConstants.PermissionManagerListener() {
                @Override
                public void onGranted() {
                    startAction(action);
                }

                @Override
                public void onUngranted(String msg) {

                }

                @Override
                public void onError(String msg) {

                }
            }, permissions);
        } else {
            startAction(action);
        }
    }

    private void startAction(int action) {
        switch (action) {
            case Action.PHONE_CALL:
                if (mPhoneCallNumber != null) {
                    IntentActionService.getInstance().phoneCall(weakReference.get(), mPhoneCallNumber);
                }
                break;
            case Action.TAKE_PHOTO:
                IntentActionService.getInstance().takePhoto(weakReference.get(), Action.TAKE_PHOTO);
                break;
            case Action.CONTACTS_BOOK:
                IntentActionService.getInstance().contactsBook(weakReference.get(), Action.CONTACTS_BOOK);
                break;
            default:
                break;
        }
    }

}