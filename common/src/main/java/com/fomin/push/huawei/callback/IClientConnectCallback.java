package com.fomin.push.huawei.callback;

import com.huawei.hms.api.HuaweiApiClient;

/**
 * HuaweiApiClient 连接结果回调
 * Created by Fomin on 2018/10/23.
 */
public interface IClientConnectCallback {
    /**
     * HuaweiApiClient 连接结果回调
     * @param rst 结果码
     * @param client HuaweiApiClient 实例
     */
    void onConnect(int rst, HuaweiApiClient client);
}
