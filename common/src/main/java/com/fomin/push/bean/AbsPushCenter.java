package com.fomin.push.bean;

import com.fomin.push.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fomin on 2018/10/17.
 */
public abstract class AbsPushCenter {
    private List<String> aliasList = new ArrayList<>();
    private List<String> topicList = new ArrayList<>();
    private List<String> accountList = new ArrayList<>();
    private List<String> unAliasList = new ArrayList<>();
    private List<String> unTopicList = new ArrayList<>();
    private List<String> unAccountList = new ArrayList<>();

    public abstract boolean isEnabled();

    public abstract String name();

    public void registerPush() {
        // 注册推送
        LogUtil.d("registerPush()");
    }

    public void unregisterPush() {
        // 关闭推送
        LogUtil.d("unregisterPush()");
    }

    public void setAlias(String alias) {
        // alias推送
        LogUtil.d("setAlias alias=" + alias);
        aliasList.add(alias);
    }

    public void unsetAlias(String alias) {
        // 取消用户别名
        LogUtil.d("unSetAlias alias=" + alias);
        unAliasList.add(alias);
    }

    public void setUserAccount(final String userAccount) {
        // 通过userAccount推送
        LogUtil.d("setUserAccount userAccount=" + userAccount);
        accountList.add(userAccount);
    }

    public void unsetUserAccount(final String userAccount) {
        // 取消用户userAccount
        LogUtil.d("unSetUserAccount userAccount=" + userAccount);
        unAccountList.add(userAccount);
    }

    public void unsetAllUserAccount() {
        // 取消所有用户userAccount
        LogUtil.d("unsetAllUserAccount.");
    }

    public void subscribe(String topic) {
        // 按照用户的订阅，开发者实现根据订阅分组群发
        LogUtil.d("subscribe topic=" + topic);
        topicList.add(topic);
    }

    public void unsubscribe(String topic) {
        //用户取消某个订阅topic
        LogUtil.d("unsubscribe topic=" + topic);
        unTopicList.add(topic);
    }

    public void resetEvent() {
        // 重新处理
        LogUtil.d("resetEvent");
        // 重新订阅事件
        for (String alias : aliasList) {
            setAlias(alias);
        }
        for (String account : accountList) {
            setUserAccount(account);
        }
        for (String topic : topicList) {
            subscribe(topic);
        }
        for (String alias : unAliasList) {
            unsetAlias(alias);
        }
        for (String account : unAccountList) {
            unsetUserAccount(account);
        }
        for (String topic : unTopicList) {
            unsubscribe(topic);
        }
    }
}
