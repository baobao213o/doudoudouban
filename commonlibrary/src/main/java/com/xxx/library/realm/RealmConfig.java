package com.xxx.library.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by gaoruochen on 18-4-8.
 */

public class RealmConfig {

    private static final String REALM_NAME = "doudoudouban.realm";

    private RealmConfig(){}

    public static Realm getDefaultConfigReal(){
        return  Realm.getInstance(new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build());

    }
}
