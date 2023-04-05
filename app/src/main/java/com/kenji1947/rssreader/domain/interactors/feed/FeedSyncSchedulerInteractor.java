package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.data.repository.AppSettingsRepositoryImpl;
import com.kenji1947.rssreader.domain.repository.AppSettingsRepository;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;
import com.kenji1947.rssreader.domain.worker.FeedSyncScheduler;
import com.kenji1947.rssreader.domain.worker.FeedSyncWorker;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by chamber on 25.03.2018.
 */

public class FeedSyncSchedulerInteractor {
    private FeedRepository feedRepository;
    private RxSchedulersProvider rxSchedulersProvider;
    private FeedSyncScheduler feedSyncScheduler;
    private FeedSyncWorker feedSyncWorker;
    private AppSettingsRepository appSettingsRepository;

    @Inject
    public FeedSyncSchedulerInteractor(FeedRepository feedRepository,
                                       RxSchedulersProvider schedulersProvider,
                                       FeedSyncScheduler feedSyncScheduler,
                                       FeedSyncWorker feedSyncWorker,
                                       AppSettingsRepository appSettingsRepository) {
        this.feedRepository = feedRepository;
        this.rxSchedulersProvider = schedulersProvider;
        this.feedSyncScheduler = feedSyncScheduler;
        this.feedSyncWorker = feedSyncWorker;
        this.appSettingsRepository = appSettingsRepository;
    }

    public Completable enableFeedSyncScheduler() {
        return appSettingsRepository.getFeedSyncSchedulerInterval()
                .flatMapCompletable(interval ->
                        Completable.fromAction(() -> {
                            feedSyncScheduler.enableFeedSyncScheduler(interval);
                        }).concatWith(feedRepository.setFeedSyncSchedulerStatus(true)));
    }


    //TODO Сначала действия с сервисом, а потом менять префы
    public Completable disableFeedSyncScheduler() {
        return feedRepository.setFeedSyncSchedulerStatus(false)
                .concatWith(Completable.fromAction(feedSyncScheduler::cancelFeedSyncScheduler));
    }

}
