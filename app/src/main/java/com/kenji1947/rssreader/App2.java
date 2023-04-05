package com.kenji1947.rssreader;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by chamber on 31.01.2018.
 */

public class App2 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //78
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
