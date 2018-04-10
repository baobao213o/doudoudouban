package com.xxx.library.realm;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.exceptions.RealmException;

/**
 * Created by gaoruochen on 18-4-3.
 */

public abstract class RealmObserver<T> implements ObservableOnSubscribe<T> {

    private final AtomicBoolean canceled = new AtomicBoolean();

    public abstract T get(Realm realm);

    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        synchronized (this) {
            if (canceled.get()) {
                return;
            }
        }
        boolean withError = false;
        T object = null;
        Realm realm = RealmConfig.getDefaultConfigReal();
        try {
            if (!canceled.get()) {
                realm.beginTransaction();
                object = get(realm);
                if (!canceled.get()) {
                    realm.commitTransaction();
                } else {
                    realm.cancelTransaction();
                }
            }
        } catch (RuntimeException e) {
            realm.cancelTransaction();
            emitter.onError(new RealmException("Error during transaction.", e));
            withError = true;
        } catch (Error e) {
            realm.cancelTransaction();
            emitter.onError(e);
            withError = true;
        }
        if (!this.canceled.get() && !withError) {
            if (object == null) {
                emitter.onError(new NullPointerException("object is null"));
            }
            emitter.onNext(object);
        }

        try {
            realm.close();
        } catch (RealmException ex) {
            emitter.onError(ex);
            withError = true;
        }
        if (!withError) {
            emitter.onComplete();
        }
        canceled.set(false);
    }
}