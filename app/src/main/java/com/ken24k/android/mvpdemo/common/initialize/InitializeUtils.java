package com.ken24k.android.mvpdemo.common.initialize;

import com.ken24k.android.mvpdemo.common.webview.X5InitRunnable;

import java.util.ArrayList;

/**
 * Created by wangming on 2020-05-28
 */

public class InitializeUtils {

    private static ArrayList<Runnable> list = new ArrayList<>();

    public static void init() {
        createRunnable();// 初始化任务
        // 添加任务
        for (Runnable runnable : list) {
            ThreadManager.executeProxy(runnable);
        }
        list.clear();
    }

    private static void createRunnable() {
        list.add(new X5InitRunnable());
    }

}
