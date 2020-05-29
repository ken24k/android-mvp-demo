package com.ken24k.android.mvpdemo.model.bean.device;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息
 * Created by wangming on 2020-05-28
 */

public class DeviceInfoBean {

    /**
     * sim卡手机号
     */
    private String[] nativePhoneNumber;
    /**
     * imei
     */
    private String imei;
    /**
     * 手机系统版本
     */
    private String system;
    /**
     * 手机型号
     */
    private String phoneType;
    /**
     * 通话记录
     */
    private List<CallLogBean> phoneCallRecordList = new ArrayList<>();
    /**
     * 通讯录
     */
    private List<ContactsBean> phoneBookList = new ArrayList<>();
    /**
     * app安装列表
     */
    private List<AppInfoBean> phoneAppList = new ArrayList<>();
    /**
     * 手机ip
     */
    private String phoneIp;
    /**
     * wifiMac
     */
    private String wifiMac;
    /**
     * wifiBssid
     */
    private String wifiBssid;
    /**
     * wifiSsid
     */
    private String wifiSsid;
    /**
     * 手机外网ip
     */
    private String phoneIpOutSide;

    public String[] getNativePhoneNumber() {
        return nativePhoneNumber;
    }

    public void setNativePhoneNumber(String[] nativePhoneNumber) {
        this.nativePhoneNumber = nativePhoneNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public List<CallLogBean> getPhoneCallRecordList() {
        return phoneCallRecordList;
    }

    public void setPhoneCallRecordList(List<CallLogBean> phoneCallRecordList) {
        this.phoneCallRecordList = phoneCallRecordList;
    }

    public List<ContactsBean> getPhoneBookList() {
        return phoneBookList;
    }

    public void setPhoneBookList(List<ContactsBean> phoneBookList) {
        this.phoneBookList = phoneBookList;
    }

    public List<AppInfoBean> getPhoneAppList() {
        return phoneAppList;
    }

    public void setPhoneAppList(List<AppInfoBean> phoneAppList) {
        this.phoneAppList = phoneAppList;
    }

    public String getPhoneIp() {
        return phoneIp;
    }

    public void setPhoneIp(String phoneIp) {
        this.phoneIp = phoneIp;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getWifiBssid() {
        return wifiBssid;
    }

    public void setWifiBssid(String wifiBssid) {
        this.wifiBssid = wifiBssid;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public String getPhoneIpOutSide() {
        return phoneIpOutSide;
    }

    public void setPhoneIpOutSide(String phoneIpOutSide) {
        this.phoneIpOutSide = phoneIpOutSide;
    }
}