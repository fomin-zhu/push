package com.fomin.push.xiaomi;

import android.content.Context;

import com.fomin.push.bean.IPushCode;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushCommandMsg;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.core.DataRepeater;
import com.fomin.push.util.LogUtil;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by Fomin on 2018/10/18.
 */
public class XiaomiPushReceiver extends PushMessageReceiver {


    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        LogUtil.d("onReceivePassThroughMessage()");
        PushMsg pushMsg = new PushMsg.Builder()
                .setContent(miPushMessage.getContent())
                .setExtraMsg(miPushMessage.getDescription())
                .setBrand(PhoneBrand.Xiaomi)
                .setKey(miPushMessage.getExtra())
                .build();
        DataRepeater.onReceivePassThroughMessage(context, pushMsg);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        LogUtil.d("onNotificationMessageArrived() message=" + miPushMessage);
        PushMsg pushMsg = new PushMsg.Builder()
                .setNotifyId(miPushMessage.getNotifyId())
                .setTitle(miPushMessage.getTitle())
                .setContent(miPushMessage.getContent())
                .setExtraMsg(miPushMessage.getDescription())
                .setBrand(PhoneBrand.Xiaomi)
                .setKey(miPushMessage.getExtra())
                .build();
        DataRepeater.onNotificationMessageArrived(context, pushMsg);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
        LogUtil.d("onNotificationMessageClicked() message=" + miPushMessage);
        PushMsg pushMsg = new PushMsg.Builder()
                .setNotifyId(miPushMessage.getNotifyId())
                .setTitle(miPushMessage.getTitle())
                .setContent(miPushMessage.getContent())
                .setExtraMsg(miPushMessage.getDescription())
                .setBrand(PhoneBrand.Xiaomi)
                .setKey(miPushMessage.getExtra())
                .build();
        DataRepeater.onNotificationMessageClicked(context, pushMsg);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        LogUtil.d("onReceiveRegisterResult() message=" + miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command) && miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
            PushMsg pushMsg = new PushMsg.Builder()
                    .setContent(cmdArg1)
                    .setBrand(PhoneBrand.Xiaomi)
                    .build();
            DataRepeater.onReceiveToken(context, pushMsg);
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        LogUtil.d("onCommandResult() message=" + miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        long code = miPushCommandMessage.getResultCode();
        String log = "";
        String type = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            type = IPushCode.TYPE_REGISTER;
            if (code == ErrorCode.SUCCESS) {
                log = "register success:" + cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            type = IPushCode.TYPE_SET_ALIAS;
            if (code == ErrorCode.SUCCESS) {
                log = "set alias success:" + cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            type = IPushCode.TYPE_UNSET_ALIAS;
            if (code == ErrorCode.SUCCESS) {
                log = "unset alias success:" + cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            type = IPushCode.TYPE_SUBSCRIBE_TOPIC;
            if (code == ErrorCode.SUCCESS) {
                log = "sub topic success:" + cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            type = IPushCode.TYPE_UNSUBSCRIBE_TOPIC;
            if (code == ErrorCode.SUCCESS) {
                log = "unsub topic success:" + cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            type = IPushCode.TYPE_ACCEPT_TIME;
            if (code == ErrorCode.SUCCESS) {
                log = "accept time:" + cmdArg1 + " to " + cmdArg2;
            }
        }
        if (!type.isEmpty()) {
            PushCommandMsg pushMsg = new PushCommandMsg.Builder()
                    .setCommand(type)
                    .setResultCode(code == ErrorCode.SUCCESS ? IPushCode.SUCCESS : IPushCode.ERROR)
                    .setExtraMsg(cmdArg1)
                    .setReason(miPushCommandMessage.getReason())
                    .setBrand(PhoneBrand.Xiaomi)
                    .build();
            DataRepeater.onCommandResult(context, pushMsg);
        }
        LogUtil.d(log);
    }
}
