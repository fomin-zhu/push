package com.fomin.push.huawei.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.fomin.push.huawei.bean.AgentResultCode;
import com.fomin.push.huawei.callback.EmptyConnectCallback;
import com.fomin.push.huawei.callback.IActivityDestroyedCallback;
import com.fomin.push.huawei.callback.IActivityPauseCallback;
import com.fomin.push.huawei.callback.IActivityResumeCallback;
import com.fomin.push.huawei.callback.IClientConnectCallback;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Huawei Api Client 管理类 | Huawei API Client Management class
 * 负责HuaweiApiClient的连接，异常处理等 | Responsible for huaweiapiclient connection, exception handling, etc.
 * Created by Fomin on 2018/10/23.
 */
public final class ApiClientMgr implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener, IActivityResumeCallback, IActivityPauseCallback, IActivityDestroyedCallback {

    private Context context;
    /**
     * client操作锁，避免连接使用紊乱 | Client operation lock, avoid connection use disorder
     */
    private static final Object APICLIENT_LOCK = new Object();
    /**
     * 回调锁，避免连接回调紊乱 | Callback lock to avoid connection callback disorder
     */
    private static final Object CALLBACK_LOCK = new Object();
    /**
     * HuaweiApiClient 实例 | Huaweiapiclient instance
     */
    private HuaweiApiClient apiClient;
    /**
     * api client 连接超时 | API Client Connection Timeout
     */
    private static final int APICLIENT_CONNECT_TIMEOUT = 30000;
    /**
     * client 连接超时消息 | Client Connection Timeout Message
     */
    private static final int APICLIENT_TIMEOUT_HANDLE_MSG = 3;
    /**
     * client 拉起activity超时消息 | Client starts Activity Timeout message
     */
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG = 4;

    /**
     * 连接回调 | Connection callback
     */
    private List<IClientConnectCallback> connCallbacks = new ArrayList<IClientConnectCallback>();


    private static class SingletonInstance {
        private static final ApiClientMgr INSTANCE = new ApiClientMgr();
    }

    public static ApiClientMgr getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 私有构造方法 | Private construction methods
     */
    private ApiClientMgr() {
    }

