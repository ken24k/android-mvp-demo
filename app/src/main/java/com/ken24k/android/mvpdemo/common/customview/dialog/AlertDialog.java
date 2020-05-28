package com.ken24k.android.mvpdemo.common.customview.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ken24k.android.mvpdemo.R;


/**
 * Created by wangming on 2020-05-28
 */

public class AlertDialog extends BaseDialog {

    private Context context;
    private TextView txt_title;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private View img_line;
    private View split_line;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private static boolean isShow = false;

    public AlertDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.view_alert_dialog;
    }

    @Override
    protected int getStyleId() {
        return R.style.DialogStyleWithDust;
    }

    @Override
    public AlertDialog builder() {
        super.builder();

        // 获取自定义Dialog布局中的控件
        txt_title = getRootView().findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = getRootView().findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = getRootView().findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = getRootView().findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = getRootView().findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        split_line = getRootView().findViewById(R.id.split_line);

        return this;
    }

    public AlertDialog setTitle() {
        return setTitle(null);
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if (title == null || "".equals(title)) {
            txt_title.setText(context.getString(R.string.notice));
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialog setMsg(String msg) {
        showMsg = true;
        if (msg == null || "".equals(msg)) {
            txt_msg.setText(context.getString(R.string.app_name));
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public AlertDialog setPositiveButton() {
        return setPositiveButton("");
    }

    public AlertDialog setPositiveButton(String text) {
        return setPositiveButton(text, null);
    }

    public AlertDialog setPositiveButton(final OnClickListener listener) {
        return setPositiveButton(context.getString(R.string.confirm), listener);
    }

    public AlertDialog setPositiveButton(String text, final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText(context.getString(R.string.confirm));
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                getDialog().dismiss();
                isShow = false;
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton() {
        return setNegativeButton("", null);
    }

    public AlertDialog setNegativeButton(String text) {
        return setNegativeButton(text, null);
    }

    public AlertDialog setNegativeButton(final OnClickListener listener) {
        return setNegativeButton(context.getString(R.string.cancle), listener);
    }

    public AlertDialog setNegativeButton(String text, final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText(context.getString(R.string.cancle));
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                getDialog().dismiss();
                isShow = false;
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText(context.getString(R.string.notice));
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            split_line.setVisibility(View.GONE);
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.view_alert_dialog_right_btn_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.view_alert_dialog_left_btn_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.view_alert_dialog_btn_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.view_alert_dialog_btn_selector);
        }
    }

    public void show() {
        if (!isShow) {
            setLayout();
            getDialog().show();
            isShow = true;
        }
    }

}
