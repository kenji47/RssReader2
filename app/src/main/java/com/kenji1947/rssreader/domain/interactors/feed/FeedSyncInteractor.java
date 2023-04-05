package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;
import com.kenji1947.rssreader.domain.worker.FeedSyncScheduler;
import com.kenji1947.rssreader.domain.worker.FeedSyncWorker;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by chamber on 07.12.2017.
 */

//Воркер обращается к FeedUpdateInteractor
public class FeedSyncInteractor {
    private FeedRepository feedRepository;
    private RxSchedulersProvider schedulersProvider;
    private FeedSyncScheduler feedUpdateScheduler;
    private FeedSyncWorker feedSyncWorker;

    @Inject
    public FeedSyncInteractor(FeedRepository feedRepository,
                              FeedSyncScheduler feedUpdateScheduler,
                              RxSchedulersProvider schedulersProvider,
                              FeedSyncWorker feedSyncWorker) {
        this.feedRepository = feedRepository;
        this.schedulersProvider = schedulersProvider;
        this.feedUpdateScheduler = feedUpdateScheduler;
        this.feedSyncWorker = feedSyncWorker;
    }

    //---
    public Completable startFeedSync() {
        return Completable.fromAction(feedSyncWorker::startFeedSync);
    }

    public Completable cancelFeedSync() {
        return Completable.fromAction(feedSyncWorker::cancelFeedSync);
    }

    //---
    public Observable<Integer> observeFeedSyncComplete() {
        return feedRepository.observeFeedSyncComplete();
    }

    //TODO Обернуть в рх?
    public void notifyFeedSyncComplete(int newArticlesCount) {
        feedRepository.notifyFeedSyncComplete(newArticlesCount);
    }
}
