package com.fomin.push.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fomin on 2018/10/18.
 */
public enum PhoneBrand implements Parcelable {
    Huawei(0, "HUAWEI"), Honor(1, "HONOR"), Xiaomi(2, "XIAOMI"), Meizu(3, "MEIZU"), Oppo(4, "OPPO"), Vivo(5, "VIVO");

    private final int id;
    private final String name;

    PhoneBrand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 获取产商ID
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 获取产商
     *
     * @return
     */
    public String getName() {
        return name;
    }


    /**
     * 根据ID获取手机产商
     *
     * @param name 手机产商名称
     * @return
     */
    public static PhoneBrand getPhoneBrand(String name) {
        for (PhoneBrand brand : PhoneBrand.values()) {
            if (brand.getName().equals(name.toUpperCase())) {
                return brand;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Creator<PhoneBrand> CREATOR = new Creator<PhoneBrand>() {
        @Override
        public PhoneBrand createFromParcel(Parcel in) {
            return PhoneBrand.values()[in.readInt()];
        }

        @Override
        public PhoneBrand[] newArray(int size) {
            return new PhoneBrand[size];
        }
    };
}
