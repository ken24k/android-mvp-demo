package com.ken24k.android.mvpdemo.model.bean.device;

/**
 * 通话记录
 * Created by wangming on 2020-05-28
 */

public class CallLogBean {

    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phoneNum;
    /**
     * 通话类型
     */
    private String type;
    /**
     * 通话时间
     */
    private String time;
    /**
     * 通话时长
     */
    private long duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}