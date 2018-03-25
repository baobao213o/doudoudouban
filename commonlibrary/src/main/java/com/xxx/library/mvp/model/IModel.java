package com.xxx.library.mvp.model;


public interface IModel {

    <T> T getDataFromRemote(Class<T> service);

    <T> T getDataFromLocal();
}
