package com.fomin.push.meizu;

import android.content.Context;
import android.text.TextUtils;

import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.meizu.cloud.pushsdk.PushManager;

/**
 * Created by Fomin on 2018/10/25.
 */
public class MeizuCenter extends AbsPushCenter {
    private Context context;
    private PushInfo pushInfo;

    public MeizuCenter(Context context) {
        this.context = context;
        this.pushInfo = PushUtil.getPushInfo(context, PhoneBrand.Meizu);
    }

    @Override
    public String name() {
        return PhoneBrand.Meizu.name();
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
        LogUtil.d("MeizuCenter, getAppId:[" + pushInfo.getAppId() + "], getAppKey:[" + pushInfo.getAppKey() + "]");
        PushManager.register(context, pushInfo.getAppId(), pushInfo.getAppKey());
    }

    @Override
    public void unregisterPush() {
        super.unregisterPush();
        PushManager.unRegister(context, pushInfo.getAppId(), pushInfo.getAppKey());
    }

    @Override
    public void setAlias(String alias) {
        super.setAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        PushManager.subScribeAlias(context, pushInfo.getAppId(), pushInfo.getAppKey(), PushManager.getPushId(context), alias);
    }

    @Override
    public void unsetAlias(String alias) {
        super.unsetAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        PushManager.unSubScribeAlias(context, pushInfo.getAppId(), pushInfo.getAppKey(), PushManager.getPushId(context), alias);
    }

    @Override
    public void subscribe(String topic) {
        super.subscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        PushManager.subScribeTags(context, pushInfo.getAppId(), pushInfo.getAppKey(), PushManager.getPushId(context), topic);
    }

    @Override
    public void unsubscribe(String topic) {
        super.unsubscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        PushManager.unSubScribeTags(context, pushInfo.getAppId(), pushInfo.getAppKey(), PushManager.getPushId(context), topic);
    }
}
