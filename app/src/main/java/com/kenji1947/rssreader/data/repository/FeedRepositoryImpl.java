package com.kenji1947.rssreader.data.repository;

import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.data.api.search_feed.SearchFeedApiService;
import com.kenji1947.rssreader.data.database.CommonUtils;
import com.kenji1947.rssreader.data.database.FeedDao;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * Created by kenji1947 on 09.11.2017.
 */

public class FeedRepositoryImpl implements FeedRepository {
    private FeedDao feedDao;
    private RxSchedulersProvider schedulersProvider;
    private PreferenceManager preferenceUtils;
    private FetchFeedApiService feedService;
    private SearchFeedApiService searchFeedApiService;

    //TODO Delete
    private Subject<Boolean> updateFeedsInBackgroundSubject;
    private Subject<Integer> feedSyncCompleteSubject = PublishSubject.create();

    public FeedRepositoryImpl(FetchFeedApiService feedService,
                              FeedDao feedDao,
                              RxSchedulersProvider schedulersProvider,
                              PreferenceManager preferenceManager,
                              SearchFeedApiService searchFeedApiService) {
        this.feedDao = feedDao;
        this.schedulersProvider = schedulersProvider;
        this.preferenceUtils = preferenceManager;
        this.feedService = feedService;
        this.searchFeedApiService = searchFeedApiService;


//        this.updateFeedsInBackgroundSubject =
//                BehaviorSubject.createDefault(preferenceUtils.isFeedSyncSchedulerEnabled());
        this.updateFeedsInBackgroundSubject = BehaviorSubject.create();

        //TODO Для чего
        initUpdateFeedsInBackgroundSubject();

    }

    private void initUpdateFeedsInBackgroundSubject() {
        isFeedSyncSchedulerEnabled().subscribe(aBoolean -> {
            Timber.d("initUpdateFeedsInBackgroundSubject " + aBoolean);
            updateFeedsInBackgroundSubject.onNext(aBoolean);
        });
    }

