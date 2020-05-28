package com.ken24k.android.mvpdemo.common.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by wangming on 2020-05-28
 */

public abstract class BaseDialog {

    private Context context;
    private Dialog dialog;
    private View view;

    protected abstract int getContentViewId();

    protected abstract int getStyleId();

    public BaseDialog(Context context) {
        this.context = context;
    }

    public BaseDialog builder() {
        dialog = new Dialog(context, getStyleId());

        // 获取Dialog布局
        view = LayoutInflater.from(context).inflate(getContentViewId(), null);

        // 定义Dialog布局和参数
        dialog.setContentView(view);

        // 设置返回键不可以点击消失
        setCancelable(false);
        // 设置弹框外不可以点击消失
        setCanceledOnTouchOutside(false);

        return this;
    }

    protected View getRootView() {
        return view;
    }

    protected Dialog getDialog() {
        return dialog;
    }

    protected BaseDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    protected BaseDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

}
