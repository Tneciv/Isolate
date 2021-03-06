package com.tneciv.zhihudaily;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tneciv.zhihudaily.utils.IMMLeaks;

import im.fir.sdk.FIR;

/**
 * Created by Tneciv
 * on 2016-07-15 15:05 .
 */
public class MyApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
        refWatcher = LeakCanary.install(this);
        IMMLeaks.fixFocusedViewLeak(this);
    }

}
