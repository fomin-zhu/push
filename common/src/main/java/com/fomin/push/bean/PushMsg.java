package com.fomin.push.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fomin on 2018/10/18.
 */
public class PushMsg implements Parcelable {

    private long notifyId;               // 通知ID
    private String title;               // 标题
    private String content;             // 内容/token
    private String extraMsg;            // 扩展消息（例如小米推送里面的传输数据）
    private PhoneBrand brand;           // 渠道
    private Map<String, String> key;    // 键值对

    private PushMsg(Builder builder) {
        this.notifyId = builder.notifyId;
        this.title = builder.title;
        this.content = builder.content;
        this.extraMsg = builder.extraMsg;
        this.brand = builder.brand;
        this.key = builder.key == null ? new HashMap<>() : builder.key;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public String getTitle() {
        return title;
    }

    public PhoneBrand getBrand() {
        return brand;
    }

    public String getContent() {
        return content;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public Map<String, String> getKey() {
        return key;
    }

    public static class Builder {
        private long notifyId;               // 通知ID
        private String title;               // 标题
        private String content;             // 内容/token
        private String extraMsg;            // 扩展消息（例如小米推送里面的传输数据）
        private PhoneBrand brand;           // 渠道
        private Map<String, String> key;    // 键值对

        public Builder setNotifyId(long notifyId) {
            this.notifyId = notifyId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setExtraMsg(String extraMsg) {
            this.extraMsg = extraMsg;
            return this;
        }

        public Builder setBrand(PhoneBrand brand) {
            this.brand = brand;
            return this;
        }

        public Builder setKey(Map<String, String> key) {
            this.key = key;
            return this;
        }

        public PushMsg build() {
            return new PushMsg(this);
        }
    }


    @Override
    public String toString() {
        return "PushMsg{" +
                "notifyId=" + notifyId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", extraMsg='" + extraMsg + '\'' +
                ", keyValue=" + key +
                ", brand=" + brand +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.notifyId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.extraMsg);
        dest.writeInt(this.brand == null ? -1 : this.brand.ordinal());
        this.key = this.key == null ? new HashMap<>() : this.key;
        dest.writeInt(this.key.size());
        for (Map.Entry<String, String> entry : this.key.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected PushMsg(Parcel in) {
        this.notifyId = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.extraMsg = in.readString();
        int tmpBrand = in.readInt();
        this.brand = tmpBrand == -1 ? null : PhoneBrand.values()[tmpBrand];
        int keySize = in.readInt();
        this.key = new HashMap<String, String>(keySize);
        for (int i = 0; i < keySize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.key.put(key, value);
        }
    }

    public static final Creator<PushMsg> CREATOR = new Creator<PushMsg>() {
        @Override
        public PushMsg createFromParcel(Parcel source) {
            return new PushMsg(source);
        }

        @Override
        public PushMsg[] newArray(int size) {
            return new PushMsg[size];
        }
    };
}
