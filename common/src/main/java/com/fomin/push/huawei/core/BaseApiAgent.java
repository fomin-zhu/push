package com.fomin.push.huawei.core;

import com.fomin.push.huawei.callback.IClientConnectCallback;
import com.fomin.push.util.LogUtil;

/**
 * API 实现类基类，用于处理公共操作
 * 目前实现的是client的连接及回调
 * Created by Fomin on 2018/10/23.
 */
public abstract class BaseApiAgent implements IClientConnectCallback {
    protected void connect() {
        LogUtil.d("connect");
        ApiClientMgr.getInstance().connect(this);
    }
}
