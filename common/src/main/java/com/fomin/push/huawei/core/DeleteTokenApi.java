package com.fomin.push.huawei.core;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.handler.DeleteTokenHandler;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.fomin.push.util.ThreadUtil;

/**
 * Created by Fomin on 2018/10/23.
 */
public class DeleteTokenApi extends BaseApiAgent {

    /**
     * 待删除的push token
     */
    private String token;

    /**
     * 调用接口回调
     */
    private DeleteTokenHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行删除TOKEN操作
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                //调用删除TOKEN需要传入通过getToken接口获取到TOKEN，并且需要对TOKEN进行非空判断
                if (!TextUtils.isEmpty(token)) {
                    if (client == null || !ApiClientMgr.getInstance().isConnect(client)) {
                        LogUtil.e("client not connted");
                        onDeleteTokenResult(rst);
                    } else {
                        try {
                            HuaweiPush.HuaweiPushApi.deleteToken(client, token);
                            onDeleteTokenResult(AgentResultCode.HMSAGENT_SUCCESS);
                        } catch (Exception e) {
                            LogUtil.e("删除TOKEN失败:" + e.getMessage());
                            onDeleteTokenResult(AgentResultCode.CALL_EXCEPTION);
                        }
                    }
                } else {
                    LogUtil.e("删除TOKEN失败: 要删除的token为空");
                    onDeleteTokenResult(AgentResultCode.EMPTY_PARAM);
                }
            }
        });
    }

    void onDeleteTokenResult(int rstCode) {
        LogUtil.i("deleteToken:callback=" + PushUtil.objDesc(handler) + " retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 删除指定的pushtoken
     * 该接口只在EMUI5.1以及更高版本的华为手机上调用该接口后才不会收到PUSH消息。
     *
     * @param token 要删除的token
     */
    public void deleteToken(String token, DeleteTokenHandler handler) {
        LogUtil.i("deleteToken:token:" + token + " handler=" + PushUtil.objDesc(handler));
        this.token = token;
        this.handler = handler;
        connect();
    }
}
