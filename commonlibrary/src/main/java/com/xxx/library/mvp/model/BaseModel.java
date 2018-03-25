package com.xxx.library.mvp.model;


import com.xxx.library.network.RetrofitManager;

public class BaseModel implements IModel{

    @Override
    public <T> T getDataFromRemote(Class<T> service) {
        return RetrofitManager.getInstance().getService(service);
    }

    @Override
    public <T> T getDataFromLocal() {
        return null;
    }
}
