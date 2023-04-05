package com.kenji1947.rssreader.domain.repository;

import com.kenji1947.rssreader.domain.entities.AppSettings;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by chamber on 25.03.2018.
 */

public interface AppSettingsRepository {
    Single<AppSettings> getAppSettings();

    Single<Boolean> isFeedSyncSchedulerEnabled();

    Completable setFeedSyncSchedulerStatus(boolean shouldUpdate);

    Completable setFeedSyncSchedulerInterval(long intervalMillis);

    Single<Long> getFeedSyncSchedulerInterval();

    //Was used by FeedListScreen appbar title
    Observable<Boolean> observeIsFeedSyncSchedulerEnabled();
}
