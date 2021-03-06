package com.fomin.push.huawei.callback;

import android.app.Activity;

/**
 * Activity onResume 事件回调接口
 * Created by Fomin on 2018/10/23.
 */
public interface IActivityResumeCallback {

    /**
     * Activity onResume回调
     * @param activity 发生 onResume 事件的activity
     */
    void onActivityResume(Activity activity);
}
