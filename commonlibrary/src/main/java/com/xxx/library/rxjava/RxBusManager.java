package com.xxx.library.rxjava;

import android.util.SparseArray;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class RxBusManager {
    private static RxBusManager instance;
    private Subject<Object> mSubject;
    private SparseArray<CompositeDisposable> mSubscriptionMap;


    public static RxBusManager getInstance() {
        if (instance == null) {
            synchronized (RxBusManager.class) {
                if (instance == null) {
                    instance = new RxBusManager();
                }
            }
        }
        return instance;
    }


    private RxBusManager() {
        mSubject = PublishSubject.create().toSerialized();
        mSubscriptionMap = new SparseArray<>();
    }


    public void post(Object object) {
        mSubject.onNext(object);
    }


    private <T> Flowable<T> getObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    public <T> Disposable registerEvent(Class<T> type, Consumer<T> action1, Consumer<Throwable> error) {
        return getObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, error);
    }


    public void addSubscription(Object object, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new SparseArray<>();
        }
        int key = object.getClass().hashCode();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }


    public void unSubscrible(Object object) {
        if (mSubscriptionMap == null) {
            return;
        }

        int key = object.getClass().hashCode();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }
        mSubscriptionMap.remove(key);
    }
}
