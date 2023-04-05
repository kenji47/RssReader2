package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
import com.kenji1947.rssreader.domain.exceptions.FeedAlreadySubscribedException;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by chamber on 18.12.2017.
 */

public class SubscribeToFeedInteractor {
    private FeedRepository feedRepository;
    private ConnectivityReceiver connectivityReceiver;
    private RxSchedulersProvider schedulersProvider;

    @Inject
    public SubscribeToFeedInteractor(FeedRepository feedRepository,
                                     ConnectivityReceiver connectivityReceiver,
                                     RxSchedulersProvider schedulersProvider) {
        this.feedRepository = feedRepository;
        this.connectivityReceiver = connectivityReceiver;
        this.schedulersProvider = schedulersProvider;
    }

    public Completable subscribeToFeed (String feedUrl) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.feedExists(feedUrl)
                        : Single.error(new NoNetworkException()))
                .flatMap(aBoolean -> aBoolean
                        ? Single.error(new FeedAlreadySubscribedException())
                        : feedRepository.fetchFeed(feedUrl))
                .flatMapCompletable(feed -> feedRepository.saveFeed(feed));
    }

    //not used
    public Completable createNewFeed2 (SearchedFeed searchedFeed) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.feedExists(searchedFeed.url)
                        : Single.error(new NoNetworkException()))
                .flatMap(aBoolean -> aBoolean
                        ? Single.error(new FeedAlreadySubscribedException())
                        : feedRepository.fetchArticles(searchedFeed.url))
                .flatMapCompletable(articles -> {
                    //TODO
                    Feed feed = new Feed(searchedFeed.title, searchedFeed.imageUrl,
                            searchedFeed.pageLink, searchedFeed.description, searchedFeed.url);
                    feed.articles = articles;
                    return feedRepository.saveFeed(feed);
                });
    }

    //not used
    public Completable createNewFeed(String feedUrl) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.feedExists(feedUrl)
                        : Single.error(new NoNetworkException()))
                .flatMapCompletable(aBoolean -> aBoolean
                        ? Completable.error(new FeedAlreadySubscribedException())
                        : feedRepository.createNewFeed(feedUrl));
    }

    public Completable createNewFeed2 (Feed feed) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.feedExists(feed.url)
                        : Single.error(new NoNetworkException()))
                .flatMap(aBoolean -> aBoolean
                        ? Single.error(new FeedAlreadySubscribedException())
                        : feedRepository.fetchArticles(feed.url))
                .flatMapCompletable(articles -> {
                    feed.articles = articles;
                    return feedRepository.saveFeed(feed);
                });
    }

    public Completable createNewFeed2 (String feedUrl) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.feedExists(feedUrl)
                        : Single.error(new NoNetworkException()))
                .flatMap(aBoolean -> aBoolean
                        ? Single.error(new FeedAlreadySubscribedException())
                        : feedRepository.fetchArticles(feedUrl))
                //get feed
                .flatMapCompletable(articles -> {
                    return Completable.complete();
                });
    }


}
