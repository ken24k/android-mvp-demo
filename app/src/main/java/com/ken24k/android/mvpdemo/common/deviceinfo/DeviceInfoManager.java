package com.ken24k.android.mvpdemo.common.deviceinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;

import com.ken24k.android.mvpdemo.app.MyApplication;
import com.ken24k.android.mvpdemo.common.permission.PermissionConstants;
import com.ken24k.android.mvpdemo.common.permission.PermissionManager;
import com.ken24k.android.mvpdemo.common.utils.DateUtils;
import com.ken24k.android.mvpdemo.model.bean.device.AppInfoBean;
import com.ken24k.android.mvpdemo.model.bean.device.CallLogBean;
import com.ken24k.android.mvpdemo.model.bean.device.ContactsBean;
import com.ken24k.android.mvpdemo.model.bean.device.DeviceInfoBean;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.WIFI_SERVICE;

/**
 * 设备信息获取工具
 * Created by wangming on 2020-05-28
 */
public class DeviceInfoManager {

    private DeviceInfoManager() {
    }

    private static class SingleTonHolder {
        private static final DeviceInfoManager INSTANCE = new DeviceInfoManager();
    }

    public static DeviceInfoManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public class Action {
        /**
         * 设备号
         */
        public static final int DEVICE_ID = 0x000002E0;
        /**
         * 通讯录
         */
        public static final int CONTACTS_INFO = 0x000002E1;
        /**
         * 通话记录
         */
        public static final int CALL_LOG = 0x000002E2;
        /**
         * 手机系统版本
         */
        public static final int PHONE_SYSTEM = 0x000002E3;
        /**
         * 手机型号
         */
        public static final int PHONE_TYPE = 0x000002E4;
        /**
         * App安装列表
         */
        public static final int APP_LIST = 0x000002E5;
        /**
         * SIM卡电话号码
         */
        public static final int SIM_PHONE_NUMBER = 0x000002E6;
        /**
         * 设备ip
         */
        public static final int DEVICE_IP = 0x000002E7;
        /**
         * 设备wifiMac
         */
        public static final int WIFI_MAC = 0x000002E8;
        /**
         * 设备wifiBssid
         */
        public static final int WIFI_BSSID = 0x000002E9;
        /**
         * 设备wifiSsid
         */
        public static final int WIFI_SSID = 0x000002D0;
        /**
         * 设备外网ip
         */
        public static final int DEVICE_IP_OUTSIDE = 0x000002D1;
    }

    /**
     * Context弱引用
     */
    private WeakReference<Context> weakReference;
    /**
     * 设备信息
     */
    private DeviceInfoBean deviceInfoBean;
    /**
     * 数据类别
     */
    private int[] mTypes;
    /**
     * 数据类别计数
     */
    private int mTypeCount;
    /**
     * 回调监听
     */
    private DeviceInfoListener mListener;

    /**
     * 获取设备信息
     */
    public void getDeviceInfo(Context context, DeviceInfoListener listener, int... types) {
        getDeviceInfo(context, listener, true, types);
    }

