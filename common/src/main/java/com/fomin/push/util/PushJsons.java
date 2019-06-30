package com.fomin.push.util;

import androidx.annotation.NonNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json converter
 * test case: MoshiTest.java
 * warning: inner class must be static
 * <p>
 * https://github.com/square/moshi
 * <p>
 * Author: Wilson on 31/5/16.
 * Email: coolsmiletree@gmail.com
 */
@SuppressWarnings("SpellCheckingInspection")
public class PushJsons {
    private final static Map<Object, JsonAdapter<List<?>>> mListAdapterCache = new HashMap<>();
    private static Moshi sMoshi;

    private PushJsons() {
    }

    @SuppressWarnings("WeakerAccess")
    public static Moshi impl() {
        if (sMoshi == null) {
            return sMoshi = new Moshi.Builder()
                    .build();
        }
        return sMoshi;
    }

    public static <T> T fromJson(String json, Class clazz) {
        //noinspection unchecked
        JsonAdapter<T> jsonAdapter = impl().adapter(classAdapter(clazz));

        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String toJson(T obj) {
        if (obj == null)
            return null;
        Class clazz = classAdapter(obj.getClass());
        //noinspection unchecked
        JsonAdapter jsonAdapter = impl().adapter(clazz);
        //noinspection unchecked
        return jsonAdapter.toJson(obj);
    }

    @NonNull
    private static <T> Class classAdapter(Class clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            clazz = Map.class;
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            clazz = Collection.class;
        }
        return clazz;
    }

//    public static <T> String toJsonArray(List<T> list, Class<T> clazz) {
//        if (list == null)
//            return null;
//        return getListJsonAdapter(clazz).toJson(list);
//    }

    private static <T> JsonAdapter<List<?>> getListJsonAdapter(Class<T> clazz) {
        JsonAdapter<List<?>> jsonAdapter = mListAdapterCache.get(clazz);
        if (jsonAdapter == null) {
            Type type = Types.newParameterizedType(List.class, clazz);
            jsonAdapter = impl().adapter(type);
            mListAdapterCache.put(clazz, jsonAdapter);
        }
        return jsonAdapter;
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        try {
            JsonAdapter<List<?>> listJsonAdapter = getListJsonAdapter(clazz);
            //noinspection unchecked
            return (List<T>) listJsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static <K, V> JsonAdapter<Map<K, V>> mapAdapter(Type keyType, Type valueType) {
        return impl().adapter(Types.newParameterizedType(Map.class, keyType, valueType));
    }

    public static <K, V> Map<K, V> fromJson(Type keyType, Type valueType, String json)   {
        JsonAdapter<Map<K, V>> mapJsonAdapter = mapAdapter(keyType, valueType);
        try {
            return mapJsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
