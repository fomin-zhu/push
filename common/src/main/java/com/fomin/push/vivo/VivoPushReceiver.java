package com.fomin.push.vivo;

import android.content.Context;

import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.core.DataRepeater;
import com.fomin.push.util.LogUtil;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

/**
 * Created by Fomin on 2018/10/24.
 */
public class VivoPushReceiver extends OpenClientPushMessageReceiver {


    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage message) {
        LogUtil.d("onNotificationMessageClicked() message=" + message);
        PushMsg pushMsg = new PushMsg.Builder()
                .setNotifyId(message.getMsgId())
                .setTitle(message.getTitle())
                .setContent(message.getContent())
                .setExtraMsg(message.getSkipContent())
                .setKey(message.getParams())
                .setBrand(PhoneBrand.Vivo)
                .build();
        DataRepeater.onNotificationMessageClicked(context, pushMsg);
    }

    @Override
    public void onReceiveRegId(Context context, String s) {
        LogUtil.d("onReceiveRegId() regId=" + s);
        PushMsg pushMsg = new PushMsg.Builder()
                .setContent(s)
                .setBrand(PhoneBrand.Vivo)
                .build();
        DataRepeater.onReceiveToken(context, pushMsg);
    }
}
