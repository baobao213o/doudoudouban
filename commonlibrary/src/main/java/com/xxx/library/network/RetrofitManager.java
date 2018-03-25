package com.xxx.library.network;


import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.xxx.library.BaseApplication;
import com.xxx.library.network.https.SSLSocketFactoryUtils;
import com.xxx.library.network.interceptor.AuthenticateInterceptor;
import com.xxx.library.network.interceptor.LogInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;

public class RetrofitManager {

    private static OkHttpClient okHttpClient;

    private static volatile RetrofitManager instance;

    private Retrofit retrofit;

    private static ArrayMap<String, Object> serviceCacheMap;

    private final static String BASE_URL = "http://www.baidu.com/";

    private RetrofitManager() {
        serviceCacheMap = new ArrayMap<>();
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initOkHttpClient())
                .baseUrl(BASE_URL)
                .build();
    }


    public <T> T getService(Class<T> service) {
        if (retrofit == null) {
            init();
        }
        T result = (T) serviceCacheMap.get(service.getName());
        if (result == null) {
            result = retrofit.create(service);
            serviceCacheMap.put(service.getName(), result);
        }
        return result;
    }

    private static OkHttpClient initOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = createOkhttp();
        }
        return okHttpClient;
    }

    private static OkHttpClient createOkhttp() {
        //缓存文件夹
        String path;
        Context context = BaseApplication.getInstance();
        if (context.getExternalCacheDir() != null) {
            path = context.getExternalCacheDir().toString();
        } else {
            path = context.getCacheDir().toString();
        }
        File cacheFile = new File(path, "cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
        Cache cache = new Cache(cacheFile, cacheSize);

        SSLSocketFactory mSSLSocketFactory = SSLSocketFactoryUtils.createSSLSocketFactory();
        if (mSSLSocketFactory == null) {
            mSSLSocketFactory = SSLSocketFactoryUtils.systemDefaultSslSocketFactory(SSLSocketFactoryUtils.systemDefaultTrustManager());
        }
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new AuthenticateInterceptor())
                .addInterceptor(new LogInterceptor(LogInterceptor.BODY))
                .sslSocketFactory(mSSLSocketFactory, SSLSocketFactoryUtils.createTrustManager())
                .hostnameVerifier(STRICT_HOSTNAME_VERIFIER)
                .cache(cache)
                .build();
    }
}

