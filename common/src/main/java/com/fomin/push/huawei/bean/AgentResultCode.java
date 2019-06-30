package com.fomin.push.huawei.bean;

/**
 * Created by Fomin on 2018/10/23.
 */
public class AgentResultCode {
    /**
     * HMSAgent 成功 | success
     */
    public static final int HMSAGENT_SUCCESS = 0;

    /**
     * HMSAgent 没有初始化 | Hmsagent not initialized
     */
    public static final int HMSAGENT_NO_INIT = -1000;

    /**
     * 请求需要activity，但当前没有可用的activity | Request requires activity, but no active activity is currently available
     */
    public static final int NO_ACTIVITY_FOR_USE = -1001;

    /**
     * 结果为空 | Result is empty
     */
    public static final int RESULT_IS_NULL = -1002;

    /**
     * 状态为空 | Status is empty
     */
    public static final int STATUS_IS_NULL = -1003;

    /**
     * 拉起activity异常，需要检查activity有没有在manifest中配置 | Pull up an activity exception and need to check if the activity is configured in manifest
     */
    public static final int START_ACTIVITY_ERROR = -1004;

    /**
     * onActivityResult 回调结果错误 | Onactivityresult Callback Result Error
     */
    public static final int ON_ACTIVITY_RESULT_ERROR = -1005;

    /**
     * 重复请求 | Duplicate Request
     */
    public static final int REQUEST_REPEATED = -1006;

    /**
     * 连接client 超时 | Connect Client Timeout
     */
    public static final int APICLIENT_TIMEOUT = -1007;

    /**
     * 调用接口异常 | Calling an interface exception
     */
    public static final int CALL_EXCEPTION = -1008;

    /**
     * 接口参数为空 | Interface parameter is empty
     */
    public static final int EMPTY_PARAM = -1009;
}
