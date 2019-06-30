package com.fomin.push.oppo;

import android.content.Context;
import android.text.TextUtils;

import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.fomin.push.bean.AbsPushCenter;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Fomin on 2018/10/25.
 */
public class OppoCenter extends AbsPushCenter {

    private Context context;
    private PushInfo pushInfo;

    public OppoCenter(Context context) {
        this.context = context;
        this.pushInfo = PushUtil.getPushInfo(context, PhoneBrand.Oppo);
        LogUtil.d("OppoCenter getAppId:" + pushInfo.getAppId() + ", getAppKey():" + pushInfo.getAppKey() + ", isEnabled():" + isEnabled());
    }

    @Override
    public String name() {
        return PhoneBrand.Oppo.name();
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
        PushManager.getInstance().register(context, pushInfo.getAppId(), pushInfo.getAppKey(), new PushCallback() {

            @Override
            public void onSetUserAccounts(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("设置账号成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("设置账号失败 code=" + code);
                }
            }

            @Override
            public void onUnsetUserAccounts(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("取消账号成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("取消账号失败 code=" + code);
                }
            }

            @Override
            public void onGetUserAccounts(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("获取账号成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("获取账号失败 code=" + code);
                }
            }

            @Override
            public void onRegister(int code, String s) {
                if (code == 0) {
                    LogUtil.d("注册成功 registerId:" + s);
                } else {
                    LogUtil.d("注册失败 code=" + code + ",msg=" + s);
                }
            }

            @Override
            public void onUnRegister(int code) {
                if (code == 0) {
                    LogUtil.d("注销成功 code=" + code);
                } else {
                    LogUtil.d("注销失败 code=" + code);
                }
            }

            @Override
            public void onGetAliases(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("获取别名成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("获取别名失败 code=" + code);
                }
            }

            @Override
            public void onSetAliases(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("设置别名成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("设置别名失败 code=" + code);
                }
            }

            @Override
            public void onUnsetAliases(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("取消别名成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("取消别名失败 code=" + code);
                }
            }

            @Override
            public void onSetTags(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("设置标签成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("设置标签失败 code=" + code);
                }
            }

            @Override
            public void onUnsetTags(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("取消标签成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("取消标签失败 code=" + code);
                }
            }

            @Override
            public void onGetTags(int code, List<SubscribeResult> list) {
                if (code == 0) {
                    LogUtil.d("获取标签成功 code=" + code + ",msg=" + Arrays.toString(list.toArray()));
                } else {
                    LogUtil.d("获取标签失败 code=" + code);
                }
            }


            @Override
            public void onGetPushStatus(final int code, int status) {
                if (code == 0 && status == 0) {
                    LogUtil.d("Push状态正常 code=" + code + ",status=" + status);
                } else {
                    LogUtil.d("Push状态错误 code=" + code + ",status=" + status);
                }
            }

            @Override
            public void onGetNotificationStatus(final int code, final int status) {
                if (code == 0 && status == 0) {
                    LogUtil.d("通知状态正常 code=" + code + ",status=" + status);
                } else {
                    LogUtil.d("通知状态错误 code=" + code + ",status=" + status);
                }
            }

            @Override
            public void onSetPushTime(final int code, final String s) {
                LogUtil.d("SetPushTime code=" + code + ",result:" + s);
            }

        });
    }

    @Override
    public void unregisterPush() {
        super.unregisterPush();
        PushManager.getInstance().unRegister();
    }

    @Override
    public void setAlias(String alias) {
        super.setAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        List<String> list = new ArrayList<>();
        list.add(alias);
        PushManager.getInstance().setAliases(list);
    }

    @Override
    public void unsetAlias(String alias) {
        super.unsetAlias(alias);
        if (PushUtil.isEmpty(alias)) return;
        PushManager.getInstance().unsetAlias(alias);
    }

    @Override
    public void setUserAccount(String userAccount) {
        super.setUserAccount(userAccount);
        if (PushUtil.isEmpty(userAccount)) return;
        PushManager.getInstance().setUserAccount(userAccount);
    }

    @Override
    public void unsetUserAccount(String userAccount) {
        super.unsetUserAccount(userAccount);
        if (PushUtil.isEmpty(userAccount)) return;
        List<String> list = new ArrayList<>();
        list.add(userAccount);
        PushManager.getInstance().unsetUserAccounts(list);
    }

    @Override
    public void subscribe(String topic) {
        super.subscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        List<String> list = new ArrayList<>();
        list.add(topic);
        PushManager.getInstance().setTags(list);
    }

    @Override
    public void unsubscribe(String topic) {
        super.unsubscribe(topic);
        if (PushUtil.isEmpty(topic)) return;
        List<String> list = new ArrayList<>();
        list.add(topic);
        PushManager.getInstance().unsetTags(list);
    }
}
