package com.xxx.library.account;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gaoruochen on 18-4-3.
 */
public class AuthStatus extends RealmObject {

    @PrimaryKey
    public String username;
    public String password;

}
