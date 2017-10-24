package com.woders.apiprovider;

/**
 * title:
 * desc: 回调接口
 * Created by jiangguangming on 2015/10/30.
 */
public interface APICallBack<T> {
    void onSuccess(T data);
    void onFailure(T data);
}
