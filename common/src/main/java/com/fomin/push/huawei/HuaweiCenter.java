package com.fomin.push.huawei;

import android.app.Application;
import android.text.TextUtils;

import com.huawei.hms.api.HuaweiApiClient;
import com.fomin.push.PushManager;
import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.callback.IClientConnectCallback;
import com.fomin.push.huawei.handler.EnableReceiveNormalMsgHandler;
import com.fomin.push.huawei.handler.EnableReceiveNotifyMsgHandler;
import com.fomin.push.huawei.handler.GetTokenHandler;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;

/**
 * Created by Fomin on 2018/10/18.
 */
public class HuaweiCenter extends AbsPushCenter {
    private PushInfo pushInfo;

    public HuaweiCenter(Application application) {
        this.pushInfo = new PushInfo();
        pushInfo.setAppId(PushUtil.getMetaData(application, "com.huawei.hms.client.appid"));
        LogUtil.d("HuaweiCenter getAppId:" + pushInfo.getAppId() + ", isEnabled():" + isEnabled());

        if (isEnabled()) {
            HMSAgent.init(application);
        }
    }

    @Override
    public String name() {
        return PhoneBrand.Huawei.name();
    }

    @Override
    public boolean isEnabled() {
        return pushInfo != null
                && !TextUtils.isEmpty(pushInfo.getAppId());
    }

    @Override
    public void registerPush() {
        super.registerPush();
        HMSAgent.connect(new IClientConnectCallback() {
            @Override
            public void onConnect(int rst, HuaweiApiClient client) {
                //华为移动服务client连接成功，在这边处理业务自己的事件
                LogUtil.d("HMS connect success!");
                HMSAgent.Push.getToken(new GetTokenHandler() {
                    @Override
                    public void onResult(int rst) {
                        LogUtil.d("get token: end code=" + rst);
                        if (rst != AgentResultCode.HMSAGENT_SUCCESS) {
                            PushManager.getInstance().resetInit();
                        }
                    }
                });
                HMSAgent.Push.enableReceiveNotifyMsg(true, new EnableReceiveNotifyMsgHandler() {
                    @Override
                    public void onResult(int rst) {
                        LogUtil.d("enableReceiveNotifyMsg:end code=" + rst);
                    }
                });
                HMSAgent.Push.enableReceiveNormalMsg(true, new EnableReceiveNormalMsgHandler() {
                    @Override
                    public void onResult(int rst) {
                        LogUtil.d("enableReceiveNormalMsg:end code=" + rst);
                    }
                });

            }
        });
    }

    @Override
    public void unregisterPush() {
        super.unregisterPush();
        HMSAgent.destroy();
    }
}
