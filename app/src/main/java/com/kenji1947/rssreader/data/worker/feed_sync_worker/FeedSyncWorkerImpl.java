package com.kenji1947.rssreader.data.worker.feed_sync_worker;

import android.content.Context;
import android.content.Intent;

import com.kenji1947.rssreader.domain.worker.FeedSyncWorker;

/**
 * Created by chamber on 17.03.2018.
 */


public class FeedSyncWorkerImpl implements FeedSyncWorker {
    private Context context;

    public FeedSyncWorkerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void startFeedSync() {
        //TODO Инжектировать сервис или интент
        context.startService(new Intent(context, FeedSyncService.class));
    }

    @Override
    public void cancelFeedSync() {
        //TODO Доделать отмену
        context.stopService(new Intent(context, FeedSyncService.class));
    }
}
