package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.alarm_manager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * Created by chamber on 27.12.2017.
 */

public class FeedUpdateIntentService extends IntentService {
    static String TAG = FeedUpdateIntentService.class.getSimpleName();

    public FeedUpdateIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("onHandleIntent " + (intent == null));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
    }
}
