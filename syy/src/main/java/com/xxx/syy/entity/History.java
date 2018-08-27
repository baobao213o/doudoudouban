package com.xxx.syy.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class History extends RealmObject {
    @PrimaryKey
    public String q;
}
