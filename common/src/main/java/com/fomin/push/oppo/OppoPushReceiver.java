package com.fomin.push.oppo;

import android.content.Context;

import com.coloros.mcssdk.PushService;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.CommandMessage;
import com.coloros.mcssdk.mode.ErrorCode;
import com.fomin.push.bean.IPushCode;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushCommandMsg;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.core.DataRepeater;
import com.fomin.push.util.LogUtil;

/**
 * Created by Fomin on 2018/10/25.
 */
public class OppoPushReceiver extends PushService {

    /**
     * 命令消息，主要是服务端对客户端调用的反馈，一般应用不需要重写此方法
     *
     * @param context
     * @param commandMessage
     */
    @Override
    public void processMessage(Context context, CommandMessage commandMessage) {
        super.processMessage(context, commandMessage);
        LogUtil.d("processMessage command=" + commandMessage);
        int command = commandMessage.getCommand();
        long code = commandMessage.getResponseCode();
        String log = "";
        String type = "";
        if (CommandMessage.COMMAND_REGISTER == command) {
            type = IPushCode.TYPE_REGISTER;
            if (code == ErrorCode.SUCCESS) {
                log = "register success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_SET_ALIAS == command) {
            type = IPushCode.TYPE_SET_ALIAS;
            if (code == ErrorCode.SUCCESS) {
                log = "set alias success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_UNSET_ALIAS == command) {
            type = IPushCode.TYPE_UNSET_ALIAS;
            if (code == ErrorCode.SUCCESS) {
                log = "unset alias success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_SET_TAGS == command) {
            type = IPushCode.TYPE_SUBSCRIBE_TOPIC;
            if (code == ErrorCode.SUCCESS) {
                log = "sub topic success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_UNSET_TAGS == command) {
            type = IPushCode.TYPE_UNSUBSCRIBE_TOPIC;
            if (code == ErrorCode.SUCCESS) {
                log = "unsub topic success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_SET_ACCOUNTS == command) {
            type = IPushCode.TYPE_SET_ACCOUNT;
            if (code == ErrorCode.SUCCESS) {
                log = "set account success:" + commandMessage.getContent();
            }
        } else if (CommandMessage.COMMAND_UNSET_ACCOUNTS == command) {
            type = IPushCode.TYPE_UNSET_ACCOUNT;
            if (code == ErrorCode.SUCCESS) {
                log = "unset account success:" + commandMessage.getContent();
            }
        }
        if (!type.isEmpty()) {
            if (CommandMessage.COMMAND_REGISTER == command) {
                PushMsg pushMsg = new PushMsg.Builder()
                        .setBrand(PhoneBrand.Oppo)
                        .setContent(commandMessage.getContent())
                        .build();
                DataRepeater.onReceiveToken(context, pushMsg);
            } else {
                PushCommandMsg pushMsg = new PushCommandMsg.Builder()
                        .setCommand(type)
                        .setResultCode(code == ErrorCode.SUCCESS ? IPushCode.SUCCESS : IPushCode.ERROR)
                        .setBrand(PhoneBrand.Oppo)
                        .setReason(commandMessage.getContent())
                        .setExtraMsg(commandMessage.getParams())
                        .build();
                DataRepeater.onCommandResult(context, pushMsg);
            }
        }
        LogUtil.d(log);
    }

    /**
     * 普通应用消息，视情况看是否需要重写
     *
     * @param context
     * @param appMessage
     */
    @Override
    public void processMessage(Context context, AppMessage appMessage) {
        super.processMessage(context, appMessage);
        LogUtil.d("processMessage appMessage=" + appMessage);
        PushMsg pushMsg = new PushMsg.Builder()
                .setBrand(PhoneBrand.Oppo)
                .setTitle(appMessage.getTitle())
                .setContent(appMessage.getContent())
                .setExtraMsg(appMessage.getRule())
                .build();
        DataRepeater.onNotificationMessageArrived(context, pushMsg);
    }
}
