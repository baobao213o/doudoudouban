package com.xxx.library.mvp.model;


import com.xxx.library.network.RetrofitManager;
import com.xxx.library.realm.RealmObserver;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class BaseModel implements IModel {

    @Override
    public <T> T postDataFromRemote(Class<T> service) {
        return RetrofitManager.getInstance().getService(service);
    }

    @Override
    public <T> Observable<T> modifyDataFromLocal(final Function<Realm, T> function) {
        return Observable.create(new RealmObserver<T>() {
            @Override
            public T get(Realm realm) {
                T t = null;
                try {
                    t = function.apply(realm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t != null) {
                    if (t instanceof RealmObject) {
                        return (T) realm.copyFromRealm((RealmObject) t);
                    } else if (t instanceof RealmList) {
                        return (T) realm.copyFromRealm((List<RealmObject>) t);
                    } else if (t instanceof RealmResults) {
                        return (T) realm.copyFromRealm((List<RealmObject>) t);
                    }
                    return t;
                }
                return t;
            }
        });
    }

    @Override
    public <T> Flowable<T> getDataFromLocal() {
        return null;
    }

}
