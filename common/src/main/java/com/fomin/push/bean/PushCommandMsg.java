package com.fomin.push.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fomin on 2018/10/18.
 */
public class PushCommandMsg implements Parcelable {
    private String command;     // 命令
    private long resultCode;    // 返回结果
    private String reason;      // 原因
    private String extraMsg;    // 扩展信息 可存放token信息
    private PhoneBrand brand;   // 渠道

    public PushCommandMsg(Builder builder) {
        this.command = builder.command;
        this.resultCode = builder.resultCode;
        this.reason = builder.reason;
        this.extraMsg = builder.extraMsg;
        this.brand = builder.brand;
    }

    public String getCommand() {
        return command;
    }

    public long getResultCode() {
        return resultCode;
    }

    public String getReason() {
        return reason;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public PhoneBrand getBrand() {
        return brand;
    }

    public static class Builder {
        private String command;     // 命令
        private long resultCode;    // 返回结果
        private String reason;      // 原因
        private String extraMsg;    // 扩展信息 可存放token信息
        private PhoneBrand brand;   // 渠道

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setResultCode(long resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
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

        public PushCommandMsg build() {
            return new PushCommandMsg(this);
        }
    }

    public String toString() {
        String var1 = "command={" + this.command + "}, resultCode={" + this.resultCode + "}, reason={" + this.reason + "}, extraMsg={" + this.extraMsg + "}, brand={" + this.brand + "}";
        return var1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.command);
        dest.writeLong(this.resultCode);
        dest.writeString(this.reason);
        dest.writeString(this.extraMsg);
        dest.writeParcelable(this.brand, flags);
    }

    protected PushCommandMsg(Parcel in) {
        this.command = in.readString();
        this.resultCode = in.readLong();
        this.reason = in.readString();
        this.extraMsg = in.readString();
        this.brand = in.readParcelable(PhoneBrand.class.getClassLoader());
    }

    public static final Creator<PushCommandMsg> CREATOR = new Creator<PushCommandMsg>() {
        @Override
        public PushCommandMsg createFromParcel(Parcel source) {
            return new PushCommandMsg(source);
        }

        @Override
        public PushCommandMsg[] newArray(int size) {
            return new PushCommandMsg[size];
        }
    };
}
