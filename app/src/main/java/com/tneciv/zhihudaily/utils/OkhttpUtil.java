package com.tneciv.zhihudaily.utils;


import okhttp3.OkHttpClient;

/**
 * Created by Tneciv
 * on 2016-06-28 11:20 .
 */
public final class OkhttpUtil {
    private static volatile OkHttpClient defaultInstance;

    private OkhttpUtil() throws InstantiationException{
        throw new InstantiationException("This class is not for instantiation");
    }

    public static OkHttpClient getInstance() {
        if (defaultInstance == null) {
            synchronized (OkHttpClient.class) {
                if (defaultInstance == null) {
                    defaultInstance = new OkHttpClient();
                }
            }
        }
        return defaultInstance;
    }
}
