package com.fomin.push.huawei.core;

import android.os.Handler;
import android.os.Looper;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.handler.EnableReceiveNotifyMsgHandler;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.fomin.push.util.ThreadUtil;

/**
 * 打开自呈现消息开关的接口。
 */
public class EnableReceiveNotifyMsgApi extends BaseApiAgent {

    /**
     * 是否打开开关
     */
    boolean enable;

    /**
     * 调用接口回调
     */
    private EnableReceiveNotifyMsgHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行开关的操作
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !ApiClientMgr.getInstance().isConnect(client)) {
                    LogUtil.e("client not connted");
                    onEnableReceiveNotifyMsgResult(rst);
                } else {
                    // 开启/关闭自呈现消息
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(client, enable);
                    onEnableReceiveNotifyMsgResult(AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onEnableReceiveNotifyMsgResult(int rstCode) {
        LogUtil.i("enableReceiveNotifyMsg:callback=" + PushUtil.objDesc(handler) + " retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 打开/关闭自呈现消息
     *
     * @param enable 打开/关闭
     */
    public void enableReceiveNotifyMsg(boolean enable, EnableReceiveNotifyMsgHandler handler) {
        LogUtil.i("enableReceiveNotifyMsg:enable=" + enable + " handler=" + PushUtil.objDesc(handler));
        this.enable = enable;
        this.handler = handler;
        connect();
    }
}
