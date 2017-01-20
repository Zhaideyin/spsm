package com.ronxuntech.component.spsm.util.test;

import okhttp3.Response;

import java.io.IOException;

/**
 * Created by train on 16/2/22.
 */
public interface HttpListener {
    void onSuccess(Response response);
    void onFailure(IOException e);
}
