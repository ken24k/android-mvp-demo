package com.ken24k.android.mvpdemo.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.app.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * android工具
 * Created by wangming on 2020-05-28
 */

public class AndroidUtils {

    /**
     * Activity跳转
     */
    public static void gotoAct(Intent intent, Activity activity, Class<?> c) {
        intent.setClass(activity, c);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * 获取versionCode
     */
    public static String getVersionCode() {
        return getVersion()[0];
    }

    /**
     * 获取versionName
     */
    public static String getVersionName() {
        return getVersion()[1];
    }

    /**
     * 获取版本号
     */
    private static String[] getVersion() {
        // 获取packagemanager的实例
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        PackageInfo packInfo;
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            packInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        String version[] = new String[2];
        version[0] = String.valueOf(packInfo.versionCode);
        version[1] = packInfo.versionName;
        return version;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 获取屏幕宽高
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        size[0] = dm.heightPixels;
        size[1] = dm.widthPixels;
        return size;
    }

    /**
     * 检测是否存在刘海屏
     */
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        } finally {

        }
        return ret;
    }

    /**
     * 获取刘海屏的参数
     */
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        } finally {

        }
        return ret;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return BigDecimal.valueOf(dpValue).multiply(BigDecimal.valueOf(scale)).add(BigDecimal.valueOf(0.5f)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return BigDecimal.valueOf(pxValue).divide(BigDecimal.valueOf(scale), 2, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(0.5f)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 显示toast
     */
    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(getResString(resId), Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int resId) {
        showToast(getResString(resId), Toast.LENGTH_LONG);
    }

    private static void showToast(String msg, int duration) {
        Toast.makeText(MyApplication.getInstance(), msg, duration).show();
    }

    public static String getResString(int resId) {
        return MyApplication.getInstance().getResources().getString(resId);
    }

    public static List<String> getResStringList(int resId) {
        return Arrays.asList(MyApplication.getInstance().getResources().getStringArray(resId));
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {

            }
        }
        return null;
    }

    /**
     * 显示超长log
     */
    public static void logI(String tag, String msg) {
        // 因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        // 把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        // 大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        // 剩余部分
        Log.i(tag, msg);
    }

    /**
     * 获取渠道号
     */
    public static String getChannelCode() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = MyApplication.getInstance().getPackageManager().getApplicationInfo(MyApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {

        }
        String channelCode = null;
        if (appInfo != null) {
            channelCode = appInfo.metaData.getString("CHANNEL_CODE");
        }
        if (channelCode == null || "".equals(channelCode.trim())) {
            channelCode = "DEFAULT";
        }
        return channelCode;
    }

    /**
     * 打开文件（*.apk）
     */
    public static void openFile(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startInstallPermissionSettingActivity(context);
        } else {
            openFileBO(context);
        }
    }

    /**
     * 打开文件（*.apk）（8.0以上）
     */
    public static final int REQUEST_INSTALL_PACKAGES = 0x000001A2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        ((Activity) context).startActivityForResult(intent, REQUEST_INSTALL_PACKAGES);
    }

    /**
     * 打开文件（*.apk）(8.0以下）
     */
    public static void openFileBO(Context context) {
        String pathName = FileUtils.ExternalFilePath.APP_APK_PATH;
        if (pathName == null) {
            return;
        }
        File file = new File(pathName);
        if (file == null || !file.exists()) {
            return;
        }
        String authority = "com.weshare.asset.approver.fileprovider";
        String inType = "application/vnd.android.package-archive";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, authority, file);
            intent.setDataAndType(uri, inType);
        } else {
            intent.setDataAndType(Uri.fromFile(file), inType);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
    }

}