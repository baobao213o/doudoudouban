package com.xxx.douban.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.xxx.douban.R;
import com.xxx.douban.ui.main.MainActivity;
import com.xxx.douban.view.HeartView;
import com.xxx.library.base.BaseActivity;
import com.xxx.library.mvp.presenter.BasePresenter;
import com.xxx.library.network.RetrofitManager;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    private boolean aniFinished = false;
    private HeartView heartView;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_splash);

        final InitCompletableOnSubscribe observerable = new InitCompletableOnSubscribe();

        heartView = findViewById(R.id.hv_main_splash);
        heartView.setListener(new HeartView.AniFinishListener() {
            @Override
            public void aniFinish() {
                aniFinished = true;
                synchronized (observerable) {
                    observerable.notify();
                }
            }
        });

        disposable = Completable.create(observerable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Intent it = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                });
    }

    class InitCompletableOnSubscribe implements CompletableOnSubscribe {

        @Override
        public void subscribe(CompletableEmitter emitter) throws Exception {
            //FIXME 初始化
            init();
            if (aniFinished) {
                emitter.onComplete();
            } else {
                synchronized (this) {
                    try {
                        wait();
                        emitter.onComplete();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void init() {
        RetrofitManager.getInstance().init();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        heartView.destroy();
    }
}
