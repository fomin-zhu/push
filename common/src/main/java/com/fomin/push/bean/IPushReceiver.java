package com.fomin.push.bean;

import android.content.Context;

/**
 * Created by Fomin on 2018/10/18.
 */
public interface IPushReceiver {
    /**
     * 接收厂商消息通知
     * oppo和vivo是接收不了产商的消息通知
     *
     * @param context context
     * @param msg 消息内容
     */
    void onNotificationMessageArrived(Context context, PushMsg msg);

    /**
     * 收到消息通知，点击通知回调方法
     * oppo、xiaomi不会回调该方法
     * @param context context
     * @param msg 消息内容
     */
    void onNotificationMessageClicked(Context context, PushMsg msg);

    /**
     * 透传消息，暂不需要处理
     *
     * @param context context
     * @param msg 消息内容
     */
    void onReceivePassThroughMessage(Context context, PushMsg msg);

    /**
     * 注册成功回调，返回相关token
     *
     * @param context  context
     * @param msg 消息内容
     */
    void onReceiveToken(Context context, PushMsg msg);

    /**
     * 相关产商消息命令回调
     * huawei不会回调该方法
     * @param context context
     * @param command 消息命令内容
     */
    void onCommandResult(Context context, PushCommandMsg command);
}
