package com.ken24k.android.mvpdemo.common.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.ken24k.android.mvpdemo.common.statusbar.StatusBarUtil;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.SPUtils;
import com.ken24k.android.mvpdemo.view.activity.WebviewActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * jsBridge接口
 * Created by wangming on 2020-05-28
 */

public class JsBridgeInterface {

    private X5Webview webView;

    public JsBridgeInterface(X5Webview webView) {
        this.webView = webView;
    }

    public static Context getContext() {
        return WebviewActivity.getInstance();
    }

    public static Activity getActivity() {
        return WebviewActivity.getInstance();
    }

    private static String h5_method_prefix = "window.";

    /********************************* JS 交互接口 ****************************************/

    /**
     * 修改状态栏颜色
     */
    @JavascriptInterface
    public static void changeStatusBarColor(String param) {
        final JSONObject jsonObject = JSONObject.parseObject(param);
        final String bgColor = jsonObject.getString("bgColor");
        final String fontColor = jsonObject.getString("fontColor");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StatusBarUtil.setStatusBar(getActivity(), bgColor, fontColor);
            }
        });
    }

    /**
     * 获取原生版本号
     */
    @JavascriptInterface
    public String getNativeVersion() {
        return AndroidUtils.getVersionName();
    }

    /********************************* 回调方法 ****************************************/

    /**
     * 大数据传输
     */
    public static void bigDataCallJs(final String callback, final String data) {
        final int limit = 1024;// 传输最大字符(1024*2048)
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<String>> emitter) {
                List<String> dataList = new ArrayList<>();
                if (data.length() > limit) {
                    dataList = getStrList(data, limit);
                } else {
                    dataList.add(data);
                }
                if (dataList == null || dataList.size() == 0) {
                    return;
                }
                emitter.onNext(dataList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> dataList) {
                        for (int i = 0; i < dataList.size(); i++) {
                            int status = i == 0 ? 1 : (i == dataList.size() - 1 ? 3 : 2);
                            callJs(callback, status + dataList.get(i));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     */
    private static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        List<String> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            String childStr;
            if ((index + 1) * length > inputString.length()) {
                childStr = inputString.substring(index * length);
            } else {
                childStr = inputString.substring(index * length, (index + 1) * length);
            }
            list.add(childStr);
        }
        return list;
    }

    /**
     * JS回调
     */
    public static void callJs(String callback, String params) {
        X5Webview webView = WebviewActivity.getInstance().getWebview();
        if (webView == null) {
            return;
        }
        if (callback == null || "".endsWith(callback)) {
            return;
        }
        callJs(webView, callback, params);
    }

    /**
     * JS回调
     */
    private static void callJs(final X5Webview webView, final String callback, final String params) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String script = h5_method_prefix + callback + "(";
                if (params != null) {
                    script = script + "'" + params + "'";
                }
                script = script + ")";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(script, null);
                } else {
                    webView.loadUrl("javascript:" + script);
                }
            }
        });
    }

}