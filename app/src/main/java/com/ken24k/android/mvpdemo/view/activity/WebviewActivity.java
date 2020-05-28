package com.ken24k.android.mvpdemo.view.activity;

import android.view.KeyEvent;
import android.view.WindowManager;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.Constants;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.SettingsUtils;
import com.ken24k.android.mvpdemo.common.webview.X5Webview;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;


/**
 * Created by wangming on 2020-05-27
 */

public class WebviewActivity extends BaseActivity<IWebviewActivity, WebviewPresenter> implements IWebviewActivity {

    private static WebviewActivity instance;
    public static String CALL_BACK;

    private String mDefaultUrl = "http://soft.imtt.qq.com/browser/tes/feedback.html";// 显示000000表示加载的是系统内核，显示大于零的数字表示加载了x5内核
    private X5Webview webView;
    private String mUrl;

    public static WebviewActivity getInstance() {
        return instance;
    }

    private String getH5Url() {
        String url = getIntent().getStringExtra("url");
        if (url != null && url.length() > 0) {
            return url;
        } else {
            return SettingsUtils.getWebUrl();
        }
    }

    public X5Webview getWebview() {
        return webView;
    }

    @Override
    protected void setInstance() {
        instance = this;
    }

    @Override
    protected WebviewPresenter createPresenter() {
        return new WebviewPresenter();
    }

    @Override
    protected void initBeforeSetContent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mUrl = getH5Url();// 获取web地址

        if (Constants.DEBUG) {
            AndroidUtils.showToast("页面地址：" + mUrl);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void bindView() {
        webView = findViewById(R.id.webview);
    }

    @Override
    protected void initView() {
        initWebview();
    }

    private void initWebview() {
        // 初始化webview
        webView.init();

        // 加载页面
        webView.loadUrl(mUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!Constants.DEBUG) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
