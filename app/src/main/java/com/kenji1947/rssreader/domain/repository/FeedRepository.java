package com.kenji1947.rssreader.domain.repository;

import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by kenji1947 on 09.11.2017.
 */

public interface FeedRepository {

    Single<List<SearchedFeed>> searchFeed(String feedName);

    Single<Feed> getFeed(long id);
    Single<List<Feed>> getFeeds();
    Completable saveArticlesForFeed(long feedId, List<Article> articles);

    Completable saveFeed(List<Feed> feeds);

    Completable saveFeed(Feed feed);

    Single<Boolean> feedExists(String feedUrl);

    Observable<List<Feed>> getFeedsAndObserve();

    Completable deleteFeed(long feedId);

    Completable createNewFeed2(Feed feed);

    Single<List<Article>> fetchArticles(String feedUrl);

    Single<Feed> fetchFeed(String feedUrl);
    Completable createNewFeed(String feedUrl);

    Single<Boolean> isFeedSyncSchedulerEnabled();
    Completable setFeedSyncSchedulerStatus(boolean shouldUpdate);

    Completable setFeedSyncSchedulerInterval(long intervalMillis);

    Single<Long> getFeedSyncSchedulerInterval();

    Observable<Boolean> observeIsFeedSyncSchedulerEnabled();

    Observable<Integer> observeFeedSyncComplete();

    void notifyFeedSyncComplete(int newArticlesCount);

    Single<AppSettings> getAppSettings();
}
