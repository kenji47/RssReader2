package com.kenji1947.rssreader.data.database;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public interface FeedDao {
    Single<List<Feed>> getFeeds();

    Observable<List<Feed>> getFeedsAndObserve() throws NoNetworkException;

    Single<Feed> getFeed(long id);

    //--
    Single<Boolean> doesFeedExist(String feedUrl);

    //Completable insertFeed(ApiFeed apiFeed);

    Completable insertFeed(Feed feed);

    //---
    //Completable updateFeed(long feedId, List<ApiArticle> apiArticles);
    Completable updateFeed(long feedId, List<Article> articles);

    Completable updateFeeds(List<Feed> feeds);

    Completable createNewFeed(String feedUrl);
    Completable deleteFeed(long feedId);

    Completable pullArticlesForFeedFromOrigin(Feed feed);
    Single<Boolean> shouldUpdateFeedsInBackground();
    Completable setShouldUpdateFeedsInBackground(boolean shouldUpdate);
}
