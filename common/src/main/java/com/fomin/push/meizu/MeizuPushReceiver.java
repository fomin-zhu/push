package com.fomin.push.meizu;

import android.content.Context;

import com.fomin.push.bean.IPushCode;
import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushCommandMsg;
import com.fomin.push.bean.PushMsg;
import com.fomin.push.core.DataRepeater;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * Created by Fomin on 2018/10/25.
 */
public class MeizuPushReceiver extends MzPushMessageReceiver {

    @Override
    @Deprecated
    public void onRegister(Context context, String s) {
        LogUtil.d("onRegister() token=" + s);
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        LogUtil.d("onRegister() result=" + b);
    }

    /**
     * 检查通知栏和透传消息开关状态回调
     *
     * @param context
     * @param pushSwitchStatus
     */
    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
        LogUtil.d("onPushStatus() message=" + pushSwitchStatus);
    }

    /**
     * 新版订阅回调
     *
     * @param context
     * @param registerStatus
     */
    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        LogUtil.d("onRegisterStatus() message=" + registerStatus);
        PushMsg pushMsg = new PushMsg.Builder()
                .setBrand(PhoneBrand.Meizu)
                .setContent(registerStatus.getPushId())
                .build();
        DataRepeater.onReceiveToken(context, pushMsg);
    }

    /**
     * 新版反订阅回调
     *
     * @param context
     * @param unRegisterStatus
     */
    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
        LogUtil.d("onUnRegisterStatus() message=" + unRegisterStatus);
    }

    /**
     * 标签回调
     *
     * @param context
     * @param subTagsStatus
     */
    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
        LogUtil.d("onSubTagsStatus() message=" + subTagsStatus);
        PushCommandMsg pushMsg = new PushCommandMsg.Builder()
                .setCommand(IPushCode.TYPE_SUBSCRIBE_TOPIC)
                .setResultCode(subTagsStatus.getCode().equals(SubAliasStatus.SUCCESS_CODE) ? IPushCode.SUCCESS : IPushCode.ERROR)
                .setBrand(PhoneBrand.Meizu)
                .setReason(subTagsStatus.message)
                .setExtraMsg(subTagsStatus.getPushId())
                .build();
        DataRepeater.onCommandResult(context, pushMsg);
    }

    /**
     * 别名回调
     *
     * @param context
     * @param subAliasStatus
     */
    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
        LogUtil.d("onSubAliasStatus() message=" + subAliasStatus);
        PushCommandMsg pushMsg = new PushCommandMsg.Builder()
                .setCommand(IPushCode.TYPE_SET_ALIAS)
                .setResultCode(subAliasStatus.getCode().equals(SubAliasStatus.SUCCESS_CODE) ? IPushCode.SUCCESS : IPushCode.ERROR)
                .setBrand(PhoneBrand.Meizu)
                .setReason(subAliasStatus.message)
                .setExtraMsg(subAliasStatus.getPushId())
                .build();
        DataRepeater.onCommandResult(context, pushMsg);
    }

    /***
     * 通知栏消息到达回调，flyme6基于android6.0以上不再回调
     * @param context
     * @param mzPushMessage
     */
    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        LogUtil.d("onNotificationMessageArrived() message=" + mzPushMessage);
        PushMsg pushMsg = new PushMsg.Builder()
                .setNotifyId(mzPushMessage.getNotifyId())
                .setTitle(mzPushMessage.getTitle())
                .setContent(mzPushMessage.getContent())
                .setExtraMsg(mzPushMessage.getTaskId())
                .setBrand(PhoneBrand.Meizu)
                .setKey(PushUtil.stringToMap(mzPushMessage.getSelfDefineContentString()))
                .build();
        DataRepeater.onNotificationMessageArrived(context, pushMsg);
    }

    /**
     * 通知栏消息点击回调
     *
     * @param context
     * @param mzPushMessage
     */
    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        LogUtil.d("onNotificationClicked() message=" + mzPushMessage);
        PushMsg pushMsg = new PushMsg.Builder()
                .setNotifyId(mzPushMessage.getNotifyId())
                .setTitle(mzPushMessage.getTitle())
                .setContent(mzPushMessage.getContent())
                .setExtraMsg(mzPushMessage.getTaskId())
                .setBrand(PhoneBrand.Meizu)
                .setKey(PushUtil.stringToMap(mzPushMessage.getSelfDefineContentString()))
                .build();
        DataRepeater.onNotificationMessageClicked(context, pushMsg);
    }
}
