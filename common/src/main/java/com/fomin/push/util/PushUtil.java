package com.fomin.push.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.fomin.push.bean.PhoneBrand;
import com.fomin.push.bean.PushInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Fomin on 2018/10/18.
 */
public class PushUtil {
    private static final String APP_ID = "%s_APP_ID";
    private static final String APP_KEY = "%s_APP_KEY";

    public static PhoneBrand getPhoneBrand() {
        String bandName = android.os.Build.BRAND;
        PhoneBrand band = PhoneBrand.getPhoneBrand(bandName);
        LogUtil.d("get PhoneBrand:" + band + ", bandName:" + bandName);
        if (band == null) {
            band = PhoneBrand.Xiaomi;
        }
        return band;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static PushInfo getPushInfo(Context context, PhoneBrand brand) {
        //读取小米对应的appId和appSecret
        PushInfo info = new PushInfo();
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            String keyId = String.format(APP_ID, brand.getName());
            String keyName = String.format(APP_KEY, brand.getName());
            info.setAppId(metaData.getString(keyId).trim());
            info.setAppKey(metaData.getString(keyName).trim());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.e("can't find PUSH_APP_ID or PUSH_APP_KEY in AndroidManifest.xml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            LogUtil.e("can't find PUSH_APP_ID or PUSH_APP_KEY in AndroidManifest.xml");
        }
        return info;
    }

    public static String getMetaData(Context context, String key) {
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            return metaData.getString(key).trim();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> bundleToMap(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            map.put(key, value.toString());
        }
        return map;
    }

    /**
     * 返回对象的描述，这里为了避免用户数据隐私的泄露，只是返回对象本身的描述 类名@hashcode
     *
     * @param object 对象
     * @return 对象的描述
     */
    public static String objDesc(Object object) {
        return object == null ? "null" : (object.getClass().getName() + '@' + Integer.toHexString(object.hashCode()));
    }

    public static Map<String, String> stringToMap(String json) {
        if (isEmpty(json)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject object = new JSONObject(json);
            Iterator iterator = object.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                map.put(key, object.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> stringAryMap(String json) {

        if (isEmpty(json)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Iterator iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    map.put(key, object.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
