package com.fomin.push.bean;

/**
 * Created by Fomin on 2018/10/18.
 */
public interface IPushAction {

    String RECEIVE_NOTIFICATION = "com.fomin.push.ACTION_RECEIVE_NOTIFICATION";
    String RECEIVE_NOTIFICATION_CLICK = "com.fomin.push.ACTION_RECEIVE_NOTIFICATION_CLICK";
    String RECEIVE_MESSAGE = "com.fomin.push.ACTION_RECEIVE_MESSAGE";
    String RECEIVE_COMMAND_RESULT = "com.fomin.push.ACTION_RECEIVE_COMMAND_RESULT";
    String RECEIVE_TOKEN = "com.fomin.push.ACTION_RECEIVE_TOKEN";
}
