package com.eas.app.api;

public interface BaseApiCallback<T> {
    void onSuccess(T result);
    void onError(Throwable t);
}
