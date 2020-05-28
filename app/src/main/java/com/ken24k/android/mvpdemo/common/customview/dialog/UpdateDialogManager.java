package com.ken24k.android.mvpdemo.common.customview.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.permission.PermissionConstants;
import com.ken24k.android.mvpdemo.common.permission.PermissionManager;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.model.bean.AppVersionBean;

/**
 * Created by wangming on 2020-05-28
 */

public class UpdateDialogManager {

    private UpdateDialogManager() {
    }

    private static class SingleTonHolder {
        private static final UpdateDialogManager INSTANCE = new UpdateDialogManager();
    }

    public static UpdateDialogManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public void showUpdateDialog(final Context context, final Activity activity, final AppVersionBean appVersion) {
        AlertDialog dialog = new AlertDialog(context).builder();
        dialog.setTitle(String.format(AndroidUtils.getResString(R.string.update_title), appVersion.getReleaseDate(), appVersion.getAppVersion()))
                .setMsg(appVersion.getDescription())
                .setPositiveButton(AndroidUtils.getResString(R.string.update_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionManager.getInstance().requestPermissions(activity, new PermissionConstants.PermissionManagerListener() {
                            @Override
                            public void onGranted() {
                                showUpdateProcessDialog(context, activity, appVersion);
                            }

                            @Override
                            public void onUngranted(String msg) {

                            }

                            @Override
                            public void onError(String msg) {

                            }
                        }, PermissionConstants.Storage.WRITE_EXTERNAL_STORAGE, PermissionConstants.Storage.READ_EXTERNAL_STORAGE);
                    }
                });
        if (appVersion.getIsCompel() == 0) {
            dialog.setNegativeButton(AndroidUtils.getResString(R.string.update_cancle));
        }
        dialog.show();
    }

    private void showUpdateProcessDialog(final Context context, final Activity activity, final AppVersionBean appVersion) {
        UpdateDialog updateDialog = new UpdateDialog(context, activity, appVersion.getDownLoadUrl())
                .setOnFailListener(new UpdateDialog.OnDownloadFail() {
                    @Override
                    public void onDownloadFail() {
                        showUpdateDialog(context, activity, appVersion);
                    }
                })
                .builder();
        updateDialog.show();
    }

}