    /**
     * 获取设备信息
     */
    public void getDeviceInfo(Context context, DeviceInfoListener listener, boolean permission, int... types) {
        weakReference = new WeakReference<>(context);
        deviceInfoBean = new DeviceInfoBean();
        mListener = listener;
        mTypes = types;
        mTypeCount = mTypes.length;

        if (permission) {
            List<String> permissionArray = new ArrayList<>();
            for (int type : types) {
                switch (type) {
                    case Action.DEVICE_ID:
                    case Action.APP_LIST:
                    case Action.SIM_PHONE_NUMBER:
                        if (!permissionArray.contains(PermissionConstants.Phone.READ_PHONE_STATE)) {
                            permissionArray.add(PermissionConstants.Phone.READ_PHONE_STATE);
                        }
                        break;
                    case Action.CONTACTS_INFO:
                        if (!permissionArray.contains(PermissionConstants.Contacts.READ_CONTACTS)) {
                            permissionArray.add(PermissionConstants.Contacts.READ_CONTACTS);
                        }
                        break;
                    case Action.CALL_LOG:
                        if (!permissionArray.contains(PermissionConstants.Phone.READ_CALL_LOG)) {
                            permissionArray.add(PermissionConstants.Phone.READ_CALL_LOG);
                        }
                        break;
                    case Action.WIFI_SSID:
                    case Action.WIFI_MAC:
                    case Action.WIFI_BSSID:
                        if (!permissionArray.contains(PermissionConstants.Location.ACCESS_FINE_LOCATION)) {
                            permissionArray.add(PermissionConstants.Location.ACCESS_FINE_LOCATION);
                        }
                        break;
                    case Action.PHONE_SYSTEM:
                    case Action.PHONE_TYPE:
                    case Action.DEVICE_IP:
                    case Action.DEVICE_IP_OUTSIDE:
                        break;
                    default:
                        break;
                }
            }
            String[] permissions = permissionArray.toArray(new String[permissionArray.size()]);

            PermissionManager.getInstance().requestPermissions((Activity) context, new PermissionConstants.PermissionManagerListener() {
                @Override
                public void onGranted() {
                    getDeviceInfoByRx();
                }

                @Override
                public void onUngranted(String msg) {
                    if (mListener != null) {
                        mListener.onResults(deviceInfoBean);
                    }
                }

                @Override
                public void onError(String msg) {
                    if (mListener != null) {
                        mListener.onResults(deviceInfoBean);
                    }
                }
            }, permissions);
        } else {
            getDeviceInfoByRx();
        }
    }

    private void getDeviceInfo() {
        for (int type : mTypes) {
            switch (type) {
                case Action.DEVICE_ID:
                    getImei(weakReference.get());
                    break;
                case Action.CONTACTS_INFO:
                    getContacts(weakReference.get());
                    break;
                case Action.CALL_LOG:
                    getCallLog(weakReference.get());
                    break;
                case Action.PHONE_SYSTEM:
                    getPhoneSystem();
                    break;
                case Action.PHONE_TYPE:
                    getPhoneType();
                    break;
                case Action.APP_LIST:
                    getAppInfoList();
                    break;
                case Action.SIM_PHONE_NUMBER:
                    getSimInfo(weakReference.get());
                    break;
                case Action.DEVICE_IP:
                    getDeviceIp(weakReference.get());
                    break;
                case Action.WIFI_MAC:
                    getPhoneWifiMac(weakReference.get());
                    break;
                case Action.WIFI_BSSID:
                    getPhoneWifiBssid(weakReference.get());
                    break;
                case Action.WIFI_SSID:
                    getPhoneWifiSsid(weakReference.get());
                    break;
                case Action.DEVICE_IP_OUTSIDE:
                    getDeviceIpOutSide();
                    break;
                default:
                    break;
            }
        }
    }

    private void getDeviceInfoByRx() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {
                getDeviceInfo();
                emitter.onNext("");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String version) {
                        callback();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface DeviceInfoListener {
        /**
         * 监听获取联系人结果
         */
        void onResults(DeviceInfoBean deviceInfoBean);
    }

    private void setCount() {
        mTypeCount--;
    }

    private void callback() {
        if (mTypeCount > 0) {
            return;
        }
        if (mListener != null) {
            mListener.onResults(deviceInfoBean);
        }
    }

    /**
     * 获取设备号
     */
    @SuppressLint("MissingPermission")
    private void getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        deviceInfoBean.setImei(imei);
        setCount();
    }

