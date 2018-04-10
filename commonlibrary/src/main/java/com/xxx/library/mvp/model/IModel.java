package com.xxx.library.mvp.model;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmModel;

public interface IModel {

    <T> T postDataFromRemote(Class<T> service);

    <T> Observable<T> modifyDataFromLocal(Function<Realm, T> function);

    <T extends RealmModel> Flowable<List<T>> getDataFromLocal(Class<T> clazz);
}
