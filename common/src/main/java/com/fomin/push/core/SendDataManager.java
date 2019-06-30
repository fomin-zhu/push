package com.fomin.push.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;

import com.fomin.push.util.LogUtil;

/**
 * Created by Fomin on 2018/10/18.
 */
public class SendDataManager {
    private static final String INTENT_DATA_PUSH = "hy_push_data";
    private static final String PUSH_BROADCAST_NAME = ".push.PushReceiver";

    /**
     * Send push data (through radio form to forward)
     *
     * @param context
     * @param action
     * @param data
     */
    public static void sendPushData(Context context, String action, Parcelable data) {
        Intent intent = new Intent(action);
        intent.putExtra(INTENT_DATA_PUSH, data);
        intent.addCategory(context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setComponent(new ComponentName(context.getPackageName(), context.getPackageName() + PUSH_BROADCAST_NAME));
        }
        context.sendBroadcast(intent);
        LogUtil.d("send data to client broadcast");
    }

    /**
     * Analytical push message data from the Intent
     *
     * @param intent
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> T parsePushData(Intent intent) {
        return intent.getParcelableExtra(INTENT_DATA_PUSH);
    }
}
