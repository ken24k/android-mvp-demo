package com.ken24k.android.mvpdemo.common.manager;

import android.app.Activity;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * activity栈管理器
 * Created by wangming on 2020-05-28
 */

public class AppManager {

    private AppManager() {
    }

    private static class SingleTonHolder {
        private static final AppManager INSTANCE = new AppManager();
    }

    public static AppManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * Activity栈
     */
    private Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack.lastElement();
    }

    /**
     * 检查当前Activity栈是否为空
     */
    public boolean isNullInActivityStack() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack.isEmpty();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        List<Activity> acts = new ArrayList<>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                acts.add(activity);
            }
        }
        for (Activity activity : acts) {
            finishActivity(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    private long lastClickTime;

    /**
     * 退出应用程序
     */
    public void appExit() {
        long between = 2000;
        long time = System.currentTimeMillis();
        if (time - lastClickTime < between) {
            try {
                appExitImmediately();
            } catch (Exception e) {

            }
        } else {
            AndroidUtils.showToast(R.string.exit_hint);
        }
        lastClickTime = time;
    }

    /**
     * 立即退出应用程序
     */
    public void appExitImmediately() {
        finishAllActivity();
        System.exit(0);
    }

    /**
     * 是否已打开Activity
     */
    public boolean existActivity(Class<?> cls) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

}