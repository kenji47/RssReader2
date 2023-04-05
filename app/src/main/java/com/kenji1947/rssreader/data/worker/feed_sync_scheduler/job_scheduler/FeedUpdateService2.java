package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.res.Configuration;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.FeedUpdateServicePresenter;
import com.kenji1947.rssreader.data.worker.notifications.NotificationFactory;
import com.kenji1947.rssreader.data.worker.notifications.NotificationManagerWrapper;
import com.kenji1947.rssreader.di.presenter.FeedUpdateServicePresenter3Component;

import javax.inject.Inject;
import javax.inject.Named;

import timber.log.Timber;

/**
 * Created by chamber on 07.12.2017.
 */

public class FeedUpdateService2 extends JobService implements FeedUpdateServiceView {
    //private static final int NEW_ARTICLES_NOTIFICATION_ID = 194747;

    @Inject
    @Named("FEED_SYNC_NOTIFICATION_ID")
    int feedSyncNotificationId = 194747;

    //@InjectPresenter
    FeedUpdateServicePresenter presenter;

//    @ProvidePresenter
//    FeedUpdateServicePresenter providePresenter() {
//        Timber.d("providePresenter");
//        return FeedUpdateServicePresenterComponent.Initializer
//                .init(App.INSTANCE.getAppComponent()).providePresenter();
    //}
    @Inject
    @Named("FeedUpdateNotificationPendingIntent")
    PendingIntent notificationPendingIntent;
    @Inject
    NotificationManagerWrapper notificationManager;
    @Inject
    NotificationFactory notificationFactory;

    private JobParameters jobParameters;

    //TODO CompositeSubscription?

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
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        presenter.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.d("onConfigurationChanged");
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Timber.d("onStartJob jobId: " + jobParameters.getJobId());
        this.jobParameters = jobParameters;
        presenter.updateAllFeeds();

        //показать уведомление тодько если колво новых статей увеличилось
        //уведомление должно отображать количество непрочитанных статей
        return true;
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

    @Override
    public void finishJob() {
        Timber.d("finishJob");
        jobFinished(jobParameters, false);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.d("onStopJob jobId: " + jobParameters.getJobId());
        return false;
    }
}