    /**
     * 超时handler用来处理client connect 超时
     * Timeout handler to handle client connect timeout
     */
    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            boolean hasConnCallbacks;
            synchronized (CALLBACK_LOCK) {
                hasConnCallbacks = !connCallbacks.isEmpty();
            }
            if (msg != null && msg.what == APICLIENT_TIMEOUT_HANDLE_MSG && hasConnCallbacks) {
                LogUtil.d("connect time out");
                resetApiClient();
                onConnectEnd(AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            } else if (msg != null && msg.what == APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG && hasConnCallbacks) {
                LogUtil.d("start activity time out");
                onConnectEnd(AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            }
            return false;
        }
    });

    /**
     * 初始化
     *
     * @param app 应用程序
     */
    public void init(Application app) {
        // 保存应用程序context
        context = app.getApplicationContext();

        // 注册activity onResume回调
        ActivityMgr.getInstance().unRegisterActivityResumeEvent(this);
        ActivityMgr.getInstance().registerActivityResumeEvent(this);

        // 注册activity onPause回调
        ActivityMgr.getInstance().unRegisterActivityPauseEvent(this);
        ActivityMgr.getInstance().registerActivityPauseEvent(this);

        // 注册activity onDestroyed 回调
        ActivityMgr.getInstance().unRegisterActivityDestroyedEvent(this);
        ActivityMgr.getInstance().registerActivityDestroyedEvent(this);
    }

    /**
     * 断开apiclient，一般不需要调用
     */
    public void release() {
        LogUtil.d("release");

        HuaweiApiClient client = getApiClient();
        if (client != null) {
            client.disconnect();
        }

        synchronized (APICLIENT_LOCK) {
            apiClient = null;
        }

        synchronized (CALLBACK_LOCK) {
            connCallbacks.clear();
        }
    }

    /**
     * 获取当前的 HuaweiApiClient
     *
     * @return HuaweiApiClient 实例
     */
    public HuaweiApiClient getApiClient() {
        synchronized (APICLIENT_LOCK) {
            return apiClient != null ? apiClient : resetApiClient();
        }
    }

    /**
     * 重新创建apiclient
     * 2种情况需要重新创建：1、首次 2、client的状态已经紊乱
     *
     * @return 新创建的client
     */
    private HuaweiApiClient resetApiClient() {
        if (context == null) {
            LogUtil.e("HMSAgent not init");
            return null;
        }
        synchronized (APICLIENT_LOCK) {
            if (apiClient != null) {
                // 对于老的apiClient，1分钟后才丢弃，防止外面正在使用过程中这边disConnect了
                disConnectClientDelay(apiClient, 60000);
            }
            LogUtil.d("reset client");

            // 这种重置client，极端情况可能会出现2个client都回调结果的情况。此时可能出现rstCode=0，但是client无效。
            // 因为业务调用封装中都进行了一次重试。所以不会有问题
            apiClient = new HuaweiApiClient.Builder(context)
                    .addApi(HuaweiPush.PUSH_API)
                    .addConnectionCallbacks(getInstance())
                    .addOnConnectionFailedListener(getInstance())
                    .build();
            return apiClient;
        }
    }

    private static void disConnectClientDelay(final HuaweiApiClient clientTmp, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clientTmp.disconnect();
            }
        }, delay);
    }

    /**
     * 判断client是否已经连接
     *
     * @param client 要检测的client
     * @return 是否已经连接
     */
    public boolean isConnect(HuaweiApiClient client) {
        return client != null && client.isConnected();
    }


    @Override
    public void onConnected() {
        LogUtil.d("connect success");
        timeoutHandler.removeMessages(APICLIENT_TIMEOUT_HANDLE_MSG);
        onConnectEnd(ConnectionResult.SUCCESS);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        LogUtil.d("connect suspended");
        connect(new EmptyConnectCallback("onConnectionSuspended try end:"));
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        timeoutHandler.removeMessages(APICLIENT_TIMEOUT_HANDLE_MSG);

        if (result == null) {
            LogUtil.d("result is null");
            onConnectEnd(AgentResultCode.RESULT_IS_NULL);
            return;
        }

        int errCode = result.getErrorCode();
        LogUtil.d("errCode=" + errCode);
        onConnectEnd(errCode);
    }

    /**
     * 连接 HuaweiApiClient,
     *
     * @param callback 连接结果回调，一定不能为null,在子线程进行回调
     */
    public void connect(IClientConnectCallback callback) {
        if (context == null) {
            asyncCallback(AgentResultCode.HMSAGENT_NO_INIT, callback);
            return;
        }
        HuaweiApiClient client = getApiClient();
        // client 有效，则直接回调
        if (client != null && client.isConnected()) {
            LogUtil.d("client is valid");
            asyncCallback(AgentResultCode.HMSAGENT_SUCCESS, callback);
            return;
        } else {
            // client无效，将callback加入队列，并启动连接
            synchronized (CALLBACK_LOCK) {
                LogUtil.d("client is invalid：size=" + connCallbacks.size());
                if (connCallbacks.isEmpty()) {
                    connCallbacks.add(callback);
                    startConnect();
                } else {
                    connCallbacks.add(callback);
                }
            }
        }
    }

    /**
     * 线程中进行Huawei Api Client 的连接
     */
    private void startConnect() {
        LogUtil.d("start thread to connect");
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HuaweiApiClient client = getApiClient();

                if (client != null) {
                    LogUtil.d("connect");
                    Activity curActivity = ActivityMgr.getInstance().getLastActivity();

                    // 考虑到有cp后台需要调用接口，HMSSDK去掉了activity不能为空的判断。这里只是取当前activity，可能为空
                    timeoutHandler.sendEmptyMessageDelayed(APICLIENT_TIMEOUT_HANDLE_MSG, APICLIENT_CONNECT_TIMEOUT);
                    client.connect(curActivity);
                } else {
                    LogUtil.d("client is generate error");
                    onConnectEnd(AgentResultCode.RESULT_IS_NULL);
                }
            }
        });
    }


    /**
     * Huawei Api Client 连接结束方法
     *
     * @param rstCode client 连接结果码
     */
    private void onConnectEnd(final int rstCode) {
        LogUtil.d("connect end:" + rstCode);

        synchronized (CALLBACK_LOCK) {
            // 回调各个回调接口连接结束
            for (IClientConnectCallback callback : connCallbacks) {
                asyncCallback(rstCode, callback);
            }
            connCallbacks.clear();
        }
    }

    /**
     * 起线程回调各个接口，避免其中一个回调者耗时长影响其他调用者
     *
     * @param rstCode  结果码
     * @param callback 回调
     */
    private void asyncCallback(final int rstCode, final IClientConnectCallback callback) {
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HuaweiApiClient client = getApiClient();
                LogUtil.d("callback connect: rst=" + rstCode + " apiClient=" + client);
                if (client != null) {
                    callback.onConnect(rstCode, client);
                }
            }
        });
    }

    @Override
    public void onActivityDestroyed(Activity activityDestroyed, Activity activityNxt) {
        if (activityNxt == null) {
            // 所有activity销毁后，重置client，否则公告的标志位还在，下次弹不出来
            resetApiClient();
        }
    }

    @Override
    public void onActivityPause(Activity activity) {
        // 通知hmssdk，activity onPause了
        HuaweiApiClient client = getApiClient();
        if (client != null) {
            client.onPause(activity);
        }
    }

    @Override
    public void onActivityResume(Activity activity) {
        // 通知hmssdk activity onResume了
        HuaweiApiClient client = getApiClient();
        if (client != null) {
            LogUtil.d("tell hmssdk: onResume");
            client.onResume(activity);
        }
    }
}
