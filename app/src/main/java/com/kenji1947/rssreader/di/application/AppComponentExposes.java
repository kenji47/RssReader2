package com.kenji1947.rssreader.di.application;

import com.kenji1947.rssreader.di.application.modules.ApiModule;
import com.kenji1947.rssreader.di.application.modules.AppModule;
import com.kenji1947.rssreader.di.application.modules.ConnectivityModule;
import com.kenji1947.rssreader.di.application.modules.DatabaseModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncSchedulerModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncWorkerModule;
import com.kenji1947.rssreader.di.application.modules.NotificationManagerModule;
import com.kenji1947.rssreader.di.application.modules.OkHttpInterceptorsModule;
import com.kenji1947.rssreader.di.application.modules.RepositoryModule;
import com.kenji1947.rssreader.di.application.modules.RouterModule;
import com.kenji1947.rssreader.di.application.modules.RxSchedulersModule;
import com.kenji1947.rssreader.di.application.modules.UtilsModule;
import com.kenji1947.rssreader.domain.interactors.feed.FeedCrudInteractor;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public interface AppComponentExposes extends
        AppModule.Exposes,
        RepositoryModule.Exposes,
        RxSchedulersModule.Exposes,
        DatabaseModule.Exposes,
        RouterModule.Exposes,
        ConnectivityModule.Exposes,
        FeedSyncSchedulerModule.Exposes,
        NotificationManagerModule.Exposes,
        FeedSyncWorkerModule.Exposes,
        //AlarmSchedulerServiceModule.Exposes,
        ApiModule.Exposes,
        OkHttpInterceptorsModule.Exposes,
        UtilsModule.Exposes{

    //Provide dependencies with @Inject
    FeedCrudInteractor provideFeedCrudInteractor();
}
