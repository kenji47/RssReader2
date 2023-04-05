package com.kenji1947.rssreader.di.application.modules;

import android.content.Context;
import android.content.Intent;

import com.kenji1947.rssreader.data.worker.feed_sync_worker.FeedSyncService;
import com.kenji1947.rssreader.data.worker.feed_sync_worker.FeedSyncWorkerImpl;
import com.kenji1947.rssreader.domain.worker.FeedSyncWorker;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 17.03.2018.
 */

@Module
public class FeedSyncWorkerModule {

    @Provides
    @Singleton
    FeedSyncWorker provideFeedSyncWorker(Context context) {
        return new FeedSyncWorkerImpl(context);
    }
    //Service Intent

    @Provides
    @Singleton
    @Named("FeedSyncServiceIntent")
    Intent provideFeedSyncServiceIntent(Context context) {
        return new Intent(context, FeedSyncService.class);
    }


    public interface Exposes {
        FeedSyncWorker provideFeedSyncWorker();
    }
}