    /**
     * 获取通讯录数据
     */
    private void getContacts(Context context) {
        List<ContactsBean> list = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                ContactsBean contactClass = new ContactsBean();
                contactClass.setName(name);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(Phone.NUMBER));
                        int type = pCur.getInt(pCur.getColumnIndex(Phone.TYPE));
                        switch (type) {
                            case Phone.TYPE_HOME:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_MOBILE:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_WORK:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_FAX_HOME:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_FAX_WORK:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_MAIN:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_OTHER:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_CUSTOM:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                            case Phone.TYPE_PAGER:
                                if (phoneNo != null && !phoneNo.equals("")) {
                                    contactClass.addContactPhoneNums(phoneNo);
                                }
                                break;
                        }
                    }
                    pCur.close();
                    list.add(contactClass);
                }
            }
        }

        deviceInfoBean.setPhoneBookList(list);
        setCount();
    }

    /**
     * 获取通话记录
     */
    @SuppressLint("MissingPermission")
    private void getCallLog(Context context) {
        List<CallLogBean> callLogList = new ArrayList<>();
        CallLogBean callLogBean;
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, new String[]{
                Calls.CACHED_NORMALIZED_NUMBER,
                Calls.NUMBER,
                Calls.CACHED_NAME,
                Calls.TYPE,
                Calls.DATE,
                Calls.DURATION
        }, null, null, Calls.DATE);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                callLogBean = new CallLogBean();

                // 电话号码
                String phoneNumber;
                phoneNumber = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NORMALIZED_NUMBER));
                if (phoneNumber == null || phoneNumber.equals("")) {
                    phoneNumber = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
                }
                callLogBean.setPhoneNum(phoneNumber);
                // 通话人名称
                String name = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME));
                callLogBean.setName(name);
                // 通话类型
                int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
                String typeStr = null;
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        typeStr = "IN";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        typeStr = "OUT";
                        break;
                }
                callLogBean.setType(typeStr);
                // 通话时间
                long date = cursor.getLong(cursor.getColumnIndex(Calls.DATE));
                if (date != 0) {
                    callLogBean.setTime(DateUtils.dateTransfer(new Date(date), DateUtils.DateFormat.date_standard));
                }
                // 通话时长
                long duration = cursor.getLong(cursor.getColumnIndex(Calls.DURATION));
                callLogBean.setDuration(duration);

                if (phoneNumber == null || phoneNumber.equals("")) {
                    continue;
                }
                if (CallLog.Calls.INCOMING_TYPE != type && CallLog.Calls.OUTGOING_TYPE != type) {
                    continue;
                }
                callLogList.add(callLogBean);
            }
            cursor.close();
        }

        deviceInfoBean.setPhoneCallRecordList(callLogList);
        setCount();
    }

    /**
     * 获取应用安装列表
     */
    private void getAppInfoList() {
        List<AppInfoBean> appInfoList = getAppInfoList(AppInfoBean.AppType.APP_ALL);
        deviceInfoBean.setPhoneAppList(appInfoList);
        setCount();
    }

    /**
     * 获取应用安装列表
     */
    private List<AppInfoBean> getAppInfoList(AppInfoBean.AppType appType) {
        List<AppInfoBean> appList = new ArrayList<>(); //用来存储获取的应用信息数据　　　　　
        List<PackageInfo> packages = MyApplication.getInstance().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfoBean tmpInfo = new AppInfoBean();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(MyApplication.getInstance().getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            if (appType == AppInfoBean.AppType.APP_ALL) {
                //手机全部应用
                appList.add(tmpInfo);
            } else if (appType == AppInfoBean.AppType.APP_SYSTEM) {
                //手机系统应用
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appList.add(tmpInfo);
                }
            } else if (appType == AppInfoBean.AppType.APP_NO_SYSTEM) {
                //手机非系统应用
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    appList.add(tmpInfo);
                }
            }
        }
        return appList;
    }

    /**
     * 获取手机系统版本
     */
    private void getPhoneSystem() {
        String version = Build.VERSION.RELEASE;
        deviceInfoBean.setSystem("Android " + version);
        setCount();
    }

    /**
     * 获取手机型号
     */
    private void getPhoneType() {
        String brand = Build.BRAND;
        String model = Build.MODEL;
        deviceInfoBean.setPhoneType(brand + " " + model);
        setCount();
    }

    /**
     * 获取SIM卡信息
     */
    @SuppressLint("MissingPermission")
    private void getSimInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String[] phoneNum = new String[2];
        phoneNum[0] = telephonyManager.getLine1Number();
        phoneNum[1] = null;
        deviceInfoBean.setNativePhoneNumber(phoneNum);
        setCount();
    }

    /**
     * 获取IP地址
     */
    private void getDeviceIp(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                deviceInfoBean.setPhoneIp(inetAddress.getHostAddress());
                                setCount();
                            }
                        }
                    }
                } catch (SocketException e) {
                    deviceInfoBean.setPhoneIp("");
                    setCount();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                deviceInfoBean.setPhoneIp(ipAddress);
                setCount();
            }
        }
        // 当前无网络连接,请在设置中打开网络
        else {
            deviceInfoBean.setPhoneIp("");
            setCount();
        }
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取手机wifiMac
     */
    private void getPhoneWifiMac(Context context) {
        String wifiMac;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wifiMac = getPhoneWifiMacAfterNoSeven();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            wifiMac = getPhoneWifiMacBeforeNoSeven();
        } else {
            wifiMac = getPhoneWifiMacBeforeNoSix(context);
        }
        deviceInfoBean.setWifiMac(wifiMac);
        setCount();
    }

    /**
     * 获取手机wifiMac:7之后
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> enNetInterface = NetworkInterface.getNetworkInterfaces();
            // 是否还有元素
            while (enNetInterface.hasMoreElements()) {
                NetworkInterface ni = enNetInterface.nextElement();// 得到下一个元素
                Enumeration<InetAddress> enIp = ni.getInetAddresses();// 得到一个ip地址的列举
                while (enIp.hasMoreElements()) {
                    ip = enIp.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            return null;
        }
        return ip;
    }

    /**
     * 获取手机wifiMac:7之后
     */
    private String getPhoneWifiMacAfterNoSeven() {
        String strMacAddr;
        try {
            // 获得Ip地址
            InetAddress ip = getLocalInetAddress();
            if (ip == null) {
                return "";
            }
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            return "";
        }
        return strMacAddr;
    }

    /**
     * 获取手机wifiMac:6-7之前
     */
    private String getPhoneWifiMacBeforeNoSeven() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            return "";
        }
        return macSerial;
    }

    /**
     * 获取手机wifiMac:6之前
     */
    private String getPhoneWifiMacBeforeNoSix(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            return winfo.getMacAddress();
        }
        return "";
    }

    /**
     * 获取手机wifiBssid
     */
    private void getPhoneWifiBssid(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        String bssid = "";
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            bssid = getNetworkState(context) == 0 ? winfo.getBSSID() : "cellular network";
        }
        deviceInfoBean.setWifiBssid(bssid);
        setCount();
    }

    /**
     * 获取手机wifiSsid
     */
    private void getPhoneWifiSsid(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        String ssid = "";
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            String s = winfo.getSSID();
            ssid = getNetworkState(context) == 0 ? s.substring(1, s.length() - 1) : "cellular network";
        }
        deviceInfoBean.setWifiSsid(ssid);
        setCount();
    }

    /**
     * 获取当前网络连接的类型
     *
     * @return int（-1-没有网络连接；0-wifi连接；2-2G；3-3G；4-4G；5-5G）
     */
    private int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return -1;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return -1;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return 0;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 2;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 3;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 4;
            default:
                return 5;
        }
    }

    /**
     * 获取设备外网IP
     */
    private void getDeviceIpOutSide() {
        String ip = "";
        InputStream in = null;
        try {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //设置浏览器ua 保证不出现503
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                // 将流转化为字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String tmpString;
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");
                if (code.equals("0")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    ip = data.getString("ip");
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {

            }
            deviceInfoBean.setPhoneIpOutSide(ip);
            setCount();
        }
    }

}