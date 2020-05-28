package com.ken24k.android.mvpdemo.model.bean;

/**
 * Created by wangming on 2020-05-27
 */

public class AppVersionBean {

    private String appType = "2";// app类别 1-IOS 2-安卓
    private String appVersion = "1.0.0";// app版本
    private String description = "description";// app版本描述
    private int isCompel = 0;// 更新方式 0-非强制 1-强制
    private String downLoadUrl = "http://downapp.baidu.com/baidusearch/AndroidPhone/11.20.0.14/1/757p/20200301173507/baidusearch_AndroidPhone_11-20-0-14_757p.apk?responseContentDisposition=attachment%3Bfilename%3D%22baidusearch_AndroidPhone_757p.apk%22&responseContentType=application%2Fvnd.android.package-archive&request_id=1584348045_7098117639&type=static";// 下载地址
    private int isUpdate = 1;// 是否更新 0-不更新  1-更新
    private String releaseDate = "2020.3.12";// 发布时间

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsCompel() {
        return isCompel;
    }

    public void setIsCompel(int isCompel) {
        this.isCompel = isCompel;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
