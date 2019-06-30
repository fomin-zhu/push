package com.fomin.push.huawei.core;

import android.os.Handler;
import android.os.Looper;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.handler.GetTokenHandler;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;

/**
 * Created by Fomin on 2018/10/23.
 */
public class GetTokenApi extends BaseApiAgent {

    /**
     * client 无效最大尝试次数
     */
    private static final int MAX_RETRY_TIMES = 1;

    /**
     * 结果回调
     */
    private GetTokenHandler handler;

    /**
     * 当前剩余重试次数
     */
    private int retryTimes = MAX_RETRY_TIMES;

    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
        if (client == null || !ApiClientMgr.getInstance().isConnect(client)) {
            LogUtil.e("client not connted");
            onPushTokenResult(rst);
            return;
        }

        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                if (result == null) {
                    LogUtil.e("result is null");
                    onPushTokenResult(AgentResultCode.RESULT_IS_NULL);
                    return;
                }

                Status status = result.getStatus();
                if (status == null) {
                    LogUtil.e("status is null");
                    onPushTokenResult(AgentResultCode.STATUS_IS_NULL);
                    return;
                }

                int rstCode = status.getStatusCode();
                LogUtil.d("status=" + status);
                // 需要重试的错误码，并且可以重试
                if ((rstCode == CommonCode.ErrorCode.SESSION_INVALID
                        || rstCode == CommonCode.ErrorCode.CLIENT_API_INVALID) && retryTimes > 0) {
                    retryTimes--;
                    connect();
                } else {
                    onPushTokenResult(rstCode);
                }
            }
        });
    }

    /**
     * 获取pushtoken接口调用回调
     * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     *
     * @param rstCode 结果码
     */
    void onPushTokenResult(int rstCode) {
        LogUtil.i("getToken:callback=" + PushUtil.objDesc(handler) + " retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
        retryTimes = MAX_RETRY_TIMES;
    }

    /**
     * 获取pushtoken接口
     * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     *
     * @param handler pushtoken接口调用回调
     */
    public void getToken(GetTokenHandler handler) {
        LogUtil.i("getToken:handler=" + PushUtil.objDesc(handler));
        this.handler = handler;
        retryTimes = MAX_RETRY_TIMES;
        connect();
    }
}
