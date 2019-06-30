package com.fomin.push.huawei.callback;

import android.app.Activity;

/**
 * Activity onDestroyed 事件回调接口
 * Created by Fomin on 2018/10/23.
 */
public interface IActivityDestroyedCallback {

    /**
     * Activity onPause回调
     * @param activityDestroyed 发生 onDestroyed 事件的activity
     * @param activityNxt 下个要显示的activity
     */
    void onActivityDestroyed(Activity activityDestroyed, Activity activityNxt);
}
