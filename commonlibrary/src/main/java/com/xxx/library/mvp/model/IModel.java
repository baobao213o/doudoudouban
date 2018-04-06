package com.xxx.library.mvp.model;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.realm.Realm;

public interface IModel {

    <T> T postDataFromRemote(Class<T> service);

    <T> Observable<T> modifyDataFromLocal(Function<Realm, T> function);

    <T> Flowable<T> getDataFromLocal();
}
