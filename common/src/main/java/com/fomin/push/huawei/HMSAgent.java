package com.fomin.push.huawei;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.huawei.hms.api.HuaweiApiAvailability;
import com.fomin.push.huawei.callback.IClientConnectCallback;
import com.fomin.push.huawei.core.ActivityMgr;
import com.fomin.push.huawei.core.ApiClientMgr;
import com.fomin.push.huawei.core.DeleteTokenApi;
import com.fomin.push.huawei.core.EnableReceiveNormalMsgApi;
import com.fomin.push.huawei.core.EnableReceiveNotifyMsgApi;
import com.fomin.push.huawei.core.GetPushStateApi;
import com.fomin.push.huawei.core.GetTokenApi;
import com.fomin.push.huawei.handler.DeleteTokenHandler;
import com.fomin.push.huawei.handler.EnableReceiveNormalMsgHandler;
import com.fomin.push.huawei.handler.EnableReceiveNotifyMsgHandler;
import com.fomin.push.huawei.handler.GetPushStateHandler;
import com.fomin.push.huawei.handler.GetTokenHandler;
import com.fomin.push.util.LogUtil;

/**
 * Created by Fomin on 2018/10/23.
 */
public final class HMSAgent {

    private static final String VER_020601002 = "020601002";
    /**
     * 当前版本号 | Current version number
     */
    public static final String CURVER = VER_020601002;

    private HMSAgent() {
    }

    /**
     * 初始化方法，建议在Application onCreate里面调用    | Initialization method, it is recommended to call in creator OnCreate
     *
     * @param app 应用程序                              | Application
     * @return true：成功 false：失败                   | True: Success false: Failed
     */
    public static boolean init(Application app) {
        LogUtil.i("init HMSAgent ");
        if (app == null) {
            LogUtil.e("the param of method HMSAgent.init app can not be null !!!");
            return false;
        }
        LogUtil.i("init HMSAgent " + CURVER + " with hmssdkver " + HuaweiApiAvailability.HMS_SDK_VERSION_CODE);
        // 检查HMSAgent 和 HMSSDK 版本匹配关系 | Check hmsagent and HMSSDK version matching relationships
        if (!checkSDKVersion(app)) {
            return false;
        }
        // 初始化activity管理类 | Initializing Activity Management Classes
        ActivityMgr.getInstance().init(app);
        // 初始化HuaweiApiClient管理类 | Initialize Huaweiapiclient Management class
        ApiClientMgr.getInstance().init(app);
        return true;
    }

    private static boolean checkSDKVersion(Context context) {
        long sdkMainVerL = HuaweiApiAvailability.HMS_SDK_VERSION_CODE / 1000;
        long agentMainVerL = Long.parseLong(CURVER) / 1000;
        if (sdkMainVerL != agentMainVerL) {
            String errMsg = "error: HMSAgent major version code (" + agentMainVerL + ") does not match HMSSDK major version code (" + sdkMainVerL + ")";
            LogUtil.e(errMsg);
            Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 释放资源，这里一般不需要调用 | Frees resources, which are generally not required to call
     */
    public static void destroy() {
        LogUtil.i("destroy HMSAgent");
        ActivityMgr.getInstance().release();
        ApiClientMgr.getInstance().release();
    }

    public static void connect(final IClientConnectCallback callback) {
        LogUtil.i("start connect");
        ApiClientMgr.getInstance().connect(callback);
    }

    /**
     * push接口封装 | Push interface Encapsulation
     */
    public static final class Push {
        /**
         * 获取pushtoken接口 | Get Pushtoken method
         * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册 | Pushtoken Broadcast issued, to listen to the broadcast, see HMS-SDK Development Preparation Pushreceiver Registration
         *
         * @param handler pushtoken接口调用回调（结果会在主线程回调） | getToken method Call callback (result will be callback in main thread)
         */
        public static void getToken(GetTokenHandler handler) {
            new GetTokenApi().getToken(handler);
        }

        /**
         * 删除指定的pushtoken | Deletes the specified Pushtoken
         * 该接口只在EMUI5.1以及更高版本的华为手机上调用该接口后才不会收到PUSH消息。 | The method will not receive a push message until it is invoked on EMUI5.1 and later Huawei handsets.
         *
         * @param token   要删除的token | Token to delete
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void deleteToken(String token, DeleteTokenHandler handler) {
            new DeleteTokenApi().deleteToken(token, handler);
        }

        /**
         * 获取push状态，push状态的回调通过广播发送。 | Gets the push state, and the push state callback is sent by broadcast.
         * 要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册 | To listen for broadcasts, see Pushreceiver Registration in HMS-SDK development preparation
         *
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void getPushState(GetPushStateHandler handler) {
            new GetPushStateApi().getPushState(handler);
        }

        /**
         * 打开/关闭通知栏消息 | Turn on/off notification bar messages
         *
         * @param enable  打开/关闭 | Turn ON/off
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNotifyMsg(boolean enable, EnableReceiveNotifyMsgHandler handler) {
            new EnableReceiveNotifyMsgApi().enableReceiveNotifyMsg(enable, handler);
        }

        /**
         * 打开/关闭透传消息 | Turn on/off the pass message
         *
         * @param enable  打开/关闭 | Turn ON/off
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNormalMsg(boolean enable, EnableReceiveNormalMsgHandler handler) {
            new EnableReceiveNormalMsgApi().enableReceiveNormalMsg(enable, handler);
        }
    }
}
