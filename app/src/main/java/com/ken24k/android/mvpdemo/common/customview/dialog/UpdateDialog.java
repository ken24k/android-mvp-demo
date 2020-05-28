package com.ken24k.android.mvpdemo.common.customview.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.download.DownFileCallback;
import com.ken24k.android.mvpdemo.common.download.DownLoadManager;
import com.ken24k.android.mvpdemo.common.download.DownModel;
import com.ken24k.android.mvpdemo.common.manager.AppManager;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.FileUtils;

/**
 * Created by wangming on 2020-05-28
 */

public class UpdateDialog extends BaseDialog {

    private Context context;
    private Activity activity;
    private TextView txt_title;
    private Button btn_pos;
    private View split_line;
    private ProgressBar progress;
    private TextView txt_progress;
    private static boolean isShow = false;
    private static String downloadUrl;
    private OnDownloadFail listener;

    public UpdateDialog(Context context, Activity activity, String downloadUrl) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.downloadUrl = downloadUrl;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.view_update_dialog;
    }

    @Override
    protected int getStyleId() {
        return R.style.DialogStyleWithDust;
    }

    @Override
    public UpdateDialog builder() {
        super.builder();

        // 获取自定义Dialog布局中的控件
        txt_title = getRootView().findViewById(R.id.txt_title);
        btn_pos = getRootView().findViewById(R.id.btn_pos);
        split_line = getRootView().findViewById(R.id.split_line);
        progress = getRootView().findViewById(R.id.progress);
        txt_progress = getRootView().findViewById(R.id.txt_progress);
        startProgress();

        return this;
    }

    public UpdateDialog setOnFailListener(OnDownloadFail listener) {
        this.listener = listener;
        return this;
    }

    public interface OnDownloadFail {
        void onDownloadFail();
    }

    private void startProgress() {
        txt_title.setText(AndroidUtils.getResString(R.string.downloading));
        btn_pos.setVisibility(View.INVISIBLE);
        btn_pos.setBackgroundResource(R.drawable.view_alert_dialog_btn_selector);
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                isShow = false;
                AppManager.getInstance().appExitImmediately();
            }
        });
        split_line.setVisibility(View.GONE);
        setProgress(0);
        downloadApk(downloadUrl);
    }

    private void setProgress(int p) {
        if (p == 0) {
            progress.setProgress(0);
        } else if (p > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progress.setProgress(p, true);
            } else {
                progress.setProgress(p);
            }
        }
        txt_progress.setText(p + "%");
    }

    private DownModel appDownModel = null;
    private int failCount = 0;

    private void downloadApk(final String downloadUrl) {
        FileUtils.deleteFile(FileUtils.ExternalFilePath.APP_APK_PATH);
        if (appDownModel == null) {
            appDownModel = new DownModel();
            appDownModel.setUrl(downloadUrl);
            appDownModel.setPath(FileUtils.ExternalFilePath.APP_APK_PATH);
        }
        DownLoadManager.getInstance().downFile(appDownModel, new DownFileCallback() {
            @Override
            public void onSuccess(final String path) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setProgress(100);
                        txt_title.setText(AndroidUtils.getResString(R.string.update_done));
                        btn_pos.setVisibility(View.VISIBLE);
                        split_line.setVisibility(View.VISIBLE);
                        AndroidUtils.openFile(context);
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        failCount++;
                        if (failCount <= 3) {
                            downloadApk(downloadUrl);
                        } else {
                            getDialog().dismiss();
                            if (listener != null) {
                                listener.onDownloadFail();
                            }
                        }
                    }
                });
            }

            @Override
            public void onProgress(long totalSize, long downSize) {
                if (appDownModel.getTotalSize() == 0) {
                    appDownModel.setTotalSize(totalSize);
                }
                appDownModel.setCurrentTotalSize(totalSize);
                appDownModel.setDownSize(downSize + appDownModel.getTotalSize() - appDownModel.getCurrentTotalSize());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = (int) (appDownModel.getDownSize() * 100 / appDownModel.getTotalSize());
                        setProgress(progress);
                    }
                });
            }
        });
    }

    public void show() {
        if (!isShow) {
            getDialog().show();
            isShow = true;
        }
    }

}
