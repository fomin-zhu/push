package com.fomin.push.huawei.core;

import com.fomin.push.huawei.callback.ICallbackCode;

/**
 * 回调线程
 * Created by Fomin on 2018/10/23.
 */
public class CallbackCodeRunnable implements Runnable {

    private ICallbackCode handlerInner;
    private int rtnCodeInner;

    public CallbackCodeRunnable(ICallbackCode handler, int rtnCode) {
        handlerInner = handler;
        rtnCodeInner = rtnCode;
    }

    @Override
    public void run() {
        if (handlerInner != null) {
            handlerInner.onResult(rtnCodeInner);
        }
    }
}