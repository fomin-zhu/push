package com.fomin.push.core;

import android.content.Context;

import com.fomin.push.bean.IPushAction;
import com.fomin.push.bean.PushCommandMsg;
import com.fomin.push.bean.PushMsg;

/**
 * Created by Fomin on 2018/10/18.
 */
public class DataRepeater {

    /**
     * COMMAND_RESULT
     *
     * @param context context
     * @param command 命令
     */
    public static void onCommandResult(Context context, PushCommandMsg command) {
        SendDataManager.sendPushData(context, IPushAction.RECEIVE_COMMAND_RESULT, command);
    }

    /**
     * MESSAGE
     *
     * @param context context
     * @param pushMsg 内容
     */
    public static void onReceivePassThroughMessage(Context context, PushMsg pushMsg) {
        SendDataManager.sendPushData(context, IPushAction.RECEIVE_MESSAGE, pushMsg);
    }

    /**
     * NOTIFICATION_CLICK
     *
     * @param context context
     * @param pushMsg 内容
     */
    public static void onNotificationMessageClicked(Context context, PushMsg pushMsg) {
        SendDataManager.sendPushData(context, IPushAction.RECEIVE_NOTIFICATION_CLICK, pushMsg);
    }

    /**
     * NOTIFICATION
     *
     * @param context context
     * @param pushMsg 内容
     */
    public static void onNotificationMessageArrived(Context context, PushMsg pushMsg) {
        SendDataManager.sendPushData(context, IPushAction.RECEIVE_NOTIFICATION, pushMsg);
    }

    /**
     * receive token
     *
     * @param context context
     * @param pushMsg pushMsg
     */
    public static void onReceiveToken(Context context, PushMsg pushMsg) {
        SendDataManager.sendPushData(context, IPushAction.RECEIVE_TOKEN, pushMsg);
    }
}
