package com.xxx.library.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoruochen on 18-3-15.
 */

public class User implements Parcelable {


    /**
     * uid : 173108839
     * following_count : 0
     * is_suicide : false
     * is_follower : false
     * alt : https://www.douban.com/people/173108839/
     * notes_count : 0
     * id : 173108839
     * blocked : false
     * followers_count : 0
     * logged_in : true
     * type : user
     * large_avatar : https://img3.doubanio.com/icon/up173108839-1.jpg
     * icon_avatar : https://img3.doubanio.com/icon/ui173108839-1.jpg
     * statuses_count : 0
     * blocking : false
     * desc :
     * name : 昵称
     * created : 2018-01-26 11:43:24
     * albums_count : 0
     * avatar : https://img3.doubanio.com/icon/u173108839-1.jpg
     * signature :
     * following : false
     */

    public String uid;
    public int following_count;
    public boolean is_suicide;
    public boolean is_follower;
    public String alt;
    public int notes_count;
    public String id;
    public boolean blocked;
    public int followers_count;
    public boolean logged_in;
    public String type;
    public String large_avatar;
    public String icon_avatar;
    public int statuses_count;
    public boolean blocking;
    public String desc;
    public String name;
    public String created;
    public int albums_count;
    public String avatar;
    public String signature;
    public boolean following;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeInt(this.following_count);
        dest.writeByte(this.is_suicide ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_follower ? (byte) 1 : (byte) 0);
        dest.writeString(this.alt);
        dest.writeInt(this.notes_count);
        dest.writeString(this.id);
        dest.writeByte(this.blocked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.followers_count);
        dest.writeByte(this.logged_in ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeString(this.large_avatar);
        dest.writeString(this.icon_avatar);
        dest.writeInt(this.statuses_count);
        dest.writeByte(this.blocking ? (byte) 1 : (byte) 0);
        dest.writeString(this.desc);
        dest.writeString(this.name);
        dest.writeString(this.created);
        dest.writeInt(this.albums_count);
        dest.writeString(this.avatar);
        dest.writeString(this.signature);
        dest.writeByte(this.following ? (byte) 1 : (byte) 0);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.following_count = in.readInt();
        this.is_suicide = in.readByte() != 0;
        this.is_follower = in.readByte() != 0;
        this.alt = in.readString();
        this.notes_count = in.readInt();
        this.id = in.readString();
        this.blocked = in.readByte() != 0;
        this.followers_count = in.readInt();
        this.logged_in = in.readByte() != 0;
        this.type = in.readString();
        this.large_avatar = in.readString();
        this.icon_avatar = in.readString();
        this.statuses_count = in.readInt();
        this.blocking = in.readByte() != 0;
        this.desc = in.readString();
        this.name = in.readString();
        this.created = in.readString();
        this.albums_count = in.readInt();
        this.avatar = in.readString();
        this.signature = in.readString();
        this.following = in.readByte() != 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
