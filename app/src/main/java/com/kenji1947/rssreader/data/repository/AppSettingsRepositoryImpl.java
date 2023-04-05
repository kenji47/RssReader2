package com.kenji1947.rssreader.data.repository;

import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.domain.repository.AppSettingsRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * Created by chamber on 25.03.2018.
 */

public class AppSettingsRepositoryImpl implements AppSettingsRepository {
    private RxSchedulersProvider rxSchedulersProvider;
    private PreferenceManager preferenceManager;
    private Subject<Boolean> isFeedSyncSchedulerSubject;

    public AppSettingsRepositoryImpl(RxSchedulersProvider rxSchedulersProvider, PreferenceManager preferenceManager) {
        this.rxSchedulersProvider = rxSchedulersProvider;
        this.preferenceManager = preferenceManager;
        this.isFeedSyncSchedulerSubject = BehaviorSubject.create();
    }

    @Override
    public Single<AppSettings> getAppSettings() {
        return Single.fromCallable(() ->
                new AppSettings(
                        preferenceManager.isFeedSyncSchedulerEnabled(),
                        preferenceManager.getFeedSyncSchedulerInterval()
                ))
                .subscribeOn(rxSchedulersProvider.getIo());
    }

    @Override
    public Single<Boolean> isFeedSyncSchedulerEnabled() {
        return Single.fromCallable(preferenceManager::isFeedSyncSchedulerEnabled)
                .subscribeOn(rxSchedulersProvider.getIo());
    }

    @Override
    public Completable setFeedSyncSchedulerStatus(boolean shouldUpdate) {
        Timber.d("setFeedSyncSchedulerStatus " + shouldUpdate);
        return Completable.fromAction(() -> {
            preferenceManager.setFeedSyncSchedulerStatus(shouldUpdate);
        })
                .subscribeOn(rxSchedulersProvider.getIo());
    }

    @Override
    public Completable setFeedSyncSchedulerInterval(long intervalMillis) {
        Timber.d("setFeedSyncSchedulerInterval " + intervalMillis);
        return Completable.fromAction(() -> preferenceManager.setFeedSyncSchedulerInterval(intervalMillis))
                .subscribeOn(rxSchedulersProvider.getIo());
    }

    @Override
    public Single<Long> getFeedSyncSchedulerInterval() {
        return Single.fromCallable(preferenceManager::getFeedSyncSchedulerInterval)
                .subscribeOn(rxSchedulersProvider.getIo());
    }

    //Not used. Was used by FeedListScreen appbar button
    @Override
    public Observable<Boolean> observeIsFeedSyncSchedulerEnabled() {
        //TODO throttleFirst. How to subscribe to subject
        //TODO throttleFirst нужно накладывать только на кнопки-источники
        return isFeedSyncSchedulerSubject.throttleFirst(300L, TimeUnit.MILLISECONDS)
                .subscribeOn(rxSchedulersProvider.getIo());
    }
}
