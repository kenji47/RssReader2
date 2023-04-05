package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.alarm_manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.SystemClock;

import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.worker.FeedSyncScheduler;

import timber.log.Timber;

/**
 * Created by chamber on 07.12.2017.
 */

public class FeedUpdateSchedulerAlarmManager implements FeedSyncScheduler {


    private PreferenceManager preferenceManager;
    private AlarmManager alarmManager;
    private PendingIntent servicePendingIntent;
    private long updateInterval;

    public FeedUpdateSchedulerAlarmManager(
            PreferenceManager preferenceManager,
            AlarmManager alarmManager,
            PendingIntent servicePendingIntent,
            long updateInterval) {

        this.preferenceManager = preferenceManager;
        this.alarmManager = alarmManager;
        this.servicePendingIntent = servicePendingIntent;
    }



    @Override
    public void enableFeedSyncScheduler(long intervalMillis) {
        Timber.d("enableFeedSyncScheduler period: " + intervalMillis);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + intervalMillis, intervalMillis, servicePendingIntent);
    }

    @Override
    public void enableFeedSyncScheduler() {
        //long period = preferenceManager.getFeedSyncSchedulerInterval();
        //long period = 5000;
        Timber.d("enableFeedSyncScheduler period: " + updateInterval);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + updateInterval, updateInterval, servicePendingIntent);
    }

    @Override
    public void cancelFeedSyncScheduler() {
        Timber.d("cancelFeedSyncScheduler");
        alarmManager.cancel(servicePendingIntent);
    }
}
