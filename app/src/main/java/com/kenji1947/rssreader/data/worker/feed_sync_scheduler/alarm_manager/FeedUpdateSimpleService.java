package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.alarm_manager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.FeedUpdateServicePresenter;
import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler.FeedUpdateServiceView;
import com.kenji1947.rssreader.data.worker.notifications.NotificationFactory;
import com.kenji1947.rssreader.data.worker.notifications.NotificationManagerWrapper;
import com.kenji1947.rssreader.di.presenter.FeedUpdateServicePresenter3Component;

import javax.inject.Inject;
import javax.inject.Named;

import timber.log.Timber;

/**
 * Created by chamber on 27.12.2017.
 */

public class FeedUpdateSimpleService extends Service implements FeedUpdateServiceView{
    //private static final int NEW_ARTICLES_NOTIFICATION_ID = 194747;

    @Inject
    @Named("FEED_SYNC_NOTIFICATION_ID")
    int feedSyncNotificationId = 194747;

    @Inject
    @Named("FeedUpdateNotificationPendingIntent")
    PendingIntent notificationPendingIntent;

    @Inject
    NotificationManagerWrapper notificationManager;
    @Inject
    NotificationFactory notificationFactory;

    private FeedUpdateServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");
        App.INSTANCE.getAppComponent().inject(this);
        presenter = FeedUpdateServicePresenter3Component.Initializer
                .init(App.INSTANCE.getAppComponent()).providePresenter();
        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("onStartCommand");
        presenter.updateAllFeeds();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        presenter.onDestroy();
    }

    @Override
    public void finishJob() {
        Timber.d("finishJob");
        stopSelf();
    }

    @Override
    public void showNewArticlesNotification(int newArticlesCount, int unreadArticles) {
        Timber.d("showNewArticlesNotification newArticlesCount:" + newArticlesCount
                + " unreadArticles:" + unreadArticles);
        notificationManager.showNotification(feedSyncNotificationId,
                notificationFactory.createNewArticlesNotificationNew(
                        getContentTitle(newArticlesCount, unreadArticles),
                        getContentText(newArticlesCount, unreadArticles),
                        notificationPendingIntent));
    }
    private String getContentTitle(int newArticlesCount, int unreadArticles) {
        return getString(R.string.notification_feed_updated_title);
    }
    private String getContentText(int newArticlesCount, int unreadArticles) {
        return getString(R.string.notification_feed_updated_text,
                newArticlesCount + "", unreadArticles + ""
        );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}
