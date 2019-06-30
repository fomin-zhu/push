package com.fomin.push.huawei.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.fomin.push.huawei.callback.IActivityDestroyedCallback;
import com.fomin.push.huawei.callback.IActivityPauseCallback;
import com.fomin.push.huawei.callback.IActivityResumeCallback;
import com.fomin.push.util.LogUtil;
import com.fomin.push.util.PushUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fomin on 2018/10/23.
 */
public final class ActivityMgr implements Application.ActivityLifecycleCallbacks {


    private static class SingletonInstance {
        private static final ActivityMgr INSTANCE = new ActivityMgr();
    }

    public static ActivityMgr getInstance() {
        return ActivityMgr.SingletonInstance.INSTANCE;
    }

    /**
     * 应用程序 | application
     */
    private Application application;

    /**
     * 最新的activity列表，如果没有则为空列表 | Latest list of activity, if no, empty list
     */
    private List<Activity> curActivities = new ArrayList<Activity>();

    /**
     * activity onResume Event Monitoring
     */
    private List<IActivityResumeCallback> resumeCallbacks = new ArrayList<IActivityResumeCallback>();

    /**
     * activity onPause Event Monitoring
     */
    private List<IActivityPauseCallback> pauseCallbacks = new ArrayList<IActivityPauseCallback>();

    /**
     * activity onDestroyed Event Monitoring
     */
    private List<IActivityDestroyedCallback> destroyedCallbacks = new ArrayList<IActivityDestroyedCallback>();

    /**
     * 私有构造方法 | Private construction methods
     * 防止外面直接创建实例 | Prevent external instances from being created directly
     */
    private ActivityMgr() {
    }

    /**
     * 初始化方法 | Initialization method
     *
     * @param app 应用程序 | application
     */
    public void init(Application app) {
        LogUtil.d("ActivityMgr init");

        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        application = app;
        app.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 释放资源，一般不需要调用 | Frees resources, and generally does not need to call
     */
    public void release() {
        LogUtil.d("ActivityMgr release");
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        clearCurActivities();
        clearActivityResumeCallbacks();
        clearActivityPauseCallbacks();
        application = null;
    }

    /**
     * 设置最新的activity | Set up the latest activity
     *
     * @param curActivity 最新的activity | Latest activity
     */
    private void setCurActivity(Activity curActivity) {
        int idxCurActivity = curActivities.indexOf(curActivity);
        if (idxCurActivity == -1) {
            curActivities.add(curActivity);
        } else if (idxCurActivity < curActivities.size() - 1) {
            curActivities.remove(curActivity);
            curActivities.add(curActivity);
        }
    }

    /**
     * 移除当前activity | Remove Current Activity
     *
     * @param curActivity 要移除的activity | Activity to remove
     */
    private void removeActivity(Activity curActivity) {
        curActivities.remove(curActivity);
    }


    /**
     * 获取最新的activity，如果没有则返回null | Gets the latest activity and returns null if not
     *
     * @return 最新的activity | Latest activity
     */
    private Activity getLastActivityInner() {
        if (curActivities.size() > 0) {
            return curActivities.get(curActivities.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * 清理activities | Clean activities
     */
    private void clearCurActivities() {
        curActivities.clear();
    }

    /**
     * 清空 activity onResume事件回调 | Clear Activity Onresume Event callback
     */
    public void clearActivityResumeCallbacks() {
        LogUtil.d("clearOnResumeCallback");
        resumeCallbacks.clear();
    }

    /**
     * 清空 activity onPause 事件回调 | Clear Activity OnPause Event callback
     */
    public void clearActivityPauseCallbacks() {
        LogUtil.d("clearOnPauseCallback");
        pauseCallbacks.clear();
    }

    /**
     * 注册activity onResume事件回调 | Registering an Activity Onresume event Callback
     *
     * @param callback activity onResume事件回调 | Activity Onresume Event Callback
     */
    public void registerActivityResumeEvent(IActivityResumeCallback callback) {
        LogUtil.d("registerOnResume:" + PushUtil.objDesc(callback));
        resumeCallbacks.add(callback);
    }

    /**
     * 反注册activity onResume事件回调 | unregistration Activity Onresume Event Callback
     *
     * @param callback 已经注册的 activity onResume事件回调 | Registered Activity Onresume Event callback
     */
    public void unRegisterActivityResumeEvent(IActivityResumeCallback callback) {
        LogUtil.d("unRegisterOnResume:" + PushUtil.objDesc(callback));
        resumeCallbacks.remove(callback);
    }

    /**
     * 注册activity onPause 事件回调 | Registering an Activity OnPause event Callback
     *
     * @param callback activity onPause 事件回调 | Activity OnPause Event Callback
     */
    public void registerActivityPauseEvent(IActivityPauseCallback callback) {
        LogUtil.d("registerOnPause:" + PushUtil.objDesc(callback));
        pauseCallbacks.add(callback);
    }

    /**
     * 反注册activity onPause事件回调 | unregistration activity OnPause Event Callback
     *
     * @param callback 已经注册的 activity onPause事件回调 | Registered Activity OnPause Event callback
     */
    public void unRegisterActivityPauseEvent(IActivityPauseCallback callback) {
        LogUtil.d("unRegisterOnPause:" + PushUtil.objDesc(callback));
        pauseCallbacks.remove(callback);
    }

    /**
     * 注册activity onDestroyed 事件回调 | Registering an Activity ondestroyed event Callback
     *
     * @param callback activity onDestroyed 事件回调 | Activity Ondestroyed Event Callback
     */
    public void registerActivityDestroyedEvent(IActivityDestroyedCallback callback) {
        LogUtil.d("registerOnDestroyed:" + PushUtil.objDesc(callback));
        destroyedCallbacks.add(callback);
    }

    /**
     * 反注册activity onDestroyed 事件回调 | unregistration Activity ondestroyed Event Callback
     *
     * @param callback 已经注册的 activity onDestroyed事件回调 | Registered Activity ondestroyed Event callback
     */
    public void unRegisterActivityDestroyedEvent(IActivityDestroyedCallback callback) {
        LogUtil.d("unRegisterOnDestroyed:" + PushUtil.objDesc(callback));
        destroyedCallbacks.remove(callback);
    }

    /**
     * 获取最新的activity | Get the latest activity
     *
     * @return 最新的activity | Latest activity
     */
    public Activity getLastActivity() {
        return getLastActivityInner();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.d("onCreated:" + PushUtil.objDesc(activity));
        setCurActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.d("onStarted:" + PushUtil.objDesc(activity));
        setCurActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.d("onResumed:" + PushUtil.objDesc(activity));
        setCurActivity(activity);

        List<IActivityResumeCallback> tmdCallbacks = new ArrayList<IActivityResumeCallback>(resumeCallbacks);
        for (IActivityResumeCallback callback : tmdCallbacks) {
            callback.onActivityResume(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.d("onPaused:" + PushUtil.objDesc(activity));
        List<IActivityPauseCallback> tmdCallbacks = new ArrayList<IActivityPauseCallback>(pauseCallbacks);
        for (IActivityPauseCallback callback : tmdCallbacks) {
            callback.onActivityPause(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d("onDestroyed:" + PushUtil.objDesc(activity));
        removeActivity(activity);

        // activity onDestroyed 事件回调 | Activity Ondestroyed Event Callback
        List<IActivityDestroyedCallback> tmdCallbacks = new ArrayList<IActivityDestroyedCallback>(destroyedCallbacks);
        for (IActivityDestroyedCallback callback : tmdCallbacks) {
            callback.onActivityDestroyed(activity, getLastActivityInner());
        }
    }
}
