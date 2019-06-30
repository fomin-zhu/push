package com.fomin.push.huawei.core;

import android.os.Handler;
import android.os.Looper;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.handler.GetPushStateHandler;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.fomin.push.util.ThreadUtil;

/**
 * 获取push状态的接口。
 */
public class GetPushStateApi extends BaseApiAgent {

    /**
     * 调用接口回调
     */
    private GetPushStateHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行获取push状态的操作
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !ApiClientMgr.getInstance().isConnect(client)) {
                    LogUtil.e("client not connted");
                    onGetPushStateResult(rst);
                } else {
                    HuaweiPush.HuaweiPushApi.getPushState(client);
                    onGetPushStateResult(AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onGetPushStateResult(int rstCode) {
        LogUtil.i("getPushState:callback=" + PushUtil.objDesc(handler) + " retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 获取push状态，push状态的回调通过广播发送。
     * 要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     */
    public void getPushState(GetPushStateHandler handler) {
        LogUtil.i("getPushState:handler=" + PushUtil.objDesc(handler));
        this.handler = handler;
        connect();
    }
}
