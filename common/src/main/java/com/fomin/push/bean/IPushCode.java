package com.fomin.push.bean;

/**
 * Created by Fomin on 2018/10/18.
 */
public interface IPushCode {
    int SUCCESS = 0;
    int ERROR = -1;
    String TYPE_REGISTER = "register";
    String TYPE_UNREGISTER = "unregister";
    String TYPE_SET_ALIAS = "set-alias";
    String TYPE_UNSET_ALIAS = "unset-alias";
    String TYPE_SET_ACCOUNT = "set-account";
    String TYPE_UNSET_ACCOUNT = "unset-account";
    String TYPE_SUBSCRIBE_TOPIC = "subscribe-topic";
    String TYPE_UNSUBSCRIBE_TOPIC = "unsubscibe-topic";
    String TYPE_ACCEPT_TIME = "accept-time";
}
