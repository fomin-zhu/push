package com.fomin.push.bean;

/**
 * Created by Fomin on 2018/10/18.
 */
public class PushInfo {
    private String appId;
    private String appKey;

    public PushInfo() {
    }

    public PushInfo(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }
}
