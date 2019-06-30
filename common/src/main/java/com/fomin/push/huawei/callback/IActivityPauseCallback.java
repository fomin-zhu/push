package com.fomin.push.huawei.callback;

import android.app.Activity;

/**
 * Activity onPause 事件回调接口
 * Created by Fomin on 2018/10/23.
 */
public interface IActivityPauseCallback {

    /**
     * Activity onPause回调
     * @param activity 发生 onPause 事件的activity
     */
    void onActivityPause(Activity activity);
}

