package com.fomin.push.huawei.callback;

import com.huawei.hms.api.HuaweiApiClient;
import com.fomin.push.util.LogUtil;

/**
 * 连接client空回调
 * Created by Fomin on 2018/10/23.
 */
public class EmptyConnectCallback implements IClientConnectCallback {

    private String msgPre;

    public EmptyConnectCallback(String msgPre){
        this.msgPre = msgPre;
    }

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
       LogUtil.d(msgPre + rst);
    }
}
