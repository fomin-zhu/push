package com.fomin.push.huawei;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.core.DataRepeater;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushJsons;
import com.fomin.push.util.PushUtil;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Fomin on 2018/10/18.
 */
public class HuaweiPushReceiver extends PushReceiver {

    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        super.onEvent(context, event, extras);
        LogUtil.d("onEvent() event=" + event.name() + " extras=" + extras.toString());
        String json = extras.getString("pushMsg");
        LogUtil.d("onEvent() event=" + json);
        PushMsg pushMsg = new PushMsg.Builder()
                .setBrand(PhoneBrand.Huawei)
                .setKey(PushUtil.stringAryMap(json))
                .build();
        DataRepeater.onNotificationMessageClicked(context, pushMsg);
    }

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        super.onToken(context, token, extras);
        LogUtil.d("onToken() token=" + token);
        PushMsg pushMsg = new PushMsg.Builder()
                .setBrand(PhoneBrand.Huawei)
                .setContent(token)
                .build();
        DataRepeater.onReceiveToken(context, pushMsg);
    }

    @Override
    public void onPushMsg(Context context, byte[] msg, String token) {
        super.onPushMsg(context, msg, token);
        LogUtil.d("onPushMsg() msgBytes=" + new String(msg, Charset.forName("UTF-8")) + " token=" + token);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
        final String passThroughBody = new String(msgBytes, Charset.forName("UTF-8"));
        LogUtil.d("onPushMsg() msgBytes=" + passThroughBody + " extras=" + extras.toString());
        final Map<String, String> extraKV = PushUtil.bundleToMap(extras);
        try {
            final Map<String, String> passThroughBodyDecoded = PushJsons.fromJson(String.class, String.class, passThroughBody);
            extraKV.putAll(passThroughBodyDecoded);
        } catch (Throwable t) {
            LogUtil.e("failed to convert pass through body from json", t);
        }

        final PushMsg pushMsg = new PushMsg.Builder()
                .setBrand(PhoneBrand.Huawei)
                .setKey(extraKV)
                .build();
        DataRepeater.onReceivePassThroughMessage(context, pushMsg);
        return super.onPushMsg(context, msgBytes, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        super.onPushState(context, pushState);
        LogUtil.d("onPushState() pushState=" + pushState);
    }
}
