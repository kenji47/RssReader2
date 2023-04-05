package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by chamber on 26.02.2018.
 */

public class SearchFeedsInteractor {
    private FeedRepository feedRepository;
    private ConnectivityReceiver connectivityReceiver;
    private RxSchedulersProvider schedulersProvider;

    @Inject
    public SearchFeedsInteractor(FeedRepository feedRepository,
                                 ConnectivityReceiver connectivityReceiver,
                                 RxSchedulersProvider schedulersProvider) {
        this.feedRepository = feedRepository;
        this.connectivityReceiver = connectivityReceiver;
        this.schedulersProvider = schedulersProvider;
    }

    public Single<List<SearchedFeed>> searchFeed(String feedName) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.searchFeed(feedName)
                        : Single.error(new NoNetworkException()))
                .flatMap(searchedFeeds -> setSubscribed(searchedFeeds));
    }

    private Single<List<SearchedFeed>> setSubscribed(List<SearchedFeed> searchedFeeds) {
        return feedRepository.getFeeds()
                .map(feeds -> {
                    Set<String> dbFeedUrlSet = new TreeSet<>();
                    for (Feed feed : feeds) {
                        dbFeedUrlSet.add(feed.url);
                    }

                    for (SearchedFeed searchedFeed : searchedFeeds) {
                        if (dbFeedUrlSet.contains(searchedFeed.url))
                            searchedFeed.isSubscribed = true;
                    }
                    return searchedFeeds;
                });
    }
}
