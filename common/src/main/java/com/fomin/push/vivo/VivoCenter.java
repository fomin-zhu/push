package com.fomin.push.vivo;

import android.content.Context;
import android.text.TextUtils;

import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

/**
 * Created by Fomin on 2018/10/24.
 */
public class VivoCenter extends AbsPushCenter {

    private Context context;
    private PushInfo pushInfo;

    public VivoCenter(Context context) {
        this.context = context;
        this.pushInfo = new PushInfo();
        pushInfo.setAppId(PushUtil.getMetaData(context, "com.vivo.push.app_id"));
        pushInfo.setAppKey(PushUtil.getMetaData(context, "com.vivo.push.api_key"));
        if (isEnabled()) {
            PushClient.getInstance(context).initialize();
        }
    }

    @Override
    public String name() {
        return PhoneBrand.Vivo.name();
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
        PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                LogUtil.d("vivo push turn on " + i);
            }
        });
    }

    @Override
    public void unregisterPush() {
        super.unregisterPush();
    }
}