    @Override
    public Single<List<SearchedFeed>> searchFeed(String feedName) {
        Timber.d("searchFeed " + feedName);
        return searchFeedApiService.searchFeed(feedName).subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Single<Feed> getFeed(long id) {
        return feedDao.getFeed(id).subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Single<List<Feed>> getFeeds() {
        Timber.d("getFeeds");
        return Single.defer(() -> feedDao.getFeeds()).subscribeOn(schedulersProvider.getIo());
//        return feedDao.getFeeds()
//                .subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Observable<List<Feed>> getFeedsAndObserve() {
        Timber.d("getFeedsAndObserve");
        return feedDao.getFeedsAndObserve().subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Completable deleteFeed(long feedId) {
        return feedDao
                .deleteFeed(feedId)
                .subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Completable saveArticlesForFeed(long feedId, List<Article> articles) {
        return feedDao.updateFeed(feedId, articles);
    }



    @Override
    public Completable saveFeed(List<Feed> feeds) {
        return feedDao.updateFeeds(feeds);
    }

    @Override
    public Completable saveFeed(Feed feed) {
        return feedDao.insertFeed(feed);
    }

    @Override
    public Single<Boolean> feedExists(final String feedUrl) {
        return Single.defer(() -> feedDao.doesFeedExist(feedUrl))
                .subscribeOn(schedulersProvider.getIo());
    }

    //TODO Зачем defer
    //Download new Feed by URL and save it in db
    @Override
    public Completable createNewFeed(String feedUrl) {
        Timber.d("generateFeed " + feedUrl);
        return Completable.defer(() -> {
            CommonUtils.longOperation(); //TODO Перевести на fetchFeedNew
            return feedService.fetchFeed(feedUrl)
                    .flatMapCompletable(feedDao::insertFeed);
        }).subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Completable createNewFeed2(Feed feed) {
        Timber.d("createNewFeed2 " + feed.url);
        return Completable.defer(() -> {
            CommonUtils.longOperation(); //TODO Перевести на fetchFeedNew
            return feedService.fetchFeed(feed.url)
                    .map(feed1 -> {
                        feed.articles = feed1.articles;
                        return feed;
                    })
                    .flatMapCompletable(feedDao::insertFeed);
        }).subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public Single<List<Article>> fetchArticles(String feedUrl) {
        Timber.d("fetchArticles " + feedUrl);
        return Single.defer(() -> feedService.fetchArticles(feedUrl));
    }

    @Override
    public Single<Feed> fetchFeed(String feedUrl) {
        Timber.d("fetchFeed " + feedUrl);
        return Single.defer(() -> feedService.fetchFeed(feedUrl));
    }

    //moved
    @Override
    public Single<Boolean> isFeedSyncSchedulerEnabled() {
        return Single.fromCallable(preferenceUtils::isFeedSyncSchedulerEnabled)
                .subscribeOn(schedulersProvider.getIo());
    }
    //moved
    @Override
    public Completable setFeedSyncSchedulerStatus(boolean shouldUpdate) {
        Timber.d("setFeedSyncSchedulerStatus " + shouldUpdate);
        //TODO Notify
        return Completable.fromAction(() -> {
            updateFeedsInBackgroundSubject.onNext(shouldUpdate); //TODO OnNext в фоновом?
            preferenceUtils.setFeedSyncSchedulerStatus(shouldUpdate);
        })
                .subscribeOn(schedulersProvider.getIo());
    }
    //moved
    @Override
    public Completable setFeedSyncSchedulerInterval(long intervalMillis) {
        Timber.d("setFeedSyncSchedulerInterval " + intervalMillis);
        return Completable.fromAction(() -> preferenceUtils.setFeedSyncSchedulerInterval(intervalMillis))
                .subscribeOn(schedulersProvider.getIo());
    }
    //moved
    @Override
    public Single<Long> getFeedSyncSchedulerInterval() {
        return Single.fromCallable(preferenceUtils::getFeedSyncSchedulerInterval)
                .subscribeOn(schedulersProvider.getIo());
    }

    //Was used by FeedListScreen appbar title
    //moved
    @Override
    public Observable<Boolean> observeIsFeedSyncSchedulerEnabled() {
        //TODO throttleFirst. How to subscribe to subject
        //TODO throttleFirst нужно накладывать только на кнопки-источники
        return updateFeedsInBackgroundSubject.throttleFirst(300L, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulersProvider.getIo());
    }

    //---
    @Override
    public Observable<Integer> observeFeedSyncComplete() {
        return feedSyncCompleteSubject.throttleFirst(300L, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public void notifyFeedSyncComplete(int newArticlesCount) {
        feedSyncCompleteSubject.onNext(newArticlesCount);
    }

    //moved
    @Override
    public Single<AppSettings> getAppSettings() {
        return Single.fromCallable(() ->
                new AppSettings(
                        preferenceUtils.isFeedSyncSchedulerEnabled(),
                        preferenceUtils.getFeedSyncSchedulerInterval()
                ))
                .subscribeOn(schedulersProvider.getIo());
    }

    //TODO Old
//    @Override
//    public Completable pullFeedFromRemote(Feed feed, boolean update) {
//
//        return Completable.defer(() -> feedService.fetchFeed(feed.url)
//                .flatMapCompletable(apiFeed -> feedDao.updateFeed(feed.id, apiFeed.articles)))
//                .subscribeOn(schedulersProvider.getIo());
//    }
    //TODO Delete
//    @Override
//    public Single<Integer> saveArticlesForFeed(Feed feed) {
//        return Single.zip(
//                feedDao.getFeed(feed.id),
//                feedService.fetchFeedNew(feed.url),
//                (feedFromDb, feedFromRemote) -> {
//                    //return zipUpdateFeed(feedFromRemote, feedFromDb);
//                    List<Article> articlesNew = new ArrayList<>();
//
//                    for (Article article : feedFromRemote.articles) {
//                        if (!feedFromDb.articles.contains(article)) {
//                            articlesNew.add(article);
//                        }
//                    }
//                    feedDao.updateFeedDomain(feed.id, articlesNew);
//                    return articlesNew.size();
//                }
//        );
//    }
}
