package com.kenji1947.rssreader.domain.interactors.settings;

import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.domain.repository.AppSettingsRepository;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;
import com.kenji1947.rssreader.domain.worker.FeedSyncScheduler;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by chamber on 25.03.2018.
 */

public class AppSettingsInteractor {
    private AppSettingsRepository settingsRepository;
    private FeedRepository feedRepository;
    private RxSchedulersProvider schedulersProvider;
    private FeedSyncScheduler feedSyncScheduler;

    @Inject
    public AppSettingsInteractor(AppSettingsRepository settingsRepository,
                                 FeedRepository feedRepository,
                                 RxSchedulersProvider schedulersProvider,
                                 FeedSyncScheduler feedSyncScheduler) {
        this.settingsRepository = settingsRepository;
        this.feedRepository = feedRepository;
        this.schedulersProvider = schedulersProvider;
        this.feedSyncScheduler = feedSyncScheduler;
    }

    public Single<AppSettings> getAppSettings(){
        return settingsRepository.getAppSettings();
    }

    public Completable setFeedSyncSchedulerInterval(long intervalMillis) {
        //TODO concatWith
        return feedRepository.setFeedSyncSchedulerInterval(intervalMillis)
                .toSingleDefault(42)//TODO How to transform to Completable without default value
                .flatMap(integer -> isFeedSyncSchedulerEnabled())
                //restart scheduler
                .flatMapCompletable(shouldUpdate ->
                        shouldUpdate
                                ? Completable.fromAction(() -> {
                            feedSyncScheduler.cancelFeedSyncScheduler();
                            feedSyncScheduler.enableFeedSyncScheduler(intervalMillis);})
                                : Completable.complete());
    }

    public Single<Boolean> isFeedSyncSchedulerEnabled() {
        return feedRepository.isFeedSyncSchedulerEnabled();
    };

        public Observable<Boolean> observeShouldUpdateFeedsInBackground() {
        return feedRepository.observeIsFeedSyncSchedulerEnabled();
    }
}
