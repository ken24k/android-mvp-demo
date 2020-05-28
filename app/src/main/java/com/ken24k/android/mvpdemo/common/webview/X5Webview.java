package com.ken24k.android.mvpdemo.common.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.ken24k.android.mvpdemo.app.MyApplication;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;

/**
 * Created by wangming on 2020-05-28
 */

public class X5Webview extends WebView {

    private String mWebviewUA = "";
    private X5WebViewListener mListener;
    private Context context;

    public X5Webview(Context context) {
        this(context, null);
    }

    public X5Webview(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public X5Webview(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, false);
    }

    public X5Webview(Context context, AttributeSet attributeSet, int i, boolean b) {
        this(context, attributeSet, i, null, b);
    }

    public X5Webview(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        this.context = context;
    }

    /**
     * 获取UserAgent
     */
    public String getUA() {
        return mWebviewUA;
    }

    /**
     * 初始化
     */
    public void init() {
        this.setWebViewClient(getClient());
        this.setWebChromeClient(getChromClient());
        initWebview();
        initJsBridge();
    }

    private WebViewClient getClient() {
        return new WebViewClient() {

            /**
             * 防止加载网页时调起系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                if (sslErrorHandler != null) {
                    sslErrorHandler.proceed();// 忽略证书错误
                }
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return null;
            }

        };
    }

    private WebChromeClient getChromClient() {
        return new WebChromeClient() {

            @Override
            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                super.onReceivedIcon(webView, bitmap);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                geolocationPermissionsCallback.invoke(s, true, false);
                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
            }
        };
    }

    private void initWebview() {
        WebSettings webSetting = this.getSettings();
        webSetting.setUserAgentString(webSetting.getUserAgentString() + mWebviewUA);

        // 支持Javascript
        webSetting.setJavaScriptEnabled(true);
        // 是否允许在WebView中访问内容URL（Content Url），内容Url访问允许WebView从安装在系统中的内容提供者载入内容。
        webSetting.setAllowContentAccess(true);
        // API18以上版本已废弃。未来版本将不支持保存WebView中的密码。设置WebView是否保存密码，默认true。
        webSetting.setSavePassword(false);
        // WebView是否保存表单数据
        webSetting.setSaveFormData(false);
        // 支持通过JS打开新窗口
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // 允许 WebView 使用 File 协议
        webSetting.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Js代码读取其他的本地文件
        webSetting.setAllowFileAccessFromFileURLs(false);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        // 不支持新窗口
        webSetting.setSupportMultipleWindows(false);
        // 开启LBS
        webSetting.setGeolocationEnabled(true);
        // 设置渲染优先级
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 支持缩放，默认为true。是BuiltInZoomControls的前提。
        webSetting.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSetting.setBuiltInZoomControls(false);
        // 隐藏原生的缩放控件
        webSetting.setDisplayZoomControls(true);

        // 可能的话使所有列的宽度不超过屏幕宽度
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 将图片调整到适合webview的大小
        webSetting.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSetting.setLoadWithOverviewMode(true);

        // 只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启应用缓存功能
        webSetting.setAppCacheEnabled(true);
        // 设置缓存最大容量
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // 开启DOM存储功能
        webSetting.setDatabaseEnabled(true);
        // 开启数据库存储功能
        webSetting.setDomStorageEnabled(true);

        // 启用地理定位
        webSetting.setGeolocationEnabled(true);
        // 设置定位信息缓存路径
        String dir = MyApplication.getInstance().getDir("database", Context.MODE_PRIVATE).getPath();
        webSetting.setGeolocationDatabasePath(dir);

        if (getX5WebViewExtension() != null) {
            // 去除滚动条
            getX5WebViewExtension().setScrollBarFadingEnabled(false);
        }

        // 支持点击事件
        this.getView().setClickable(true);
    }

    /**
     * 初始化jsBridge
     */
    private void initJsBridge() {
        // 注册JS接口
        addJavascriptInterface(new JsBridgeInterface(this), "native");
    }

    public interface X5WebViewListener {

        /**
         * 开始加载
         *
         * @param webView
         * @param s
         * @param bitmap
         */
        void onLoadingStart(WebView webView, String s, Bitmap bitmap);

        /**
         * 加载完成
         */
        void onLoadingFinish(String url);

        /**
         * 加载失败
         */
        void onLoadingFailed();

        /**
         * 加载进度完成
         */
        void onProgressFinish();

        /**
         * url拦截
         */
        void onUrlIntercept(String url);

    }

    @Override
    public void loadUrl(String s) {
        if (!s.startsWith("http")) {
            s = "http://" + s;
        }
        super.loadUrl(s);
    }

    public void setX5WebViewListener(X5WebViewListener listener) {
        this.mListener = listener;
    }

}
