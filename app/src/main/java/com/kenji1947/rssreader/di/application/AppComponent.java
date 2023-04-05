package com.kenji1947.rssreader.di.application;

import com.kenji1947.rssreader.di.application.modules.ApiModule;
import com.kenji1947.rssreader.di.application.modules.AppModule;
import com.kenji1947.rssreader.di.application.modules.ConnectivityModule;
import com.kenji1947.rssreader.di.application.modules.DatabaseModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncWorkerModule;
import com.kenji1947.rssreader.di.application.modules.NotificationManagerModule;
import com.kenji1947.rssreader.di.application.modules.OkHttpInterceptorsModule;
import com.kenji1947.rssreader.di.application.modules.RepositoryModule;
import com.kenji1947.rssreader.di.application.modules.RouterModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncSchedulerModule;
import com.kenji1947.rssreader.di.application.modules.RxSchedulersModule;
import com.kenji1947.rssreader.di.application.modules.UtilsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kenji1947 on 11.11.2017.
 */


@Singleton
@Component(modules = {
        AppModule.class,
        RouterModule.class,
        RepositoryModule.class,
        RxSchedulersModule.class,
        DatabaseModule.class,
        ConnectivityModule.class,
        FeedSyncSchedulerModule.class,
        //AlarmSchedulerServiceModule.class,
        FeedSyncWorkerModule.class,
        NotificationManagerModule.class,
        ApiModule.class,
        OkHttpInterceptorsModule.class,
        UtilsModule.class})
public interface AppComponent extends AppComponentExposes, AppComponentInjects {
}
