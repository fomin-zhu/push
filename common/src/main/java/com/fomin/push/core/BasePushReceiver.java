package com.fomin.push.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.fomin.push.bean.IPushAction;
import com.fomin.push.bean.IPushReceiver;
import com.fomin.push.bean.PushCommandMsg;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.util.LogUtil;

/**
 * Created by Fomin on 2018/10/18.
 */
public abstract class BasePushReceiver extends BroadcastReceiver implements IPushReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Parcelable parcelable = SendDataManager.parsePushData(intent);
        if (IPushAction.RECEIVE_COMMAND_RESULT.equals(action)) {
            onCommandResult(context, (PushCommandMsg) parcelable);
        } else if (IPushAction.RECEIVE_NOTIFICATION.equals(action)) {
            onNotificationMessageArrived(context, (PushMsg) parcelable);
        } else if (IPushAction.RECEIVE_NOTIFICATION_CLICK.equals(action)) {
            onNotificationMessageClicked(context, (PushMsg) parcelable);
        } else if (IPushAction.RECEIVE_MESSAGE.equals(action)) {
            onReceivePassThroughMessage(context, (PushMsg) parcelable);
        }else if (IPushAction.RECEIVE_TOKEN.equals(action)) {
            onReceiveToken(context, (PushMsg) parcelable);
        }
       LogUtil.d(String.format("%s-%s", action, String.valueOf(parcelable)));
    }
}
