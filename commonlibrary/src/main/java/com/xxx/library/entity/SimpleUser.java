package com.xxx.library.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoruochen on 18-3-24.
 */

public class SimpleUser implements Parcelable {


    /**
     * name : 昵称
     * created : 2018-01-26 11:43:24
     * is_banned : false
     * is_suicide : false
     * avatar : https://img3.doubanio.com/icon/u173108839-1.jpg
     * signature :
     * uid : 173108839
     * alt : https://www.douban.com/people/173108839/
     * desc :
     * type : user
     * id : 173108839
     * large_avatar : https://img3.doubanio.com/icon/up173108839-1.jpg
     */

    public String name;
    public String created;
    public boolean is_banned;
    public boolean is_suicide;
    public String avatar;
    public String signature;
    public String uid;
    public String alt;
    public String desc;
    public String type;
    public String id;
    public String large_avatar;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.created);
        dest.writeByte(this.is_banned ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_suicide ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar);
        dest.writeString(this.signature);
        dest.writeString(this.uid);
        dest.writeString(this.alt);
        dest.writeString(this.desc);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeString(this.large_avatar);
    }

    public SimpleUser() {
    }

    protected SimpleUser(Parcel in) {
        this.name = in.readString();
        this.created = in.readString();
        this.is_banned = in.readByte() != 0;
        this.is_suicide = in.readByte() != 0;
        this.avatar = in.readString();
        this.signature = in.readString();
        this.uid = in.readString();
        this.alt = in.readString();
        this.desc = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.large_avatar = in.readString();
    }

    public static final Parcelable.Creator<SimpleUser> CREATOR = new Parcelable.Creator<SimpleUser>() {
        @Override
        public SimpleUser createFromParcel(Parcel source) {
            return new SimpleUser(source);
        }

        @Override
        public SimpleUser[] newArray(int size) {
            return new SimpleUser[size];
        }
    };
}
