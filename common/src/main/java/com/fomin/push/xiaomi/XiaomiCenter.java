package com.fomin.push.xiaomi;

import android.content.Context;
import android.text.TextUtils;

import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by Fomin on 2018/10/18.
 */
public class XiaomiCenter extends AbsPushCenter {
    private Context context;
    private PushInfo pushInfo;


    public XiaomiCenter(Context context) {
        this.context = context;
        this.pushInfo = PushUtil.getPushInfo(context, PhoneBrand.Xiaomi);
    }

    @Override
    public String name() {
        return PhoneBrand.Xiaomi.name();
    }

    @Override
    public boolean isEnabled() {
        return pushInfo != null
                && !TextUtils.isEmpty(pushInfo.getAppId())
                && !TextUtils.isEmpty(pushInfo.getAppKey());
    }

    @Override
    public void registerPush() {
        super.registerPush();
        MiPushClient.registerPush(context, pushInfo.getAppId(), pushInfo.getAppKey());

        Logger.setLogger(context, new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content) {
                LogUtil.d(content);
            }

            @Override
            public void log(String content, Throwable t) {
                LogUtil.e(content, t);
            }
        });
    }

    @Override
    public void unregisterPush() {
        super.unregisterPush();
        MiPushClient.unregisterPush(context);
    }

    @Override
    public void setAlias(String alias) {
        super.setAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        MiPushClient.setAlias(context, alias, null);
    }

    @Override
    public void unsetAlias(String alias) {
        super.unsetAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        MiPushClient.unsetAlias(context, alias, null);
    }

    @Override
    public void setUserAccount(String userAccount) {
        super.setUserAccount(userAccount);
        if (PushUtil.isEmpty(userAccount)) return;
        MiPushClient.setUserAccount(context, userAccount, null);
    }

    @Override
    public void unsetUserAccount(String userAccount) {
        super.unsetUserAccount(userAccount);
        if (PushUtil.isEmpty(userAccount)) return;
        MiPushClient.unsetUserAccount(context, userAccount, null);
    }

    @Override
    public void unsetAllUserAccount() {
        super.unsetAllUserAccount();
        //清空所有旧的user account
        List<String> historyList = MiPushClient.getAllUserAccount(context);
        if (historyList != null && !historyList.isEmpty()) {
            for (String old : historyList) {
                MiPushClient.unsetUserAccount(context, old, null);
            }
        }
    }

    @Override
    public void subscribe(String topic) {
        super.subscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        MiPushClient.subscribe(context, topic, null);
    }

    @Override
    public void unsubscribe(String topic) {
        super.unsubscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        MiPushClient.unsubscribe(context, topic, null);
    }
}
