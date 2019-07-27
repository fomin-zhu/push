**1、概述**

android push集成了小米，华为，魅族，vivo和oppo五大厂家的push sdk，方便Android各项目集成push。

**2、Push类图**

![图片](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWFnZXMtY2RuLnNoaW1vLmltL25KQ3l4cFFYWHJvZEtCZGovJUU2JTlDJUFBJUU1JTkxJUJEJUU1JTkwJThEJUU2JTk2JTg3JUU0JUJCJUI2XzZfLnBuZyF0aHVtYm5haWw)

**3、Push时序图**

![图片](https://images-cdn.shimo.im/nJCyxpQXXrodKBdj/未命名文件_6_.png!thumbnail)

**4、使用姿势**

* 配置global.gradle和build.gradle

在project的build增加配置
```
allprojects {
    repositories {
        maven {url "http://developer.huawei.com/repo"}
        maven{url 'https://raw.githubusercontent.com/fomin-zhu/repo/master'}
    }
}
```
在module的build增加配置
```
dependencies {
  implementation 'com.fomin.push:push:1.0.0'
}
```
* 配置AndroidManifest
```
注意:appId如果是数字，请在前面加上"\ "，避免读取数据错误
<!--小米推送静态注册-->
<meta-data
    android:name="XIAOMI_APP_ID"
    android:value="\ xxxxxxxxxxx" />

<meta-data
    android:name="XIAOMI_APP_KEY"
    android:value="\ xxxxxxxxxxx" />
<!--魅族推送静态注册-->
<meta-data
    android:name="MEIZU_APP_ID"
    android:value="xxxxxxxxxxx" />

<meta-data
    android:name="MEIZU_APP_KEY"
    android:value="xxxxxxxxxxx" />
<!--Oppo推送静态注册-->
<meta-data
    android:name="OPPO_APP_ID"
    android:value="xxxxxxxxxxx" />

<meta-data
    android:name="OPPO_APP_KEY"
    android:value="xxxxxxxxxxx" />

<!--华为推送appId-->
<meta-data
    android:name="com.huawei.hms.client.appid"
    android:value="xxxxxxxxxxx"/>

<!--vivo推送appId-->
<meta-data
    android:name="com.vivo.push.app_id"
    android:value="xxxxxxxxxxx" />
<meta-data
    android:name="com.vivo.push.api_key"
    android:value="xxxxxxxxxxx" />
    
<!--自定义BroadcastReceiver-->
<receiver android:name=".push.PushReceiver">
    <intent-filter>
        <action android:name="com.fomin.push.ACTION_RECEIVE_NOTIFICATION" />
        <action android:name="com.fomin.push.ACTION_RECEIVE_NOTIFICATION_CLICK" />
        <action android:name="com.fomin.push.ACTION_RECEIVE_MESSAGE" />
        <action android:name="com.fomin.push.ACTION_RECEIVE_COMMAND_RESULT" />
        <action android:name="com.fomin.push.ACTION_RECEIVE_TOKEN" />
        <category android:name="${applicationId}" />
    </intent-filter>
</receiver>
```
* Application初始化
```
override fun onCreate() {
    if (!Systems.isInMainProcess(application)) return
    super.onCreate()
    pushCenter = PushFactory.init(application)
}

companion object {
    fun pushCenter(): AbsPushCenter? {
        return pushCenter
    }

    fun setPushCenter(pushCenter: AbsPushCenter) {
        this.pushCenter = pushCenter
    }
}
```
* 自定义BroadcastReceiver，类名和包路径固定，类名为PushReceiver，包路径$applicationId+".push.PushReceiver"，需要兼容8.0系统
```
class PushReceiver : BasePushReceiver() {

    /**
     * 获取注册推送返回的token或regId
     */
    override fun onReceiveToken(context: Context?, msg: PushMsg?) {
        Log.d(PushUtil.TAG, "call onReceiveToken(): context=[$context], message=[$msg]")
    }

    /**
     * 获取给服务器发送命令的结果，结果封装在MiPushCommandMessage类中。
     */
    override fun onCommandResult(context: Context?, command: PushCommandMsg?) {
        Log.d(PushUtil.TAG, "call onCommandResult(): context=[$context], message=[$command]")
    }

    /**
     * 接收服务器推送的通知消息，用户点击后触发，消息封装在 MiPushMessage类中。
     */
    override fun onNotificationMessageClicked(context: Context?, msg: PushMsg?) {
        Log.d(PushUtil.TAG, " call onNotificationMessageClicked(): context=[$context], miPushMessage=[$msg]")
    }

    override fun onReceivePassThroughMessage(context: Context?, msg: PushMsg?) {
        Log.d(PushUtil.TAG, " call onReceivePassThroughMessage(): context=[$context], msg=[$msg]")
    }

    /**
     * 接收服务器推送的通知消息，消息到达客户端时触发，还可以接受应用在前台时不弹出通知的通知消息，消息封装在 MiPushMessage类中。
     * 在MIUI上，只有应用处于启动状态，或者自启动白名单中，才可以通过此方法接受到该消息。
     */
    override fun onNotificationMessageArrived(context: Context?, msg: PushMsg?) {
        Log.d(PushUtil.TAG, " call onNotificationMessageArrived(): context=[$context], miPushMessage=[$msg]")
    }
}
```
* 混淆
```
-keep class com.huawei.** {*;}
-keep class com.huaying.seal.common.mipush.PushReceiver{*;}
-dontwarn com.xiaomi.push.**
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.meizu.cloud.**{*;}
-keep class com.coloros.mcssdk.**{*;}
```

