package com.fomin.push;

import android.app.Application;

import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.huawei.HuaweiCenter;
import com.fomin.push.meizu.MeizuCenter;
import com.fomin.push.oppo.OppoCenter;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.fomin.push.vivo.VivoCenter;
import com.fomin.push.xiaomi.XiaomiCenter;

/**
 * Created by Fomin on 2018/11/5.
 */
public class PushManager {

    private Application application;
    private AbsPushCenter pushCenter;

    private PushManager() {
    }

    public static PushManager getInstance() {
        return PushManagerInstance.INSTANCE;
    }

    private static class PushManagerInstance {
        private static final PushManager INSTANCE = new PushManager();
    }

    public void init(Application application) {
        this.application = application;
        try {
            create(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }

    public boolean isInit() {
        if (pushCenter == null) {
            LogUtil.e("pushCenter not init");
            return false;
        }
        return true;
    }

    public AbsPushCenter getPushCenter() {
        return pushCenter;
    }

    private void create(boolean isDefault) {
        if (application == null) {
            throw new AssertionError("未初始化Application");
        }
        createPushCenter(isDefault);
        LogUtil.d("Factory init " + pushCenter.name());
        pushCenter.registerPush();
    }

    private void createPushCenter(boolean isDefault) {
        if (isDefault) {
            pushCenter = defPushCenter();
        } else {
            switch (PushUtil.getPhoneBrand()) {
                case Oppo:
                    pushCenter = new OppoCenter(application.getApplicationContext());
                    break;
                case Meizu:
                    pushCenter = new MeizuCenter(application.getApplicationContext());
                    break;
                case Vivo:
                    pushCenter = new VivoCenter(application.getApplicationContext());
                    break;
                case Honor:
                case Huawei:
                    pushCenter = new HuaweiCenter(application);
                    break;
                default:
                    pushCenter = defPushCenter();
                    break;
            }
        }

        if (!pushCenter.isEnabled()) {
            LogUtil.d("Factory selected pushCenter " + pushCenter.name() + " is disable, switch to default center");
            pushCenter = defPushCenter();
        }
    }

    private AbsPushCenter defPushCenter() {
        AbsPushCenter center = new XiaomiCenter(application.getApplicationContext());
        if (!center.isEnabled()) {
            throw new AssertionError("defPushCenter 初始化失败，请检查默认推送渠道的配置(小米)");
        }
        return center;
    }

    public void resetInit() {
        if (pushCenter != null) pushCenter.unregisterPush();
        if (application == null) {
            LogUtil.e("resetInit error,application is null");
            return;
        }
        try {
            create(true);
            pushCenter.resetEvent();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }
}
